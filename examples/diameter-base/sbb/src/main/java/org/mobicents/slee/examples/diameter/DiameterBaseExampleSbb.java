/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.examples.diameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;

import net.java.slee.resource.diameter.base.AccountingClientSessionActivity;
import net.java.slee.resource.diameter.base.AccountingServerSessionActivity;
import net.java.slee.resource.diameter.base.DiameterActivityContextInterfaceFactory;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.DiameterProvider;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.ErrorAnswer;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;

/**
 * Base accounting example is a simple application that interacts with
 * Ericsson Diameter SDK (both client and server).
 * More info {@linkplain http://groups.google.com/group/mobicents-public/web/mobicents-diameter-base} 
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Erick Svenson
 */
public abstract class DiameterBaseExampleSbb implements javax.slee.Sbb {

  private Tracer tracer;

  private SbbContext sbbContext = null; // This SBB's context

  private Context myEnv = null; // This SBB's environment

  private DiameterActivityContextInterfaceFactory acif = null;
  
  private DiameterProvider provider = null;

  private DiameterMessageFactory messageFactory = null;
  private DiameterAvpFactory avpFactory = null;

  private TimerFacility timerFacility = null;
  private boolean actAsServer = false;
  private String originIP = "127.0.0.1";
  private String originPort = "1812";
  private String originRealm = "mobicents.org";

  private String destinationIP = "127.0.0.1";
  private String destinationPort = "21812";
  private String destinationRealm = "mobicents.org";

  public void setSbbContext(SbbContext context) {

    this.sbbContext = context;

    this.tracer = sbbContext.getTracer(DiameterBaseExampleSbb.class.getSimpleName());

    try {
      myEnv = (Context) new InitialContext().lookup("java:comp/env");

      provider = (DiameterProvider) myEnv.lookup("slee/resources/diameter-base-ra/provider");
      
      acif = (DiameterActivityContextInterfaceFactory) myEnv.lookup("slee/resources/diameter-base-ra/acif");
      
      if(tracer.isFineEnabled()) {
        tracer.fine("Got Provider:" + provider);
      }

      messageFactory = provider.getDiameterMessageFactory();
      if(tracer.isFineEnabled()) {
        tracer.fine("Got Message Factory:" + provider);
      }

      avpFactory = provider.getDiameterAvpFactory();
      if(tracer.isFineEnabled()) {
        tracer.fine("Got AVP Factory:" + provider);
      }

      // Get the timer facility
      timerFacility = (TimerFacility) myEnv.lookup("slee/facilities/timer");
    }
    catch (Exception e) {
      tracer.severe("Unable to set sbb context.", e);
    }
  }

  public void unsetSbbContext() {
    if(tracer.isFineEnabled()) {
      tracer.fine("unsetSbbContext invoked.");
    }

    this.sbbContext = null;
  }

  public void sbbCreate() throws javax.slee.CreateException {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbCreate invoked.");
    }
  }

  public void sbbPostCreate() throws javax.slee.CreateException {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbPostCreate invoked.");
    }
  }

  public void sbbActivate() {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbActivate invoked.");
    }
  }

  public void sbbPassivate() {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbPassivate invoked.");
    }
  }

  public void sbbRemove() {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbRemove invoked.");
    }
  }

  public void sbbLoad() {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbLoad invoked.");
    }
  }

  public void sbbStore() {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbStore invoked.");
    }
  }

  public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbExceptionThrown invoked.");
    }
  }

  public void sbbRolledBack(RolledBackContext context) {
    if(tracer.isFineEnabled()) {
      tracer.fine("sbbRolledBack invoked.");
    }
  }

  protected SbbContext getSbbContext() {
    if(tracer.isFineEnabled()) {
      tracer.fine("getSbbContext invoked.");
    }

    return sbbContext;
  }

  // ##########################################################################
  // ## EVENT HANDLERS ##
  // ##########################################################################

  public void onServiceStartedEvent(javax.slee.serviceactivity.ServiceStartedEvent event, ActivityContextInterface aci) {
    if(tracer.isFineEnabled()) {
      tracer.fine("onServiceStartedEvent invoked.");
    }

    try {
      // check if it's my service that is starting
      ServiceActivity sa = ((ServiceActivityFactory) myEnv.lookup("slee/serviceactivity/factory")).getActivity();
      if (sa.equals(aci.getActivity())) {
        if(tracer.isInfoEnabled()) {
          tracer.info("################################################################################");
          tracer.info("### D I A M E T E R   E X A M P L E   A P P L I C A T I O N  :: S T A R T E D ##");
          tracer.info("################################################################################");
        }

        messageFactory = provider.getDiameterMessageFactory();
        avpFactory = provider.getDiameterAvpFactory();

        if(tracer.isFineEnabled()) {
          tracer.fine("Performing sanity check...");
          tracer.fine("Provider [" + provider + "]");
          tracer.fine("Message Factory [" + messageFactory + "]");
          tracer.fine("AVP Factory [" + avpFactory + "]");
          tracer.fine("Check completed. Result: " + ((provider != null ? 1 : 0) + (messageFactory != null ? 1 : 0) + (avpFactory != null ? 1 : 0)) + "/3");
        }

        if(tracer.isInfoEnabled()) {
          tracer.info("Connected to " + provider.getPeerCount() + " peers.");
          for (DiameterIdentity peer : provider.getConnectedPeers()) {
            tracer.info("Connected to Peer[" + peer.toString() + "]");
          }
        }

        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("example.properties"));
        this.actAsServer = props.getProperty("example.mode") == null ? this.actAsServer : !props.getProperty("example.mode").trim().equals("client");
        this.originIP = props.getProperty("origin.ip") == null ? this.originIP : props.getProperty("origin.ip");
        this.originPort = props.getProperty("origin.port") == null ? this.originPort : props.getProperty("origin.port");
        this.originRealm = props.getProperty("origin.realm") == null ? this.originRealm : props.getProperty("origin.realm");

        this.destinationIP = props.getProperty("destination.ip") == null ? this.destinationIP : props.getProperty("destination.ip");
        this.destinationPort = props.getProperty("destination.port") == null ? this.destinationPort : props.getProperty("destination.port");
        this.destinationRealm = props.getProperty("destination.realm") == null ? this.destinationRealm : props.getProperty("destination.realm");

        if(tracer.isInfoEnabled()) {
          tracer.info("Diameter Base Example :: Initialized in " + (actAsServer ? "SERVER" : "CLIENT") + " mode.");
        }

        if (!actAsServer) {
          TimerOptions options = new TimerOptions();
          timerFacility.setTimer(aci, null, System.currentTimeMillis() + 30000, options);
        }
      }
    }
    catch (Exception e) {
      tracer.severe("Unable to handle service started event...", e);
    }
  }

  public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
    sendAccountingRequest();
  }

  public void onAccountingRequest(AccountingRequest acr, ActivityContextInterface aci) {
    long start = System.currentTimeMillis();
    if (tracer.isInfoEnabled()) {
      tracer.info("Accounting-Request received. [" + acr + "]");
    }

    boolean actAsProxy = false;

    try {
      // Are we gonna act as a proxy?
      if (actAsProxy) {
        // In here we act as a "proxy". Just for testing we take the original message,
        // replace the Origin/Destination Host/Realm AVPs and send it to the emulator.

        boolean hasDestinationHost = false;
        boolean hasDestinationRealm = false;

        List<DiameterAvp> avps = new ArrayList<DiameterAvp>();

        for (DiameterAvp avp : acr.getAvps()) {
          switch (avp.getCode()) {
          case DiameterAvpCodes.ORIGIN_HOST:
            avps.add(avpFactory.createAvp(DiameterAvpCodes.ORIGIN_HOST, "aaa://"+originIP+":"+originPort.getBytes()));
            break;
          case DiameterAvpCodes.ORIGIN_REALM:
            avps.add(avpFactory.createAvp(DiameterAvpCodes.ORIGIN_REALM, originRealm.getBytes()));
            break;
          case DiameterAvpCodes.DESTINATION_HOST:
            avps.add(avpFactory.createAvp(DiameterAvpCodes.DESTINATION_HOST, "aaa://"+destinationIP+":"+destinationPort.getBytes()));
            hasDestinationHost = true;
            break;
          case DiameterAvpCodes.DESTINATION_REALM:
            avps.add(avpFactory.createAvp(DiameterAvpCodes.DESTINATION_REALM, destinationRealm.getBytes()));
            hasDestinationRealm = true;
            break;
          default:
            avps.add(avp);
          }
        }

        if (!hasDestinationHost) {
          avps.add(avpFactory.createAvp(DiameterAvpCodes.DESTINATION_HOST, "127.0.0.1".getBytes()));
        }

        if (!hasDestinationRealm) {
          avps.add(avpFactory.createAvp(DiameterAvpCodes.DESTINATION_REALM, "mobicents.org".getBytes()));
        }

        DiameterAvp[] avpArray = new DiameterAvp[avps.size()];
        avpArray = avps.toArray(avpArray);
        if (tracer.isInfoEnabled()) {
          tracer.info("Creating Custom Message...");
        }
        DiameterMessage proxiedACR = messageFactory.createAccountingRequest(avpArray);
        if (tracer.isInfoEnabled()) {
          tracer.info("Created Custom Message[" + proxiedACR + "]");

          tracer.info("Sending Custom Message...");
        }
        provider.createActivity().sendMessage(proxiedACR);
        if (tracer.isInfoEnabled()) {
          tracer.info("Sent Custom Message[" + proxiedACR + "]");
        }
      }
      else {
        // In here we act as a server and just say it's SUCCESS.

        if (aci.getActivity() instanceof AccountingServerSessionActivity) {
          AccountingServerSessionActivity assa = (AccountingServerSessionActivity) aci.getActivity();

          AccountingAnswer ans = assa.createAccountingAnswer(acr);
          ans.setResultCode(2001); // 2001 = SUCCESS

          if (tracer.isInfoEnabled()) {
            tracer.info("Sending Accounting-Answer [" + ans + "]");
          }

          assa.sendAccountingAnswer(ans);
          if (tracer.isInfoEnabled()) {
            tracer.info("Accounting-Answer sent.");
          }
        }
      }
    }
    catch (Exception e) {
      tracer.severe("", e);
    }

    long end = System.currentTimeMillis();

    if (tracer.isInfoEnabled()) {
      tracer.info("Accounting-Request proccessed. [" + (end - start) + "ms]");
    }
  }

  public void onAccountingAnswer(AccountingAnswer aca, ActivityContextInterface aci) {
    if (tracer.isInfoEnabled()) {
      tracer.info("Accounting-Answer received. Result-Code[" + aca.getResultCode() + "].");
    }
  }

  public void onErrorAnswer(ErrorAnswer era, ActivityContextInterface aci) {
    if (tracer.isInfoEnabled()) {
      tracer.info("Error-Answer received.");
    }
  }

  // ##########################################################################
  // ## PRIVATE METHODS ##
  // ##########################################################################

  private void sendAccountingRequest() {
    try {
      AccountingClientSessionActivity activity = provider.createAccountingClientSessionActivity();

      List<DiameterAvp> avps = new ArrayList<DiameterAvp>();

      avps.add(avpFactory.createAvp(DiameterAvpCodes.SESSION_ID, activity.getSessionId().getBytes()));

      DiameterAvp avpVendorId = avpFactory.createAvp(DiameterAvpCodes.VENDOR_ID, 193);
      DiameterAvp avpAcctApplicationId = avpFactory.createAvp(DiameterAvpCodes.ACCT_APPLICATION_ID, 19302);

      avps.add(avpFactory.createAvp(DiameterAvpCodes.VENDOR_SPECIFIC_APPLICATION_ID, new DiameterAvp[] { avpVendorId, avpAcctApplicationId }));

      avps.add(avpFactory.createAvp(DiameterAvpCodes.ORIGIN_HOST, this.originIP.getBytes()));
      avps.add(avpFactory.createAvp(DiameterAvpCodes.ORIGIN_REALM, this.originRealm.getBytes()));

      avps.add(avpFactory.createAvp(DiameterAvpCodes.DESTINATION_HOST, (this.destinationIP + ":" + this.destinationPort).getBytes()));
      avps.add(avpFactory.createAvp(DiameterAvpCodes.DESTINATION_REALM, this.destinationRealm.getBytes()));

      // Subscription ID
      DiameterAvp subscriptionIdType = avpFactory.createAvp(193, 555, 0);
      DiameterAvp subscriptionIdData = avpFactory.createAvp(193, 554, "00001000");
      avps.add(avpFactory.createAvp(193, 553, new DiameterAvp[] { subscriptionIdType, subscriptionIdData }));

      // Requested Service Unit
      DiameterAvp unitType = avpFactory.createAvp(193, 611, 2);
      DiameterAvp valueDigits = avpFactory.createAvp(193, 617, 10L);
      DiameterAvp unitValue = avpFactory.createAvp(193, 612, new DiameterAvp[] { valueDigits });
      avps.add(avpFactory.createAvp(193, 606, new DiameterAvp[] { unitType, unitValue }));

      // Record Number and Type
      avps.add(avpFactory.createAvp(DiameterAvpCodes.ACCOUNTING_RECORD_NUMBER, 0));
      avps.add(avpFactory.createAvp(DiameterAvpCodes.ACCOUNTING_RECORD_TYPE, 1));

      // Requested action
      avps.add(avpFactory.createAvp(193, 615, 0));

      // Service Parameter Type
      DiameterAvp serviceParameterType = avpFactory.createAvp(193, 608, 0);
      DiameterAvp serviceParameterValue = avpFactory.createAvp(193, 609, "510");
      avps.add(avpFactory.createAvp(193, 607, new DiameterAvp[] { serviceParameterType, serviceParameterValue }));

      // Service Parameter Type
      DiameterAvp serviceParameterType2 = avpFactory.createAvp(193, 608, 14);
      DiameterAvp serviceParameterValue2 = avpFactory.createAvp(193, 609, "20");
      avps.add(avpFactory.createAvp(193, 607, new DiameterAvp[] { serviceParameterType2, serviceParameterValue2 }));

      DiameterAvp[] avpArray = new DiameterAvp[avps.size()];
      avpArray = avps.toArray(avpArray);

      if (tracer.isInfoEnabled()) {
        tracer.info("Creating Accounting-Request...");
      }

      AccountingRequest acr = messageFactory.createAccountingRequest(avpArray);

      if (tracer.isInfoEnabled()) {
        tracer.info("Created Accounting-Request [" + acr + "]");
        tracer.info("Sending Accounting-Request...");
      }
      
      ActivityContextInterface aci = acif.getActivityContextInterface(activity);
      
      activity.sendAccountRequest(acr);
      
      aci.attach(sbbContext.getSbbLocalObject());
      
      if (tracer.isInfoEnabled()) {
        tracer.info("Sent Accounting-Request");
      }
    }
    catch (Exception e) {
      tracer.severe("", e);
    }
  }
}
