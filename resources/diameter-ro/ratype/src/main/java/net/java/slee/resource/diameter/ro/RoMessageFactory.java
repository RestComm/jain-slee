package net.java.slee.resource.diameter.ro;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.cca.events.avp.CcRequestType;
import net.java.slee.resource.diameter.ro.events.RoCreditControlRequest;

/**
 * Used by applications to create Diameter Ro request messages.
 * Ro answer messages can be created using the RoServerSessionActivity.createRoCreditControlAnswer() method. 
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RoMessageFactory {

  public static final long _RO_TGPP_VENDOR_ID = 10415L;
  public static final int  _RO_AUTH_APP_ID = 4;

  /**
   * Creates a Credit Control Request message.
   * 
   * @return
   */
  public RoCreditControlRequest createRoCreditControlRequest(/*CcRequestType type*/);

  /**
   * Creates a Credit Control Request message with the Session-Id AVP populated with the sessionId parameter.
   * 
   * @param sessionId
   * @return
   */
  public RoCreditControlRequest createRoCreditControlRequest(String sessionId/*, CcRequestType type*/);

  /**
   * 
   * @return Base Diameter message factory
   */
  public DiameterMessageFactory getBaseMessageFactory();

}
