package org.mobicents.jcc.inap.protocol;
import junit.framework.*;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationType;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationTypeList;

/*
 * CallInformationRequestTest.java
 * JUnit based test
 *
 * Created on 23 Август 2007 г., 9:18
 */

/**
 *
 * @author Oleg Kulikov
 */
public class CallInformationRequestTest extends TestCase {
    
    private byte[] bin = new byte[] {
        0x02, 0x01, 0x2D, 0x30, 0x10,
        (byte)0xA0, 0x09, 0x0A, 0x01, 0x01, 0x0A, 0x01, 0x02, 0x0A, 0x01, 0x1E,
        (byte)0xA3, 0x03, (byte)0x80, 0x01, 0x02
    };
    
    public CallInformationRequestTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
    }
    
    @Override
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(CallInformationRequestTest.class);
        return suite;
    }
    
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.CallInformationRequest.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        RequestedInformationTypeList list = new RequestedInformationTypeList();
        list.add(RequestedInformationType.CALL_STOP_TIME);
        list.add(RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME);
        list.add(RequestedInformationType.RELEASE_CAUSE);
        
        LegID legID = new LegID(LegID.SENDING_SIDE_ID, LegID.SECOND_LEG);

        CallInformationRequest cir = new CallInformationRequest(list, legID);
        
        byte[] data = cir.toByteArray();
        
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
