package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.LcsClientName;
import net.java.slee.resource.diameter.ro.events.avp.LcsFormatIndicator;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * LcsClientNameImpl.java
 *
 * <br>Project:  mobicents
 * <br>3:28:17 AM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LcsClientNameImpl extends GroupedAvpImpl implements LcsClientName {

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public LcsClientNameImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#getLcsDataCodingScheme()
   */
  public String getLcsDataCodingScheme() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#getLcsFormatIndicator()
   */
  public LcsFormatIndicator getLcsFormatIndicator() {
    return (LcsFormatIndicator) getAvpAsEnumerated(DiameterRoAvpCodes.LCS_FORMAT_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID, LcsFormatIndicator.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#getLcsNameString()
   */
  public String getLcsNameString() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_NAME_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#hasLcsDataCodingScheme()
   */
  public boolean hasLcsDataCodingScheme() {
    return hasAvp( DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#hasLcsFormatIndicator()
   */
  public boolean hasLcsFormatIndicator() {
    return hasAvp( DiameterRoAvpCodes.LCS_FORMAT_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#hasLcsNameString()
   */
  public boolean hasLcsNameString() {
    return hasAvp( DiameterRoAvpCodes.LCS_NAME_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#setLcsDataCodingScheme(String)
   */
  public void setLcsDataCodingScheme( String lcsDataCodingScheme ) {
    addAvp(DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsDataCodingScheme);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#setLcsFormatIndicator(net.java.slee.resource.diameter.ro.events.avp.LcsFormatIndicator)
   */
  public void setLcsFormatIndicator( LcsFormatIndicator lcsFormatIndicator ) {
    addAvp(DiameterRoAvpCodes.LCS_FORMAT_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsFormatIndicator.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#setLcsNameString(String)
   */
  public void setLcsNameString( String lcsNameString ) {
    addAvp(DiameterRoAvpCodes.LCS_NAME_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsNameString);
  }

}
