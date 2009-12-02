package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * 
 * InterOperatorIdentifierImpl.java
 *
 * <br>Project:  mobicents
 * <br>8:03:55 PM Apr 11, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class InterOperatorIdentifierImpl extends GroupedAvpImpl implements InterOperatorIdentifier {

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public InterOperatorIdentifierImpl( int code, long vendorId, int mnd, int prt, byte[] value )
  {
    super( code, vendorId, mnd, prt, value );
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#getOriginatingIoi()
   */
  public String getOriginatingIoi() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.ORIGINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#getTerminatingIoi()
   */
  public String getTerminatingIoi() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.TERMINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#hasOriginatingIoi()
   */
  public boolean hasOriginatingIoi() {
    return hasAvp(DiameterRoAvpCodes.ORIGINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#hasTerminatingIoi()
   */
  public boolean hasTerminatingIoi() {
    return hasAvp(DiameterRoAvpCodes.TERMINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#setOriginatingIoi(String)
   */
  public void setOriginatingIoi( String originatingIoi ) {
    addAvp(DiameterRoAvpCodes.ORIGINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID, originatingIoi);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#setTerminatingIoi(String)
   */
  public void setTerminatingIoi( String terminatingIoi ) {
    addAvp(DiameterRoAvpCodes.TERMINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID, terminatingIoi);
  }

}
