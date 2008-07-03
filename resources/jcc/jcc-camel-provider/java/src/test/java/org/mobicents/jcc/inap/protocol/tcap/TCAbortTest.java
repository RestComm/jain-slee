/*
 * TCAbortTest.java
 * JUnit based test
 *
 * Created on 27 Август 2007 г., 18:19
 */

package org.mobicents.jcc.inap.protocol.tcap;

import junit.framework.*;

/**
 *
 * @author Oleg Kulikov
 */
public class TCAbortTest extends TestCase {
    
    private byte[] bin = new byte[] {
        (byte)0x49, 0x04, (byte)0x60, 0x14, (byte)0x9F, 
        (byte)0xB8, (byte)0x6B, 0x12, 0x28, 0x10, 
        0x06, 0x07, 0x00, 0x11, (byte)0x86, 0x05, 0x01, 0x01, 0x01, (byte)0xA0, 
        0x05, 0x64, 
        0x03, (byte)0x80, 0x01, 0x00
    };
    
    private byte[] hex = new byte[] {
        (byte)0x67, (byte)0x1A, (byte)0x49, 0x04, (byte)0x60, 0x14, (byte)0x9F, 
        (byte)0xB8, (byte)0x6B, 0x12, 0x28, 0x10, 
        0x06, 0x07, 0x00, 0x11, (byte)0x86, 0x05, 0x01, 0x01, 0x01, (byte)0xA0, 
        0x05, 0x64, 
        0x03, (byte)0x80, 0x01, 0x00
    };
    
    public TCAbortTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TCAbortTest.class);
        return suite;
    }

    public void testTxID() {
        try {
            TCAbort abort = new TCAbort(bin);
            assertEquals(1611964344L, abort.getTxID());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
}
