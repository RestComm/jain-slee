/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.slee.resource.diameter.sh.events;

import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.CurrentLocationType;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.IdentitySetType;
import net.java.slee.resource.diameter.sh.events.avp.RequestedDomainType;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.sh.events.DiameterShMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.UserIdentityAvpImpl;

/**
 * 
 * Implementation of {@link UserDataRequest} interface.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class UserDataRequestImpl extends DiameterShMessageImpl implements UserDataRequest {

  /**
   * @param msg
   */
  public UserDataRequestImpl(Message msg) {
    super(msg);

    msg.setRequest(true);

    super.longMessageName = "User-Data-Request";
    super.shortMessageName = "UDR";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getCurrentLocation()
   */
  public CurrentLocationType getCurrentLocation() {
    return (CurrentLocationType) getAvpAsEnumerated(DiameterShAvpCodes.CURRENT_LOCATION, DiameterShAvpCodes.SH_VENDOR_ID, CurrentLocationType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getDataReferences()
   */
  public DataReferenceType[] getDataReferences() {
    return (DataReferenceType[]) getAvpsAsEnumerated(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, DataReferenceType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getIdentitySet()
   */
  public IdentitySetType getIdentitySet() {
    return IdentitySetType.fromInt(getAvpAsInteger32(DiameterShAvpCodes.IDENTITY_SET, DiameterShAvpCodes.SH_VENDOR_ID));    
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getRequestedDomain()
   */
  public RequestedDomainType getRequestedDomain() {
    return RequestedDomainType.fromInt(getAvpAsInteger32(DiameterShAvpCodes.REQUESTED_DOMAIN, DiameterShAvpCodes.SH_VENDOR_ID));    
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getServerName()
   */
  public String getServerName() {
    return getAvpAsUTF8String(DiameterShAvpCodes.SERVER_NAME, DiameterShAvpCodes.SH_VENDOR_ID);    
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getServiceIndications()
   */
  public byte[][] getServiceIndications() {
    return getAvpsAsRaw(DiameterShAvpCodes.SERVICE_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getUserIdentity()
   */
  public UserIdentityAvp getUserIdentity() {
    return (UserIdentityAvp) getAvpAsCustom(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, UserIdentityAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#hasCurrentLocation()
   */
  public boolean hasCurrentLocation() {
    return hasAvp(DiameterShAvpCodes.CURRENT_LOCATION, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#hasIdentitySet()
   */
  public boolean hasIdentitySet() {
    return hasAvp(DiameterShAvpCodes.IDENTITY_SET, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#hasRequestedDomain()
   */
  public boolean hasRequestedDomain() {
    return hasAvp(DiameterShAvpCodes.REQUESTED_DOMAIN, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#hasServerName()
   */
  public boolean hasServerName() {
    return hasAvp(DiameterShAvpCodes.SERVER_NAME, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#hasUserIdentity()
   */
  public boolean hasUserIdentity() {
    return hasAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setCurrentLocation(net.java.slee.resource.diameter.sh.events.avp.CurrentLocationType)
   */
  public void setCurrentLocation(CurrentLocationType currentLocation) {
    addAvp(DiameterShAvpCodes.CURRENT_LOCATION, DiameterShAvpCodes.SH_VENDOR_ID, (long)currentLocation.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setDataReference(net.java.slee.resource.diameter.sh.events.avp.DataReferenceType)
   */
  public void setDataReference(DataReferenceType dataReference) {
    addAvp(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, (long)dataReference.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setDataReferences(net.java.slee.resource.diameter.sh.events.avp.DataReferenceType[])
   */
  public void setDataReferences(DataReferenceType[] dataReferences) {
    for(DataReferenceType dataReference : dataReferences) {
      setDataReference(dataReference);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setIdentitySet(net.java.slee.resource.diameter.sh.events.avp.IdentitySetType)
   */
  public void setIdentitySet(IdentitySetType identitySet) {
    addAvp(DiameterShAvpCodes.IDENTITY_SET, DiameterShAvpCodes.SH_VENDOR_ID, (long)identitySet.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setRequestedDomain(net.java.slee.resource.diameter.sh.events.avp.RequestedDomainType)
   */
  public void setRequestedDomain(RequestedDomainType requestedDomain) {
    addAvp(DiameterShAvpCodes.REQUESTED_DOMAIN, DiameterShAvpCodes.SH_VENDOR_ID, (long)requestedDomain.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setServerName(java.lang.String)
   */
  public void setServerName(String serverName) {
    addAvp(DiameterShAvpCodes.SERVER_NAME, DiameterShAvpCodes.SH_VENDOR_ID, serverName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setServiceIndication(byte[])
   */
  public void setServiceIndication(byte[] serviceIndication) {
    addAvp(DiameterShAvpCodes.SERVICE_INDICATION, DiameterShAvpCodes.SH_VENDOR_ID, serviceIndication);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setServiceIndications(byte[][])
   */
  public void setServiceIndications(byte[][] serviceIndications) {
    for(byte[] serviceIndication : serviceIndications) {
      setServiceIndication(serviceIndication);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setUserIdentity(net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp)
   */
  public void setUserIdentity(UserIdentityAvp userIdentity) {
    addAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, userIdentity.byteArrayValue());
  }

}
