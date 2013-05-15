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
package org.hbird.business.archive.solr;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.hbird.exchange.commandrelease.CommandRequest;
import org.hbird.exchange.constants.StandardArguments;
import org.hbird.exchange.core.BusinessCard;
import org.hbird.exchange.core.Command;
import org.hbird.exchange.core.EntityInstance;
import org.hbird.exchange.core.Parameter;
import org.hbird.exchange.core.Part;
import org.hbird.exchange.core.State;
import org.hbird.exchange.dataaccess.CommitRequest;
import org.hbird.exchange.dataaccess.DataRequest;
import org.hbird.exchange.dataaccess.DeletionRequest;
import org.hbird.exchange.interfaces.IApplicableTo;
import org.hbird.exchange.interfaces.IDerivedFrom;
import org.hbird.exchange.interfaces.IEntityInstance;
import org.hbird.exchange.interfaces.IGenerationTimestamped;
import org.hbird.exchange.interfaces.IGroundStationSpecific;
import org.hbird.exchange.interfaces.ISatelliteSpecific;
import org.hbird.exchange.navigation.LocationContactEvent;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.tasking.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

/**
 * The Solr producer.
 * 
 * @author Gert Villemos
 */
public class SolrProducer extends DefaultProducer {

    private static final transient Logger LOG = LoggerFactory.getLogger(SolrProducer.class);

    /** The Solr endpoint. All configuration options are set on the endpoint. */
    private final SolrEndpoint endpoint;

    protected XStream xstream = new XStream();

    protected Collection<SolrInputDocument> batch = new ArrayList<SolrInputDocument>();

    /*
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

        if (exchange.getIn().getHeader("SUBMIT") != null) {
            submit();
        }
        else {

            try {
                Object body = exchange.getIn().getBody();

                if (body instanceof BusinessCard) {
                    return;
                }

                if (body instanceof DeletionRequest) {
                    String request = "";
                    if (((DeletionRequest) body).isDeleteAll()) {
                        request = "*:*";
                    }
                    else {
                        request = createRequest((DataRequest) body);
                    }
                    LOG.info("Deleting based on string = " + request);
                    delete(request);
                    commit();
                }
                else if (body instanceof CommitRequest) {
                    submit();
                    commit();
                }
                else if (body instanceof DataRequest) {
                    DataRequest dataRequest = (DataRequest) body;

                    if (dataRequest.hasArgument(StandardArguments.INITIALIZATION)
                            && dataRequest.getArgumentValue(StandardArguments.INITIALIZATION, Boolean.class)) {
                        LOG.info("Received Data Request ('{}') from '{}'. Initializing.", body.getClass().getSimpleName(), dataRequest.getIssuedBy());

                        String request = createRequest((DataRequest) body);
                        LOG.info("Search string = " + request);
                        List<EntityInstance> results = doInitializationRequest(dataRequest, request);

                        LOG.info("Returning {} entries.", results.size());
                        exchange.getOut().setBody(results);
                    }
                    else {
                        LOG.info("Received Data Request ('{}') from '{}'. Retrieving.", body.getClass().getSimpleName(), dataRequest.getIssuedBy());

                        String request = createRequest((DataRequest) body);
                        LOG.info("Search string = {}.", request);

                        SolrQuery query = createQuery(dataRequest, request);
                        List<EntityInstance> results = retrieve(query);

                        LOG.info("Returning {} entries.", results.size());
                        exchange.getOut().setBody(results);
                    }
                }
                else if (body instanceof List) {
                    /** Insert each element. */
                    for (EntityInstance entry : (List<EntityInstance>) body) {
                        insert(entry);
                    }
                }
                else if (body instanceof EntityInstance) {
                    insert((EntityInstance) body);
                }
            }
            catch (Exception e) {
                LOG.error("Failed to process Exchange {}", exchange.getExchangeId(), e);
            }
        }
    }

    protected String createRequest(DataRequest body) {
        String request = "";

        String classPart = null;
        String sourcePart = null;
        String namePart = null;
        String idPart = null;
        String isStateOfPart = null;
        String ofSatellitePart = null;
        String locationPart = null;
        String derivedFromPart = null;
        String applicableToPart = null;
        String visibilityPart = null;
        String isPartOfPart = null;

        /** Set the class of data we are retrieving. */
        if (body.hasArgumentValue(StandardArguments.CLASS)) {

            if (body.shallIncludeStates()) {
                classPart = "(class:" + body.getArgumentValue(StandardArguments.CLASS, String.class) + " OR class:State)";
            }
            else {
                classPart = "class:" + body.getArgumentValue(StandardArguments.CLASS, String.class);
            }
        }

        /** Set the type of data we are retrieving. */
        if (body.hasArgumentValue(StandardArguments.ISSUED_BY)) {
            sourcePart = "issuedBy:\"" + body.getArgumentValue(StandardArguments.ISSUED_BY, String.class) + "\"";
        }

        /** Set the Names, if there. */
        if (body.hasArgumentValue(StandardArguments.NAMES)) {
            namePart = "";
            for (String name : (List<String>) body.getArgumentValue(StandardArguments.NAMES, List.class)) {
                namePart = namePart.equals("") ? "name:\"" + name + "\"" : namePart + " OR name:\"" + name + "\"";

                /** Include states if required to */
                if (body.shallIncludeStates() == true) {
                    namePart += "OR isStateOf:\"" + name + "\"";
                }
            }
        }

        /** Set the type of data we are retrieving. */
        if (body.hasArgumentValue(StandardArguments.ENTITY_ID)) {
            sourcePart = "entityID:\"" + body.getArgumentValue(StandardArguments.ENTITY_ID, String.class) + "\"";
        }

        /** Set the isStateOf, if there. */
        if (body.hasArgumentValue(StandardArguments.IS_STATE_OF)) {
            isStateOfPart = "isStateOf:\"" + body.getArgumentValue(StandardArguments.IS_STATE_OF, String.class) + "\"";
        }

        if (body.hasArgumentValue(StandardArguments.DERIVED_FROM)) {
            String id = body.getArgumentValue(StandardArguments.DERIVED_FROM, String.class);
            derivedFromPart = "derivedFrom:\"" + id + "\"";
        }

        if (body.hasArgumentValue(StandardArguments.APPLICABLE_TO)) {
            String id = body.getArgumentValue(StandardArguments.APPLICABLE_TO, String.class);
            applicableToPart = "applicableTo:\"" + id + "\"";
        }

        if (body.hasArgumentValue(StandardArguments.SATELLITE_NAME)) {
            ofSatellitePart = "ofSatellite:\"" + body.getArgumentValue(StandardArguments.SATELLITE_NAME, String.class) + "\"";
        }

        if (body.hasArgumentValue(StandardArguments.GROUND_STATION_NAME)) {
            locationPart = "ofLocation:\"" + body.getArgumentValue(StandardArguments.GROUND_STATION_NAME, String.class) + "\"";
        }

        if (body.hasArgumentValue(StandardArguments.VISIBILITY)) {
            visibilityPart = "visibility:" + body.getArgumentValue(StandardArguments.VISIBILITY, Boolean.class);
        }

        if (body.hasArgumentValue(StandardArguments.IS_PART_OF)) {
            isPartOfPart = "isPartOf:\"" + body.getArgumentValue(StandardArguments.IS_PART_OF, String.class) + "\"";
        }

        if (sourcePart != null) {
            request += request.equals("") ? sourcePart : " AND " + sourcePart;
        }

        if (classPart != null) {
            request += request.equals("") ? classPart : " AND " + classPart;
        }

        if (derivedFromPart != null) {
            request += request.equals("") ? "(" + derivedFromPart + ")" : " AND (" + derivedFromPart + ")";
        }

        if (applicableToPart != null) {
            request += request.equals("") ? "(" + applicableToPart + ")" : " AND (" + applicableToPart + ")";
        }

        if (visibilityPart != null) {
            request += request.equals("") ? visibilityPart : " AND " + visibilityPart;
        }

        if (namePart != null && isStateOfPart != null) {
            request += request.equals("") ? "(" + namePart + " OR " + isStateOfPart + ")" : " AND (" + namePart + " OR " + isStateOfPart + ")";
        }
        else if (namePart != null && isStateOfPart == null) {
            request += request.equals("") ? "(" + namePart + ")" : " AND (" + namePart + ")";
        }
        else if (namePart == null && isStateOfPart != null) {
            request += request.equals("") ? "(" + isStateOfPart + ")" : " AND (" + isStateOfPart + ")";
        }

        if (ofSatellitePart != null) {
            request += request.equals("") ? ofSatellitePart : " AND " + ofSatellitePart;
        }

        if (locationPart != null) {
            request += request.equals("") ? locationPart : " AND " + locationPart;
        }

        if (isPartOfPart != null) {
            request += request.equals("") ? isPartOfPart : " AND " + isPartOfPart;
        }

        request += createTimestampElement(body.getArgumentValue(StandardArguments.FROM, Long.class), body.getArgumentValue(StandardArguments.TO, Long.class));

        return request;
    }

    protected String createTimestampElement(Long from, Long to) {
        String timePart = null;
        if (from != null && to != null) {
            if (from == to) {
                timePart = "timestamp:" + to;
            }
            else {
                timePart = "timestamp:[" + from + " TO " + to + "]";

            }
        }
        else if (from == null && to != null) {
            timePart = "timestamp:[* TO " + to + "]";
        }
        else if (from != null && to == null) {
            timePart = "timestamp:[" + from + " TO *]";
        }

        return timePart == null ? "" : " AND " + timePart;
    }

    protected String createIsStateOfElement(String isStateOf) {
        return isStateOf == null || isStateOf.equals("") ? "" : " AND isStateOf:\"" + isStateOf + "\"";
    }

    private List<EntityInstance> doInitializationRequest(DataRequest body, String request) {

        SolrQuery query = new SolrQuery(request);

        query.setRows(1);
        query.setSortField(endpoint.getSortField(), endpoint.getSortOrder());

        /** Configure facets. */
        query.setFacet(true);
        query.setQuery(request);
        query.setFacetSort(StandardArguments.COUNT);
        query.setFacetLimit(-1);
        query.setFacetMissing(false);
        query.setFacetMinCount(1);

        query.addFacetField(StandardArguments.NAME);

        query.setQueryType("basic");

        List<EntityInstance> results = new ArrayList<EntityInstance>();

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

                        LOG.info("Found " + facetfield.getValueCount() + " entries for facet value " + facetfield.getName());

                        for (Count count : facetfield.getValues()) {

                            String isStateOf = "";
                            if (body.hasArgumentValue(StandardArguments.IS_STATE_OF)) {
                                isStateOf = body.getArgumentValue(StandardArguments.IS_STATE_OF, String.class);
                            }

                            SolrQuery sampleQuery = new SolrQuery("name:\"" + count.getName() + "\"" + createTimestampElement(body.getFrom(), body.getTo())
                                    + createIsStateOfElement(isStateOf));
                            LOG.info("Using sample query '" + sampleQuery + "' to get facet " + count.getName());
                            sampleQuery.setRows(body.getRows());
                            sampleQuery.setSortField(StandardArguments.TIMESTAMP, ORDER.desc);
                            sampleQuery.setQueryType("basic");

                            for (EntityInstance newObj : retrieve(sampleQuery)) {
                                results.add(newObj);
                                LOG.info("Added object '" + newObj.getID() + "'");
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

        return query;
    }

    private void delete(String query) {

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
    protected List<EntityInstance> retrieve(SolrQuery query) {

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

        return Utilities.getResultSet(response, query.getRows());
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
    protected void insert(EntityInstance io) throws Exception {

        LOG.info("Storing object: " + io.prettyPrint());

        /** The document we will be storing. */
        SolrInputDocument document = new SolrInputDocument();

        document.setField(StandardArguments.HAS_URI, io.getInstanceID());

        document.addField(StandardArguments.NAME, io.getName());
        document.addField(StandardArguments.ENTITY_ID, io.getID());
        document.addField(StandardArguments.DESCRIPTION, io.getDescription());
        document.addField(StandardArguments.ISSUED_BY, ((IEntityInstance) io).getIssuedBy());
        document.addField(StandardArguments.TIMESTAMP, ((IEntityInstance) io).getTimestamp());

        /**
         * TODO Gert; find a nice way of managing subclassing. For now we put the class as generic 'Part' as well as
         * specific subtype.
         */
        if (io instanceof Part) {
            document.addField(StandardArguments.CLASS, "Part");
        }
        document.addField(StandardArguments.CLASS, io.getClass().getSimpleName());

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
            document.addField("carryingCommand", commandrequest.getID());

            for (String lockState : commandrequest.getLockStates()) {
                document.addField("withLockState", lockState);
            }

            for (Task task : commandrequest.getTasks()) {
                insert(task);
                document.addField("withTask", task.getID());
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
            document.addField(StandardArguments.ORBIT_NUMBER, event.getOrbitNumber());
        }

        if (io instanceof IGenerationTimestamped) {
            document.addField("generationTimestamp", ((IGenerationTimestamped) io).getGenerationTime());
        }
        if (io instanceof ISatelliteSpecific) {
            document.addField("ofSatellite", ((ISatelliteSpecific) io).getSatelliteId());
        }
        if (io instanceof IGroundStationSpecific) {
            document.addField("ofLocation", ((IGroundStationSpecific) io).getGroundStationId());
        }
        if (io instanceof IApplicableTo) {
            document.addField("applicableTo", ((IApplicableTo) io).getApplicableTo());
        }
        if (io instanceof IDerivedFrom) {
            document.addField("derivedFrom", ((IDerivedFrom) io).getDerivedFrom());
        }

        /** Insert the serialization. */
        document.setField("serialization", "<![CDATA[" + xstream.toXML(io) + "]]");

        /** Send the document to the SOLR server. */
        synchronized (batch) {
            batch.add(document);
        }
    }

    /**
     * Method to be called at intervals. Will insert the data in the batch in the SOLR server.
     * 
     */
    public void submit() {
        synchronized (batch) {
            if (batch.isEmpty() == false) {
                LOG.info("Submitting '" + batch.size() + "' documents for storage.");
                UpdateResponse response = null;
                try {
                    response = endpoint.getServer().add(batch);
                    if (response.getStatus() == 500) {
                        LOG.error("Failed to submit the document to the SOLR server. Server returned status code '{}'.", response.getStatus());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    /** Always clear. */
                    batch.clear();
                }
            }
        }
    }
}
