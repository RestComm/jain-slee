/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.diameter.sh.events;

import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.CurrentLocationType;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.IdentitySetType;
import net.java.slee.resource.diameter.sh.events.avp.SessionPriorityType;
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

  private static final long serialVersionUID = 8420851563858038672L;

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
  public IdentitySetType[] getIdentitySets() {
    return (IdentitySetType[]) getAvpsAsEnumerated(DiameterShAvpCodes.IDENTITY_SET, DiameterShAvpCodes.SH_VENDOR_ID, IdentitySetType.class);
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
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getDSAITag()
   */
  public byte[][] getDSAITags() {
    return getAvpsAsRaw(DiameterShAvpCodes.DSAI_TAG, DiameterShAvpCodes.SH_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getSessionPriority()
   */
  public SessionPriorityType getSessionPriority() {
    return SessionPriorityType.fromInt(getAvpAsInteger32(DiameterShAvpCodes.SESSION_PRIORITY, DiameterShAvpCodes.SH_VENDOR_ID));
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#getRequestedNodes()
   */
  public long getRequestedNodes() {
    return getAvpAsUnsigned32(DiameterShAvpCodes.REQUESTED_NODES, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#hasCurrentLocation()
   */
  public boolean hasCurrentLocation() {
    return hasAvp(DiameterShAvpCodes.CURRENT_LOCATION, DiameterShAvpCodes.SH_VENDOR_ID);
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
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#hasSessionPriority()
   */
  public boolean hasSessionPriority() {
    return hasAvp(DiameterShAvpCodes.SESSION_PRIORITY, DiameterShAvpCodes.SH_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#hasRequestedNodes()
   */
  public boolean hasRequestedNodes() {
    return hasAvp(DiameterShAvpCodes.REQUESTED_NODES, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setCurrentLocation(net.java.slee.resource.diameter.sh.events.avp.CurrentLocationType)
   */
  public void setCurrentLocation(CurrentLocationType currentLocation) {
    addAvp(DiameterShAvpCodes.CURRENT_LOCATION, DiameterShAvpCodes.SH_VENDOR_ID, currentLocation.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setDataReference(net.java.slee.resource.diameter.sh.events.avp.DataReferenceType)
   */
  public void setDataReference(DataReferenceType dataReference) {
    addAvp(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, dataReference.getValue());
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
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setIdentitySet(IdentitySetType)
   */
  public void setIdentitySet(IdentitySetType identitySet)
  {
    addAvp(DiameterShAvpCodes.IDENTITY_SET, DiameterShAvpCodes.SH_VENDOR_ID, identitySet.getValue());
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setIdentitySets(IdentitySetType[])
   */
  public void setIdentitySets(IdentitySetType[] identitySets)
  {
	  for(IdentitySetType identitySet : identitySets) {
		  setIdentitySet(identitySet);
	    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setRequestedDomain(net.java.slee.resource.diameter.sh.events.avp.RequestedDomainType)
   */
  public void setRequestedDomain(RequestedDomainType requestedDomain) {
    addAvp(DiameterShAvpCodes.REQUESTED_DOMAIN, DiameterShAvpCodes.SH_VENDOR_ID, requestedDomain.getValue());
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

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setDSAITag(byte[])
   */
  public void setDSAITag(byte[] dsaiTag) {
    addAvp(DiameterShAvpCodes.DSAI_TAG, DiameterShAvpCodes.SH_VENDOR_ID, dsaiTag);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setDSAITag(byte[])
   */
  public void setDSAITags(byte[][] dsaiTags) {
	  for(byte[] dsaiTag : dsaiTags) {
		  setDSAITag(dsaiTag);
	    }    
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setSessionPriority(SessionPriority)
   */
  public void setSessionPriority(SessionPriorityType sesionPriority) {
    addAvp(DiameterShAvpCodes.SESSION_PRIORITY, DiameterShAvpCodes.SH_VENDOR_ID, sesionPriority.getValue());
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.UserDataRequest#setRequestedNodes(long)
   */
  public void setRequestedNodes(long requestedNodes) {
    addAvp(DiameterShAvpCodes.REQUESTED_NODES, DiameterShAvpCodes.SH_VENDOR_ID, requestedNodes);
  }
}
