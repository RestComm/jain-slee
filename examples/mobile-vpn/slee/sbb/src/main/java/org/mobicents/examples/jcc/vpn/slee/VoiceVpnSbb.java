/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.examples.jcc.vpn.slee;

import java.util.Date;
import javax.csapi.cc.jcc.JccConnection;
import javax.csapi.cc.jcc.JccConnectionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.StatelessSession;
import org.mobicents.examples.jcc.vpn.rules.Call;

/**
 *
 * @author kulikov
 */
public abstract class VoiceVpnSbb implements Sbb {

    private final static String RULEBASE_NAME = "java:/mobicents/voicevpn/RuleBase";    
    private final static String EMERGENCY_CALL = "0[1,2,3,4][0]{0,1}";
    
    private Logger logger = Logger.getLogger(VoiceVpnSbb.class);
    
    private SbbContext sbbContext;
    private RuleBase ruleBase;
    
    
    /**
     * EventHandler method for incoming events of type "InitEvent". InitEvent is
     * defined in the deployment descriptor "sbb-jar.xml".
     * This method is invoked by the SLEE if an event of type INIT is received and fired
     * by the resource adaptor.
     */
    public void onAuthorizeCallAttempt(JccConnectionEvent event, ActivityContextInterface aci) {
        JccConnection connection = event.getConnection();
        
        String callingParty = connection.getOriginatingAddress().getName();
        String calledParty = connection.getAddress().getName();
        
        logger.info(connection.getCall() + ",callingParty={" + callingParty + 
                "}, calledParty={" + calledParty + "}, state=AUTHORIZE_ATTEMPT");
        
        Call call = new Call(callingParty, calledParty, "IN");
        if (routeCall(call, connection)) {
            asSbbActivityContextInterface(aci).setCall(call);
        } else {
            aci.detach(sbbContext.getSbbLocalObject());
        }       
    }
    
    /**
     * EventHandler method for incoming events of type "InitEvent". InitEvent is
     * defined in the deployment descriptor "sbb-jar.xml".
     * This method is invoked by the SLEE if an event of type INIT is received and fired
     * by the resource adaptor.
     */
    public void onAddressAnalyze(JccConnectionEvent event, ActivityContextInterface aci) {
        JccConnection connection = event.getConnection();
        String destination = connection.getDestinationAddress();

        String callingParty = connection.getAddress().getName();
        String calledParty = connection.getDestinationAddress();
        
        logger.info(connection.getCall() + ",callingParty={" + callingParty + 
                "}, calledParty={" + calledParty + "}, state=ADDRESS_ANALYZE");
        
        Call call = new Call(callingParty, calledParty, "OUT");        
        if (routeCall(call, connection)) {
            asSbbActivityContextInterface(aci).setCall(call);
        } else {
            aci.detach(sbbContext.getSbbLocalObject());
        }        
    }

    public void onConnectionConnected(JccConnectionEvent event, ActivityContextInterface aci) {
        asSbbActivityContextInterface(aci).setStartTime(new Date());
    }    
    
    public void onConnectionDisconnected(JccConnectionEvent event, ActivityContextInterface aci) {
        int cause = event.getCause();
        logger.info("Connection disconnected event: cause =" + cause);
        
        if (cause != JccConnectionEvent.CAUSE_NORMAL) {
            return;
        }
        
        Call call = asSbbActivityContextInterface(aci).getCall();
        
        Date startTime = asSbbActivityContextInterface(aci).getStartTime();
        Date now = new Date();
        
        int duration = (int) ((now.getTime() - startTime.getTime())/1000);
        String A,B;
        int dir = 0;
        
        if (call.getDirection().equals("OUT")) {
            A = call.getCallingParty();
            B = call.getCalledParty();
        } else {
            B = call.getCallingParty();
            A = call.getCalledParty();
            dir = 1;
        }
        this.writeCDR(call.getNetworkID(), startTime, dir, A, B, duration);
    }
    
    private boolean routeCall(Call call, JccConnection connection) {
        //checking rule base itself
        if (ruleBase == null) {
            //if rulebase is not assigned (server side error) and dialed number
            //matches to emergency service number we should try to connect
            //caller to dialed number
            boolean isEmergency =this.isEmergencyCall(call);
            
            logger.error("Route rule base is not defined, direction=" + 
                    call.getDirection() + ", emergency=" + isEmergency);

            if (isEmergency) {
                return connect(connection, connection.getDestinationAddress());
            } else {
                return release(connection, JccConnectionEvent.CAUSE_CALL_RESTRICTED);
            }
            
        }
        
        //nice, rule base is assigned. 
        //let's check if the caller is VPN subscriber?
        StatelessSession routeSession = ruleBase.newStatelessSession();
        routeSession.execute(call);
        
        if (call.getNetworkID() == null || call.getNetworkID().trim().length() == 0) {
            logger.info(connection.getCall() + 
                    ",callingParty={" + call.getCallingParty() + 
                "}, calledParty={" + call.getCalledParty() + "}, non VPN user");
            return false;
        } else {
 		logger.info(connection.getCall() + ", VPN ID= " + call.getNetworkID());
        }

        boolean isEmergency = this.isEmergencyCall(call);
        
        //again, if dialed number relates to emergency call connect it immediately
        if (isEmergency) {
            logger.info(connection.getCall() + 
                ",callingParty={" + call.getCallingParty() + 
                "}, calledParty={" + call.getCalledParty() + "}, Emergency call");
            //we have to change format
            String destination = connection.getDestinationAddress();
            String[] tokens = destination.split(",");
            destination = tokens[0] + ",NoA=SUBSCRIBER";
            
            logger.info(connection.getCall() + 
                ",callingParty={" + call.getCallingParty() + 
                "}, calledParty={" + call.getCalledParty() + "}, forwarding to " + destination);
            return connect(connection, destination);
        }
        
        //looks like it is a regular call
        //let's find the transformatio rule
        routeSession.execute(call);
        String destination = call.getDestinationFormat() != null? 
            call.getDestination() + ",NoA=" + call.getDestinationFormat() :
            call.getDestination() + ",NoA=" + call.getCalledPartyFormat();
        
        logger.info(connection.getCall() + 
                    ",callingParty={" + call.getCallingParty() + 
                "}, calledParty={" + call.getCalledParty() + 
                "}, forwarding to {"  + destination +
                "}, CallerID=" + call.getCallerID());
            
        if (call.getDestination() == null) {
            return release(connection, JccConnectionEvent.CAUSE_CALL_RESTRICTED);
        }
        
        if (call.getCallerID() != null) {
            destination += "#" + call.getCallerID();
        }
        
        return connect(connection, destination);
    }
    
    /**
     * Check call destination if this a call is a callto emergency service.
     * 
     * @param call represents call.
     * @return true if this call routed to emergency service.
     */
    private boolean isEmergencyCall(Call call) {
        return call.getDirection().equals("OUT") && 
                call.getCalledParty().matches(EMERGENCY_CALL);
    }
    
    /**
     * Connects call to the specified destination.
     * 
     * @param connection represnts call leg to be connected.
     * @param destination the destination to connect to.
     */
    private boolean connect(JccConnection connection, String destination) {
        try {
            connection.selectRoute(destination);
            return true;
        } catch (Exception e) {
            logger.error("Could not forward call to " + destination, e);
            return false;
        }
    }
    
    /**
     * Releaces this connection with specified cause code.
     * 
     * @param connection the connection to be released.
     * @param cause the cause code.
     */
    private boolean release(JccConnection connection, int cause) {
        try {
            connection.release(cause);
        } catch (Exception e) {
            logger.warn("Could not release connection gracefully", e);
        }
        return false;
    }
    
    private void writeCDR(String vpnID, Date callTime, int direction, String A, String B, int duration) {
        logger.info("Writting CDR");
    }
    
    public abstract ConnectionActivityContext asSbbActivityContextInterface(
            ActivityContextInterface aci);
    
    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        try {
            InitialContext ic = new InitialContext();
            ruleBase = (RuleBase) ic.lookup(RULEBASE_NAME);
        } catch (NamingException e) {
            logger.error("Could not access routing rule base", e);
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
