package org.mobicents.jcc.inap.protocol.tcap;
import junit.framework.*;
import java.io.IOException;
import org.mobicents.jcc.inap.protocol.util.MessageReader;
import org.mobicents.jcc.inap.protocol.CallInformationReport;
import org.mobicents.jcc.inap.protocol.Operation;

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
public class ContinueCallReportTest extends TestCase {

    private byte[] hexDump;
    
    public ContinueCallReportTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        MessageReader reader;
        try {
            reader = new MessageReader("/data/continue-callreport.txt");
            hexDump = reader.getData();
            print(hexDump);
        } catch (Exception e) {
            fail("Could not load binary data");
        }
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ContinueCallReportTest.class);
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
    
    public void testMsgTypeTest() {
        TCMessage m = null;
        try {
            m = new TCContinue(hexDump);
        } catch (IOException ex) {
            fail("Could not decode: " + ex);
        }
        
        if (m.getType() != m.CONTINUE) {
            fail("Unexpected message type: " + m.getType());
        }
    }
    
    public void testTxID() {
        TCMessage m = null;
        try {
            m = new TCContinue(hexDump);
        } catch (IOException ex) {
            fail("Could not decode: " + ex);
        }
        if (m.getTxID() != 1880397044L) {
            fail("Unexpected TxID: " + m.getTxID());
        }
    }
    
    public void testDecode() {        
        try {
            TCMessage m = new TCContinue(hexDump);
            if (m.getDialogue() != null) {
                fail("Dialogue not expected");
            }
            
            if (m.getComponents() == null) {
                fail("Component expected");
            }
            
            Components components = m.getComponents();
            if (components.size() != 1) {
                fail("Expected only one component");
            }
            
            Invoke invoke = ((Invoke)components.get(0));
            assertEquals(2, invoke.getInvokeID());
            
            Operation operation = invoke.getOperation();
            if (operation == null) {
                fail("Operation expected");
            }
            if (!(operation instanceof CallInformationReport)) {
                fail("CallInformationReport expected");
            }
        } catch (IOException ex) {
            fail("Could not decode: " + ex);
        }
    }
}
