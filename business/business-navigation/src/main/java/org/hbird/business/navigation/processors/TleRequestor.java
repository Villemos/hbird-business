package org.hbird.business.navigation.processors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.apache.camel.Handler;
import org.hbird.business.api.IdBuilder;
import org.hbird.exchange.navigation.Satellite;
import org.hbird.exchange.navigation.TleOrbitalParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Requests and parses TLE lines from space-track.org in Camel Route
 */
@Component
public class TleRequestor {

    protected static final Logger LOG = LoggerFactory.getLogger(SatelliteRequestor.class);

    private String baseURL = "https://www.space-track.org";
    private String authPath = "/auth/login";
    private String userName;
    private String password;

    private IdBuilder idBuilder;

    public TleRequestor(String userName, String password, IdBuilder idBuilder) {
        this.userName = userName;
        this.password = password;
        this.idBuilder = idBuilder;
    }

    @Handler
    public List<TleOrbitalParameters> requestAndParse(List<Satellite> satellites) {
        if (satellites == null || satellites.isEmpty()) {
            LOG.debug("No satellites found, to request TLEs");
            return new ArrayList<TleOrbitalParameters>();
        }

        // Create empty TLE map
        Map<Integer, TleOrbitalParameters> tles = new HashMap<Integer, TleOrbitalParameters>();
        for (Satellite sat : satellites) {
            try {
                int noradID = Integer.parseInt(sat.getDesignator());
                TleOrbitalParameters top = new TleOrbitalParameters(idBuilder.buildID(sat.getID(), "TLE"), "TLE");
                top.setSatelliteId(sat.getID());
                top.setIssuedBy("automatically requested from space-track.org");
                tles.put(noradID, top);
            }
            catch (NumberFormatException npe) {
                // In case designator is not an integer
                LOG.warn("Satellite designator should be its NORAD ID; Satellite: " + sat.getSatelliteID()
                        + ", Designator: \"" + sat.getDesignator() + "\"");
            }
        }
        if (tles.isEmpty()) {
            LOG.warn("NORAD IDs not set for any satellites");
            return new ArrayList<TleOrbitalParameters>();
        }

        // Request TLE lines from Space-Track
        List<String> tleLines = requestTLEs(tles.keySet());
        if (tleLines.size() != tles.size() * 2) {
            LOG.warn("Something wrong with Space-Track query or satellites' NORAD IDs, failed to return TLE lines for all satellites");
        }

        // Parse and return TleOrbitalParameters list
        return parseTLEs(tleLines, tles);
    }

    public List<String> requestTLEs(Set<Integer> noradIDs) {
        StringBuilder queryIds = new StringBuilder();
        for (Integer sat : noradIDs) {
            queryIds.append(sat).append(",");
        }
        queryIds.deleteCharAt(queryIds.length() - 1);
        HttpsURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        try {

            String auth = "identity=" + userName + "&password=" + password;
            String query = "/basicspacedata/query/class/tle_latest/NORAD_CAT_ID/" + queryIds.toString()
                    + "/ORDINAL/1/format/tle";
            URL authURL = new URL(baseURL + authPath);
            URL queryurl = new URL(baseURL + query);

            CookieManager manager = new CookieManager();
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(manager);

            conn = (HttpsURLConnection) authURL.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();
            os.write(auth.getBytes());
            os.flush();

            // Needed to get auth cookies
            is = conn.getInputStream();

            br = new BufferedReader(new InputStreamReader((queryurl.openStream())));

            String output;
            List<String> lines = new ArrayList<String>();
            while ((output = br.readLine()) != null) {
                lines.add(output);
            }
            return lines;
        }
        catch (Exception e) {
            LOG.error("Failed to load TLEs from space-track.org", e);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    LOG.error("Failed to close InputStream", e);
                }
            }
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    LOG.error("Failed to close BufferedReader", e);
                }
            }
        }
        return new ArrayList<String>();
    }

    public List<TleOrbitalParameters> parseTLEs(List<String> TLEs, Map<Integer, TleOrbitalParameters> satellites) {
        List<TleOrbitalParameters> tles = new ArrayList<TleOrbitalParameters>();
        try {
            for (int i = 0; i < TLEs.size(); i += 2) {
                String ln1 = TLEs.get(i);
                Integer key = Integer.parseInt(ln1.substring(2, 7));
                TleOrbitalParameters top = satellites.get(key);
                top.setTleLine1(ln1);
                top.setTleLine2(TLEs.get(i + 1));
                tles.add(top);
            }
        }
        catch (Exception e) {
            LOG.error("Failed to parse TLE lines", e);
        }
        return tles;
    }
}
