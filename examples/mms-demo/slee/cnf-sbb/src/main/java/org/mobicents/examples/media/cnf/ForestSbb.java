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
package org.mobicents.examples.media.cnf;

import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import org.apache.log4j.Logger;
import org.mobicents.examples.media.Announcement;
import org.mobicents.media.msc.common.MsLinkMode;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionEvent;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsLinkEvent;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class ForestSbb implements Sbb {

    public final static String CNF_ENDPOINT = "media/trunk/Conference/$";
    public final static String CRICKETS = "http://localhost:8080/msdemo/audio/crickets.wav";
    public final static String MOCKING = "http://localhost:8080/msdemo/audio/mocking.wav";
    public final static String CUCKOO = "http://localhost:8080/msdemo/audio/cuckoo.wav";
    private SbbContext sbbContext;
    private MsProvider msProvider;
    private MediaRaActivityContextInterfaceFactory mediaAcif;
    private Logger logger = Logger.getLogger(ForestSbb.class);

    /**
     * Starts dialog.
     * 
     * @param endpointName the user's endpoint.
     */
    public void enter(String endpointName) {
        logger.info("Joining " + endpointName + " with " + CNF_ENDPOINT);

        MsConnection connection = (MsConnection) getConnectionActivity().getActivity();
        MsSession session = connection.getSession();
        MsLink link = session.createLink(MsLinkMode.FULL_DUPLEX);

        ActivityContextInterface linkActivity = null;
        try {
            linkActivity = mediaAcif.getActivityContextInterface(link);
        } catch (UnrecognizedActivityException ex) {
        }

        linkActivity.attach(sbbContext.getSbbLocalObject());
        link.join(endpointName, CNF_ENDPOINT);
    }

    public void onConfBridgeCreated(MsLinkEvent evt, ActivityContextInterface aci) {
        MsLink link = evt.getSource();
        String endpointName = link.getEndpoints()[1];

        logger.info("Created conference bridge: " + endpointName);
        ActivityContextInterface connectionActivity = this.getConnectionActivity();

        ChildRelation childRelation = this.getParticipantSbb();
        try {
            logger.info("Joining crickets: " + CRICKETS);
            Announcement crickets = (Announcement) childRelation.create();
            connectionActivity.attach(crickets);

            List cricketVoice = new ArrayList();
            cricketVoice.add(CRICKETS);
            crickets.play(endpointName, cricketVoice, true);

            logger.info("Joining mocking: " + MOCKING);
            List mockingVoice = new ArrayList();
            mockingVoice.add(MOCKING);
            Announcement mocking = (Announcement) childRelation.create();
            connectionActivity.attach(mocking);

            mocking.play(endpointName, mockingVoice, true);

            logger.info("Joining cuckoo: " + CUCKOO);
            Announcement cuckoo = (Announcement) childRelation.create();
            connectionActivity.attach(cuckoo);

            List cuckooVoice = new ArrayList();
            cuckooVoice.add(CUCKOO);
            cuckoo.play(endpointName, cuckooVoice, true);
        } catch (CreateException e) {
            logger.error("Unexpected error", e);
        }
    }

    public void onConfBridgeFailed(MsLinkEvent evt, ActivityContextInterface aci) {
        logger.error("Joining error: cause = " + evt.getCause());
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

    public abstract ChildRelation getParticipantSbb();

    public ActivityContextInterface getConnectionActivity() {
        ActivityContextInterface[] activities = sbbContext.getActivities();
        for (int i = 0; i < activities.length; i++) {
            if (activities[i].getActivity() instanceof MsConnection) {
                return activities[i];
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
