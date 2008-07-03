/*
 * ComponentsTest.java
 * JUnit based test
 *
 * Created on 25 Август 2007 г., 7:50
 */

package org.mobicents.jcc.inap.protocol.tcap;

import junit.framework.*;
import org.mobicents.jcc.inap.protocol.ApplyCharging;
import org.mobicents.jcc.inap.protocol.CallInformationRequest;
import org.mobicents.jcc.inap.protocol.Connect;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationType;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationTypeList;
import org.mobicents.jcc.inap.protocol.util.MessageReader;

/**
 *
 * @author Oleg Kulikov
 */
public class ComponentsTest extends TestCase {
    
    private byte[] hexDump;
    private byte[] bin;
    
    public ComponentsTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        MessageReader reader;
        try {
            reader = new MessageReader("/data/components-invoke-idp.txt");
            hexDump = reader.getData();
            reader = new MessageReader("/data/ac-ci-con.txt");
            bin = reader.getData();
        } catch (Exception e) {
            fail("Could not load binary data");
        }
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ComponentsTest.class);
        
        return suite;
    }
    
    public void testDecode() {
        try {
            Components components = new Components(hexDump);
            assertEquals(1,components.size());
            
            Invoke invoke = (Invoke) components.get(0);
            assertEquals(1, invoke.getInvokeID());
        } catch (Exception e) {
            fail("Could not decode invoke: " + e);
        }
    }
    
    private void addApplyCharging(Components components, int invokeID) {
        ApplyCharging applyCharging = new ApplyCharging(
                LegID.SENDING_SIDE_ID,
                LegID.SECOND_LEG);
        components.add(new Invoke(invokeID, applyCharging));
    }
    
    private void addCallInformationRequest(Components components, int invokeID) {
        RequestedInformationTypeList list = new RequestedInformationTypeList();
        list.add(RequestedInformationType.CALL_STOP_TIME);
        list.add(RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME);
        list.add(RequestedInformationType.RELEASE_CAUSE);
        
        LegID legID = new LegID(LegID.SENDING_SIDE_ID, LegID.SECOND_LEG);
        
        CallInformationRequest cir = new CallInformationRequest(list, legID);
        components.add(new Invoke(invokeID, cir));
    }
    
    private void addConnect(Components components, int invokeID) {
        CalledPartyNumber cpn = new CalledPartyNumber(CalledPartyNumber.INTERNATIONAL, 0, 1,"79044345673");
        Connect connect = new Connect(cpn);
        components.add(new Invoke(invokeID, connect));
    }
    
    public void testToByteArray() {
        Components components = new Components();
        addApplyCharging(components, 1);
        addCallInformationRequest(components, 3);
        addConnect(components, 4);
        
        byte[] data = components.toByteArray();
        
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
}
