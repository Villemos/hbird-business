package eu.estcube.gsconnector;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;

public class SplitterRadioStatusTest {
    
    private SplitterRadioStatus radioStatusSplitter;

    @Before
    public void setUp() throws Exception {        
        radioStatusSplitter = new SplitterRadioStatus();
    }

    @Test
    public void testSplitMessage() {
        List<Message> answer = radioStatusSplitter.splitMessage();
        assertTrue(answer.size()== 17);
    }
}
