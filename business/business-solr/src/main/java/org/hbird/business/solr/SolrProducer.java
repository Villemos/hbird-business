/**
 * Licensed to the Hummingbird Foundation (HF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The HF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hbird.business.solr;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.hbird.exchange.businesscard.BusinessCard;
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.DataSet;
import org.hbird.exchange.core.IApplicableTo;
import org.hbird.exchange.core.IDerived;
import org.hbird.exchange.core.IGenerationTimestamped;
import org.hbird.exchange.core.IGroundStationSpecific;
import org.hbird.exchange.core.ISatelliteSpecific;
import org.hbird.exchange.core.Named;
import org.hbird.exchange.core.NamedInstanceIdentifier;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.tasking.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

/**
 * The Solr producer.
 */
public class SolrProducer extends DefaultProducer {

    private static final transient Logger LOG = LoggerFactory.getLogger(SolrProducer.class);

    /** The Solr endpoint. All configuration options are set on the endpoint. */
    private final SolrEndpoint endpoint;

    protected XStream xstream = new XStream();

    /**
     * Constructor
     * 
     * @param endpoint The endpoint.
     */
    public SolrProducer(SolrEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            Object body = exchange.getIn().getBody();

            if (body instanceof BusinessCard) {
                return;
            }

            if (body instanceof DeletionRequest) {
                delete((DeletionRequest) body);
                commit();
            }
            else if (body instanceof CommitRequest) {
                commit();
            }
            else if (body instanceof DataRequest) {
                DataRequest dataRequest = (DataRequest) body;

                if (dataRequest.hasArgument(StandardArguments.INITIALIZATION)
                        && dataRequest.getArgumentValue(StandardArguments.INITIALIZATION, Boolean.class)) {
                    LOG.info("Received Data Request ('{}'). Initializing.", body.getClass().getSimpleName());

                    String request = createRequest((DataRequest) body);
                    LOG.info("Search string = " + request);
                    List<Named> results = doInitializationRequest(dataRequest, request);

                    LOG.info("Returning {} entries.", results.size());
                    exchange.getOut().setBody(results);
                }
                else {
                    LOG.info("Received Data Request ('{}'). Retrieving.", body.getClass().getSimpleName());

                    String request = createRequest((DataRequest) body);
                    LOG.info("Search string = {}.", request);

                    SolrQuery query = createQuery(dataRequest, request);
                    List<Named> results = retrieve(query);

                    LOG.info("Returning {} entries.", results.size());
                    exchange.getOut().setBody(results);
                }
            }
            else if (body instanceof DataSet) {
                /** Insert each element. */
                for (Named entry : ((DataSet) body).getDataset()) {
                    insert(entry);
                }
            }
            else if (body instanceof Named) {
                insert((Named) body);
            }
        }
        catch (Exception e) {
            LOG.error("Failed to process Exchange {}", exchange.getExchangeId(), e);
        }
    }

    protected String createRequest(DataRequest body) {
        String request = null;

        String classPart = null;
        String typePart = null;
        String namePart = null;
        String isStateOfPart = null;
        String ofSatellitePart = null;
        String locationPart = null;
        String derivedFromPart = null;
        String applicableToPart = null;
        String visibilityPart = null;

        /** Set the class of data we are retrieving. */
        // if (body.getArgument(StandardArguments.CLASS) != null) {
        // classPart = "class:" + body.getArgument(StandardArguments.CLASS);
        // }

        /** Set the type of data we are retrieving. */
        if (body.hasArgumentValue(StandardArguments.TYPE)) {
            typePart = "type:" + body.getArgumentValue(StandardArguments.TYPE, String.class);
        }

        /** Set the Names, if there. */
        if (body.hasArgumentValue(StandardArguments.NAMES)) {
            String separator = "";
            namePart = "";
            for (String name : (List<String>) body.getArgumentValue(StandardArguments.NAMES, List.class)) {
                namePart += separator + "name:" + name;
                separator = " OR ";

                /** Include states if required to */
                if (body.shallIncludeStates() == true) {
                    namePart += separator + "isStateOf:" + name;
                }
            }
        }

        /** Set the isStateOf, if there. */
        if (body.hasArgumentValue(StandardArguments.IS_STATE_OF)) {
            isStateOfPart = "isStateOf:" + body.getArgumentValue(StandardArguments.IS_STATE_OF, String.class);
        }

        if (body.hasArgumentValue(StandardArguments.DERIVED_FROM)) {
            NamedInstanceIdentifier id = body.getArgumentValue(StandardArguments.DERIVED_FROM, NamedInstanceIdentifier.class);
            derivedFromPart = "derivedFromName:" + id.getName() + " AND derivedFromTimestamp:" + id.getTimestamp() + " AND derivedFromType:" + id.getType();
        }

        if (body.hasArgumentValue(StandardArguments.APPLICABLE_TO)) {
            NamedInstanceIdentifier id = body.getArgumentValue(StandardArguments.APPLICABLE_TO, NamedInstanceIdentifier.class);
            applicableToPart = "applicableToName:" + id.getName() + " AND applicableToTimestamp:" + id.getTimestamp() + " AND applicableToType:" + id.getType();
        }

        if (body.hasArgumentValue(StandardArguments.SATELLITE_NAME)) {
            ofSatellitePart = "ofSatellite:" + body.getArgumentValue(StandardArguments.SATELLITE_NAME, String.class);
        }

        if (body.hasArgumentValue(StandardArguments.GROUND_STATION_NAME)) {
            locationPart = "ofLocation:" + body.getArgumentValue(StandardArguments.GROUND_STATION_NAME, String.class);
        }

        if (body.hasArgumentValue(StandardArguments.VISIBILITY)) {
            visibilityPart = "visibility:" + body.getArgumentValue(StandardArguments.VISIBILITY, Boolean.class);
        }

        if (request == null && typePart != null) {
            request = typePart;
        }

        if (classPart != null) {
            request += " AND " + classPart;
        }

        if (derivedFromPart != null) {
            request += " AND (" + derivedFromPart + ")";
        }

        if (applicableToPart != null) {
            request += " AND (" + applicableToPart + ")";
        }

        if (visibilityPart != null) {
            request += " AND " + visibilityPart;
        }

        if (namePart != null && isStateOfPart != null) {
            request += " AND (" + namePart + " OR " + isStateOfPart + ")";
        }
        else {
            if (request == null && namePart != null) {
                request = "(" + namePart + ")";
            }
            else if (namePart != null) {
                request += " AND (" + namePart + ")";
            }

            if (request == null && isStateOfPart != null) {
                request = isStateOfPart;
            }
            else if (isStateOfPart != null) {
                request += " AND " + isStateOfPart;
            }
        }

        if (ofSatellitePart != null) {
            request += " AND " + ofSatellitePart;
        }

        if (locationPart != null) {
            request += " AND " + locationPart;
        }

        request += createTimestampElement(body.getArgumentValue(StandardArguments.FROM, Long.class), body.getArgumentValue(StandardArguments.TO, Long.class));

        return request;
    }

    protected String createTimestampElement(Long from, Long to) {
        String timePart = null;
        if (from != null && to != null) {
            timePart = "timestamp:[" + from + " TO " + to + "]";
        }
        else if (from == null && to != null) {
            timePart = "timestamp:[* TO " + to + "]";
        }
        else if (from != null && to == null) {
            timePart = "timestamp:[" + from + " TO *]";
        }

        return timePart == null ? "" : " AND " + timePart;
    }

    private List<Named> doInitializationRequest(DataRequest body, String request) {

        // String queryString = "";
        // if (request.getArgument(StandardArguments.IS_STATE_OF) != null) {
        // queryString += "isStateOf:" + (String) request.getArgument(StandardArguments.IS_STATE_OF);
        // }
        //
        // if (request.getArgument(StandardArguments.NAMES) != null) {
        // queryString += " OR name:(";
        // String separator = "";
        // for (String name : (List<String>) request.getArgument(StandardArguments.NAMES)) {
        // queryString += separator + name;
        // separator = " OR ";
        // }
        // queryString += ")";
        // }
        //
        // if (request.getArgument("attime") != null) {
        // queryString += " AND " + createTimestampElement(null, (Long) request.getArgument("attime"));
        // }

        SolrQuery query = new SolrQuery(request);

        query.setRows(1);
        query.setSortField(endpoint.getSortField(), endpoint.getSortOrder());

        /** Configure facets. */
        query.setFacet(true);
        query.setQuery(request);
        query.setFacetSort(StandardArguments.COUNT);
        query.setFacetLimit(-1);
        query.setFacetPrefix(endpoint.getFacetprefix());
        query.setFacetMissing(false);
        query.setFacetMinCount(1);

        query.addFacetField(StandardArguments.NAME);

        query.setQueryType("basic");

        List<Named> results = new ArrayList<Named>();

        QueryResponse response;
        try {
            response = endpoint.getServer().query(query);
            if (response.getStatus() != 0) {
                LOG.error("Failed to execute retrieval request. Failed with status '{}'.", response.getStatus());
            }

            if (response.getFacetFields() != null) {

                /** For each facet, retrieve 'rows' samples. */
                for (FacetField facetfield : response.getFacetFields()) {

                    if (facetfield.getValues() != null) {
                        for (Count count : facetfield.getValues()) {
                            SolrQuery sampleQuery = new SolrQuery("name:" + count.getName() + createTimestampElement(body.getFrom(), body.getTo()));
                            sampleQuery.setRows(body.getRows());
                            sampleQuery.setSortField(StandardArguments.TIMESTAMP, ORDER.desc);
                            sampleQuery.setQueryType("basic");

                            for (Named newObj : retrieve(sampleQuery)) {
                                results.add(newObj);
                                LOG.info("Added object '" + newObj.getName() + "' with timestamp '" + newObj.getTimestamp() + "'");
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            LOG.error("Failed to handle initialization request; query: '{}'.", request, e);
        }

        return results;
    }

    protected SolrQuery createQuery(Command body, String request) {

        SolrQuery query = new SolrQuery(request);

        query.setQueryType("basic");

        if (body.hasArgumentValue(StandardArguments.ROWS)) {
            query.setRows(body.getArgumentValue(StandardArguments.ROWS, Integer.class));
        }
        else {
            query.setRows(endpoint.getRows());
        }

        if (body.hasArgumentValue(StandardArguments.SORT)) {

            ORDER sortOrder = ORDER.desc;
            if (body.hasArgumentValue(StandardArguments.SORT_ORDER)) {
                if ("ASC".equals(body.getArgumentValue(StandardArguments.SORT_ORDER, String.class))) {
                    sortOrder = ORDER.asc;
                }
            }

            query.setSortField(body.getArgumentValue(StandardArguments.SORT, String.class), sortOrder);
        }

        /** If we are asked for facets, then add the facets. */
        if (endpoint.getFacets()) {
            query.setFacet(true);
            query.addFacetField(endpoint.getFacetField());
        }

        return query;
    }

    private void delete(DeletionRequest body) {

        String query = body.getArgumentValue(StandardArguments.DELETION_QUERY, String.class);
        LOG.info("Deleting based on query '{}'.", query);

        try {
            UpdateResponse response = endpoint.getServer().deleteByQuery(query);
            if (response.getStatus() == 500) {
                LOG.error("Failed to delete; query '{}'.", query);
            }
        }
        catch (Exception e) {
            LOG.error("Failed to handle deletion request; query '{}'.", query, e);
        }
    }

    /**
     * Method to process an exchange interpreted as a insert request.
     * 
     * The body of the exchange IN message is expected to hold the main part
     * of the text to be indexed. In addition other fields may be used to
     * categorizing the data according to different categories, such as
     * 'person' or 'organisation'.
     * 
     * CONVENTIONS
     * 
     * - A reference to a specific object is through a field with name '[]Instance'.
     * - A reference to a
     * 
     * 
     * @param exchange
     * @throws Exception
     */
    protected void insert(Named io) throws Exception {

        if (io.getType().equals("LocationContactEvent")) {
            log.debug("");
        }

        LOG.info("Storing object: " + io.prettyPrint());

        /** The document we will be storing. */
        SolrInputDocument document = new SolrInputDocument();

        Named namedIo = io;

        document.setField(StandardArguments.HAS_URI, io.getType() + ":" + io.getName() + ":" + io.getTimestamp());
        document.addField(StandardArguments.ISSUED_BY, namedIo.getIssuedBy());
        document.addField(StandardArguments.NAME, namedIo.getName());
        document.addField(StandardArguments.TYPE, namedIo.getType());
        document.addField(StandardArguments.DESCRIPTION, namedIo.getDescription());
        document.addField(StandardArguments.TIMESTAMP, namedIo.getTimestamp());
        document.addField(StandardArguments.DATA_SET_ID, namedIo.getDatasetidentifier());

        document.addField(StandardArguments.CLASS, namedIo.getClass().getSimpleName());

        if (io instanceof Parameter) {
            Parameter parameter = (Parameter) io;
            document.addField(StandardArguments.UNIT, parameter.getUnit());
            encodeValue(StandardArguments.VALUE, parameter.getValue(), document);
        }
        else if (io instanceof State) {
            State parameter = (State) io;
            document.addField(StandardArguments.IS_STATE_OF, parameter.getIsStateOf());
            encodeValue(StandardArguments.STATE, parameter.getValue(), document);
        }
        else if (io instanceof CommandRequest) {
            CommandRequest commandrequest = (CommandRequest) io;
            insert(commandrequest.getCommand());
            document.addField("carryingCommand", commandrequest.getUuid());

            for (String lockState : commandrequest.getLockStates()) {
                document.addField("withLockState", lockState);
            }

            for (Task task : commandrequest.getTasks()) {
                insert(task);
                document.addField("withTask", task.getUuid());
            }
        }
        else if (io instanceof Command) {
            Command command = (Command) io;
            document.addField(StandardArguments.DESTINATION, command.getDestination());
        }
        else if (io instanceof Satellite) {
            Satellite satellite = (Satellite) io;
            document.addField("withID", satellite.getSatelliteNumber());
            document.addField("designator", satellite.getDesignator());
        }
        else if (io instanceof LocationContactEvent) {
            LocationContactEvent event = (LocationContactEvent) io;
            document.addField(StandardArguments.VISIBILITY, event.isVisible);
        }

        if (io instanceof IGenerationTimestamped) {
            document.addField("generationTimestamp", ((IGenerationTimestamped) io).getGenerationTime());
        }
        if (io instanceof ISatelliteSpecific) {
            document.addField("ofSatellite", ((ISatelliteSpecific) io).getSatelliteName());
        }
        if (io instanceof IGroundStationSpecific) {
            document.addField("ofLocation", ((IGroundStationSpecific) io).getGroundStationName());
        }
        if (io instanceof IDerived) {
            document.addField("derivedFromName", ((IDerived) io).from().getName());
            document.addField("derivedFromTimestamp", ((IDerived) io).from().getTimestamp());
            document.addField("derivedFromType", ((IDerived) io).from().getType());
        }
        if (io instanceof IApplicableTo) {
            document.addField("applicableToName", ((IApplicableTo) io).applicableTo().getName());
            document.addField("applicableToTimestamp", ((IApplicableTo) io).applicableTo().getTimestamp());
            document.addField("applicableToType", ((IApplicableTo) io).applicableTo().getType());
        }

        /** Insert the serialization. */
        document.setField("serialization", "<![CDATA[" + xstream.toXML(io) + "]]");

        /** Send the document to the SOLR server. */
        UpdateResponse response = endpoint.getServer().add(document);
        if (response.getStatus() == 500) {
            LOG.error("Failed to submit the document to the SOLR server. Server returned status code '{}'.", response.getStatus());
        }
    }

    protected void encodeValue(String key, Object value, SolrInputDocument document) {
        if (value == null) {
            // nothing to encode; return
            return;
        }
        if (value instanceof Integer || value.getClass() == int.class) {
            document.addField(key + "_i", value);
        }
        if (value instanceof Double || value.getClass() == double.class) {
            document.addField(key + "_d", value);
        }
        if (value instanceof Float || value.getClass() == float.class) {
            document.addField(key + "_f", value);
        }
        if (value instanceof Long || value.getClass() == long.class) {
            document.addField(key + "_l", value);
        }
        if (value instanceof Boolean || value.getClass() == boolean.class) {
            document.addField(key + "_b", value);
        }
        if (value instanceof String || value.getClass() == char.class) {
            document.addField(key + "_s", value);
        }
        else {
            /** Dont encode. */
        }
    }

    protected void commit() throws SolrServerException, IOException {
        LOG.info("Forcing commit of changes.");
        UpdateResponse commitResponse = endpoint.getServer().commit();
        if (commitResponse.getStatus() == 500) {
            LOG.error("Failed to commit the document to the SOLR server. Server returned status code '{}'.", commitResponse.getStatus());
        }
    }

    /**
     * Retrieves a number of entries from the repository, based on the configured
     * query.
     * 
     * @param exchange
     * @throws SolrServerException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws RemoteException
     */
    protected List<Named> retrieve(SolrQuery query) {

        LOG.debug("Issuing request '{}'.", query.toString());

        /**
         * Configure the request.
         * 
         * One keywords are supported
         * FROMLAST. Will be replaced with the timestamp of the last retrieval (initial is 0).
         */
        /**
         * Search and set result set. Notice that this will return the results upto the
         * configured number of rows. More results may thus be in the repository.
         */

        QueryResponse response = null;

        try {
            response = endpoint.getServer().query(query);
            if (response.getStatus() != 0) {
                LOG.error("Failed to execute retrieval request. Failed with status '{}'.", response.getStatus());
            }
        }
        catch (Exception e) {
            LOG.error("Failed to handle query; query: '{}'.", query.toString(), e);
        }

        ResultSet results = Utilities.getResultSet(response, query.getRows());
        return results.getResults();
    }
}
