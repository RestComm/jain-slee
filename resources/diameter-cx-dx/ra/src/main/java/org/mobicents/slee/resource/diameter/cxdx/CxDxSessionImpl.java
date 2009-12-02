package org.mobicents.slee.resource.diameter.cxdx;

import java.util.ArrayList;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cxdx.CxDxAVPFactory;
import net.java.slee.resource.diameter.cxdx.CxDxMessageFactory;
import net.java.slee.resource.diameter.cxdx.CxDxSessionActivity;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.app.StateChangeListener;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.cxdx.handlers.CxDxSessionCreationListener;

/**
 *
 * CxDxSessionImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class CxDxSessionImpl extends DiameterActivityImpl implements CxDxSessionActivity ,StateChangeListener{

  protected CxDxMessageFactoryImpl cxdxMessageFactory = null;
  protected CxDxAVPFactory cxdxAvpFactory = null;

  protected DiameterMessage lastRequest = null;
  protected ArrayList<DiameterAvp> sessionAvps = new ArrayList<DiameterAvp>();
  
  protected boolean terminated = false;
  //FIXME: missed this in last release, this has to be fixed in base....!!!!!!!!!!!!!!!!!!
  protected CxDxSessionCreationListener cxdxSessionListener;

  public CxDxSessionImpl(CxDxMessageFactory messageFactory, CxDxAVPFactory avpFactory, Session session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint) {
    super(null, null, session, raEventListener, destinationHost, destinationRealm, endpoint);

    this.cxdxMessageFactory = (CxDxMessageFactoryImpl) messageFactory;
    this.cxdxAvpFactory = avpFactory;
    //this.cxdxSessionListener = (CxDxSessionCreationListener)raEventListener;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxSession#getCxDxAvpFactory()
   */
  public CxDxAVPFactory getCxDxAvpFactory() {
    return this.cxdxAvpFactory;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxSession#getCxDxMessageFactory()
   */
  public CxDxMessageFactory getCxDxMessageFactory() {
    return this.cxdxMessageFactory;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxSession#getSessionId()
   */
  public String getSessionId() {
    return session.getSessionId();
  }

  public void fetchSessionData(DiameterMessage cxdxRequest) {
    this.lastRequest = cxdxRequest;
  }

  public Object getSessionListener() {
    return cxdxSessionListener;
  }

  public void setSessionListener(Object ra) {
    this.cxdxSessionListener = (CxDxSessionCreationListener) ra;
  }

  /**
   * Fills message with session AVPs if present and/or needed.
   * 
   * @param message
   */
  protected void fillSessionAVPs(DiameterMessage message) {
    // If there's Destination-Host/Realm, add the AVPs
    if(message.getHeader().isRequest()) {
      if (destinationHost != null) {
        message.setDestinationHost(destinationHost);
      }

      if (destinationRealm != null) {
        message.setDestinationRealm(destinationRealm);
      }
    }

    // Fill extension avps if present
    if (sessionAvps.size() > 0) {
      try {
        message.setExtensionAvps(sessionAvps.toArray(new DiameterAvp[sessionAvps.size()]));
      }
      catch (AvpNotAllowedException e) {
        logger.error("Failed to add Session AVPs to request.", e);
      }
    }

    // Guarantee session-id is present
    if (!message.hasSessionId() && sessionId != null) {
      message.setSessionId(sessionId);
    }
  }
}
