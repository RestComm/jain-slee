/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.slee.demo.ivr.sip;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;
import org.mobicents.slee.demo.ivr.media.Connection;
import org.mobicents.slee.demo.ivr.media.ConnectionState;

/**
 *
 * @author kulikov
 */
public abstract class BaseSbb implements Sbb {

    protected static int CALL_ID_GEN = 1;
    protected static int GEN = 1000;
    
    public final static String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");
    public final static String ENDPOINT_NAME = "/mobicents/media/IVR/$";
    
    protected SbbContext sbbContext;
    
    protected JainMgcpProvider mgcpProvider;
    protected MgcpActivityContextInterfaceFactory mgcpAcif;

    protected Tracer tracer;

    protected SleeSipProvider sipProvider;
    protected AddressFactory addressFactory;
    protected HeaderFactory headerFactory;
    protected MessageFactory messageFactory;
    protected SipActivityContextInterfaceFactory acif;
    
    protected String callIdentifier;
    
    
    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        tracer = sbbContext.getTracer("Call Controller");
        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");

            // initialize SIP API
            sipProvider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");

            addressFactory = sipProvider.getAddressFactory();
            headerFactory = sipProvider.getHeaderFactory();
            messageFactory = sipProvider.getMessageFactory();
            acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

            // initialize media api

            mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider/demo");
            mgcpAcif = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory/demo");

        } catch (Exception ne) {
            tracer.severe("Could not set SBB context:", ne);
        }
    }

    public void unsetSbbContext() {
        sbbContext = null;
    }

    protected ActivityContextInterface getConnectionActivity() {
        ActivityContextInterface activities[] = sbbContext.getActivities();
        for (ActivityContextInterface aci : activities) {
            if (aci.getActivity() instanceof MgcpConnectionActivity) {
                return aci;
            }
        }
        return null;
    }

    protected ActivityContextInterface getDialogActivity() {
        ActivityContextInterface activities[] = sbbContext.getActivities();
        for (ActivityContextInterface aci : activities) {
            if (aci.getActivity() instanceof Dialog) {
                return aci;
            }
        }
        return null;
    }
    
    public abstract Connection asSbbActivityContextInterface(
            ActivityContextInterface aci);
    
    protected ConnectionState getState() {
        ActivityContextInterface activity = this.getDialogActivity();
        return this.asSbbActivityContextInterface(activity).getState();
    }
    
    protected void setState(ConnectionState state) {
        ActivityContextInterface activity = this.getDialogActivity();
        asSbbActivityContextInterface(activity).setState(state);
    }
    

    protected String getEndpointID() {
        ActivityContextInterface activity = this.getDialogActivity();
        return this.asSbbActivityContextInterface(activity).getEndpoint();
    }
    
    protected void setEndpoint(String endpoint) {
        ActivityContextInterface activity = this.getDialogActivity();
        asSbbActivityContextInterface(activity).setEndpoint(endpoint);
    }

    protected String getConnectionID() {
        ActivityContextInterface activity = this.getDialogActivity();
        return this.asSbbActivityContextInterface(activity).getConnectionID();
    }
    
    protected void setConnectionID(String connectionID) {
        ActivityContextInterface activity = this.getDialogActivity();
        asSbbActivityContextInterface(activity).setConnectionID(connectionID);
    }

    protected String getCallID() {
        ActivityContextInterface activity = this.getDialogActivity();
        return this.asSbbActivityContextInterface(activity).getCallID();
    }
    
    protected void setCallID(String callID) {
        ActivityContextInterface activity = this.getDialogActivity();
        asSbbActivityContextInterface(activity).setCallID(callID);
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
