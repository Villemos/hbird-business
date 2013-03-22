package eu.estcube.gsconnector;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.camel.Message;
import org.junit.Test;

public class SplitterAbstractStatusTest {

    private SplitterAbstractStatus abstractStatusSplitter;

    @Test
    public void testRadioSplitMessage() {
        abstractStatusSplitter = new SplitterRadioStatus();
        List<Message> answer = abstractStatusSplitter.splitMessage();
        assertTrue(answer.size() == 17);
    }

    @Test
    public void testRotatorSplitMessage() {
        abstractStatusSplitter = new SplitterRotatorStatus();
        List<Message> answer = abstractStatusSplitter.splitMessage();
        assertTrue(answer.size() == 1);
    }

}
