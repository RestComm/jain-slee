package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.ApplicationServerInformation;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * ApplicationServerInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>12:55:05 AM Apr 11, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ApplicationServerInformationImpl extends GroupedAvpImpl implements ApplicationServerInformation {

  public ApplicationServerInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ApplicationServerInformation#getApplicationProvidedCalledPartyAddresses()
   */
  public String[] getApplicationProvidedCalledPartyAddresses() {
    return getAvpsAsUTF8String(DiameterRoAvpCodes.APPLICATION_PROVIDED_CALLED_PARTY_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ApplicationServerInformation#getApplicationServer()
   */
  public String getApplicationServer() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.APPLICATION_SERVER, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ApplicationServerInformation#hasApplicationServer()
   */
  public boolean hasApplicationServer() {
    return hasAvp(DiameterRoAvpCodes.APPLICATION_SERVER, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ApplicationServerInformation#setApplicationProvidedCalledPartyAddress(String)
   */
  public void setApplicationProvidedCalledPartyAddress( String applicationProvidedCalledPartyAddress ) {
    addAvp(DiameterRoAvpCodes.APPLICATION_PROVIDED_CALLED_PARTY_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, applicationProvidedCalledPartyAddress);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ApplicationServerInformation#setApplicationProvidedCalledPartyAddresses(String[])
   */
  public void setApplicationProvidedCalledPartyAddresses( String[] applicationProvidedCalledPartyAddresses ) {
    for(String applicationProvidedCalledPartyAddress : applicationProvidedCalledPartyAddresses) {
      setApplicationProvidedCalledPartyAddress(applicationProvidedCalledPartyAddress);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.ApplicationServerInformation#setApplicationServer(String)
   */
  public void setApplicationServer( String applicationServer ) {
    addAvp(DiameterRoAvpCodes.APPLICATION_SERVER, DiameterRoAvpCodes.TGPP_VENDOR_ID, applicationServer);
  }
}
