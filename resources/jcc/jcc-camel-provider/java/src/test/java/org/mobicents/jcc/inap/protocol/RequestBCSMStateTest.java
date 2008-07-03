package org.mobicents.jcc.inap.protocol;
import junit.framework.*;
import org.mobicents.jcc.inap.protocol.parms.BCSMEvent;

/*
 * RequestBCSMStateTest.java
 * JUnit based test
 *
 * Created on 23 Август 2007 г., 10:57
 */

/**
 *
 * @author Oleg Kulikov
 */
public class RequestBCSMStateTest extends TestCase {
    
    private byte[] bin = new byte[] {
        0x02, 0x01, 0x17, 0x30, (byte)0x0A,
        (byte) 0xA0, 0x08, 0x30, 0x06, (byte)0x80, 0x01, 0x12, (byte)0x81, 0x01,
        0x01
    };
    
    public RequestBCSMStateTest(String testName) {
        super(testName);
    }
    
    
    @Override
    protected void setUp() throws Exception {
    }
    
    @Override
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(RequestBCSMStateTest.class);
        return suite;
    }
    
    private void print(byte[] x) {
        for (int i = 0; i < x.length; i++) {
            String s = Integer.toHexString(x[i] & 0xff);
            if (s.length() == 2) {
                System.out.print(" 0x" + s);
            } else {
                System.out.print(" 0x0" + s);
            }
            System.out.println();
        }
    }
    
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.RequestBCSMState.
     */
    public void testToByteArray() {
        RequestBCSMState rstate = new RequestBCSMState();
        rstate.add(new BCSMEvent(BCSMEvent.T_ABANDON));
        byte[] data = rstate.toByteArray();
        print(data);
        
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + 
                    " bytes instead of " + data.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
