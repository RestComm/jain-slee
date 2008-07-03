package org.mobicents.jcc.inap.protocol.tcap;
import junit.framework.*;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationType;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationTypeList;
import org.mobicents.jcc.inap.protocol.util.MessageReader;
import org.mobicents.jcc.inap.protocol.ApplyCharging;
import org.mobicents.jcc.inap.protocol.CallInformationRequest;
import org.mobicents.jcc.inap.protocol.Connect;

/*
 * InvokeComponentTest.java
 * JUnit based test
 *
 * Created on 23 Август 2007 г., 10:52
 */

/**
 *
 * @author Oleg Kulikov
 */
public class ContinueConnectTest extends TestCase {

    private byte[] bin;
    
    public ContinueConnectTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        MessageReader reader;
        try {
            reader = new MessageReader("/data/continue-connect.txt");
            bin = reader.getData();
        } catch (Exception e) {
            fail("Could not load binary data");
        }
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ContinueConnectTest.class);
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
        CalledPartyNumber cpn = new CalledPartyNumber(CalledPartyNumber.INTERNATIONAL, 0,1, "79044345673");
        Connect connect = new Connect(cpn);
        components.add(new Invoke(invokeID, connect));
    }
    
    public void testToByteArray() {
        Components components = new Components();
        addApplyCharging(components, 1);
        addCallInformationRequest(components, 3);
        addConnect(components, 4);
        
        TCContinue message = new TCContinue(1880328954L);
        message.setDialogue(new DialoguePortion());
        message.setComponents(components);
        
        byte[] data = message.toByteArray();
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i 
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
}
