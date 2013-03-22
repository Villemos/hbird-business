package eu.estcube.gsconnector;


import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;

public class SplitterRotatorStatusTest {
    private SplitterRotatorStatus rotatorStatusSplitter;

    @Before
    public void setUp() throws Exception {        
        rotatorStatusSplitter = new SplitterRotatorStatus();
    }

    @Test
    public void testSplitMessage() {
        List<Message> answer = rotatorStatusSplitter.splitMessage();
        assertTrue(answer.size()== 1);
    }

}
