package org.mobicents.slee.resource.diameter.base;

import java.io.IOException;

import javax.slee.resource.SleeEndpoint;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.api.app.StateChangeListener;

import net.java.slee.resource.diameter.base.AccountingServerSessionActivity;
import net.java.slee.resource.diameter.base.AccountingSessionState;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;

public class AccountingServerSessionActivityImpl extends AccountingSessionActivityImpl
		implements AccountingServerSessionActivity {

	
	protected ServerAccSession serverSession=null;
	
	public AccountingServerSessionActivityImpl(
			DiameterMessageFactoryImpl messageFactory,
			DiameterAvpFactoryImpl avpFactory, ServerAccSession serverSession,
			EventListener<Request, Answer> raEventListener, long timeout,
			DiameterIdentityAvp destinationHost,
			DiameterIdentityAvp destinationRealm,SleeEndpoint endpoint) {
		super(messageFactory, avpFactory, null, raEventListener, timeout,
				destinationHost, destinationRealm,endpoint);


		this.serverSession=serverSession;
		this.serverSession.addStateChangeNotification(this);
		super.setCurrentWorkingSession(this.serverSession.getSessions().get(0));
		
	}

	public void sendAccountAnswer(AccountingAnswer answer) throws IOException {
		// FIXME: baranowb - add setting of proper session
		super.sendMessage(answer);
	}

	

	public void stateChanged(Enum arg0, Enum arg1) {
		// TODO Auto-generated method stub
		
	}

}
