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

import java.io.Reader;
import java.net.URI;
import java.util.Properties;
import javax.media.mscontrol.Configuration;
import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MediaConfigException;
import javax.media.mscontrol.MediaObject;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.MsControlFactory;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.resource.video.VideoLayout;
import org.mobicents.slee.resource.mediacontrol.McResourceAdaptor;

/**
 *
 * @author kulikov
 */
public class FactoryLocal implements MsControlFactory {

    private MsControlFactory factory;
    private McResourceAdaptor ra;
    
    public FactoryLocal(MsControlFactory factory, McResourceAdaptor ra) {
        this.factory = factory;
        this.ra = ra;
    }
    
    public Properties getProperties() {
        return factory.getProperties();
    }

    public MediaSession createMediaSession() throws MsControlException {
        return new SessionLocal(factory.createMediaSession(), ra);
    }

    public MediaConfig getMediaConfig(Configuration<?> cfg) throws MediaConfigException {
        return factory.getMediaConfig(cfg);
    }

    public MediaConfig getMediaConfig(Reader reader) throws MediaConfigException {
        return factory.getMediaConfig(reader);
    }

    public Parameters createParameters() {
        return factory.createParameters();
    }

    public VideoLayout createVideoLayout(String arg0, Reader arg1) throws MediaConfigException {
        return factory.createVideoLayout(arg0, arg1);
    }

    public VideoLayout[] getPresetLayouts(int arg0) throws MediaConfigException {
        return factory.getPresetLayouts(arg0);
    }

    public VideoLayout getPresetLayout(String arg0) throws MediaConfigException {
        return factory.getPresetLayout(arg0);
    }

    public MediaObject getMediaObject(URI arg0) {
        return factory.getMediaObject(arg0);
    }

}
