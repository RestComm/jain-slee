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

import java.net.URI;
import java.util.Iterator;
import javax.media.mscontrol.Configuration;
import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaObject;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameter;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mixer.MediaMixer;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.vxml.VxmlDialog;
import org.mobicents.slee.resource.mediacontrol.McResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.MediaGroupActivityHandle;
import org.mobicents.slee.resource.mediacontrol.NetworkConnectionActivityHandle;

/**
 *
 * @author kulikov
 */
public class SessionLocal implements MediaSession {

    private MediaSession session;
    private McResourceAdaptor ra;
    
    protected SessionLocal(MediaSession session, McResourceAdaptor ra) {
        this.session = session;
        this.ra = ra;
    }
    
    public NetworkConnection createNetworkConnection(Configuration<NetworkConnection> cfg) throws MsControlException {
        NetworkConnectionLocal local = new NetworkConnectionLocal(session.createNetworkConnection(cfg), ra);
        NetworkConnectionActivityHandle h = new NetworkConnectionActivityHandle(ra, local);
        ra.createActivity(h, local);
        return local;
    }

    public NetworkConnection createNetworkConnection(Configuration<NetworkConnection> config, Parameters params) throws MsControlException {
        NetworkConnectionLocal local = new NetworkConnectionLocal(session.createNetworkConnection(config, params), ra);
        NetworkConnectionActivityHandle h = new NetworkConnectionActivityHandle(ra, local);
        ra.createActivity(h, local);
        return local;
    }

    public NetworkConnection createNetworkConnection(MediaConfig cfg, Parameters params) throws MsControlException {
        NetworkConnectionLocal local = new NetworkConnectionLocal(session.createNetworkConnection(cfg, params), ra);
        NetworkConnectionActivityHandle h = new NetworkConnectionActivityHandle(ra, local);
        ra.createActivity(h, local);
        return local;
    }

    public MediaGroup createMediaGroup(Configuration<MediaGroup> config) throws MsControlException {
        MediaGroupLocal local = new MediaGroupLocal(this, session.createMediaGroup(config));
        MediaGroupActivityHandle h = new MediaGroupActivityHandle(ra, local);
        ra.createActivity(h, local);
        return local;
    }

    public MediaGroup createMediaGroup(Configuration<MediaGroup> arg0, Parameters arg1) throws MsControlException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MediaGroup createMediaGroup(MediaConfig arg0, Parameters arg1) throws MsControlException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MediaMixer createMediaMixer(Configuration<MediaMixer> arg0) throws MsControlException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MediaMixer createMediaMixer(Configuration<MediaMixer> arg0, Parameters arg1) throws MsControlException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MediaMixer createMediaMixer(MediaConfig arg0, Parameters arg1) throws MsControlException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public VxmlDialog createVxmlDialog(Parameters arg0) throws MsControlException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getAttribute(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttribute(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAttribute(String arg0, Object arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterator<String> getAttributeNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public URI getURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void release() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setParameters(Parameters arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Parameters getParameters(Parameter[] arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Parameters createParameters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterator<MediaObject> getMediaObjects() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T extends MediaObject> Iterator<T> getMediaObjects(Class<T> arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
