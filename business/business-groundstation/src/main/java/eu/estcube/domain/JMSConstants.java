package eu.estcube.domain;

public class JMSConstants {
    public static final String CMDG_HEADER_RADIO = "RADIO_ES5EC_dummy";
    public static final String CMDG_HEADER_ROTATOR = "ROTATOR_ES5EC_dummy";
    public static final String CMDG_HEADER_START_TIME = "start_time";
    public static final String CMDG_HEADER_END_TIME = "end_time";
    public static final String AMQ_GS_COMMANDS = "activemq:topic:hbird.gs.";
    public static final String AMQ_GS_COMMANDS_RADIO = "activemq:queue:gsCommands?selector="
            + JMSConstants.HEADER_COMPONENT_ID + "%3D'" + JMSConstants.CMDG_HEADER_RADIO + "'";
    public static final String AMQ_GS_COMMANDS_ROTATOR = "activemq:queue:hbird.antennaschedule."
            + JMSConstants.HEADER_COMPONENT_ID + "%3D'" + JMSConstants.CMDG_HEADER_ROTATOR + "'";
    public static final String AMQ_COMMANDS_RECEIVE_TIME = "contactTimeReceive";
    public static final String AMQ_COMMANDS_CONSUME_TIME = "activemq:queue:contactTimeReceive";

    public static final String AMQ_GS_RECEIVE = "activemq:topic:gsReceive";
    public static final String AMQ_GS_SEND = "activemq:topic:gsSend";
    public static final String AMQ_GS_META_SEND = "activemq:topic:gsMetaSend";
    public static final String AMQ_PS_QUEUE = "activemq:topic:psTopic";
    public static final String AMQ_WEBCAM_SEND = "activemq:topic:webcamSend";
    public static final String AMQ_WEATHERSTATION_SEND = "activemq:topic:weatherStationSend";
    public static final String AMQ_VERSIONS_REQUEST = "activemq:topic:versionsRequest";
    public static final String AMQ_VERSIONS_RECEIVE = "activemq:topic:versionsReceive";
    public static final String AMQ_WEBSERVER_FOR_LOG = "activemq:topic:log";
    public static final String AMQ_WEBSERVER_FOR_SYSTEM = "activemq:topic:system";
    public static final String AMQ_WEBSERVER_FOR_MAP = "activemq:topic:orbitalpredictions";

    public static final String GS_ALL_NAMES = "*";
    public static final String GS_ID_HEADER = "groundStationID";
    public static final String GS_RIG_CTLD = "rigctld";
    public static final String GS_ROT_CTLD = "rotctld";
    public static final String GS_DEVICE_END_MESSAGE = "RPRT";
    public static final String GS_ENCODE_STR_TO_BYTE = "encodeStrToByte";
    public static final String GS_RADIO_PROTOCOL_ENCODER = "radioProtocolEncoder";
    public static final String GS_ROTATOR_PROTOCOL_ENCODER = "rotatorProtocolEncoder";
    public static final String GS_NEW_LINE_DECODER = "newLineDecoder";
    public static final String GS_STRING_DECODER = "stringDecoder";
    public static final String GS_HAMLIB_DECODER = "hamlibDecoder";
    public static final String GS_RADIO_PROTOCOL_DECODER = "radioProtocolDecoder";
    public static final String GS_ROTATOR_PROTOCOL_DECODER = "rotatorProtocolDecoder";
    public static final int GS_MAX_FRAME_LENGTH = 10240;

    public static final String DIRECT_CHOOSE_DEVICE = "direct:chooseDevice";
    public static final String DIRECT_SEND = "direct:gsSend";
    public static final String DIRECT_RIG_CTLD = "direct:rigctld";
    public static final String DIRECT_ROT_CTLD = "direct:rotctld";
    public static final String DIRECT_ROT_CTLD_INTERMEDIATE = "direct:rotctldIntermediate";
    public static final String DIRECT_RIG_CTLD_INTERMEDIATE = "direct:rigctldIntermediate";
    public static final String DIRECT_POLL_COMMAND = "direct:pollCommand";
    public static final String DIRECT_RIG_STATUS = "direct:rigStatus";
    public static final String DIRECT_ROT_STATUS = "direct:rotStatus";
    public static final String DIRECT_STATUS = "direct:status";
    public static final String DEVICE_HEADER = "device";

    public static final int POLL_COMMAND_TIMEOUT = 1000;
    public static final String POLL_SET_COMMAND = "setCommand";
    public static final String POLL_GET_COMMAND = "getCommand";
    public static final String POLL_NOT_REQUIRED = "notRequired";

    public static final String ENCODING_STRING_FORMAT = "ASCII";

    public static final String HEADER_POLLING = "polling";
    public static final String HEADER_FORWARD = "forward";
    public static final String HEADER_DEVICE = "device";
    public static final String HEADER_COMPONENT_ID = "componentId";
    public static final String HEADER_COMPONENT = "component";
    public static final String HEADER_GROUNDSTATIONID = "groundStationID";
    public static final String HEADER_FROM_PS = "fromPS";

    public static final String COMPONENT_WEBSERVER = "Webserver";
    public static final String COMPONENT_CONNECTOR = "Connector";
    public static final String COMPONENT_ROTATOR = "Rotator";
    public static final String COMPONENT_WEBCAMERA_TRANSMITTER = "Webcamera transmitter";
    public static final String COMPONENT_WEATHERSTATION_TRANSMITTER = "WeatherStation transmitter";
    public static final String COMPONENT_PARAMETER_STORAGE = "Parameter storage";

    public static final String CLASS_POLL_COMMANDS = "pollCommands";

    public static final int NORMAL_BEACON_MESSAGE_LENGTH = 43;
    public static final int SAFE_BEACON_MESSAGE_LENGTH = 52;
    public static final int HEX_BASE = 16;
    public static final String INTERNAL_SCHEDULE = "activemq:topic:hbird.groundstation.commandqueue";

}