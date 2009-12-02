package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.LocationEstimateType;
import net.java.slee.resource.diameter.ro.events.avp.LocationType;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;


/**
 * LocationTypeImpl.java
 *
 * <br>Project:  mobicents
 * <br>12:00:23 PM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LocationTypeImpl extends GroupedAvpImpl implements LocationType {

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public LocationTypeImpl( int code, long vendorId, int mnd, int prt, byte[] value )
  {
    super( code, vendorId, mnd, prt, value );
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LocationType#getDeferredLocationEventType()
   */
  public String getDeferredLocationEventType() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.DEFERRED_LOCATION_EVENT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LocationType#getLocationEstimateType()
   */
  public LocationEstimateType getLocationEstimateType() {
    return (LocationEstimateType) getAvpAsEnumerated(DiameterRoAvpCodes.LOCATION_ESTIMATE_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, LocationEstimateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LocationType#hasDeferredLocationEventType()
   */
  public boolean hasDeferredLocationEventType() {
    return hasAvp( DiameterRoAvpCodes.DEFERRED_LOCATION_EVENT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LocationType#hasLocationEstimateType()
   */
  public boolean hasLocationEstimateType() {
    return hasAvp( DiameterRoAvpCodes.LOCATION_ESTIMATE_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LocationType#setDeferredLocationEventType(String)
   */
  public void setDeferredLocationEventType( String deferredLocationEventType ) {
    addAvp(DiameterRoAvpCodes.DEFERRED_LOCATION_EVENT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, deferredLocationEventType);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LocationType#setLocationEstimateType(net.java.slee.resource.diameter.ro.events.avp.LocationEstimateType)
   */
  public void setLocationEstimateType( LocationEstimateType locationEstimateType ) {
    addAvp(DiameterRoAvpCodes.LOCATION_ESTIMATE_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, locationEstimateType.getValue());
  }

}
