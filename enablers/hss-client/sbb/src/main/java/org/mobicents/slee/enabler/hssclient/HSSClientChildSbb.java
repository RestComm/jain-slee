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
package org.mobicents.slee.enabler.hssclient;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.SbbContextExt;

import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.client.ShClientActivity;
import net.java.slee.resource.diameter.sh.client.ShClientActivityContextInterfaceFactory;
import net.java.slee.resource.diameter.sh.client.ShClientMessageFactory;
import net.java.slee.resource.diameter.sh.client.ShClientProvider;
import net.java.slee.resource.diameter.sh.client.ShClientSubscriptionActivity;
import net.java.slee.resource.diameter.sh.events.DiameterShMessage;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataAnswer;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.IdentitySetType;
import net.java.slee.resource.diameter.sh.events.avp.RequestedDomainType;
import net.java.slee.resource.diameter.sh.events.avp.SubsReqType;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

/**
 * IMS User Profile SLEE Enabler.
 * 
 * @author <a href=mailto:brainslog@gmail.com> Alexandre Mendonca </a>
 */
public abstract class HSSClientChildSbb implements Sbb, HSSClientChild {

  private static Tracer tracer;

  protected SbbContextExt sbbContext;

  protected ShClientProvider diameterShClientSbbInterface = null;
  protected ShClientActivityContextInterfaceFactory diameterShClientACIF = null;

  protected ShClientMessageFactory diameterShClientMessageFactory = null;
  protected DiameterShAvpFactory diameterShClientAvpFactory = null;

  public abstract RequestMappingACI asSbbActivityContextInterface(ActivityContextInterface aci);

  protected HSSClientParentSbbLocalObject getParent() {
	  return (HSSClientParentSbbLocalObject) sbbContext.getSbbLocalObject().getParent();
  }
  
  // -- SBB LOCAL OBJECT METHODS ----------------------------------------------
  
  public String getRepositoryData(String publicIdentity, byte[][] serviceIndications, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.REPOSITORY_DATA);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);
    udr.setServiceIndications(serviceIndications);

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);
    
    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getIMSPublicIdentity(String publicIdentity, byte[] msisdn, int identitySet, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, msisdn);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.IMS_PUBLIC_IDENTITY);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);
    udr.setIdentitySet(IdentitySetType.fromInt(identitySet));

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getIMSUserState(String publicIdentity, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.IMS_USER_STATE);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getSCSCFName(String publicIdentity, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.S_CSCFNAME);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getInitialFilterCriteria(String publicIdentity, String serverName, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.INITIAL_FILTER_CRITERIA);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);
    udr.setServerName(serverName);

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getLocationInformation(byte[] msisdn, int requestedDomain, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(null, msisdn);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.LOCATION_INFORMATION);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);
    udr.setRequestedDomain(RequestedDomainType.fromInt(requestedDomain));

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getUserState(byte[] msisdn, int requestedDomain, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(null, msisdn);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.USER_STATE);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);
    udr.setRequestedDomain(RequestedDomainType.fromInt(requestedDomain));

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getChargingInformation(String publicIdentity, byte[] msisdn, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, msisdn);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.CHARGING_INFORMATION);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getMSISDN(String publicIdentity, byte[] msisdn, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, msisdn);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.MSISDN);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String getPSIActivation(String publicIdentity, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    UserDataRequest udr = diameterShClientMessageFactory.createUserDataRequest(publicIdentityAvp, DataReferenceType.PSI_ACTIVATION);
    udr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    udr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      udr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendUserDataRequest(udr);

    // Store request for future matching
    storeRequestInACI(activity, udr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String updateRepositoryData(String publicIdentity, String data, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    ProfileUpdateRequest pur = diameterShClientMessageFactory.createProfileUpdateRequest(publicIdentityAvp, DataReferenceType.REPOSITORY_DATA, data.getBytes());
    pur.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    pur.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      pur.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendProfileUpdateRequest(pur);

    // Store request for future matching
    storeRequestInACI(activity, pur);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String updatePSIActivation(String publicIdentity, String data, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    ProfileUpdateRequest pur = diameterShClientMessageFactory.createProfileUpdateRequest(publicIdentityAvp, DataReferenceType.PSI_ACTIVATION, data.getBytes());
    pur.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    pur.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      pur.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendProfileUpdateRequest(pur);

    // Store request for future matching
    storeRequestInACI(activity, pur);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String subscribeRepositoryData(String publicIdentity, byte[][] serviceIndications, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    SubscribeNotificationsRequest snr = diameterShClientMessageFactory.createSubscribeNotificationsRequest(publicIdentityAvp, DataReferenceType.REPOSITORY_DATA, SubsReqType.fromInt(subscriptionRequestType));
    snr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    snr.setServiceIndications(serviceIndications);

    // Set destination -- Realm is mandatory, host is optional
    snr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      snr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendSubscribeNotificationsRequest(snr);
    
    // Store request for future matching
    storeRequestInACI(activity, snr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String subscribeIMSUserState(String publicIdentity, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    SubscribeNotificationsRequest snr = diameterShClientMessageFactory.createSubscribeNotificationsRequest(publicIdentityAvp, DataReferenceType.IMS_USER_STATE, SubsReqType.fromInt(subscriptionRequestType));
    snr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    snr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      snr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendSubscribeNotificationsRequest(snr);
    
    // Store request for future matching
    storeRequestInACI(activity, snr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String subscribeSCSCFName(String publicIdentity, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    SubscribeNotificationsRequest snr = diameterShClientMessageFactory.createSubscribeNotificationsRequest(publicIdentityAvp, DataReferenceType.S_CSCFNAME, SubsReqType.fromInt(subscriptionRequestType));
    snr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    snr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      snr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendSubscribeNotificationsRequest(snr);
    
    // Store request for future matching
    storeRequestInACI(activity, snr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String subscribeInitialFilterCriteria(String publicIdentity, String serverName, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    SubscribeNotificationsRequest snr = diameterShClientMessageFactory.createSubscribeNotificationsRequest(publicIdentityAvp, DataReferenceType.INITIAL_FILTER_CRITERIA, SubsReqType.fromInt(subscriptionRequestType));
    snr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);
    snr.setServerName(serverName);

    // Set destination -- Realm is mandatory, host is optional
    snr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      snr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendSubscribeNotificationsRequest(snr);
    
    // Store request for future matching
    storeRequestInACI(activity, snr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }

  public String subscribePSIActivation(String publicIdentity, int subscriptionRequestType, String destinationRealm, String destinationHost) throws IOException {
    UserIdentityAvp publicIdentityAvp = createUserIdentityAvp(publicIdentity, null);

    SubscribeNotificationsRequest snr = diameterShClientMessageFactory.createSubscribeNotificationsRequest(publicIdentityAvp, DataReferenceType.PSI_ACTIVATION, SubsReqType.fromInt(subscriptionRequestType));
    snr.setAuthSessionState(AuthSessionStateType.NO_STATE_MAINTAINED);

    // Set destination -- Realm is mandatory, host is optional
    snr.setDestinationRealm(new DiameterIdentity(destinationRealm));
    if(destinationHost != null) {
      snr.setDestinationHost(new DiameterIdentity(destinationHost));
    }

    ShClientActivity activity = getShClientActivity();
    activity.sendSubscribeNotificationsRequest(snr);
    
    // Store request for future matching
    storeRequestInACI(activity, snr);
    
    // Return Session-Id, may be used as identifier
    return activity.getSessionId();
  }
  
  // -- HELPER METHODS --------------------------------------------------------

  private UserIdentityAvp createUserIdentityAvp(String publicIdentity, byte[] msisdn) {
    UserIdentityAvp userIdentityAvp = diameterShClientAvpFactory.createUserIdentity();

    if (publicIdentity != null) {
      userIdentityAvp.setPublicIdentity(publicIdentity);
    }
    else if (msisdn != null && msisdn.length > 0) {
      userIdentityAvp.setMsisdn(new String(msisdn));
    }

    return userIdentityAvp;
  }

  private String[] getUserIdentityValues(DiameterShMessage shMessage) {
    UserIdentityAvp uIdAvp = null;

    if(shMessage instanceof UserDataRequest) {
      uIdAvp = ((UserDataRequest)shMessage).getUserIdentity();
    }
    else if(shMessage instanceof ProfileUpdateRequest) {
      uIdAvp = ((ProfileUpdateRequest)shMessage).getUserIdentity();
    }
    else if(shMessage instanceof SubscribeNotificationsRequest) {
      uIdAvp = ((SubscribeNotificationsRequest)shMessage).getUserIdentity();
    }
    else if(shMessage instanceof PushNotificationRequest) {
      uIdAvp = ((PushNotificationRequest)shMessage).getUserIdentity();
    }

    String publicIdentity = null;
    String msisdn = null;
    if(uIdAvp != null) {
      publicIdentity = uIdAvp.getPublicIdentity();
      msisdn = uIdAvp.getMsisdn();
      if(publicIdentity == null && msisdn == null) {
        tracer.warning("Unable to retrieve Public User/Service Identity OR MSISDN. At least one of them should be present.");
      }
    }
    else {
      tracer.warning("User-Identity AVP missing in Diameter Sh Message.");
    }

    return new String[]{publicIdentity, msisdn};
  }

  private ShClientActivity getShClientActivity() throws IOException {
    try {
      ShClientActivity activity = diameterShClientSbbInterface.createShClientActivity();
      ActivityContextInterface aci = diameterShClientACIF.getActivityContextInterface(activity);
      aci.attach(sbbContext.getSbbLocalObject());
      return activity;
    }
    catch (Exception e) {
      throw new IOException(e.getMessage(), e);
    }
  }
  
  private void storeRequestInACI(ShClientActivity activity, DiameterShMessage message) {
    ActivityContextInterface aci = diameterShClientACIF.getActivityContextInterface(activity);
    RequestMappingACI rmACI = asSbbActivityContextInterface(aci);
    
    rmACI.setRequestData(new MessageData(message));
  }

  // -- EVENT HANDLERS FOR DIAMETER REQUESTS ----------------------------------

  public void onSubscriptionNotificationsAnswer(SubscribeNotificationsAnswer event, RequestMappingACI aci) {
    MessageData snrData = aci.getRequestData();

    aci.detach(sbbContext.getSbbLocalObject());

    SubsReqType subsReqType = snrData.getSubsReqType();
    long resultCode = event.getResultCode();

    // only one data ref should be present.. but at least one must!
    DataReferenceType dataRef = snrData.getDataReferences()[0];
    switch(dataRef.getValue()) {
    case DataReferenceType._REPOSITORY_DATA:
      byte[][] serviceIndications = snrData.getServiceIndications();

      getParent().subscribeRepositoryDataResponse(snrData.getPublicIdentity(), serviceIndications, subsReqType.getValue(), resultCode);
      break;
    case DataReferenceType._IMS_USER_STATE:
      getParent().subscribeIMSUserStateResponse(snrData.getPublicIdentity(), subsReqType.getValue(), resultCode);
      break;
    case DataReferenceType._S_CSCFNAME:
      getParent().subscribeSCSCFNameResponse(snrData.getPublicIdentity(), subsReqType.getValue(), resultCode);
      break;
    case DataReferenceType._INITIAL_FILTER_CRITERIA:
      getParent().subscribeInitialFilterCriteriaResponse(snrData.getPublicIdentity(), snrData.getServerName(), subsReqType.getValue(), resultCode);
      break;
    case DataReferenceType._PSI_ACTIVATION:
      getParent().subscribePSIActivationResponse(snrData.getPublicIdentity(), subsReqType.getValue(), resultCode);
      break;
    default:
      //
    }
  }

  public void onProfileUpdateAnswer(ProfileUpdateAnswer event, RequestMappingACI aci) {
    MessageData purData = aci.getRequestData();

    aci.detach(sbbContext.getSbbLocalObject());
    DataReferenceType dataRef = purData.getDataReference();

    long resultCode = event.getResultCode();

    switch(dataRef.getValue()) {
    case DataReferenceType._REPOSITORY_DATA:
      getParent().updateRepositoryDataResponse(purData.getPublicIdentity(), resultCode);
      break;
    case DataReferenceType._PSI_ACTIVATION:
      getParent().updatePSIActivationResponse(purData.getPublicIdentity(), resultCode);
      break;
      default:
        //
    }

  }

  public void onPushNotificationRequest(PushNotificationRequest event, RequestMappingACI aci) {
    // Send success response
    try {
      ShClientSubscriptionActivity activity  = (ShClientSubscriptionActivity) aci.getActivity();
      activity.sendPushNotificationAnswer(2001L, true);
    }
    catch (IOException e) {
      tracer.warning("Failed to send Push-Notification-Answer.", e);
    }
    
    // Retrieve useful data from request
    String [] userIdentityValues = getUserIdentityValues(event);
    String data = new String(event.getUserData());

    // Deliver to parent
    getParent().receivedProfileUpdate(userIdentityValues[0], userIdentityValues[1].getBytes(), data, event.getOriginRealm().toString(), event.getOriginHost().toString());
  }

  public void onUserDataAnswer(UserDataAnswer event, RequestMappingACI aci) {
    MessageData udrData = aci.getRequestData();

    String data = new String(event.getUserData());
    long resultCode = event.getResultCode();

    // only one data ref should be present.. but at least one must!
    DataReferenceType dataRef = udrData.getDataReferences()[0];
    switch(dataRef.getValue()) {
    case DataReferenceType._REPOSITORY_DATA:
      getParent().deliverRepositoryData(udrData.getPublicIdentity(), udrData.getServiceIndications(), resultCode, data);
      break;
    case DataReferenceType._IMS_PUBLIC_IDENTITY:
      IdentitySetType identitySet = udrData.getIdentitySet();
      getParent().deliverIMSPublicIdentity(udrData.getPublicIdentity(), udrData.getMsisdn(), identitySet != null ? identitySet.getValue() : null, resultCode, data);
      break;
    case DataReferenceType._IMS_USER_STATE:
      getParent().deliverIMSUserState(udrData.getPublicIdentity(), resultCode, data);
      break;
    case DataReferenceType._S_CSCFNAME:
      getParent().deliverSCSCFName(udrData.getPublicIdentity(), resultCode, data);
      break;
    case DataReferenceType._INITIAL_FILTER_CRITERIA:
      getParent().deliverInitialFilterCriteria(udrData.getPublicIdentity(), udrData.getServerName(), resultCode, data);
      break;
    case DataReferenceType._LOCATION_INFORMATION:
      RequestedDomainType requestedDomain = udrData.getRequestedDomain();
      getParent().deliverLocationInformation(udrData.getMsisdn(), requestedDomain != null ? requestedDomain.getValue() : null, resultCode, data);
      break;
    case DataReferenceType._USER_STATE:
      requestedDomain = udrData.getRequestedDomain();
      getParent().deliverUserState(udrData.getMsisdn(), requestedDomain != null ? requestedDomain.getValue() : null, resultCode, data);
      break;
    case DataReferenceType._CHARGING_INFORMATION:
      getParent().deliverChargingInformation(udrData.getPublicIdentity(), udrData.getMsisdn(), resultCode, data);
      break;
    case DataReferenceType._MSISDN:
      getParent().deliverMSISDN(udrData.getPublicIdentity(), udrData.getMsisdn(), resultCode, data);
      break;
    case DataReferenceType._PSI_ACTIVATION:
      getParent().deliverPSIActivation(udrData.getPublicIdentity(), resultCode, data);
      break;
    default:
      //
    }

    aci.detach(sbbContext.getSbbLocalObject());
  }

  // -- SBB OBJECT LIFECYCLE METHODS ------------------------------------------

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbActivate()
   */
  @Override
  public void sbbActivate() {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbCreate()
   */
  @Override
  public void sbbCreate() throws CreateException {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception, java.lang.Object, javax.slee.ActivityContextInterface)
   */
  @Override
  public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbLoad()
   */
  @Override
  public void sbbLoad() {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbPassivate()
   */
  @Override
  public void sbbPassivate() {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbPostCreate()
   */
  @Override
  public void sbbPostCreate() throws CreateException {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbRemove()
   */
  @Override
  public void sbbRemove() {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
   */
  @Override
  public void sbbRolledBack(RolledBackContext arg0) {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#sbbStore()
   */
  @Override
  public void sbbStore() {
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
   */
  @Override
  public void setSbbContext(SbbContext sbbContext) {
    this.sbbContext = (SbbContextExt) sbbContext;
    if (tracer == null) {
      tracer = sbbContext.getTracer(HSSClientChildSbb.class.getSimpleName());
    }
    try {
      Context context = (Context) new InitialContext().lookup("java:comp/env");
      diameterShClientSbbInterface = (ShClientProvider) context.lookup("slee/resources/diameter-sh-client-ra-interface");
      diameterShClientACIF = (ShClientActivityContextInterfaceFactory) context.lookup("slee/resources/JDiameterShClientResourceAdaptor/java.net/0.8.1/acif");

      diameterShClientMessageFactory = diameterShClientSbbInterface.getClientMessageFactory();
      diameterShClientAvpFactory = diameterShClientSbbInterface.getClientAvpFactory();
    }
    catch (NamingException e) {
      tracer.severe("Can't set sbb context.", e);
    }
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.Sbb#unsetSbbContext()
   */
  @Override
  public void unsetSbbContext() {
    this.sbbContext = null;
  }
}
