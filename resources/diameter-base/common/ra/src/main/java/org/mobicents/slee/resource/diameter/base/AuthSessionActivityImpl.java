package org.mobicents.slee.resource.diameter.base;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.AuthSessionActivity;
import net.java.slee.resource.diameter.base.AuthSessionState;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.app.StateChangeListener;

public abstract class AuthSessionActivityImpl extends DiameterActivityImpl implements AuthSessionActivity , StateChangeListener{

  public AuthSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, Session session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint) {
    super(messageFactory, avpFactory, session, raEventListener, destinationHost, destinationRealm, endpoint);
  }

  protected AuthSessionState state = null;

  public AuthSessionState getSessionState() {
    return state;
  }

}
