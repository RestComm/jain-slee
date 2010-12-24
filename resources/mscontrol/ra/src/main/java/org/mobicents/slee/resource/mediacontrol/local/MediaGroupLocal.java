/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.slee.resource.mediacontrol.local;

import java.io.Serializable;
import java.net.URI;
import java.util.Iterator;
import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MediaObject;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameter;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.join.JoinEventListener;
import javax.media.mscontrol.join.Joinable;
import javax.media.mscontrol.join.Joinable.Direction;
import javax.media.mscontrol.join.JoinableStream;
import javax.media.mscontrol.join.JoinableStream.StreamType;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.Player;
import javax.media.mscontrol.mediagroup.Recorder;
import javax.media.mscontrol.mediagroup.signals.SignalDetector;
import javax.media.mscontrol.mediagroup.signals.SignalGenerator;
import javax.media.mscontrol.resource.Action;
import javax.media.mscontrol.resource.AllocationEventListener;

/**
 *
 * @author kulikov
 */
public class MediaGroupLocal extends MsActivity implements MediaGroup, LocalJoinable {

    private String ID = Long.toHexString(System.currentTimeMillis());
    
    private MediaGroup group;
    private SessionLocal session;
    
    public MediaGroupLocal(SessionLocal session, MediaGroup group) {
        this.group = group;
        this.session = session;
    }
    
    public Player getPlayer() throws MsControlException {
        return group.getPlayer();
    }

    public Recorder getRecorder() throws MsControlException {
        return group.getRecorder();
    }

    public SignalDetector getSignalDetector() throws MsControlException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SignalGenerator getSignalGenerator() throws MsControlException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void stop() {
        group.stop();
    }

    public JoinableStream getJoinableStream(StreamType type) throws MsControlException {
        return group.getJoinableStream(type);
    }

    public JoinableStream[] getJoinableStreams() throws MsControlException {
        return group.getJoinableStreams();
    }

    public Joinable[] getJoinees(Direction arg0) throws MsControlException {
        return group.getJoinees();
    }

    public Joinable[] getJoinees() throws MsControlException {
        return group.getJoinees();
    }

    public void join(Direction direction, Joinable otherParty) throws MsControlException {
        group.join(direction, otherParty);
    }

    public void unjoin(Joinable otherParty) throws MsControlException {
        group.unjoin(otherParty);
    }

    public void joinInitiate(Direction direction, Joinable otherParty, Serializable context) throws MsControlException {
        group.joinInitiate(direction, otherParty, context);
    }

    public void unjoinInitiate(Joinable otherParty, Serializable context) throws MsControlException {
        group.unjoinInitiate(otherParty, context);
    }

    public void addListener(JoinEventListener arg0) {
        throw new SecurityException("SBB can't register listener");
    }

    public void removeListener(JoinEventListener arg0) {
        throw new SecurityException("SBB can't register listener");
    }

    public MediaSession getMediaSession() {
        return session;
    }

    public void triggerAction(Action action) {
       group.triggerAction(action);
    }

    public <R> R getResource(Class<R> res) throws MsControlException {
        return group.getResource(res);
    }

    public MediaConfig getConfig() {
        return group.getConfig();
    }

    public void confirm() throws MsControlException {
        group.confirm();
    }

    public URI getURI() {
        return group.getURI();
    }

    public void release() {
        group.release();
    }

    public void setParameters(Parameters parameters) {
        group.setParameters(parameters);
    }

    public Parameters getParameters(Parameter[] list) {
        return group.getParameters(list);
    }

    public Parameters createParameters() {
       return group.createParameters();
    }

    public Iterator<MediaObject> getMediaObjects() {
        return group.getMediaObjects();
    }

    public <T extends MediaObject> Iterator<T> getMediaObjects(Class<T> res) {
        return group.getMediaObjects(res);
    }

    public void addListener(AllocationEventListener arg0) {
        throw new SecurityException("SBB can't register listener");
    }

    public void removeListener(AllocationEventListener arg0) {
        throw new SecurityException("SBB can't register listener");
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (other == null) {
            return false;
        }
        
        if (other.getClass() != this.getClass()) {
            return false;
        }
        return ((MediaGroupLocal) other).ID.equals(this.ID);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.group != null ? this.group.hashCode() : 0);
        return hash;
    }

    @Override
    public String getID() {
        return ID;
    }

    public Joinable getOrigin() {
        return this.group;
    }
}
