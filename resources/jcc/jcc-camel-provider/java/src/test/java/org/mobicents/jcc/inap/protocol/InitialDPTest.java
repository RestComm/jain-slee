/*
 * InitialDPTest.java
 * JUnit based test
 *
 * Created on 24 Август 2007 г., 17:35
 */

package org.mobicents.jcc.inap.protocol;

import junit.framework.*;
import java.io.IOException;
import org.mobicents.jcc.inap.protocol.util.MessageReader;

/**
 *
 * @author Oleg Kulikov
 */
public class InitialDPTest extends TestCase {
    
    private byte[] hexDump;
    
    public InitialDPTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        MessageReader reader;
        try {
            reader = new MessageReader("/data/idpargs1.txt");
            hexDump = reader.getData();
        } catch (Exception e) {
            fail("Could not load binary data");
        }
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testDecode() {
        try {
            InitialDP idp = new InitialDP(hexDump);
            assertEquals("9047500009", idp.getCalledPartyNumber().getAddress());
            assertEquals("9023629582", idp.getCallingPartyNumber().getAddress());
        } catch (IOException e) {
            fail("Parse error: " + e.getMessage());
        }
    }
    
    public void testServiceKey() {
        try {
            InitialDP idp = new InitialDP(hexDump);
            assertEquals(3, idp.getServiceKey());
        } catch (IOException e) {
            fail("Parse error: " + e.getMessage());
        }
    }
}
