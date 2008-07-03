/*
 * RequestedInformationListTest.java
 * JUnit based test
 *
 * Created on 25 Август 2007 г., 20:09
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;
import org.mobicents.jcc.inap.protocol.util.MessageReader;

/**
 *
 * @author Oleg Kulikov
 */
public class RequestedInformationListTest extends TestCase {
    
    private byte[] hex;
    
    public RequestedInformationListTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        MessageReader reader;
        try {
            reader = new MessageReader("/data/reqinflist.txt");
            hex = reader.getData();
        } catch (Exception e) {
            fail("Could not load binary data");
        }
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RequestedInformationListTest.class);
        return suite;
    }


    public void testDecode() {
        try {
            RequestedInformationList list = new RequestedInformationList(hex);
            assertEquals(3, list.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    public void testEncode() {
        
    }
}
