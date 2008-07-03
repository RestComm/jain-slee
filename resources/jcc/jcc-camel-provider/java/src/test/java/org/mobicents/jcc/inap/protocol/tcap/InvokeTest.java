package org.mobicents.jcc.inap.protocol.tcap;
import junit.framework.*;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.util.MessageReader;
import org.mobicents.jcc.inap.protocol.ApplyCharging;
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
public class InvokeTest extends TestCase {
    
    private byte[] bin = new byte[] {
        (byte)0xA1, 0x1A, 0x02, 0x01, 0x01, 0x02, 0x01, 0x23, 0x30, 0x12, (byte)0x80,
        0x0B, (byte)0xA0, 0x09, (byte)0x80, 0x02, 0x46, 0x50, (byte)0xA1,
        0x03, 0x01, 0x01, 0x01, (byte)0xA2, 0x03, (byte)0x80, 0x01, 0x02
    };
    
    private byte[] hexDump;
    
    public InvokeTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        MessageReader reader;
        try {
            //reader = new MessageReader("org/mobicents/jcc/inap/protocol/data/invoke-idp.txt");
            reader = new MessageReader("/data/invoke-idp.txt");
            hexDump = reader.getData();
        } catch (Exception e) {
            fail("Could not load binary data");
        }
    }
    
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(InvokeTest.class);
        return suite;
    }
    
    public void testInvokeID() {
        try {
            Invoke invoke = new Invoke(hexDump);
            assertEquals(1, invoke.getInvokeID());
        } catch (Exception e) {
            fail("Could not decode invoke: " + e);
        }
    }
    
    public void testOperation() {
        try {
            Invoke invoke = new Invoke(hexDump);
            Operation op = invoke.getOperation();
            if (op == null) fail("Operation expected");
            if (!(op instanceof InitialDP)) fail("InitialDP expected");
        } catch (Exception e) {
            fail("Could not decode invoke: " + e);
        }
        
    }
    
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.InvokeComponent.
     */
    public void testToByteArray() throws Exception {
        ApplyCharging applyCharging = new ApplyCharging(LegID.SENDING_SIDE_ID, 
                LegID.SECOND_LEG);
        Invoke invoke = new Invoke(1, applyCharging);
        
        byte[] data = invoke.toByteArray();
        
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
