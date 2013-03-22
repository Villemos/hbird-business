package eu.estcube.domain.transport.tnc;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * TNC Frame for communicating with TNC device over serial port using KISS
 * protocol.
 */
public class TncFrame implements Serializable {

    /** Serial version UUID. */
    private static final long serialVersionUID = 3658972077914616377L;

    /**
     * Enumeration of TNC commands.
     */
    public enum TncCommand {

        // @formatter:off
        /** Command to send data over TNC. */
        DATA            ((byte) 0x00), 
        /** Command to set TX delay on TNC device. */
        TX_DELAY        ((byte) 0x01),
        /** Command to set P value on TNC device. */
        P               ((byte) 0x02),
        /** Command to set SLOT_TIME in TNC device. */
        SLOT_TIME       ((byte) 0x03),
        /** Command to set TX tail on TNC device. */
        TX_TAIL         ((byte) 0x04),
        /** Command to set full duplex mode of TNC device. */
        FULL_DUPLEX     ((byte) 0x05),
        /** Command to set hardware specific parameter on TNC device. */
        SET_HARDWARE    ((byte) 0x06),
        /** Command to return from KISS mode in TNC device. */
        RETURN          ((byte) 0xFF),
        // @formatter:on
        ;

        /** Static lookup map. */
        private static final Map<Byte, TncCommand> MAP = new HashMap<Byte, TncCommand>();

        static {
            for (TncCommand command : EnumSet.allOf(TncCommand.class)) {
                MAP.put(command.getValue(), command);
            }
        }

        /** Command value as byte. */
        private final byte value;

        /**
         * Creates new TncCommand.
         * 
         * @param value
         */
        private TncCommand(byte value) {
            this.value = value;
        }

        /**
         * Returns byte value of the command.
         * 
         * @return byte value of the command
         */
        public byte getValue() {
            return value;
        }

        /**
         * Returns {@link TncCommand} for given byte value
         * 
         * @param value byte value of command
         * @return TncCommand for given byte value or null
         */
        public static TncCommand fromByte(byte value) {
            return MAP.get(value);
        }
    }

    /** TNC command. */
    protected final TncCommand command;

    /** TNC frame target/source port on TNC device. */
    protected final int target;

    /** TNC frame content as byte array. */
    protected final byte[] data;

    /**
     * Creates new TncFrame.
     * 
     * @param command TNC command
     * @param target TNC frame target/source
     * @param data TNC Frame data
     */
    public TncFrame(TncCommand command, int target, byte[] data) {
        this.command = command;
        this.target = target;
        this.data = data;
    }

    /**
     * Returns type.
     * 
     * @return the type
     */
    public TncCommand getCommand() {
        return command;
    }

    /**
     * Returns target.
     * 
     * @return the target
     */
    public int getTarget() {
        return target;
    }

    /**
     * Returns data.
     * 
     * @return the data
     */
    public byte[] getData() {
        return data;
    }
}
