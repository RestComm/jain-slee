package org.mobicents.slee.resource.diameter.base;

import java.io.IOException;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.AccountingClientSessionActivity;
import net.java.slee.resource.diameter.base.AccountingSessionState;
import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.acc.ClientAccSession;
import org.jdiameter.api.app.StateChangeListener;

public class AccountingClientSessionActivityImpl extends AccountingSessionActivityImpl
		implements AccountingClientSessionActivity {

	protected AccountingSessionState state=null;
	protected ClientAccSession clientSession=null;
	public AccountingClientSessionActivityImpl(
			DiameterMessageFactoryImpl messageFactory,
			DiameterAvpFactoryImpl avpFactory, ClientAccSession clientSession,
			EventListener<Request, Answer> raEventListener, long timeout,
			DiameterIdentityAvp destinationHost,
			DiameterIdentityAvp destinationRealm,SleeEndpoint endpoint) {
		super(messageFactory, avpFactory, null, raEventListener, timeout,
				destinationHost, destinationRealm,endpoint);
		this.clientSession=clientSession;
		super.setCurrentWorkingSession(this.clientSession.getSessions().get(0));
		this.clientSession.addStateChangeNotification(this);
		// TODO Auto-generated constructor stub
	}

	public void sendAccountRequest(AccountingRequest request) throws IOException {

		//FIXME: baranowb - add here magic to pick up and set in super correct Session in case of Relly agent
		
		super.sendMessage( request);

	}

	public void stateChanged(Enum arg0, Enum arg1) {
		// TODO impl
		
	}

}
