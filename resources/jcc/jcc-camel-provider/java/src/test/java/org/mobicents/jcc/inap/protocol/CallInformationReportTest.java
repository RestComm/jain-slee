/*
 * CallInformationReportTest.java
 * JUnit based test
 *
 * Created on 27 Август 2007 г., 14:38
 */

package org.mobicents.jcc.inap.protocol;

import junit.framework.*;
import java.io.IOException;
import org.mobicents.jcc.inap.protocol.parms.CallConnectedElapsedTime;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformation;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationType;
import org.mobicents.jcc.inap.protocol.util.MessageReader;

/**
 *
 * @author Oleg Kulikov
 */
public class CallInformationReportTest extends TestCase {
    
    private byte[] hexDump;
    
    public CallInformationReportTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        MessageReader reader;
        try {
            reader = new MessageReader("/data/callreport.txt");
            hexDump = reader.getData();
        } catch (Exception e) {
            fail("Could not load binary data");
        }
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CallInformationReportTest.class);
        return suite;
    }

    public void testReqInfo() {
        try {
            CallInformationReport rep = new CallInformationReport(hexDump);
            int count = rep.getReqInfo().size();
            assertEquals(3,count);
            
            RequestedInformation ri = (RequestedInformation)rep.getReqInfo().get(1);
            assertEquals(RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME, ri.getType());
            
            CallConnectedElapsedTime dur = (CallConnectedElapsedTime) ri.getValue();
            dur.getValue();
            System.out.println(dur.getValue());
            System.out.println(dur);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    public void testLegID() {
        try {
            CallInformationReport rep = new CallInformationReport(hexDump);
            LegID legID = rep.getLegID();
            assertEquals(LegID.RECEIVING_SIDE_ID,legID.getSideID());
            assertEquals(LegID.SECOND_LEG,legID.getLegID());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
}
