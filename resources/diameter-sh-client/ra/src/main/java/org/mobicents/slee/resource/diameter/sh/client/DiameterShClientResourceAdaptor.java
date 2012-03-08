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

package org.mobicents.slee.resource.diameter.sh.client;

import static org.jdiameter.client.impl.helpers.Parameters.MessageTimeOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.management.ObjectName;
import javax.naming.OperationNotSupportedException;
import javax.slee.Address;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.Validator;
import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterException;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.client.ShClientActivity;
import net.java.slee.resource.diameter.sh.client.ShClientMessageFactory;
import net.java.slee.resource.diameter.sh.client.ShClientProvider;
import net.java.slee.resource.diameter.sh.client.ShClientSubscriptionActivity;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer;
import net.java.slee.resource.diameter.sh.events.UserDataAnswer;

import org.jboss.mx.util.MBeanServerLocator;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.Peer;
import org.jdiameter.api.PeerTable;
import org.jdiameter.api.Request;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.Session;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.sh.ClientShSession;
import org.jdiameter.api.sh.events.ProfileUpdateAnswer;
import org.jdiameter.api.sh.events.PushNotificationRequest;
import org.jdiameter.api.sh.events.SubscribeNotificationsRequest;
import org.jdiameter.api.sh.events.UserDataRequest;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.client.impl.app.sh.ShClientSessionImpl;
import org.jdiameter.common.impl.app.AppAnswerEventImpl;
import org.jdiameter.common.impl.app.AppRequestEventImpl;
import org.jdiameter.common.impl.app.sh.ShSessionFactoryImpl;
import org.mobicents.diameter.stack.DiameterListener;
import org.mobicents.diameter.stack.DiameterStackMultiplexerMBean;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;
import org.mobicents.slee.resource.diameter.AbstractClusteredDiameterActivityManagement;
import org.mobicents.slee.resource.diameter.DiameterActivityManagement;
import org.mobicents.slee.resource.diameter.LocalDiameterActivityManagement;
import org.mobicents.slee.resource.diameter.ValidatorImpl;
import org.mobicents.slee.resource.diameter.base.DiameterActivityHandle;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterBaseMarshaler;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.base.EventIDFilter;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;
import org.mobicents.slee.resource.diameter.sh.DiameterShAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.sh.EventIDCache;
import org.mobicents.slee.resource.diameter.sh.events.ProfileUpdateAnswerImpl;
import org.mobicents.slee.resource.diameter.sh.events.ProfileUpdateRequestImpl;
import org.mobicents.slee.resource.diameter.sh.events.PushNotificationAnswerImpl;
import org.mobicents.slee.resource.diameter.sh.events.PushNotificationRequestImpl;
import org.mobicents.slee.resource.diameter.sh.events.SubscribeNotificationsAnswerImpl;
import org.mobicents.slee.resource.diameter.sh.events.SubscribeNotificationsRequestImpl;
import org.mobicents.slee.resource.diameter.sh.events.UserDataAnswerImpl;
import org.mobicents.slee.resource.diameter.sh.events.UserDataRequestImpl;

/**
 * 
 * <br>Project: mobicents-diameter-server
 * <br>11:08:09 AM May 26, 2009 
 * <br>
 *
 * Mobicents Diameter Sh (Client-side) Resource Adaptor
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DiameterShClientResourceAdaptor implements ResourceAdaptor, DiameterListener , DiameterRAInterface ,org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor<String, DiameterActivity> {

  private static final long serialVersionUID = 1L;

  // Config Properties Names ---------------------------------------------

  private static final String AUTH_APPLICATION_IDS = "authApplicationIds";

  // Config Properties Values --------------------------------------------

  private List<ApplicationId> authApplicationIds;

  /**
   * caches the eventIDs, avoiding lookup in container
   */
  public final EventIDCache eventIdCache = new EventIDCache();

  /**
   * tells the RA if an event with a specified ID should be filtered or not
   */
  private final EventIDFilter eventIDFilter = new EventIDFilter();

  /**
   * The ResourceAdaptorContext interface is implemented by the SLEE. It provides the Resource
   * Adaptor with the required capabilities in the SLEE to execute its work. The ResourceAdaptorCon-
   * text object holds references to a number of objects that are of interest to many Resource Adaptors. A
   * resource adaptor object is provided with a ResourceAdaptorContext object when the setResour-
   * ceAdaptorContext method of the ResourceAdaptor interface is invoked on the resource adaptor
   * object. 
   */
  private ResourceAdaptorContext raContext;

  /**
   * FT/HA version of RA context.
   */
  private FaultTolerantResourceAdaptorContext<String, DiameterActivity> ftRAContext;

  /**
   * The SLEE endpoint defines the contract between the SLEE and the resource
   * adaptor that enables the resource adaptor to deliver events
   * asynchronously to SLEE endpoints residing in the SLEE. This contract
   * serves as a generic contract that allows a wide range of resources to be
   * plugged into a SLEE environment via the resource adaptor architecture.
   * For further information see JSLEE v1.1 Specification Page 307 The
   * sleeEndpoint will be initialized in entityCreated() method.
   */
  private transient SleeEndpoint sleeEndpoint = null;

  /**
   * A tracer is represented in the SLEE by the Tracer interface. Notification sources access the Tracer Facil-
   * ity through a Tracer object that implements the Tracer interface. A Tracer object can be obtained by
   * SBBs via the SbbContext interface, by resource adaptor entities via the ResourceAdaptorContext
   * interface, and by profiles via the ProfileContext interface. 
   */
  private Tracer tracer;
  private DiameterBaseMarshaler marshaler = new DiameterBaseMarshaler();
  // Diameter Specific Properties ----------------------------------------

  private Stack stack;
  private SessionFactory sessionFactory = null;
  private long messageTimeout = 5000;

  private long activityRemoveDelay = 30000;

  private ObjectName diameterMultiplexerObjectName = null;
  private DiameterStackMultiplexerMBean diameterMux = null;

  private DiameterAvpFactory baseAvpFactory = null;
  private DiameterShAvpFactory shAvpFactory = null;
  
  private ShClientMessageFactory shClientMessageFactory = null;

  /**
   * the EventLookupFacility is used to look up the event id of incoming
   * events
   */
  private transient EventLookupFacility eventLookup = null;

  /**
   * The list of activites stored in this resource adaptor. If this resource
   * adaptor were a distributed and highly available solution, this storage
   * were one of the candidates for distribution.
   */
  //private transient ConcurrentHashMap<ActivityHandle, DiameterActivity> activities = null;
  private transient DiameterActivityManagement activities = null;

  /**
   * A link to the DiameterProvider which then will be exposed to Sbbs
   */
  private transient ShClientProviderImpl raProvider = null;

  /**
   * for all events we are interested in knowing when the event failed to be processed
   */
  private static final int EVENT_FLAGS = getEventFlags();

  private static int getEventFlags() {
    int eventFlags = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;
    eventFlags = EventFlags.setRequestProcessingFailedCallback(eventFlags);
    eventFlags = EventFlags.setRequestProcessingSuccessfulCallback(eventFlags);
    return eventFlags;
  }

  private static final int DEFAULT_ACTIVITY_FLAGS = ActivityFlags.setRequestSleeActivityGCCallback(ActivityFlags.REQUEST_ENDED_CALLBACK);
  private static final int MARSHALABLE_ACTIVITY_FLAGS = ActivityFlags.setSleeMayMarshal(DEFAULT_ACTIVITY_FLAGS);

  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[]{};

  private static final String[] EMPTY_STRING_ARRAY = new String[]{};

  public DiameterShClientResourceAdaptor() {
    // TODO: Initialize any default values.
  }

  // Lifecycle methods ---------------------------------------------------

  public void setResourceAdaptorContext(ResourceAdaptorContext context) {
    this.raContext = context;
    this.tracer = context.getTracer("DiameterShClientResourceAdaptor");
    this.sleeEndpoint = context.getSleeEndpoint();
    this.eventLookup = context.getEventLookupFacility();
    this.raProvider = new ShClientProviderImpl(this);
  }

  public void unsetResourceAdaptorContext() {
    this.raContext = null;
    this.tracer = null;
    this.sleeEndpoint = null;
    this.eventLookup = null;
    this.raProvider = null;
  }

  // FT Lifecycle methods ------------------------------------------------

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#setFaultTolerantResourceAdaptorContext
   *   (org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext)
   */
  public void setFaultTolerantResourceAdaptorContext(FaultTolerantResourceAdaptorContext<String, DiameterActivity> ctx) {
    this.ftRAContext = ctx;
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#unsetFaultTolerantResourceAdaptorContext()
   */
  public void unsetFaultTolerantResourceAdaptorContext() {
    this.ftRAContext = null;
    //clear this.activities ??
  }

  // FT methods ----------------------------------------------------------

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#dataRemoved(java.io.Serializable)
   */
  public void dataRemoved(String arg0) {
    this.activities.remove(getActivityHandle(arg0));
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#failOver(java.io.Serializable)
   */
  public void failOver(String arg0) {
    throw new UnsupportedOperationException();
  }

  public void raActive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShClient RA :: raActive.");
    }

    try {
      if(tracer.isInfoEnabled()) {
        tracer.info("Activating Diameter ShClient RA Entity");
      }

      this.diameterMultiplexerObjectName = new ObjectName("diameter.mobicents:service=DiameterStackMultiplexer");

      Object object = MBeanServerLocator.locateJBoss().invoke(this.diameterMultiplexerObjectName, "getMultiplexerMBean", EMPTY_OBJECT_ARRAY, EMPTY_STRING_ARRAY);

      if(object instanceof DiameterStackMultiplexerMBean) {
        this.diameterMux = (DiameterStackMultiplexerMBean) object;
      }

      //this.activities = new ConcurrentHashMap<ActivityHandle, DiameterActivity>();

      initStack();

      // Initialize activity mgmt
      initActivitiesMgmt();

      // Initialize factories
      this.baseAvpFactory = new DiameterAvpFactoryImpl();
      this.shAvpFactory = new DiameterShAvpFactoryImpl(baseAvpFactory);

      this.shClientMessageFactory = new ShClientMessageFactoryImpl(stack);

      // Set the first configured Application-Id as default for message factory
      ApplicationId firstAppId = authApplicationIds.get(0);
      ((ShClientMessageFactoryImpl)this.shClientMessageFactory).setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

      // Setup session factories
      this.sessionFactory = this.stack.getSessionFactory();

      ((ISessionFactory) sessionFactory).registerAppFacory(ClientShSession.class, new ShClientSessionFactory(this, this.sessionFactory));
    }
    catch (Exception e) {
      tracer.severe("Error Activating Diameter ShClient RA Entity", e);
    }
  }

  public void raStopping() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShClient RA :: raStopping.");
    }

    try {
      diameterMux.unregisterListener(this);
    }
    catch (Exception e) {
      tracer.severe("Failed to unregister ShClient RA from Diameter Mux.", e);
    }

    //    synchronized (this.activities) {
    //      for (ActivityHandle activityHandle : activities.keySet()) {
    //        try {
    //          if(tracer.isInfoEnabled()) {
    //            tracer.info("Ending activity [" + activityHandle + "]");
    //          }
    //
    //          activities.get(activityHandle).endActivity();
    //        }
    //        catch (Exception e) {
    //          tracer.severe("Error Deactivating Activity", e);
    //        }
    //      }
    //    }

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter ShClient RA :: entityDeactivating completed.");
    }
  }

  public void raInactive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShClient RA :: entityDeactivated.");
    }

    //    synchronized (this.activities) {
    //      activities.clear();
    //    }
    activities = null;

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter ShClient RA :: INACTIVE completed.");
    }
  }

  public void raConfigure(ConfigProperties properties) {
    parseApplicationIds((String) properties.getProperty(AUTH_APPLICATION_IDS).getValue());
  }

  private void parseApplicationIds(String appIdsStr) {
    if(appIdsStr != null) {
      appIdsStr = appIdsStr.replaceAll(" ", "");

      String[] appIdsStrings  = appIdsStr.split(",");

      List<ApplicationId> appIds = new ArrayList<ApplicationId>();

      for(String appId : appIdsStrings) {
        String[] vendorAndAppId = appId.split(":");
        appIds.add(ApplicationId.createByAuthAppId(Long.valueOf(vendorAndAppId[0]), Long.valueOf(vendorAndAppId[1]))); 
      }

      authApplicationIds = appIds;
    }
  }

  public void raUnconfigure() {
    // Clean up!
    this.activities = null;
    this.raContext = null;
    this.eventLookup = null;
    this.raProvider = null;
    this.sleeEndpoint = null;
    this.stack = null;
  }

  // Configuration management methods ------------------------------------

  public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
    // TODO Auto-generated method stub
  }

  public void raConfigurationUpdate(ConfigProperties properties) {
    // this ra does not support config update while entity is active    
  }

  // Interface access methods -------------------------------------------- 

  public Object getResourceAdaptorInterface(String className) {
    // this ra implements a single ra type
    return raProvider;
  }

  /*
   * (non-Javadoc)
   * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
   */
  public Marshaler getMarshaler() {
    return this.marshaler;
  }

  // Event filtering methods ---------------------------------------------

  public void serviceActive(ReceivableService serviceInfo) {
    eventIDFilter.serviceActive(serviceInfo);   
  }

  public void serviceStopping(ReceivableService serviceInfo) {
    eventIDFilter.serviceStopping(serviceInfo);
  }

  public void serviceInactive(ReceivableService serviceInfo) {
    eventIDFilter.serviceInactive(serviceInfo); 
  }

  // Mandatory callback methods ------------------------------------------

  public void queryLiveness(ActivityHandle handle) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter ShClient RA :: queryLiveness :: handle[" + handle + "].");
    }

    DiameterActivityImpl activity = (DiameterActivityImpl) activities.get((DiameterActivityHandle)handle);

    if (activity != null && !activity.isValid()) {
      try {
        sleeEndpoint.endActivity(handle);
      }
      catch (Exception e) {
        tracer.severe("Failure ending non-live activity.", e);
      }
    }
  }

  public Object getActivity(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShClient RA :: getActivity :: handle[" + handle + "].");
    }

    return this.activities.get((DiameterActivityHandle)handle);
  }

  public ActivityHandle getActivityHandle(Object activity) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShClient RA :: getActivityHandle :: activity[" + activity + "].");
    }

    if (!(activity instanceof DiameterActivity)) {
      return null;
    }

    DiameterActivityImpl inActivity = (DiameterActivityImpl) activity;

    return inActivity.getActivityHandle();
  }

  public void administrativeRemove(ActivityHandle handle) {
    // TODO what to do here?
  }

  // Optional callback methods -------------------------------------------

  public void eventProcessingFailed(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags, FailureReason reason) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter ShClient RA :: eventProcessingFailed :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "], reason[" + reason + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)) {
        return;
      }

      processAfterEventDelivery(handle, eventType, event, address, service, flags);
  }

  public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter ShClient RA :: eventProcessingSuccessful :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }

    processAfterEventDelivery(handle, eventType, event, address, service, flags);
  }

  public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShClient RA :: eventUnreferenced :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], service[" + service + "], flags[" + flags + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }

    processAfterEventDelivery(handle, eventType, event, address, service, flags);
  }

  private void processAfterEventDelivery(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);
    if (activity != null) {
      synchronized (activity) {
        if (activity.isTerminateAfterProcessing()) {
          activity.endActivity();
        }
      }
    }
  }

  public void activityEnded(ActivityHandle handle) {
    tracer.info("Diameter ShClient RA :: activityEnded :: handle[" + handle + ".");

    if(this.activities != null) {
      synchronized (this.activities) {
        this.activities.remove((DiameterActivityHandle)handle);
      }
    }
  }

  public void activityUnreferenced(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShClient RA :: activityUnreferenced :: handle[" + handle + "].");
    }

    //this.activityEnded(handle);
    if(handle instanceof DiameterActivityHandle) {
      this.endActivity((DiameterActivityHandle) handle);
    }
  }

  // Event and Activities management -------------------------------------

  public boolean fireEvent(Object event, ActivityHandle handle, FireableEventType eventID, Address address, boolean useFiltering, boolean transacted) {

    if (useFiltering && eventIDFilter.filterEvent(eventID)) {
      if (tracer.isFineEnabled()) {
        tracer.fine("Event " + eventID + " filtered");
      }
    }
    else if (eventID == null) {
      tracer.severe("Event ID for " + eventID + " is unknown, unable to fire.");
    }
    else {
      if (tracer.isFineEnabled()) {
        tracer.fine("Firing event " + event + " on handle " + handle);
      }
      try {
        /* TODO: Support transacted fire of events when in cluster
        if (transacted){
          this.raContext.getSleeEndpoint().fireEventTransacted(handle, eventID, event, address, null, EVENT_FLAGS);
        }
        else */{
          this.raContext.getSleeEndpoint().fireEvent(handle, eventID, event, address, null, EVENT_FLAGS);
        }       
        return true;
      }
      catch (Exception e) {
        tracer.severe("Error firing event.", e);
      }
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#fireEvent(java.lang.String, org.jdiameter.api.Request, org.jdiameter.api.Answer)
   */
  public void fireEvent(String sessionId, Message message) {
    DiameterMessage event = (DiameterMessage) createEvent(message);

    FireableEventType eventId = eventIdCache.getEventId(eventLookup, message);

    this.fireEvent(event, getActivityHandle(sessionId), eventId, null, true, message.isRequest());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endActivity(DiameterActivityHandle handle) {
    sleeEndpoint.endActivity(handle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startActivityRemoveTimer(DiameterActivityHandle handle) {
    this.activities.startActivityRemoveTimer(handle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopActivityRemoveTimer(DiameterActivityHandle handle) {
    this.activities.stopActivityRemoveTimer(handle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(DiameterActivityHandle handle, DiameterActivity activity) {
    activities.update(handle, activity);
  }

  @Override
  public ApplicationId[] getSupportedApplications() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Create Event object from a JDiameter message (request or answer)
   * 
   * @return a DiameterMessage object wrapping the request/answer
   * @throws OperationNotSupportedException
   */
  private DiameterMessage createEvent(Message message) {
    if (message == null) {
      throw new NullPointerException("Message argument cannot be null while creating event.");
    }

    int commandCode = message.getCommandCode();

    if (message.isError()) {
      return new ErrorAnswerImpl(message);
    }

    boolean isRequest = message.isRequest();

    switch (commandCode) {
    case PushNotificationRequestImpl.commandCode: // PNR/PNA
      return isRequest ? new PushNotificationRequestImpl(message) : new PushNotificationAnswerImpl(message);
    case ProfileUpdateRequestImpl.commandCode: // PUR/PUA
      return isRequest ? new ProfileUpdateRequestImpl(message) : new ProfileUpdateAnswerImpl(message);
    case SubscribeNotificationsRequestImpl.commandCode: // SNR/SNA
      return isRequest ? new SubscribeNotificationsRequestImpl(message) : new SubscribeNotificationsAnswerImpl(message);
    case net.java.slee.resource.diameter.sh.events.UserDataRequest.commandCode: // UDR/UDA
      return isRequest ? new UserDataRequestImpl(message) : new UserDataAnswerImpl(message);

    default:
      return new ExtensionDiameterMessageImpl(message);
    }
  }

  // Session Management --------------------------------------------------

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.sh.handlers.ShClientSessionListener#sessionDestroyed(java.lang.String, org.jdiameter.api.sh.ClientShSession)
   */
  public void sessionDestroyed(String sessionId,ClientShSession session) {
    try {
      this.sleeEndpoint.endActivity(getActivityHandle(sessionId));
    }
    catch (Exception e) {
      tracer.severe( "Failed to end activity with handle[" + getActivityHandle(sessionId) );
    }
  }

  /**
   * Method for performing tasks when activity is created, such as informing SLEE about it and storing into internal map.
   * 
   * @param ac the activity that has been created
   */
  private void activityCreated(DiameterActivity ac, boolean suspended) {
    try {
      // Inform SLEE that Activity Started
      DiameterActivityImpl activity = (DiameterActivityImpl) ac;

      if (suspended) {
        sleeEndpoint.startActivitySuspended(activity.getActivityHandle(), activity, MARSHALABLE_ACTIVITY_FLAGS);
      }
      else {
        sleeEndpoint.startActivity(activity.getActivityHandle(), activity, MARSHALABLE_ACTIVITY_FLAGS);
      }

      // Put it into our activites map
      activities.put(activity.getActivityHandle(), activity);

      if(tracer.isInfoEnabled()) {
        tracer.info("Activity started [" + activity.getActivityHandle() + "]");
      }
    }
    catch (Exception e) {
      tracer.severe("Error creating/starting activity.", e);
    }
  }

  // Others --------------------------------------------------------------

  /**
   * Retrieves the RA context
   */
  public ResourceAdaptorContext getRaContext() {
    return raContext;
  }

  // Private Methods -----------------------------------------------------

  /**
   * Initializes the RA Diameter Stack.
   * 
   * @throws Exception
   */
  private synchronized void initStack() throws Exception {
    // Register in the Mux as a Sh app listener.
    this.diameterMux.registerListener(this, (ApplicationId[]) authApplicationIds.toArray(new ApplicationId[authApplicationIds.size()]));

    // Get the stack (should not mess with)
    this.stack = this.diameterMux.getStack();
    this.messageTimeout = stack.getMetaData().getConfiguration().getLongValue(MessageTimeOut.ordinal(), (Long) MessageTimeOut.defValue());

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter ShClient RA :: Successfully initialized stack.");
    }
  }
  private void initActivitiesMgmt()
  {
    final DiameterRAInterface lst = this;
    if (this.ftRAContext.isLocal()) {
      // local mgmt;
      tracer.info(raContext.getEntityName()+" -- running in local mode.");
      this.activities = new LocalDiameterActivityManagement(this.raContext, activityRemoveDelay);
    } else {
      tracer.info(raContext.getEntityName()+" -- running in cluster mode.");
      final org.mobicents.slee.resource.cluster.ReplicatedData<String, DiameterActivity> clusteredData = this.ftRAContext.getReplicateData(true);
      // get special one
      this.activities = new AbstractClusteredDiameterActivityManagement(this.ftRAContext, activityRemoveDelay,this.raContext.getTracer(""), stack, this.raContext.getSleeTransactionManager(), clusteredData) {

        @Override
        protected void performBeforeReturn(DiameterActivityImpl activity) {
          // do all the dirty work;

          try {

            Session session = null;
            if (activity.getClass().equals(DiameterActivityImpl.class)) {
              // check as first. since it requires session
              // recreation.
              //JIC: is this required?

              session = this.diameterStack.getSessionFactory().getNewSession(activity.getSessionId());
              performBeforeReturnOnBase(activity, session);
              return;
            }else if(activity instanceof ShClientActivity)
            {
              ShClientActivityImpl sh = (ShClientActivityImpl) activity;
              ClientShSession appSession = this.diameterStack.getSession(activity.getSessionId(), ClientShSession.class);
              session = appSession.getSessions().get(0);
              performBeforeReturnOnBase(activity, session);
              performBeforeReturnSh(sh,session);
              sh.setSession(appSession);

            }else if(activity instanceof ShClientSubscriptionActivity)
            {
              ShClientSubscriptionActivityImpl sh = (ShClientSubscriptionActivityImpl) activity;
              ClientShSession appSession = this.diameterStack.getSession(activity.getSessionId(), ClientShSession.class);
              session = appSession.getSessions().get(0);
              performBeforeReturnOnBase(activity, session);
              performBeforeReturnSh(sh,session);
              sh.setSession(appSession);

            }else
            {
              throw new IllegalArgumentException("Got wrong activity: "+activity);
            }

          } catch (Exception e) {
            throw new DiameterException(e);
          }
        }

        private void performBeforeReturnSh(ShClientSubscriptionActivityImpl sh,Session session) {
          ShClientMessageFactoryImpl messageFactory = new ShClientMessageFactoryImpl(session, stack);

          // Set the first configured Application-Id as default for message factory
          ApplicationId firstAppId = authApplicationIds.get(0);
          messageFactory.setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

          sh.setClientMessageFactory(messageFactory);
          sh.setClientAvpFactory(shAvpFactory);
        }

        private void performBeforeReturnSh(ShClientActivityImpl sh, Session session) {
          ShClientMessageFactoryImpl messageFactory = new ShClientMessageFactoryImpl(session, stack);

          // Set the first configured Application-Id as default for message factory
          ApplicationId firstAppId = authApplicationIds.get(0);
          messageFactory.setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

          sh.setClientMessageFactory(messageFactory);
          sh.setClientAvpFactory(shAvpFactory);
        }

        private void performBeforeReturnOnBase(DiameterActivityImpl ac,Session session) {
          DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session, stack, new DiameterIdentity[] {});
          ac.setAvpFactory(baseAvpFactory);
          ac.setMessageFactory(msgFactory);
          ac.setCurrentWorkingSession(session);
          ac.setSessionListener(lst);
        }

        @Override
        public DiameterActivity get(DiameterActivityHandle handle) {
          DiameterActivity da = super.get(handle);

          return da;
        }

        @Override
        public void put(DiameterActivityHandle handle, DiameterActivity activity) {
          super.put(handle, activity);
        }

        @Override
        public DiameterActivity remove(DiameterActivityHandle handle) {
          return super.remove(handle);
        }


      };
    }
  }

  protected DiameterActivityHandle getActivityHandle(String sessionId) {
    return new DiameterActivityHandle(sessionId);
  }

  // NetworkReqListener Implementation -----------------------------------

  public Answer processRequest(Request request) {
    try {
      raProvider.createActivity(request);
    }
    catch (Throwable e) {
      tracer.severe(e.getMessage(), e);
    }

    // returning null so we can answer later
    return null;
  }

  public void receivedSuccessMessage(Request req, Answer ans) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShClient RA :: receivedSuccessMessage :: " + "Request[" + req + "], Answer[" + ans + "].");
    }

    try {
      if(tracer.isInfoEnabled()) {
        tracer.info("Received Message Result-Code: " + ans.getResultCode().getUnsigned32());
      }
    }
    catch (AvpDataException ignore) {
      // ignore, this was just for informational purposes...
    }
  }

  public void timeoutExpired(Request req) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter ShClient RA :: timeoutExpired :: " + "Request[" + req + "].");
    }

    try {
      // Message delivery timed out - we have to remove activity
      ((DiameterActivity) getActivity(getActivityHandle(req.getSessionId()))).endActivity();
    }
    catch (Exception e) {
      tracer.severe("Failure processing timeout message.", e);
    }
  }

  // Sh-Client Session Factory -------------------------------------------

  private class ShClientSessionFactory extends  ShSessionFactoryImpl{

    DiameterShClientResourceAdaptor ra = null;

    public ShClientSessionFactory(DiameterShClientResourceAdaptor ra, SessionFactory sf) {
      super(sf);
      this.ra = ra;
    }

    public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args) {
      try {
        //FIXME: add proper handling for SessionId
        if (aClass == ClientShSession.class) {
          ShClientSessionImpl clientSession = (ShClientSessionImpl) super.getNewSession(sessionId, aClass, applicationId, args);

          return clientSession;
        }
        else {
          throw new IllegalArgumentException("Wrong session class!!["+aClass+"]. Supported["+ClientShSession.class+"]");
        }
      }
      catch (Exception e) {
        tracer.severe("Failure to obtain new Accounting Session.", e);
      }

      return null;
    }

    public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
      if(tracer.isFineEnabled()) {
        tracer.fine("Diameter ShClient RA :: doOtherEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
      }

      this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), answer != null ? answer.getMessage() : request.getMessage());
    }

    public void doProfileUpdateAnswerEvent(ClientShSession appSession, org.jdiameter.api.sh.events.ProfileUpdateRequest request, ProfileUpdateAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
      if(tracer.isFineEnabled()) {
        tracer.fine("doProfileUpdateAnswerEvent :: appSession[" + appSession + "], request[" + request + "], answer[" + answer + "]");
      }

      this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), answer.getMessage());
    }

    public void doPushNotificationRequestEvent(ClientShSession appSession, PushNotificationRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
      if(tracer.isFineEnabled()) {
        tracer.fine("doPushNotificationRequestEvent :: appSession[" + appSession + "], request[" + request + "], answer[" + null + "]");
      }

      this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), request.getMessage());
    }

    public void doSubscribeNotificationsAnswerEvent(ClientShSession appSession, SubscribeNotificationsRequest request, org.jdiameter.api.sh.events.SubscribeNotificationsAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
      if(tracer.isFineEnabled()) {
        tracer.fine("doSubscribeNotificationsAnswerEvent :: appSession[" + appSession + "], request[" + request + "], answer[" + answer + "]");
      }

      this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), answer.getMessage());
    }

    public void doUserDataAnswerEvent(ClientShSession appSession, UserDataRequest request, org.jdiameter.api.sh.events.UserDataAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
      if(tracer.isFineEnabled()) {
        tracer.fine("doUserDataAnswerEvent :: appSession[" + appSession + "], request[" + request + "], answer[" + answer + "]");
      }

      this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), answer.getMessage());
    }

    public void stateChanged(Enum oldState, Enum newState) {
      if(tracer.isInfoEnabled()) {
        tracer.info("Diameter Sh ClientSessionFactory :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
      }
    }

    /* (non-Javadoc)
     * @see org.jdiameter.common.impl.app.sh.ShSessionFactoryImpl#stateChanged(org.jdiameter.api.app.AppSession, java.lang.Enum, java.lang.Enum)
     */
    @Override
    public void stateChanged(AppSession source, Enum oldState, Enum newState) {
      if(tracer.isInfoEnabled()) {
        tracer.info("Diameter Sh ClientSessionFactory :: stateChanged :: source["+ source +"] :: oldState[" + oldState + "], newState[" + newState + "]");
      }
      //		DiameterActivityHandle dah = getActivityHandle(source.getSessionId());
      //		Object activity = getActivity(dah);
      //		if (activity != null) {
      //			if(source instanceof ClientShSession )
      //			{
      //				try{
      //					//damn, no common, do something unexpected
      //					StateChangeListener<AppSession> scl = (StateChangeListener<AppSession>) activity;
      //					scl.stateChanged(source, oldState, newState);
      //				}catch(Exception e)
      //				{
      //					tracer.warning("Failed to deliver state, for: " + dah + " on stateChanged( " + source + ", " + oldState + ", " + newState + " )", e);
      //				}
      //				
      //			}
      //		} else {
      //			tracer.warning("No activity for: " + dah + " on stateChanged( " + source + ", " + oldState + ", " + newState + " )");
      //		}
      //		
    }

    public AppAnswerEvent createProfileUpdateAnswer(Answer answer) {
      return new AppAnswerEventImpl(answer);
    }

    public AppRequestEvent createProfileUpdateRequest(Request request) {
      return new AppRequestEventImpl(request);
    }

    public AppAnswerEvent createPushNotificationAnswer(Answer answer) {
      return new AppAnswerEventImpl(answer);
    }

    public AppRequestEvent createPushNotificationRequest(Request request) {
      return new AppRequestEventImpl(request);
    }

    public AppAnswerEvent createSubscribeNotificationsAnswer(Answer answer) {
      return new AppAnswerEventImpl(answer);
    }

    public AppRequestEvent createSubscribeNotificationsRequest(Request request) {
      return new AppRequestEventImpl(request);
    }

    public AppAnswerEvent createUserDataAnswer(Answer answer) {
      return new AppAnswerEventImpl(answer);
    }

    public AppRequestEvent createUserDataRequest(Request request) {
      return new AppRequestEventImpl(request);
    }

    public long getApplicationId() {
      return authApplicationIds.get(0).getAuthAppId();
    }

    public long getMessageTimeout() {
      return this.ra.messageTimeout;
    }
  }

  // Provider Implementation ---------------------------------------------

  private class ShClientProviderImpl implements ShClientProvider
  {
    protected DiameterShClientResourceAdaptor ra = null;
    protected Validator validator = new ValidatorImpl();

    public ShClientProviderImpl(DiameterShClientResourceAdaptor ra) {
      super();
      this.ra = ra;
    }

    /**
     * This method is for internal use only, it creates activities for requests that 
     * do not fall in certain app range or no activities were found <br>
     * It should distinguish between initial requests, requests with different domains etc. 
     * Answer for creating XXXServerSession or basic diameter activity lies in this method
     * 
     * @param message
     * @return
     */
    DiameterActivity createActivity(Message message) throws CreateActivityException {
      DiameterActivity activity = activities.get(getActivityHandle(message.getSessionId()));

      if (activity == null) {
        //FIXME: baranowb: here we can receive only (valid) PNR, other message are errors?
        if(message.getCommandCode() != PushNotificationRequestImpl.commandCode) {
          throw new CreateActivityException("Cant create activity for unexpected message:\r\n" + message);
        }

        return (ShClientSubscriptionActivityImpl) this.createShClientSubscriptionActivity(new PushNotificationRequestImpl( message));
      }

      return activity;
    }

    private ShClientSubscriptionActivity createShClientSubscriptionActivity(net.java.slee.resource.diameter.sh.events.PushNotificationRequest pushNotificationRequest) {
      ClientShSession session = null;

      try {
        session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, null, ClientShSession.class, new Object[]{((DiameterMessageImpl)pushNotificationRequest).getGenericData()});

        if (session == null) {
          tracer.severe("Failure creating Sh-Client Subscription Session (null).");
          return null;
        }
      }
      catch (Exception e) {
        tracer.severe("Failure creating Sh-Client Subscription Session.", e);
        return null;
      }

      ShClientSubscriptionActivityImpl activity = new ShClientSubscriptionActivityImpl(getSessionShMessageFactory(session), shAvpFactory, session, null, null);
      activity.fetchSessionData(pushNotificationRequest,true);
      activity.setSessionListener(ra);
      activityCreated(activity, false);

      //FIXME: baranowb: this is akward, jdiam has weird api
      //This is trick to trigger fire and state machine
      ((ShClientSessionImpl)session).processRequest((Request) ((DiameterMessageImpl)pushNotificationRequest).getGenericData());

      return activity;
    }

    public ShClientActivity createShClientActivity() throws CreateActivityException {
      ClientShSession session = null;

      try {
        session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, null, ClientShSession.class, EMPTY_OBJECT_ARRAY);

        if (session == null) {
          tracer.severe("Failure creating Sh-Client Session (null).");
          return null;
        }
      }
      catch (Exception e) {
        tracer.severe("Failure creating Sh-Client Session.", e);
        return null;
      }

      ShClientActivityImpl activity = new ShClientActivityImpl(getSessionShMessageFactory(session), shAvpFactory, session, null, null);

      activity.setSessionListener(ra);
      activityCreated(activity, false /*true*/);

      return activity;
    }

    public ShClientMessageFactory getClientMessageFactory() {
      return shClientMessageFactory;
    }

    public net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer profileUpdateRequest(ProfileUpdateRequest message) throws IOException {
      // This is sync, we dont care about activities or FSM, someone else should care...
      if (message == null) {
        throw new IOException("Cant send null message");
      }

      try {

        String sessionID = message.getSessionId();
        if (sessionID == null) {
          throw new IllegalArgumentException("Session Id must not be null.");
        }
        Session session = stack.getSessionFactory().getNewSession(sessionID);
        Future<Message> f = session.send(((DiameterMessageImpl) message).getGenericData());
        return new ProfileUpdateAnswerImpl(f.get());
      }
      catch (AvpNotAllowedException e) {
        throw e;
      }
      catch (Exception e) {
        throw new IOException("Failed to send due to: " + e.getLocalizedMessage());
      }

    }

    public SubscribeNotificationsAnswer subscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest message) throws IOException {
      // This is sync, we dont care about activities or FSM, someone else should care...
      if (message == null) {
        throw new IOException("Cant send null message");
      }

      try {

        String sessionID = message.getSessionId();
        if (sessionID == null) {
          throw new IllegalArgumentException("Session Id must not be null.");
        }
        Session session = stack.getSessionFactory().getNewSession(sessionID);
        Future<Message> f = session.send(((DiameterMessageImpl) message).getGenericData());
        return new SubscribeNotificationsAnswerImpl(f.get());
      }
      catch (AvpNotAllowedException e) {
        throw e;
      }
      catch (Exception e) {
        throw new IOException("Failed to send due to: " + e);
      }
    }

    public UserDataAnswer userDataRequest(net.java.slee.resource.diameter.sh.events.UserDataRequest message) throws IOException {
      // This is sync, we dont care about activities or FSM, someone else should care...
      if (message == null) {
        throw new IOException("Cant send null message");
      }

      try {

        String sessionID = message.getSessionId();
        if (sessionID == null) {
          throw new IllegalArgumentException("Session Id must not be null.");
        }
        Session session = stack.getSessionFactory().getNewSession(sessionID);
        Future<Message> f = session.send(((DiameterMessageImpl) message).getGenericData());

        return new UserDataAnswerImpl(f.get());
      }
      catch (AvpNotAllowedException e) {
        throw e;
      }
      catch (Exception e) {
        throw new IOException("Failed to send due to: " + e.getLocalizedMessage());
      }
    }

    public ShClientSubscriptionActivity createShClientSubscriptionActivity() throws CreateActivityException {
      ClientShSession session = null;

      try {
        session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, null, ClientShSession.class, EMPTY_OBJECT_ARRAY);

        if (session == null) {
          tracer.severe("Failure creating Sh-Client Subscription Session (null).");
          return null;
        }
      }
      catch (Exception e) {
        tracer.severe("Failure creating Sh-Client Subscription Session.", e);
        return null;
      }

      ShClientSubscriptionActivityImpl activity = new ShClientSubscriptionActivityImpl(getSessionShMessageFactory(session), shAvpFactory, session, null, null);
      activity.setSessionListener(ra);
      activityCreated(activity, false /*true*/);

      return activity;
    }

    public DiameterShAvpFactory getClientAvpFactory() {
      return shAvpFactory;
    }

    private ShClientMessageFactory getSessionShMessageFactory(AppSession session) {
      return new ShClientMessageFactoryImpl(session.getSessions().get(0), stack);
    }

    public DiameterIdentity[] getConnectedPeers() {
      if (ra.stack != null) {
        try {
          // Get the list of peers from the stack
          List<Peer> peers = stack.unwrap(PeerTable.class).getPeerTable();

          DiameterIdentity[] result = new DiameterIdentity[peers.size()];

          int i = 0;

          // Get each peer from the list and make a DiameterIdentity
          for (Peer peer : peers) {
            DiameterIdentity identity = new DiameterIdentity(peer.getUri().toString());
            result[i++] = identity;
          }

          return result;
        }
        catch (Exception e) {
          tracer.severe("Failure getting peer list.", e);
        }
      }

      return null;
    }

    public int getPeerCount() {
      return getConnectedPeers().length;
    }

    /* (non-Javadoc)
     * @see net.java.slee.resource.diameter.sh.client.ShClientProvider#getValidator()
     */
    @Override
    public Validator getValidator() {
      return this.validator;
    }
  }

}
