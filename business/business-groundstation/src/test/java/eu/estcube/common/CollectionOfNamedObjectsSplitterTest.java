package eu.estcube.common;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Message;
import org.hbird.exchange.core.Label;
import org.hbird.exchange.core.Named;
import org.junit.Before;
import org.junit.Test;

public class CollectionOfNamedObjectsSplitterTest {

    private CollectionOfNamedObjectsSplitter splitter;
    private ArrayList<Named> namedObjects;
    private String issuedBy = "issuer";
    private String description = "description";
    private String labelName1 = "labelName1";
    private String labelName2 = "labelName2";
    private String value1 = "100";
    private String value2 = "101";

    @Before
    public void setUp() {
        splitter = new CollectionOfNamedObjectsSplitter();
        Named named1 = new Label(issuedBy, labelName1, description, value1);
        Named named2 = new Label(issuedBy, labelName2, description, value2);
        namedObjects = new ArrayList<Named>();
        namedObjects.add(named1);
        namedObjects.add(named2);
    }

    @Test
    public void testSplitter() {
        List<Message> messages = splitter.splitMessage(namedObjects);
        assertEquals(messages.size(), namedObjects.size());

        for (int i = 0; i < namedObjects.size(); i++) {
            assertEquals(messages.get(i).getBody(Named.class), namedObjects.get(i));
        }
    }
}
