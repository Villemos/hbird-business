package eu.estcube.domain.transport.tnc;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import eu.estcube.domain.transport.tnc.TncFrame;
import eu.estcube.domain.transport.tnc.TncFrame.TncCommand;

/**
 *
 */
public class TncFrameTest {

    private static final TncCommand COMMAND = TncCommand.P;
    private static final int TARGET = 3;
    private static final byte[] DATA = new byte[] { 0x0F, 0x00, 0x00, 0x01 };
    
    private TncFrame tncFrame;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        tncFrame = new TncFrame(COMMAND, TARGET, DATA);
    }

    /**
     * Test method for {@link eu.estcube.domain.transport.tnc.TncFrame#TncFrame(eu.estcube.domain.transport.tnc.TncFrame.TncCommand, int, byte[])}.
     */
    @Test
    public void testTncFrame() {
        testGetCommand();
        testGetTarget();
        testGetData();
    }

    /**
     * Test method for {@link eu.estcube.domain.transport.tnc.TncFrame#getCommand()}.
     */
    @Test
    public void testGetCommand() {
        assertEquals(COMMAND, tncFrame.getCommand());
    }

    /**
     * Test method for {@link eu.estcube.domain.transport.tnc.TncFrame#getTarget()}.
     */
    @Test
    public void testGetTarget() {
        assertEquals(TARGET, tncFrame.getTarget());
    }

    /**
     * Test method for {@link eu.estcube.domain.transport.tnc.TncFrame#getData()}.
     */
    @Test
    public void testGetData() {
        assertTrue(Arrays.equals(DATA, tncFrame.getData()));
    }

    /**
     * Test method for {@link eu.estcube.domain.transport.tnc.TncFrame#TncCommand}.
     */
    public void testTncCommand() {
        for (TncCommand cmd : TncCommand.values()) {
            assertEquals(cmd, TncCommand.fromByte(cmd.getValue()));
        }
    }
}
