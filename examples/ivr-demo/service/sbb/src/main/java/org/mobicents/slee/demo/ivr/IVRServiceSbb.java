/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.demo.ivr;

import jain.protocol.ip.mgcp.JainMgcpProvider;
import jain.protocol.ip.mgcp.message.Notify;
import java.io.InputStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;

/**
 *
 * @author kulikov
 */
public class IVRServiceSbb implements Sbb {

    private SbbContext sbbContext;
    private ProcessInstance menu;
    
    private Tracer tracer;
    
    private JainMgcpProvider mgcpProvider;
    private MgcpActivityContextInterfaceFactory mgcpAcif;
    
    public void onCallConnected(CallConnectedEvent event, ActivityContextInterface aci) {
        System.out.println("=======YES YES YES==========");
        InputStream in = IVRServiceSbb.class.getResourceAsStream("/ivr-interface.xml");

        ProcessDefinition processDefinition = ProcessDefinition.parseXmlInputStream(in);
        menu = new ProcessInstance(processDefinition);

        //hold caller ID as process variable
        ContextInstance ctx = menu.getContextInstance();
        ctx.setVariable("caller", event.getCaller());

        //also we shoudl pass reference of the media connection and media provider
        //to process as variables
        ctx.setVariable("provider", mgcpProvider);
        ctx.setVariable("connection", event.getConnection());

        //save process instance as attribute with SBB activity context
        menu.getRootToken().signal();    
    }
    
    public void onAnnouncementComplete(Notify event, ActivityContextInterface aci) {
        
    }

    public void onTouchTone(Notify event, ActivityContextInterface aci) {
        
    }
    
    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        tracer = sbbContext.getTracer("VoiceMenu[Call blocking]");
        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");

            // initialize media api
            mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider/demo");
            mgcpAcif = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory/demo");
        } catch (Exception ne) {
            tracer.severe("Could not set SBB context:", ne);
        }
    }

    public void unsetSbbContext() {
    }

    public void sbbCreate() throws CreateException {
    }

    public void sbbPostCreate() throws CreateException {
    }

    public void sbbActivate() {
    }

    public void sbbPassivate() {
    }

    public void sbbLoad() {
    }

    public void sbbStore() {
    }

    public void sbbRemove() {
   }

    public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
    }

    public void sbbRolledBack(RolledBackContext arg0) {
    }

}
