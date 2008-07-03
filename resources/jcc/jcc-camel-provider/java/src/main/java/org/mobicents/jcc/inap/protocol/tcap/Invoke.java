/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.jcc.inap.protocol.tcap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import org.mobicents.jcc.inap.protocol.CallInformationReport;
import org.mobicents.jcc.inap.protocol.EventReportBCSM;
import org.mobicents.jcc.inap.protocol.InitialDP;
import org.mobicents.jcc.inap.protocol.Operation;
import org.mobicents.jcc.inap.protocol.UnknownOperation;

/**
 *
 * @author Oleg Kulikov
 */
public class Invoke extends Component {
    
    private int invokeID;
    private Operation operation;
    
    /** Creates a new instance of Invoke */
    public Invoke(int invokeID, Operation operation) {
        this.invokeID = invokeID;
        this.operation = operation;
    }
    
    public Invoke(byte[] bin) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bin);

        //decode invokeID
        int tag = in.read();
        int len = Util.readLen(in);
        
        invokeID = in.read();
        
        //decode operation code
        tag = Util.readTag(in);
        len = Util.readLen(in);
        
        int code = in.read() & 0xff; //Util.readTag(in);
        
        tag = in.read();
        len = Util.readLen(in);

        byte[] buff = new byte[len];
        in.read(buff);
        
        switch (code) {
            case Operation.INITIAL_DP :
                operation = new InitialDP(buff);
                break;
            case Operation.CALL_INFORMATION_REPORT:
                operation = new CallInformationReport(buff);
                break;
            case Operation.EVENT_REPORT_BCSM :
                operation = new EventReportBCSM(buff);
                break;
            default : operation = new UnknownOperation(code, buff);
        }
    }
    
    public int getInvokeID() {
        return invokeID;
    }
    
    public Operation getOperation() {
        return operation;
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        bout.write(0x02);     //TAG: class=0 code=2 type=0
        bout.write(0x01);     //Length........................ 1 Dec
        bout.write(invokeID); //Invoke ID
        
        try {
            bout.write(operation.toByteArray());
        } catch (IOException e) {
        }
        
        byte[] buff = bout.toByteArray();
        bout = new ByteArrayOutputStream();
        
        bout.write(0xA1);
        bout.write(buff.length);
        
        try {
            bout.write(buff);
        } catch (IOException e) {
            //never happen
        }
        
        return bout.toByteArray();
    }
    
    public String toString() {
        return "[InvokeID=" + invokeID + ", " + operation + "]";
    }
}
