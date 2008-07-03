package org.mobicents.jcc.inap.protocol.parms;
import junit.framework.*;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/*
 * BCSMEventTest.java
 * JUnit based test
 *
 * Created on 23 Август 2007 г., 10:07
 */

/**
 *
 * @author Oleg Kulikov
 */
public class BCSMEventTest extends TestCase {
    
    private byte[] bin = new byte[] {
        0x30, 0x06, (byte)0x80, 0x01, 0x12, (byte)0x81, 0x01, 0x01
    };
    
    public BCSMEventTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(BCSMEventTest.class);
        return suite;
    }
    
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.BCSMEvent.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        BCSMEvent evt = new BCSMEvent(BCSMEvent.T_ABANDON);
        byte[] data = evt.toByteArray();
        
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
