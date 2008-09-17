package org.mobicents.slee.resource.diameter.sh.client;

import java.io.IOException;

import javax.slee.resource.SleeEndpoint;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;
import net.java.slee.resource.diameter.sh.client.ShClientActivity;
import net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.server.events.UserDataRequest;

/**
 * 
 * <br><br>Super project:  mobicents-jainslee-server
 * <br>16:46:45 2008-09-10	
 * <br>
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author Erick Svenson
 */
public class ShClientActivityImpl extends DiameterActivityImpl implements ShClientActivity {

	public ShClientActivityImpl(DiameterMessageFactoryImpl messageFactory, DiameterAvpFactoryImpl avpFactory, Session session, EventListener<Request, Answer> raEventListener,
			long timeout, DiameterIdentityAvp destinationHost, DiameterIdentityAvp destinationRealm, SleeEndpoint endpoint) {
		super(messageFactory, avpFactory, session, raEventListener, timeout, destinationHost, destinationRealm, endpoint);
		// TODO Auto-generated constructor stub
	}

	public void sendProfileUpdateRequest(ProfileUpdateRequest message) throws IOException {
		super.sendMessage(message);

	}

	public void sendSubscribeNotificationsRequest(SubscribeNotificationsRequest message) throws IOException {
		super.sendMessage(message);

	}

	public void sendUserDataRequest(UserDataRequest message) throws IOException {
		super.sendMessage(message);

	}

	public void endActivity() {
		// TODO Auto-generated method stub

	}

	
	//FIXME: those two methods are weird!!!
	public DiameterAvpFactory getDiameterAvpFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public DiameterMessageFactory getDiameterMessageFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSessionId() {
		return super.getSessionId();
	}

	public void sendMessage(DiameterMessage message) throws IOException {
		super.sendMessage(message);
	}

}
