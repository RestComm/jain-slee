/*
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
package org.mobicents.examples.media.recorder;

import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;
import org.apache.log4j.Logger;
import org.mobicents.examples.media.Util;
import org.mobicents.media.msc.common.MsLinkMode;
import org.mobicents.media.server.impl.common.events.EventID;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionEvent;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsLinkEvent;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.mscontrol.MsSignalGenerator;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class RecorderDemoSbb implements Sbb {

    private final static String INFO_MSG =
            "recorder.wav";
    private final static String RECORDER =
            "test.wav";
    private final static String IVR_ENDPOINT = "media/endpoint/IVR";
    
    private SbbContext sbbContext;
    private MsProvider msProvider;
    private MediaRaActivityContextInterfaceFactory mediaAcif;
    private TimerFacility timerFacility;
    private Logger logger = Logger.getLogger(RecorderDemoSbb.class);

    /**
     * (Non Java-doc).
     * 
     * @see org.mobicents.examples.media.Demo#startConversation(String, ActivityContextInterface).
     */
    public void startDemo(String endpointName) {
        logger.info("Joining " + endpointName + " with " + IVR_ENDPOINT);

        MsConnection connection = (MsConnection) getConnectionActivity().getActivity();
        MsSession session = connection.getSession();
        MsLink link = session.createLink(MsLinkMode.FULL_DUPLEX);

        ActivityContextInterface linkActivity = null;
        try {
            linkActivity = mediaAcif.getActivityContextInterface(link);
        } catch (UnrecognizedActivityException ex) {
        }

        linkActivity.attach(sbbContext.getSbbLocalObject());
        link.join(endpointName, IVR_ENDPOINT);
                
    }

    public void onIVRConnected(MsLinkEvent evt, ActivityContextInterface aci) {
        logger.info("Joined IVR connected, Starting announcement and recorder");
        MsLink link = evt.getSource();
        setUserEndpoint(link.getEndpoints()[1]);
        
        MsSignalGenerator generator = msProvider.getSignalGenerator(getUserEndpoint());
        try {
            ActivityContextInterface generatorActivity = mediaAcif.getActivityContextInterface(generator);
            generatorActivity.attach(sbbContext.getSbbLocalObject());
            generator.apply(EventID.PLAY_RECORD, new String[]{Util.getURL(INFO_MSG), RECORDER});
        } catch (UnrecognizedActivityException e) {
        }

        try {
            startTimer(sbbContext.getActivities()[0], 35);
            logger.info("Timer started");
        } catch (NamingException e) {
            logger.error("Unexpected error",e);
        }        
  
    }   
    
    public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
        //disable recorder
            logger.info("Timer event,play back recorder file");
        String url = "file://" + System.getProperty("jboss.server.data.dir") + "/" + RECORDER;
        
        MsSignalGenerator generator = msProvider.getSignalGenerator(getUserEndpoint());
        generator.apply(EventID.PLAY, new String[]{url});
    }
    
    public void onAnnouncementComplete(MsLinkEvent evt, ActivityContextInterface aci) {
        logger.info("**** COMPLETE****");
    }

    public void onUserDisconnected(MsConnectionEvent evt, ActivityContextInterface aci) {
        System.out.println("Finita la commedia");
        ActivityContextInterface activities[] = sbbContext.getActivities();
        for (int i = 0; i < activities.length; i++) {
            if (activities[i].getActivity() instanceof MsLink) {
                ((MsLink) activities[i].getActivity()).release();
            }
        }

    }
    
    private ActivityContextInterface getConnectionActivity() {
        ActivityContextInterface[] activities = sbbContext.getActivities();
        for (int i = 0; i < activities.length; i++) {
            if (activities[i].getActivity() instanceof MsConnection) {
                return activities[i];
            }
        }

        return null;
    }
    
    /**
     * CMP field accessor
     *  
     * @return the name of the user's endpoint. 
     */
    public abstract String getUserEndpoint();

    /**
     * CMP field accessor
     *  
     * @param endpoint the name of the user's endpoint. 
     */
    public abstract void setUserEndpoint(String endpointName);

    /**
     * Relation to Welcome dialog
     * 
     * @return child relation object.
     */
    public abstract ChildRelation getAnnouncementSbb();

    private void startTimer(ActivityContextInterface aci, int duration) throws NamingException {
        Context ctx = (Context) new InitialContext().lookup("java:comp/env");
        timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
        
        TimerOptions options = new TimerOptions(false, 1000 * duration, TimerPreserveMissed.NONE);
        Address address = new Address(AddressPlan.IP, "127.0.0.1");
        Date now = new Date();
        
        timerFacility.setTimer(aci, address, now.getTime() + 1000 * duration, options);
    }

    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            timerFacility = (TimerFacility) ctx.lookup("slee/facilities/timer");
            msProvider = (MsProvider) ctx.lookup("slee/resources/media/1.0/provider");
            mediaAcif = (MediaRaActivityContextInterfaceFactory) ctx.lookup("slee/resources/media/1.0/acifactory");
        } catch (Exception e) {
            logger.error("Could not set SBB context", e);
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
