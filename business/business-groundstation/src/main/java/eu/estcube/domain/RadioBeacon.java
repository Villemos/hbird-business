package eu.estcube.domain;

import java.util.HashMap;

public class RadioBeacon {

    private String radioBeacon;
    private HashMap<String, String> parameters = new HashMap<String, String>();

    public String getRadioBeacon() {
        return radioBeacon;
    }

    public void setRadioBeacon(String radioBeacon) {
        this.radioBeacon = radioBeacon;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RadioBeacon: " + radioBeacon;
    }

}