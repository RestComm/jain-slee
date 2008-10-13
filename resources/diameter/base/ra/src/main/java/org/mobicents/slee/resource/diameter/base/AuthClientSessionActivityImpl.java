package org.mobicents.slee.resource.diameter.base;

import java.io.IOException;

import javax.slee.resource.SleeEndpoint;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.auth.ClientAuthSession;

import net.java.slee.resource.diameter.base.AuthClientSessionActivity;
import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;

public class AuthClientSessionActivityImpl extends AuthSessionActivityImpl
		implements AuthClientSessionActivity {

	
	protected ClientAuthSession clientSession=null;
	
	public AuthClientSessionActivityImpl(
			DiameterMessageFactoryImpl messageFactory,
			DiameterAvpFactoryImpl avpFactory, ClientAuthSession clientSession,
			EventListener<Request, Answer> raEventListener, long timeout,
			DiameterIdentityAvp destinationHost,
			DiameterIdentityAvp destinationRealm,SleeEndpoint endpoint) {
		super(messageFactory, avpFactory, null, raEventListener, timeout,
				destinationHost, destinationRealm,endpoint);
		
		this.clientSession=clientSession;
		this.clientSession.addStateChangeNotification(this);
		super.setCurrentWorkingSession(clientSession.getSessions().get(0));
		
	}

	public void sendAbortSessionAnswer(AbortSessionAnswer answer) throws IOException {
		super.sendMessage(answer);

	}

	public void sendAuthRequest(DiameterMessage request) throws IOException {
		super.sendMessage(request);

	}

	public void sendReAuthAnswer(ReAuthAnswer answer) throws IOException {
		super.sendMessage(answer);

	}

	public void sendSessionTerminationRequest(SessionTerminationRequest request) throws IOException {
		super.sendMessage(request);

	}

	public void stateChanged(Enum arg0, Enum arg1) {
		// TODO Auto-generated method stub

	}

}
