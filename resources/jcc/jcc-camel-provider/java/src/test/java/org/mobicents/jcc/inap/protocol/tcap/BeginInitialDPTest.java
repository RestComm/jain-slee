package org.mobicents.jcc.inap.protocol.tcap;
import junit.framework.*;
import java.io.IOException;
import org.mobicents.jcc.inap.protocol.util.MessageReader;
import org.mobicents.jcc.inap.protocol.InitialDP;
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
public class BeginInitialDPTest extends TestCase {

    private byte[] hexDump;
    
    public BeginInitialDPTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        MessageReader reader;
        try {
            reader = new MessageReader("/data/begin-idp.txt");
            hexDump = reader.getData();
            print(hexDump);
        } catch (Exception e) {
            fail("Could not load binary data");
        }
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(BeginInitialDPTest.class);
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
            m = new TCBegin(hexDump);
        } catch (IOException ex) {
            fail("Could not decode: " + ex);
        }
        
        if (m.getType() != m.BEGIN) {
            fail("Unexpected message type: " + m.getType());
        }
    }
    
    public void testTxID() {
        TCMessage m = null;
        try {
            m = new TCBegin(hexDump);
        } catch (IOException ex) {
            fail("Could not decode: " + ex);
        }
        if (m.getTxID() != 2148746992L) {
            fail("Unexpected TxID: " + m.getTxID());
        }
    }
    
    public void testDecode() {        
        try {
            TCMessage m = new TCBegin(hexDump);
            if (m.getDialogue() == null) {
                fail("Dialogue expected");
            }
            
            if (m.getComponents() == null) {
                fail("Component expected");
            }
            
            Components components = m.getComponents();
            assertEquals(1, components.size());
            
            Invoke invoke = ((Invoke)components.get(0));
            assertEquals(1, invoke.getInvokeID());
            
            Operation operation = invoke.getOperation();
            if (operation == null) {
                fail("Operation expected");
            }
            if (!(operation instanceof InitialDP)) {
                fail("InitialDP expected");
            }
        } catch (IOException ex) {
            fail("Could not decode: " + ex);
        }
    }
}
