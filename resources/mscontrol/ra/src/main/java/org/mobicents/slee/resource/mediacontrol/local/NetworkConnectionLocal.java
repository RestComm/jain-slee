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
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.networkconnection.SdpPortManager;
import javax.media.mscontrol.resource.Action;
import javax.media.mscontrol.resource.AllocationEventListener;
import org.mobicents.slee.resource.mediacontrol.McResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.NetworkConnectionActivityHandle;

/**
 *
 * @author kulikov
 */
public class NetworkConnectionLocal extends MsActivity implements NetworkConnection, LocalJoinable {

    private String ID = Long.toHexString(System.currentTimeMillis());
    private NetworkConnection connection;
    private SdpPortManagerLocal sdpPortManager;
    private McResourceAdaptor ra;
    private MediaSessionLocal mediaSession;
        
    protected NetworkConnectionLocal(NetworkConnection connection, MediaSessionLocal mediaSession, McResourceAdaptor ra) throws MsControlException {
        this.connection = connection;
        this.ra = ra;
        this.mediaSession = mediaSession;
        this.sdpPortManager = new SdpPortManagerLocal(connection.getSdpPortManager(), this, mediaSession);
        
    }
    
    @Override
    public String getID() {
        return ID;
    }
    
    public SdpPortManager getSdpPortManager() throws MsControlException {
    	return sdpPortManager;
    }

    public JoinableStream getJoinableStream(StreamType type) throws MsControlException {
    	//TODO: change this. add proxy
        return connection.getJoinableStream(type);
    }

    public JoinableStream[] getJoinableStreams() throws MsControlException {
    	//TODO: change this. add proxy
        return connection.getJoinableStreams();
    }

    public Joinable[] getJoinees(Direction direction) throws MsControlException {
    	//TODO: change this. add proxy
        return connection.getJoinees(direction);
    }

    public Joinable[] getJoinees() throws MsControlException {
    	//TODO: change this. add proxy
        return connection.getJoinees();
    }

    public void join(Direction direction, Joinable other) throws MsControlException {
        connection.join(direction, other);
    }

    public void unjoin(Joinable other) throws MsControlException {
        connection.unjoin(other);
    }

    public void joinInitiate(Direction direction, Joinable otherParty, Serializable context) throws MsControlException {
        this.connection.joinInitiate(direction, ((LocalJoinable)otherParty).getOrigin(), context);
    }

    public void unjoinInitiate(Joinable otherParty, Serializable context) throws MsControlException {
        this.connection.unjoinInitiate(otherParty, context);
    }

    public void addListener(JoinEventListener listener) {
        throw new SecurityException("SBB can not assign listener");
    }
    
    public void removeListener(JoinEventListener arg0) {
        throw new SecurityException("SBB can not assign listener");
    }

    public void addActivityHandle(NetworkConnectionActivityHandle h) {
        connection.addListener(h);
    }
    
    public void removeActivityHandle(NetworkConnectionActivityHandle h) {
        connection.removeListener(h);
    }
    
    public MediaSession getMediaSession() {
       return mediaSession;
    }

    public void triggerAction(Action arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <R> R getResource(Class<R> type) throws MsControlException {
        return connection.getResource(type);
    }

    public MediaConfig getConfig() {
        return connection.getConfig();
    }

    public void confirm() throws MsControlException {
        connection.confirm();
    }

    public URI getURI() {
        return connection.getURI();
    }

    public void release() {
        connection.release();
    }

    public void setParameters(Parameters p) {
        connection.setParameters(p);
    }

    public Parameters getParameters(Parameter[] p) {
        return connection.getParameters(p);
    }

    public Parameters createParameters() {
        return connection.createParameters();
    }

    public Iterator<MediaObject> getMediaObjects() {
        return connection.getMediaObjects();
    }

    public <T extends MediaObject> Iterator<T> getMediaObjects(Class<T> t) {
        return connection.getMediaObjects(t);
    }

    public void addListener(AllocationEventListener arg0) {
        throw new SecurityException("SBB can not assign listener");
    }

    
    public void removeListener(AllocationEventListener arg0) {
        throw new SecurityException("SBB can not assign listener");
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
        return ((NetworkConnectionLocal) other).ID.equals(this.ID);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.ID != null ? this.ID.hashCode() : 0);
        return hash;
    }

    public Joinable getOrigin() {
        return this.connection;
    }
}
