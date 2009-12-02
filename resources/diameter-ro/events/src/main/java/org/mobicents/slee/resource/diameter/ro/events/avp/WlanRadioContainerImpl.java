package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.LocationType;
import net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * WlanRadioContainerImpl.java
 *
 * <br>Project:  mobicents
 * <br>4:33:06 PM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class WlanRadioContainerImpl extends GroupedAvpImpl implements WlanRadioContainer {

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public WlanRadioContainerImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer#getLocationType()
   */
  public LocationType getLocationType() {
    return (LocationType) getAvpAsCustom(DiameterRoAvpCodes.LOCATION_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, LocationTypeImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer#getWlanTechnology()
   */
  public long getWlanTechnology() {
    return getAvpAsUnsigned32(DiameterRoAvpCodes.WLAN_TECHNOLOGY, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer#hasLocationType()
   */
  public boolean hasLocationType() {
    return hasAvp( DiameterRoAvpCodes.LOCATION_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer#hasWlanTechnology()
   */
  public boolean hasWlanTechnology() {
    return hasAvp( DiameterRoAvpCodes.WLAN_TECHNOLOGY, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer#setLocationType(net.java.slee.resource.diameter.ro.events.avp.LocationType)
   */
  public void setLocationType( LocationType locationType ) {
    addAvp(DiameterRoAvpCodes.LOCATION_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, locationType.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer#setWlanTechnology(long)
   */
  public void setWlanTechnology( long wlanTechnology ) {
    addAvp(DiameterRoAvpCodes.WLAN_TECHNOLOGY, DiameterRoAvpCodes.TGPP_VENDOR_ID, wlanTechnology);
  }

}
