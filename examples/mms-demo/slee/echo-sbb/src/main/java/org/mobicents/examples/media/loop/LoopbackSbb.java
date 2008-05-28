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

package org.mobicents.examples.media.loop;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import org.apache.log4j.Logger;
import org.mobicents.media.msc.common.MsLinkMode;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsLinkEvent;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class LoopbackSbb implements Sbb {

    public final static String LOOP_ENDPOINT = "media/test/Loopback/1";
    private SbbContext sbbContext;
    private MsProvider msProvider;
    private MediaRaActivityContextInterfaceFactory mediaAcif;
    private Logger logger = Logger.getLogger(LoopbackSbb.class);
    
    /**
     * Starts dialog.
     * 
     * @param endpointName the user's endpoint.
     * @param aci the user's activity context interface.
     */
    public void startConversation(String endpointName) {
        logger.info("Joining " + endpointName + " with " + LOOP_ENDPOINT);
        
        MsConnection connection = (MsConnection) sbbContext.getActivities()[0].getActivity();
        MsSession session = connection.getSession();
        MsLink link = session.createLink(MsLinkMode.FULL_DUPLEX);

        ActivityContextInterface linkActivity = null;
        try {
            linkActivity = mediaAcif.getActivityContextInterface(link);
        } catch (UnrecognizedActivityException ex) {
        }

        linkActivity.attach(sbbContext.getSbbLocalObject());
        link.join(endpointName, LOOP_ENDPOINT);
    }
    
    /**
     * Terminates conversation
     */
    public void stop() {
        getLink().release();
    }

    public void onLinkCreated(MsLinkEvent evt, ActivityContextInterface aci) {
        logger.info("Link created, Endpoint=" + evt.getSource().getEndpoints()[1]);
    }

    public void onLinkFailed(MsLinkEvent evt, ActivityContextInterface aci) {
        logger.error("Joining error: cause = " + evt.getCause());
    }
        
    public MsLink getLink() {
        ActivityContextInterface[] activities = sbbContext.getActivities();
        for (int i = 0; i < activities.length; i++) {
            if (activities[i].getActivity() instanceof MsLink) {
                return (MsLink) activities[i].getActivity();
            }
        }
        return null;
    }
    
    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            msProvider = (MsProvider) ctx.lookup("slee/resources/media/1.0/provider");
            mediaAcif = (MediaRaActivityContextInterfaceFactory) ctx.lookup("slee/resources/media/1.0/acifactory");
        } catch (Exception ne) {
            logger.error("Could not set SBB context:", ne);
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
