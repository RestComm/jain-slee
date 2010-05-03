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

package org.mobicents.jcc.inap.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import org.mobicents.jcc.inap.protocol.parms.BCSMEvent;

/**
 *
 * @author Oleg Kulikov
 */
public class RequestBCSMState extends Operation {
	public static final int _TAG = 16;
	public static final int _TAG_CLASS = 0;
	public static final boolean _IS_PRIMITIVE = false;
	
    private ArrayList events = new ArrayList();
    
    /** Creates a new instance of RequestBCSMState */
    public RequestBCSMState() {
    }
    
    public void add(BCSMEvent event) {
        events.add(event);
    }
    
    public void remove(BCSMEvent event) {
        events.remove(event);
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        
        try {
            Iterator list = events.iterator();
            while (list.hasNext()) {
                BCSMEvent event = (BCSMEvent) list.next();
                bout.write(event.toByteArray());
            }
            //collection of events without tags
            byte[] buff = bout.toByteArray();
            bout = new ByteArrayOutputStream();
            
            //int len = bout.size();
            bout.reset();
            //BCSM events
            bout.write(0xA0);
            bout.write(buff.length);
            bout.write(buff);
            
           // buff = bout.toByteArray();
           // bout = new ByteArrayOutputStream();
            
            //Request report BCSM event
            //parameter
            //bout.write(0x30);
            //bout.write(buff.length);
            //bout.write(buff);
            
           // buff = bout.toByteArray();
           // bout = new ByteArrayOutputStream();
            
            //operation code!
            //bout.write(0x02);
            //bout.write(0x01);
            //bout.write(REQUEST_REPORT_BCSM_EVENT);
            //bout.write(buff.length);
           // bout.write(buff);
            
        } catch (IOException e) {
            //never happen
        }
        return bout.toByteArray();
    }
 
    public String toString() {
        String s = "RequestReportBCSMEvent[";
        Iterator list = events.iterator();
        while (list.hasNext()) {
            s += list.next() + ";";
        }
        
        s += "]";
        return s;
    }
}
