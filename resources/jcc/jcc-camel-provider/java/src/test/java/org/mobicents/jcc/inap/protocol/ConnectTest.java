/*
 * ConnectTest.java
 * JUnit based test
 *
 * Created on 23 Август 2007 г., 20:05
 */

package org.mobicents.jcc.inap.protocol;

import junit.framework.*;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;

/**
 *
 * @author Oleg Kulikov
 */
public class ConnectTest extends TestCase {
    
    byte[] bin = new byte[] {
        0x02, 0x01, 0x14, (byte)0x30, 0x0C, (byte)0xA0, 0x0A, 0x04,
        0x08, (byte)0x84,
        0x10, (byte)0x97, 0x40,  0x34, (byte)0x54, (byte)0x76, 0x03
    };
    
    public ConnectTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ConnectTest.class);
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
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.Connect.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        CalledPartyNumber cpn = new CalledPartyNumber(CalledPartyNumber.INTERNATIONAL, 0, 1, "79044345673");
        print(cpn.toByteArray());
        
        Connect connect = new Connect(cpn);
        
        byte[] data = connect.toByteArray();
        
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes[len=" + data.length + "]");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i 
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
