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
package org.mobicents.slee.resource.mediacontrol.wrapper;

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
 * This wrapper is actually a provider of RA.
 * 
 * @author baranowb
 * 
 */
public class MsControlFactoryWrapper implements MsControlFactory {

	protected MsControlFactory wrappedFactory;
	protected McResourceAdaptor ra;

	/**
	 * @param wrappedFactory
	 * @param ra
	 */
	public MsControlFactoryWrapper(MsControlFactory wrappedFactory, McResourceAdaptor ra) {
		super();
		this.wrappedFactory = wrappedFactory;
		this.ra = ra;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MsControlFactory#createMediaSession()
	 */
	@Override
	public MediaSession createMediaSession() throws MsControlException {
		MediaSessionWrapper msw = new MediaSessionWrapper(this.wrappedFactory.createMediaSession(), this.ra);
		try {
			this.ra.startActivity(msw);
		} catch (Exception e) {
			throw new MsControlException("Failed to create MsControl resource.", e);
		}
		return msw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MsControlFactory#createParameters()
	 */
	@Override
	public Parameters createParameters() {
		return this.wrappedFactory.createParameters();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MsControlFactory#createVideoLayout(java.lang.String
	 * , java.io.Reader)
	 */
	@Override
	public VideoLayout createVideoLayout(String mimeType, Reader xmlDef) throws MediaConfigException {
		return this.wrappedFactory.createVideoLayout(mimeType, xmlDef);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MsControlFactory#getMediaConfig(javax.media.mscontrol
	 * .Configuration)
	 */
	@Override
	public MediaConfig getMediaConfig(Configuration<?> configuration) throws MediaConfigException {
		return this.wrappedFactory.getMediaConfig(configuration);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MsControlFactory#getMediaConfig(java.io.Reader)
	 */
	@Override
	public MediaConfig getMediaConfig(Reader xmlDef) throws MediaConfigException {
		return this.wrappedFactory.getMediaConfig(xmlDef);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MsControlFactory#getMediaObject(java.net.URI)
	 */
	@Override
	public MediaObject getMediaObject(URI arg0) {
		// TODO
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MsControlFactory#getPresetLayout(java.lang.String)
	 */
	@Override
	public VideoLayout getPresetLayout(String type) throws MediaConfigException {
		return this.wrappedFactory.getPresetLayout(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MsControlFactory#getPresetLayouts(int)
	 */
	@Override
	public VideoLayout[] getPresetLayouts(int numberOfLiveRegions) throws MediaConfigException {
		return this.wrappedFactory.getPresetLayouts(numberOfLiveRegions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MsControlFactory#getProperties()
	 */
	@Override
	public Properties getProperties() {
		return this.wrappedFactory.getProperties();
	}

}
