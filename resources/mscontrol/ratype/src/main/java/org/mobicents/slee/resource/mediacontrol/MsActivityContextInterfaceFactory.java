/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.slee.resource.mediacontrol;

import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mixer.MediaMixer;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.vxml.VxmlDialog;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

/**
 * Activity Context Interface factory.
 * 
 * @author kulikov
 * @author baranowb
 */
public interface MsActivityContextInterfaceFactory {

	public ActivityContextInterface getActivityContextInterface(MediaSession mediaSession) throws NullPointerException, UnrecognizedActivityException,
			FactoryException;

	public ActivityContextInterface getActivityContextInterface(NetworkConnection connection) throws NullPointerException, UnrecognizedActivityException,
			FactoryException;

	public ActivityContextInterface getActivityContextInterface(MediaGroup group) throws NullPointerException, UnrecognizedActivityException, FactoryException;

	public ActivityContextInterface getActivityContextInterface(MediaMixer mixer) throws NullPointerException, UnrecognizedActivityException, FactoryException;

	public ActivityContextInterface getActivityContextInterface(VxmlDialog mixer) throws NullPointerException, UnrecognizedActivityException, FactoryException;

}
