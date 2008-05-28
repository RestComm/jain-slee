/*
 * CallSbb.java
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
package org.mobicents.examples.media;

import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import org.apache.log4j.Logger;
import org.mobicents.media.msc.common.MsLinkMode;
import org.mobicents.media.server.impl.common.events.EventID;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionEvent;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsLinkEvent;
import org.mobicents.mscontrol.MsNotifyEvent;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.mscontrol.MsSignalGenerator;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class AnnouncementSbb implements Sbb {

    public final static String ANNOUNCEMENT_ENDPOINT = "media/trunk/Announcement/$";
    private SbbContext sbbContext;
    private MsProvider msProvider;
    private MediaRaActivityContextInterfaceFactory mediaAcif;
    private Logger logger = Logger.getLogger(AnnouncementSbb.class);

    public void play(String userEndpoint,
            List announcements, boolean keepAlive) {
        //hold announcement sequence in the activity context interface
        this.setKeepAlive(keepAlive);
        this.setIndex(0);
        this.setSequence(announcements);

        //join user endpoint with any of the announcement endpoint
        //ActivityContextInterface connectionActivity = sbbContext.getActivities()[0];
        ActivityContextInterface connectionActivity = this.getUserActivity();
        logger.info("Joining " + userEndpoint + " with " + ANNOUNCEMENT_ENDPOINT);

        MsConnection connection = (MsConnection) connectionActivity.getActivity();
        MsSession session = connection.getSession();
        MsLink link = session.createLink(MsLinkMode.FULL_DUPLEX);

        ActivityContextInterface linkActivity = null;
        try {
            linkActivity = mediaAcif.getActivityContextInterface(link);
        } catch (UnrecognizedActivityException ex) {
        }

        linkActivity.attach(sbbContext.getSbbLocalObject());
        link.join(userEndpoint, ANNOUNCEMENT_ENDPOINT);
    }

    public void onLinkCreated(MsLinkEvent evt, ActivityContextInterface aci) {
        MsLink link = evt.getSource();
        String announcementEndpoint = link.getEndpoints()[1];

        logger.info("Announcement endpoint: " + announcementEndpoint);
        this.setAnnouncementEndpoint(announcementEndpoint);

        playNext();
    }

    public void onAnnouncementComplete(MsNotifyEvent evt, ActivityContextInterface aci) {
        logger.info("Announcement complete: " + (this.getIndex() - 1));
        if (this.getIndex() < this.getSequence().size()) {
            logger.info("Playing announcement[" + this.getIndex() + "]");
            playNext();
            return;
        }

        if (this.getIndex() == this.getSequence().size() && !this.getKeepAlive()) {
            logger.info("Releasing link");
            MsLink link = this.getLink();
            link.release();
        } else {
            this.setIndex(0);
            playNext();
        }
    }

    public void playNext() {
        String url = (String) this.getSequence().get(this.getIndex());
        MsSignalGenerator generator = msProvider.getSignalGenerator(getAnnouncementEndpoint());
        try {
            ActivityContextInterface generatorActivity = mediaAcif.getActivityContextInterface(generator);
            generatorActivity.attach(sbbContext.getSbbLocalObject());
            generator.apply(EventID.PLAY, new String[]{url});
            this.setIndex(getIndex() + 1);
        } catch (UnrecognizedActivityException e) {
        }
    }

    public void onLinkReleased(MsLinkEvent evt, ActivityContextInterface aci) {
        logger.info("Link release completed");

        ActivityContextInterface connectionAci = getUserActivity();
        if (connectionAci != null && !connectionAci.isEnding()) {
            connectionAci.detach(sbbContext.getSbbLocalObject());
            fireLinkReleased(evt, connectionAci, null);
        }
    }

    public void onUserDisconnected(MsConnectionEvent evt, ActivityContextInterface aci) {
        logger.info("Disconnecting from " + getAnnouncementEndpoint());
        MsLink link = getLink();
        if (link != null) {
            link.release();
        }
    }

    public abstract void fireLinkReleased(MsLinkEvent evt,
            ActivityContextInterface aci, Address address);

    public MsLink getLink() {
        ActivityContextInterface[] activities = sbbContext.getActivities();
        for (int i = 0; i < activities.length; i++) {
            if (activities[i].getActivity() instanceof MsLink) {
                return (MsLink) activities[i].getActivity();
            }
        }
        return null;
    }

    public ActivityContextInterface getUserActivity() {
        ActivityContextInterface activities[] = sbbContext.getActivities();
        for (int i = 0; i < activities.length; i++) {
            if (activities[i].getActivity() instanceof MsConnection) {
                return activities[i];
            }
        }
        return null;
    }

    public abstract String getAnnouncementEndpoint();

    public abstract void setAnnouncementEndpoint(String endpoint);

    public abstract int getIndex();

    public abstract void setIndex(int index);

    public abstract List getSequence();

    public abstract void setSequence(List sequence);

    public abstract boolean getKeepAlive();

    public abstract void setKeepAlive(boolean keepAlive);

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
