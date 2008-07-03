/*
 * RequestInformationTypeListTest.java
 * JUnit based test
 *
 * Created on 25 Август 2007 г., 15:09
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;

/**
 *
 * @author Oleg Kulikov
 */
public class RequestInformationTypeListTest extends TestCase {
    
    private byte[] bin = new byte[] {
        (byte)0xA0, 0x09, 0x0A, 0x01, 0x01, 0x0A, 0x01, 0x02, 0x0A, 0x01, 0x1E
    };
    
    public RequestInformationTypeListTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(RequestInformationTypeListTest.class);
        return suite;
    }
    
    /**
     * Test of add method, of class org.itech.jcc.inap.protocol.parms.RequestInformationTypeList.
     */
    public void testAdd() {
        System.out.println("add");
        RequestedInformationTypeList list = new RequestedInformationTypeList();
        list.add(RequestedInformationType.CALL_STOP_TIME);
        assertEquals(1, list.size());
    }
    
    /**
     * Test of remove method, of class org.itech.jcc.inap.protocol.parms.RequestInformationTypeList.
     */
    public void testRemove() {
        System.out.println("remove");
        RequestedInformationTypeList list = new RequestedInformationTypeList();
        list.add(RequestedInformationType.CALL_STOP_TIME);
        assertEquals(1, list.size());
        list.remove(RequestedInformationType.CALL_STOP_TIME);
        assertEquals(0, list.size());
    }
    
    /**
     * Test of size method, of class org.itech.jcc.inap.protocol.parms.RequestInformationTypeList.
     */
    public void testSize() {
        System.out.println("remove");
        RequestedInformationTypeList list = new RequestedInformationTypeList();
        list.add(RequestedInformationType.CALL_STOP_TIME);
        assertEquals(1, list.size());
        list.remove(RequestedInformationType.CALL_STOP_TIME);
        assertEquals(0, list.size());
    }
    
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.parms.RequestInformationTypeList.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        RequestedInformationTypeList list = new RequestedInformationTypeList();
        list.add(RequestedInformationType.CALL_STOP_TIME);
        list.add(RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME);
        list.add(RequestedInformationType.RELEASE_CAUSE);
        
        byte[] data = list.toByteArray();
        if (bin.length != data.length) {
            fail("The lenth of encoded data should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
