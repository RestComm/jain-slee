/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.mobicents.slee.enabler.hssclient.example;

import java.io.IOException;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;

import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.enabler.hssclient.HSSClientChildSbbLocalObject;
import org.mobicents.slee.enabler.hssclient.HSSClientParent;

/**
 * 
 * @author <a href=mailto:brainslog@gmail.com> Alexandre Mendonca </a>
 */
public abstract class HSSClientParentSbb implements Sbb, HSSClientParent {

  private static Tracer tracer;

  private static final String PUBLIC_IDENTITY_PSI = "sip:alice@open-ims.test";
  private static final String PUBLIC_IDENTITY_IMS_USER_STATE = "sip:bob@open-ims.test";

  private static final String DESTINATION_HOST = null;
  private static final String DESTINATION_REALM = "open-ims.test";

  protected SbbContext sbbContext;

  // SBB Object Lifecycle Methods ---------------------------------------------

  public void sbbActivate() {
  }

  public void sbbCreate() throws CreateException {
  }

  public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {
  }

  public void sbbLoad() {
  }

  public void sbbPassivate() {
  }

  public void sbbPostCreate() throws CreateException {
  }

  public void sbbRemove() {
  }

  public void sbbRolledBack(RolledBackContext arg0) {
  }

  public void sbbStore() {
  }

  public void setSbbContext(SbbContext sbbContext) {
    this.sbbContext = sbbContext;
    if (tracer == null) {
      tracer = sbbContext.getTracer(HSSClientParentSbb.class.getSimpleName());
    }
  }

  public void unsetSbbContext() {
    this.sbbContext = null;
  }

  public abstract void setNotifyCount(int b);

  public abstract int getNotifyCount();

  public abstract ChildRelationExt getHSSClientChildSbbChildRelation();

  public void onStartServiceEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {
	  HSSClientChildSbbLocalObject child;

    try {
      child = (HSSClientChildSbbLocalObject) this.getHSSClientChildSbbChildRelation().create(ChildRelationExt.DEFAULT_CHILD_NAME);
      
      try {
        // Request alice PSI Activation
        child.getPSIActivation(PUBLIC_IDENTITY_PSI, DESTINATION_REALM, DESTINATION_HOST);
        // child.getIMSPublicIdentity(PUBLIC_IDENTITY, null, 0, DESTINATION_REALM, DESTINATION_HOST);
        if (tracer.isInfoEnabled()) {
          tracer.info("###### STEP 1 # Sent User-Data-Request to HSS for '" + PUBLIC_IDENTITY_PSI + "'");
        }
      }
      catch (IOException ioe) {
        tracer.severe("Unable to send UDR.", ioe);			  
      }
    }
    catch (CreateException ce) {
      tracer.severe("Failed to create Child Relation.", ce);
    }
  }

  public void onActivityEndEvent(javax.slee.ActivityEndEvent event, ActivityContextInterface aci) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Received Activtiy End: " + aci.getActivity());
    }

    if (aci.getActivity() instanceof ServiceActivity) {
      ServiceActivity sa = (ServiceActivity) aci.getActivity();
      if (sa.getService().equals(this.sbbContext.getService())) {
        // TODO: terminate
      }
    }
  }

  // Parent Interface implementation ------------------------------------------

  @Override
  public void deliverRepositoryData(String publicIdentity, byte[][] serviceIndications, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "Repository Data";
      String serviceIndicationsString = "";
      for(byte[] serviceIndication : serviceIndications) {
        serviceIndicationsString += new String(serviceIndication);
      }
      tracer.info(publicIdentity + "/" + serviceIndicationsString + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverIMSPublicIdentity(String publicIdentity, byte[] msisdn, int identitySet, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "IMS Public Identity";
      tracer.info((publicIdentity != null ? publicIdentity : new String(msisdn)) + "/" + identitySet + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverIMSUserState(String publicIdentity, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "IMS User State";
      tracer.info(publicIdentity + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverSCSCFName(String publicIdentity, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "S-CSCF Name";
      tracer.info(publicIdentity + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverInitialFilterCriteria(String publicIdentity, String serverName, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "S-CSCF Name";
      tracer.info(publicIdentity + "/" + serverName + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverLocationInformation(byte[] msisdn, int requestedDomain, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "Location Information";
      tracer.info(new String(msisdn) + "/" + requestedDomain + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverUserState(byte[] msisdn, int requestedDomain, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "User State";
      tracer.info(new String(msisdn) + "/" + requestedDomain + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverChargingInformation(String publicIdentity, byte[] msisdn, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "Charging Information";
      tracer.info((publicIdentity != null ? publicIdentity : new String(msisdn)) + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverMSISDN(String publicIdentity, byte[] msisdn, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "MSISDN";
      tracer.info((publicIdentity != null ? publicIdentity : new String(msisdn)) + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }
  }

  @Override
  public void deliverPSIActivation(String publicIdentity, long resultCode, String data) {
    if (tracer.isInfoEnabled()) {
      String dataType = "S-CSCF Name";
      tracer.info(publicIdentity + " '" + dataType + "' delivery: Result-Code = '" + resultCode + "'");
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }

    if (tracer.isInfoEnabled()) {
      tracer.info("###### STEP 2 # Received User-Data-Answer from HSS for '" + PUBLIC_IDENTITY_PSI + "'");
    }

    // Now we try to update PSI-Activation data
    HSSClientChildSbbLocalObject child = (HSSClientChildSbbLocalObject) this.getHSSClientChildSbbChildRelation().get(ChildRelationExt.DEFAULT_CHILD_NAME);
    try {
    	if (data.contains("<PSIActivation>0</PSIActivation>")) {
    		if (tracer.isInfoEnabled()) {
    			tracer.info("###### STEP 3 # Setting PSI Activation from INACTIVE to ACTIVE in User-Data ...");
    		}
    		data = data.replaceAll("<PSIActivation>0</PSIActivation>", "<PSIActivation>1</PSIActivation>");
    	}
    	else {
    		if (tracer.isInfoEnabled()) {
    			tracer.info("###### STEP 3 # Setting PSI Activation from ACTIVE to INACTIVE in User-Data ...");
    		}
    		data = data.replaceAll("<PSIActivation>1</PSIActivation>", "<PSIActivation>0</PSIActivation>");
    	}
    	child.updatePSIActivation(PUBLIC_IDENTITY_PSI, data, DESTINATION_REALM, DESTINATION_HOST);
    	if (tracer.isInfoEnabled()) {
    		tracer.info("###### STEP 4 # Sent Profile-Update-Request to HSS for '" + PUBLIC_IDENTITY_PSI + "'");
    	}
    }
    catch (IOException ioe) {
    	tracer.severe("Unable to send PUR.", ioe);             
    }

  }

  @Override
  public void updateRepositoryDataResponse(String publicIdentity, long resultCode) {
    if (tracer.isInfoEnabled()) {
      String dataType = "Repository Data";
      tracer.info("Update to " + publicIdentity + "'s '" + dataType + "' response: Result-Code = '" + resultCode + "'");
    }
  }

  @Override
  public void updatePSIActivationResponse(String publicIdentity, long resultCode) {
    if (tracer.isInfoEnabled()) {
      String dataType = "PSI Activation";
      tracer.info("Update to " + publicIdentity + "'s '" + dataType + "' response: Result-Code = '" + resultCode + "'");
    }

    if (tracer.isInfoEnabled()) {
      tracer.info("###### STEP 5 # Received Profile-Update-Answer from HSS for '" + PUBLIC_IDENTITY_PSI + "'");
    }

    // Now we try to subscribe to alice's IMS User State
    HSSClientChildSbbLocalObject child = (HSSClientChildSbbLocalObject) this.getHSSClientChildSbbChildRelation().get(ChildRelationExt.DEFAULT_CHILD_NAME);
    try {
    	if (tracer.isInfoEnabled()) {
    		tracer.info("###### STEP 6 # Subscribing to IMS User State for '" + PUBLIC_IDENTITY_IMS_USER_STATE + "'");
    	}
    	child.subscribeIMSUserState(PUBLIC_IDENTITY_IMS_USER_STATE, 0, DESTINATION_REALM, DESTINATION_HOST);
    }
    catch (IOException ioe) {
    	tracer.severe("Unable to send SNR.", ioe);             
    }

  }

  @Override
  public void subscribeRepositoryDataResponse(String publicIdentity, byte[][] serviceIndications, int subscriptionRequestType, long resultCode) {
    if (tracer.isInfoEnabled()) {
      String dataType = "Repository Data";
      String reqType = subscriptionRequestType == 0 ? "SUBSCRIBE" : "UNSUBSCRIBE";
      String serviceIndicationsString = "";
      for(byte[] serviceIndication : serviceIndications) {
        serviceIndicationsString += new String(serviceIndication);
      }
      tracer.info(publicIdentity + "/" + serviceIndicationsString + " answer to " + reqType + " '" + dataType + "': Result-Code = '" + resultCode + "'");
    }
  }

  @Override
  public void subscribeIMSUserStateResponse(String publicIdentity, int subscriptionRequestType, long resultCode) {
    if (tracer.isInfoEnabled()) {
      String dataType = "User State";
      String reqType = subscriptionRequestType == 0 ? "SUBSCRIBE" : "UNSUBSCRIBE";
      tracer.info(publicIdentity + " answer to " + reqType + " '" + dataType + "': Result-Code = '" + resultCode + "'");
    }

    // If it's good, we are subscribed
    if (resultCode == 2001) {
      if (tracer.isInfoEnabled()) {
        tracer.info("###### STEP 7 # Subscribed successfuly to IMS User State for '" + PUBLIC_IDENTITY_IMS_USER_STATE + "'");
        tracer.info("###### STEP 8 # !USER ACTION NEEDED! Please change (register or unregister) the SIP client for '" + PUBLIC_IDENTITY_IMS_USER_STATE + "'...");
      }
    }
    else {
      tracer.severe("Failed to Subscribe to User Profile/IMS User State for '" + PUBLIC_IDENTITY_IMS_USER_STATE + "'.");
    }
  }

  @Override
  public void subscribeSCSCFNameResponse(String publicIdentity, int subscriptionRequestType, long resultCode) {
    if (tracer.isInfoEnabled()) {
      String dataType = "S-CSCF Name";
      String reqType = subscriptionRequestType == 0 ? "SUBSCRIBE" : "UNSUBSCRIBE";
      tracer.info(publicIdentity + " answer to " + reqType + " '" + dataType + "': Result-Code = '" + resultCode + "'");
    }
  }

  @Override
  public void subscribeInitialFilterCriteriaResponse(String publicIdentity, String serverName, int subscriptionRequestType, long resultCode) {
    if (tracer.isInfoEnabled()) {
      String dataType = "Initial Filter Criteria";
      String reqType = subscriptionRequestType == 0 ? "SUBSCRIBE" : "UNSUBSCRIBE";
      tracer.info(publicIdentity + "/" + serverName + " answer to " + reqType + " '" + dataType + "': Result-Code = '" + resultCode + "'");
    }
  }

  @Override
  public void subscribePSIActivationResponse(String publicIdentity, int subscriptionRequestType, long resultCode) {
    if (tracer.isInfoEnabled()) {
      String dataType = "PSI Activation";
      String reqType = subscriptionRequestType == 0 ? "SUBSCRIBE" : "UNSUBSCRIBE";
      tracer.info(publicIdentity + " answer to " + reqType + " '" + dataType + "': Result-Code = '" + resultCode + "'");
    }
  }

  @Override
  public void receivedProfileUpdate(String userIdentity, byte[] msisdn, String data, String originRealm, String originHost) {
    if (tracer.isInfoEnabled()) {
      tracer.info("Received updated User Profile for " + (userIdentity != null ? userIdentity : new String(msisdn)) + " from " + originRealm + "/" + originHost);
      if (data != null) {
        tracer.info("User-Data: " + data);
      }
    }

    if (tracer.isInfoEnabled()) {
      tracer.info("###### STEP 9 # Received profile update for '" + PUBLIC_IDENTITY_IMS_USER_STATE + "'. EXAMPLE COMPLETED SUCCESSFULY!");
    }
  }

}
