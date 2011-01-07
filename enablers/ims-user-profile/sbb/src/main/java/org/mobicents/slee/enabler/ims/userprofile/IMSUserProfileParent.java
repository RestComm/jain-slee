/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.enabler.ims.userprofile;

import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;

/**
 * 
 * @author <a href=mailto:brainslog@gmail.com> Alexandre Mendonca </a>
 */
public interface IMSUserProfileParent {

  // Sh-Pull Operations Callbacks ---------------------------------------------

  public void deliverRepositoryData(String publicIdentity, byte[][] serviceIndication, long resultCode, ShData data);

  public void deliverIMSPublicIdentity(String publicIdentity, byte[] msisdn, int identitySet, long resultCode, ShData data);

  public void deliverIMSUserState(String publicIdentity, long resultCode, ShData data);

  public void deliverSCSCFName(String publicIdentity, long resultCode, ShData data);

  public void deliverInitialFilterCriteria(String publicIdentity, String serverName, long resultCode, ShData data);

  public void deliverLocationInformation(byte[] msisdn, int requestedDomain, long resultCode, ShData data);

  public void deliverUserState(byte[] msisdn, int requestedDomain, long resultCode, ShData data);

  public void deliverChargingInformation(String publicIdentity, byte[] msisdn, long resultCode, ShData data);

  public void deliverMSISDN(String publicIdentity, byte[] msisdn, long resultCode, ShData data);

  public void deliverPSIActivation(String publicIdentity, long resultCode, ShData data);

  // Sh-Update Operations Callbacks -------------------------------------------

  public String updateRepositoryDataResponse(String publicIdentity, long resultCode);

  public String updatePSIActivationResponse(String publicIdentity, long resultCode);

  // Sh-Subscribe Operations Callbacks ----------------------------------------

  public String subscribeRepositoryDataResponse(String publicIdentity, byte[][] serviceIndications, int subscriptionRequestType, long resultCode);

  public String subscribeIMSUserStateResponse(String publicIdentity, int subscriptionRequestType, long resultCode);

  public String subscribeSCSCFNameResponse(String publicIdentity, int subscriptionRequestType, long resultCode);

  public String subscribeInitialFilterCriteriaResponse(String publicIdentity, String serverName, int subscriptionRequestType, long resultCode);

  public String subscribePSIActivationResponse(String publicIdentity, int subscriptionRequestType, long resultCode);

}
