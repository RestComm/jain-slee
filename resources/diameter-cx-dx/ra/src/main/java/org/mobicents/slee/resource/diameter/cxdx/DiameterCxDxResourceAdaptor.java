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

package org.mobicents.slee.resource.diameter.cxdx;

import static org.jdiameter.client.impl.helpers.Parameters.MessageTimeOut;

import java.util.ArrayList;
import java.util.List;

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
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cxdx.CxDxAVPFactory;
import net.java.slee.resource.diameter.cxdx.CxDxClientSessionActivity;
import net.java.slee.resource.diameter.cxdx.CxDxMessageFactory;
import net.java.slee.resource.diameter.cxdx.CxDxProvider;
import net.java.slee.resource.diameter.cxdx.CxDxServerSessionActivity;
import net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest;
import net.java.slee.resource.diameter.cxdx.events.MultimediaAuthenticationRequest;
import net.java.slee.resource.diameter.cxdx.events.PushProfileRequest;
import net.java.slee.resource.diameter.cxdx.events.RegistrationTerminationRequest;
import net.java.slee.resource.diameter.cxdx.events.ServerAssignmentRequest;
import net.java.slee.resource.diameter.cxdx.events.UserAuthorizationRequest;
import net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes;

import org.jboss.mx.util.MBeanServerLocator;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Peer;
import org.jdiameter.api.PeerTable;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.cxdx.ClientCxDxSession;
import org.jdiameter.api.cxdx.ServerCxDxSession;
import org.jdiameter.api.sh.ServerShSession;
import org.jdiameter.client.api.ISessionFactory;
import org.mobicents.diameter.stack.DiameterListener;
import org.mobicents.diameter.stack.DiameterStackMultiplexerMBean;
import org.mobicents.slee.resource.diameter.DiameterActivityManagement;
import org.mobicents.slee.resource.diameter.LocalDiameterActivityManagement;
import org.mobicents.slee.resource.diameter.ValidatorImpl;
import org.mobicents.slee.resource.diameter.base.DiameterActivityHandle;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterBaseMarshaler;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.base.EventIDFilter;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.handlers.AccountingSessionFactory;
import org.mobicents.slee.resource.diameter.base.handlers.AuthorizationSessionFactory;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;
import org.mobicents.slee.resource.diameter.cxdx.events.LocationInfoAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.LocationInfoRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.MultimediaAuthenticationAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.MultimediaAuthenticationRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.PushProfileAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.PushProfileRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.RegistrationTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.RegistrationTerminationRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.ServerAssignmentAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.ServerAssignmentRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.UserAuthorizationAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.UserAuthorizationRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.handlers.CxDxSessionFactory;

/**
 * Diameter Cx/Dx Resource Adaptor
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DiameterCxDxResourceAdaptor implements ResourceAdaptor, DiameterListener, DiameterRAInterface/*, FaultTolerantResourceAdaptor<String, DiameterActivity>*/  {

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

  //  /**
  //   * FT/HA version of RA context.
  //   */
  //  private FaultTolerantResourceAdaptorContext<String, DiameterActivity> ftRAContext;

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

  private DiameterBaseMarshaler marshaler/*= new DiameterBaseMarshaler()*/;
  // Diameter Specific Properties ----------------------------------------

  private Stack stack;

  private long messageTimeout = 5000;
  private long activityRemoveDelay = 30000;

  private ObjectName diameterMultiplexerObjectName = null;
  private DiameterStackMultiplexerMBean diameterMux = null;

  // Base Factories
  private DiameterAvpFactory baseAvpFactory = null;
  private DiameterMessageFactoryImpl baseMessageFactory;
  
  private SessionFactory sessionFactory = null;

  // Cx/Dx Specific Factories
  private CxDxAVPFactory cxdxAvpFactory;
  private CxDxMessageFactory cxdxMessageFactory;

  private CxDxSessionFactory cxdxSessionFactory = null;

  /**
   * the EventLookupFacility is used to look up the event id of incoming
   * events
   */
  private transient EventLookupFacility eventLookup = null;

  /**
   * The list of activities stored in this resource adaptor. If this resource
   * adaptor were a distributed and highly available solution, this storage
   * were one of the candidates for distribution.
   */
  //private transient ConcurrentHashMap<ActivityHandle, DiameterActivity> activities = null;
  private transient DiameterActivityManagement activities = null;

  /**
   * A link to the DiameterProvider which then will be exposed to Sbbs
   */
  private transient CxDxProviderImpl raProvider = null;

  protected transient AuthorizationSessionFactory authSessionFactory = null;
  protected transient AccountingSessionFactory accSessionFactory = null;
  protected transient SessionFactory proxySessionFactory = null;

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

  public DiameterCxDxResourceAdaptor() {
    // TODO: Initialize any default values.
  }

  // Lifecycle methods ---------------------------------------------------

  public void setResourceAdaptorContext(ResourceAdaptorContext context) {
    this.raContext = context;

    this.tracer = context.getTracer("DiameterCxDxResourceAdaptor");

    this.sleeEndpoint = context.getSleeEndpoint();
    this.eventLookup = context.getEventLookupFacility();
    this.raProvider = new CxDxProviderImpl(this);

  }

  public void unsetResourceAdaptorContext() {
    this.raContext = null;

    this.tracer = null;

    this.sleeEndpoint = null;
    this.eventLookup = null;
  }

  //// FT Lifecycle methods ------------------------------------------------//  
  ///*
  // * (non-Javadoc)
  // * 
  // * @seeorg.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#
  // * setFaultTolerantResourceAdaptorContext
  // * (org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext)
  // */
  //public void setFaultTolerantResourceAdaptorContext(FaultTolerantResourceAdaptorContext<String, DiameterActivity> ctx) {
  //  this.ftRAContext = ctx;
  //}
  //
  ///*
  // * (non-Javadoc)
  // * 
  // * @seeorg.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#
  // * unsetFaultTolerantResourceAdaptorContext()
  // */
  //public void unsetFaultTolerantResourceAdaptorContext() {
  //  this.ftRAContext = null;
  //  //clear this.activities ??
  //}
  //  
  //// FT methods ----------------------------------------------------------
  //
  ///*
  // * (non-Javadoc)
  // * 
  // * @see
  // * org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#dataRemoved
  // * (java.io.Serializable)
  // */
  //public void dataRemoved(String arg0) {
  //  this.activities.remove(getActivityHandle(arg0));
  //}
  //
  ///* (non-Javadoc)
  // * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#failOver(java.io.Serializable)
  // */
  //public void failOver(String arg0) {
  //  throw new UnsupportedOperationException();
  //}

  public void raActive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Cx/Dx RA :: raActive.");
    }

    try {
      if(tracer.isInfoEnabled()) {
        tracer.info("Activating Diameter Cx/Dx RA Entity");
      }

      this.diameterMultiplexerObjectName = new ObjectName("diameter.mobicents:service=DiameterStackMultiplexer");

      Object object = MBeanServerLocator.locateJBoss().invoke(this.diameterMultiplexerObjectName, "getMultiplexerMBean", new Object[]{}, new String[]{});

      if(object instanceof DiameterStackMultiplexerMBean) {
        this.diameterMux = (DiameterStackMultiplexerMBean) object;
      }

      //this.activities = new ConcurrentHashMap<ActivityHandle, DiameterActivity>();

      // Initialize the protocol stack
      initStack();

      // Initialize activities mgmt
      initActivitiesMgmt();

      // Initialize factories
      this.baseAvpFactory = new DiameterAvpFactoryImpl();
      this.baseMessageFactory = new DiameterMessageFactoryImpl(stack);
	   
      this.cxdxAvpFactory = new CxDxAVPFactoryImpl(baseAvpFactory);
      this.cxdxMessageFactory = new CxDxMessageFactoryImpl(baseMessageFactory,stack);

      // Set the first configured Application-Id as default for message factory
      ApplicationId firstAppId = authApplicationIds.get(0);
      ((CxDxMessageFactoryImpl)this.cxdxMessageFactory).setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());
 
      // Setup session factories
      this.sessionFactory = this.stack.getSessionFactory();
      this.cxdxSessionFactory = new CxDxSessionFactory(this,messageTimeout,sessionFactory);

      ((ISessionFactory) sessionFactory).registerAppFacory(ServerCxDxSession.class, cxdxSessionFactory);
      ((ISessionFactory) sessionFactory).registerAppFacory(ClientCxDxSession.class, cxdxSessionFactory);
    }
    catch (Exception e) {
      tracer.severe("Error Activating Diameter Cx/Dx RA Entity", e);
    }
  }

  public void raStopping() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Cx/Dx RA :: raStopping.");
    }

    try {
      diameterMux.unregisterListener(this);
    }
    catch (Exception e) {
      tracer.severe("Failed to unregister Cx/Dx RA from Diameter Mux.", e);
    }

    //synchronized (this.activities) {
    //  for (ActivityHandle activityHandle : activities.keySet()) {
    //    try {
    //      if(tracer.isInfoEnabled()) {
    //        tracer.info("Ending activity [" + activityHandle + "]");
    //      }
    //
    //      activities.get(activityHandle).endActivity();
    //    }
    //    catch (Exception e) {
    //      tracer.severe("Error Deactivating Activity", e);
    //    }
    //  }
    //}

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Cx/Dx RA :: raStopping completed.");
    }
  }

  public void raInactive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Cx/Dx RA :: raInactive.");
    }

    //synchronized (this.activities) {
    //  activities.clear();
    //}
    activities = null;

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Cx/Dx RA :: raInactive completed.");
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
      tracer.info("Diameter Cx/Dx RA :: queryLiveness :: handle[" + handle + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)){
      return;
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
      tracer.fine("Diameter Cx/Dx RA :: getActivity :: handle[" + handle + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)){
      return null;
    }
    return this.activities.get((DiameterActivityHandle)handle);
  }

  public ActivityHandle getActivityHandle(Object activity) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Cx/Dx RA :: getActivityHandle :: activity[" + activity + "].");
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
      tracer.info("Diameter Cx/Dx RA :: eventProcessingFailed :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "], reason[" + reason + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }

    processAfterEventDelivery(handle, eventType, event, address, service, flags);
  }

  public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Cx/Dx RA :: eventProcessingSuccessful :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }

    processAfterEventDelivery(handle, eventType, event, address, service, flags);
  }

  public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Cx/Dx RA :: eventUnreferenced :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], service[" + service + "], flags[" + flags + "].");
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
    tracer.info("Diameter Cx/Dx RA :: activityEnded :: handle[" + handle + ".");

    if(this.activities != null) {
      synchronized (this.activities) {
        this.activities.remove((DiameterActivityHandle)handle);
      }
    }
  }

  public void startActivityRemoveTimer(DiameterActivityHandle handle) {
    this.activities.startActivityRemoveTimer(handle);
  }

  public void stopActivityRemoveTimer(DiameterActivityHandle handle) {
    this.activities.stopActivityRemoveTimer(handle);
  }
	
  public void activityUnreferenced(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Cx/Dx RA :: activityUnreferenced :: handle[" + handle + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }
    this.activityEnded(handle);
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

  @Override
  public void endActivity(DiameterActivityHandle handle) {
    sleeEndpoint.endActivity(handle);
  }

  @Override
  public void update(DiameterActivityHandle handle, DiameterActivity activity) {
    activities.update(handle, activity);
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
    case UserAuthorizationRequest.COMMAND_CODE: // UAR/UAA
      return isRequest ? new UserAuthorizationRequestImpl(message) : new UserAuthorizationAnswerImpl(message);
    case ServerAssignmentRequest.COMMAND_CODE: // SAR/SAA
      return isRequest ? new ServerAssignmentRequestImpl(message) : new ServerAssignmentAnswerImpl(message);
    case LocationInfoRequest.COMMAND_CODE: // LIR/LIA
      return isRequest ? new LocationInfoRequestImpl(message) : new LocationInfoAnswerImpl(message);
    case MultimediaAuthenticationRequest.COMMAND_CODE: // MAR/MAA
      return isRequest ? new MultimediaAuthenticationRequestImpl(message) : new MultimediaAuthenticationAnswerImpl(message);
    case RegistrationTerminationRequest.COMMAND_CODE: // RTR/RTA
      return isRequest ? new RegistrationTerminationRequestImpl(message) : new RegistrationTerminationAnswerImpl(message);
    case PushProfileRequest.COMMAND_CODE: // MAR/MAA
      return isRequest ? new PushProfileRequestImpl(message) : new PushProfileAnswerImpl(message);

    default:
      return new ExtensionDiameterMessageImpl(message);
    }
  }

  // Session Management --------------------------------------------------

  /**
   * Method for performing tasks when activity is created, such as informing SLEE about it and storing into internal map.
   * 
   * @param ac the activity that has been created
   */
  private void addActivity(DiameterActivity ac, boolean suspended) {
    try {
      // Inform SLEE that Activity Started
      DiameterActivityImpl activity = (DiameterActivityImpl) ac;

      if (suspended) {
        sleeEndpoint.startActivitySuspended(activity.getActivityHandle(), activity, DEFAULT_ACTIVITY_FLAGS);
      }
      else {
        sleeEndpoint.startActivity(activity.getActivityHandle(), activity, DEFAULT_ACTIVITY_FLAGS);
      }

      // Set the listener
      activity.setSessionListener(this);

      // Put it into our activites map
      activities.put(activity.getActivityHandle(), activity);

      if(tracer.isInfoEnabled()) {
        tracer.info("Activity started [" + activity.getActivityHandle() + "]");
      }
    }
    catch (Exception e) {
      tracer.severe("Error creating activity", e);

      throw new RuntimeException("Error creating activity", e);
    }
  }

  // Private Methods -----------------------------------------------------

  /**
   * Initializes the RA Diameter Stack.
   * 
   * @throws Exception
   */
  private synchronized void initStack() throws Exception {
    // Register in the Mux as an app listener.
    this.diameterMux.registerListener(this, (ApplicationId[]) authApplicationIds.toArray(new ApplicationId[authApplicationIds.size()]));

    // Get the stack (should not mess with)
    this.stack = this.diameterMux.getStack();
    this.messageTimeout = stack.getMetaData().getConfiguration().getLongValue(MessageTimeOut.ordinal(), (Long) MessageTimeOut.defValue());

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Cx/Dx RA :: Successfully initialized stack.");
    }
  }
  private void initActivitiesMgmt() {
    this.activities = new LocalDiameterActivityManagement(this.raContext, this.activityRemoveDelay);

    /* Not Used. No HA for Cx/Dx is needed
    if (this.ftRAContext.isLocal()) {
      // local mgmt;
      if(tracer.isInfoEnabled()) {
        tracer.info(raContext.getEntityName() + " -- running in LOCAL mode.");
      }
      this.activities = new LocalDiameterActivityManagement();
    }
    else {
      if(tracer.isInfoEnabled()) {
        tracer.info(raContext.getEntityName() + " -- running in CLUSTER mode.");
      }
      final org.mobicents.slee.resource.cluster.ReplicatedData<String, DiameterActivity> clusteredData = this.ftRAContext.getReplicateData(true);
      // get special one
      this.activities = new AbstractClusteredDiameterActivityManagement(
          this.raContext.getTracer(""), stack, this.raContext.getSleeTransactionManager(), clusteredData) {

        @Override
        protected void performBeforeReturn(DiameterActivityImpl activity) {
          // do all the dirty work;
          try {
            Session session = null;
            if (activity.getClass().equals(DiameterActivityImpl.class)) {
              // check as first. since it requires session recreation.
              // JIC
              session = this.diameterStack.getSessionFactory().getNewSession(activity.getSessionId());
              performBeforeReturnOnBase(activity, session);
              return;
            }
            else if (activity instanceof CxDxClientSessionActivity) {
              CxDxClientSessionImpl acc = (CxDxClientSessionImpl) activity;
              ClientCxDxSession appSession = this.diameterStack.getSession(activity.getSessionId(), ClientCxDxSession.class);
              session = appSession.getSessions().get(0);
              performBeforeReturnOnBase(activity, session);
              performBeforeReturnCxDx(acc, session);
              acc.setSession(appSession);
            }
            else if (activity instanceof CxDxServerSessionActivity) {
              CxDxServerSessionImpl acc = (CxDxServerSessionImpl) activity;
              ServerCxDxSession appSession = this.diameterStack.getSession(activity.getSessionId(), ServerCxDxSession.class);
              session = appSession.getSessions().get(0);
              performBeforeReturnOnBase(activity, session);
              performBeforeReturnCxDx(acc, session);
              acc.setSession(appSession);
            }
            else {
              throw new IllegalArgumentException("Unknown activity type: " + activity);
            }
          }
          catch (Exception e) {
            throw new DiameterException(e);
          }
        }

        private void performBeforeReturnCxDx(CxDxSessionImpl acc, Session session) {
          acc.setCxDxMessageFactory(new CxDxMessageFactoryImpl(session, stack));
          acc.setCxDxAvpFactory(cxdxAvpFactory);
        }

        private void performBeforeReturnOnBase(DiameterActivityImpl ac, Session session) {
          DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session, stack, new DiameterIdentity[] {});
          ac.setAvpFactory(baseAvpFactory);
          ac.setMessageFactory(msgFactory);
          ac.setCurrentWorkingSession(session);
          ac.setSessionListener(lst);
        }

        @Override
        public DiameterActivity get(DiameterActivityHandle handle) {
          return super.get(handle);
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
    }*/
  }

  /**
   * Create the Diameter Activity Handle for an given session id
   * 
   * @param sessionId the session identifier to create the activity handle from
   * @return a DiameterActivityHandle for the provided sessionId
   */
  protected DiameterActivityHandle getActivityHandle(String sessionId) {
    return new DiameterActivityHandle(sessionId);
  }

  // NetworkReqListener Implementation -----------------------------------

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.NetworkReqListener#processRequest(org.jdiameter.api.Request)
   */
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

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.EventListener#receivedSuccessMessage(org.jdiameter.api.Message, org.jdiameter.api.Message)
   */
  public void receivedSuccessMessage(Request request, Answer answer) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Cx/Dx RA :: receivedSuccessMessage :: " + "Request[" + request + "], Answer[" + answer + "].");
    }

    try {
      if(tracer.isInfoEnabled()) {
        tracer.info("Received Message Result-Code: " + answer.getResultCode().getUnsigned32());
      }
    }
    catch (AvpDataException ignore) {
      // ignore, this was just for informational purposes...
    }
  }

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.EventListener#timeoutExpired(org.jdiameter.api.Message)
   */
  public void timeoutExpired(Request request) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Cx/Dx RA :: timeoutExpired :: " + "Request[" + request + "].");
    }

    try {
      // Message delivery timed out - we have to remove activity
      ((DiameterActivity) getActivity(getActivityHandle(request.getSessionId()))).endActivity();
    }
    catch (Exception e) {
      tracer.severe("Failure processing timeout message.", e);
    }
  }

  // CxDx Session Creation Listener --------------------------------------

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.cxdx.handlers.CxDxSessionCreationListener#sessionCreated(org.jdiameter.api.cxdx.ServerCxDxSession)
   */
  public void sessionCreated(ServerCxDxSession session) {
    DiameterMessageFactoryImpl baseMsgFactory = new DiameterMessageFactoryImpl(session.getSessions().get(0), stack);
    CxDxMessageFactoryImpl sessionMsgFactory = new CxDxMessageFactoryImpl(baseMsgFactory,session.getSessions().get(0), stack, new DiameterIdentity[]{});

    // Set the first configured Application-Id as default for message factory
    ApplicationId firstAppId = authApplicationIds.get(0);
    sessionMsgFactory.setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

    CxDxServerSessionImpl serverActivity = new CxDxServerSessionImpl(sessionMsgFactory, cxdxAvpFactory, session, this, null, null, stack);
    //session.addStateChangeNotification(serverActivity);
    //addActivity(serverActivity);
    serverActivity.setSessionListener(this);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.cxdx.handlers.CxDxSessionCreationListener#sessionCreated(org.jdiameter.api.cxdx.ClientCxDxSession)
   */
  public void sessionCreated(ClientCxDxSession session) {
    DiameterMessageFactoryImpl baseMsgFactory = new DiameterMessageFactoryImpl(session.getSessions().get(0), stack);
    CxDxMessageFactoryImpl sessionMsgFactory = new CxDxMessageFactoryImpl(baseMsgFactory,session.getSessions().get(0), stack, new DiameterIdentity[]{});

    // Set the first configured Application-Id as default for message factory
    ApplicationId firstAppId = authApplicationIds.get(0);
    sessionMsgFactory.setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

    CxDxClientSessionImpl clientActivity = new CxDxClientSessionImpl(sessionMsgFactory, cxdxAvpFactory, session, this, null, null, sleeEndpoint);
    //session.addStateChangeNotification(clientActivity);
    //addActivity(clientActivity);
    clientActivity.setSessionListener(this);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.cxdx.handlers.CxDxSessionCreationListener#sessionCreated(org.jdiameter.api.Session)
   */
  public void sessionCreated(Session session) {
    DiameterMessageFactoryImpl sessionMsgFactory = new DiameterMessageFactoryImpl(session, stack, null, null);
    DiameterActivityImpl activity = new DiameterActivityImpl(sessionMsgFactory, baseAvpFactory, session, this, null, null);

    // TODO: Do we need to manage session?
    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity, false);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.cxdx.handlers.CxDxSessionCreationListener#getSupportedApplications()
   */
  public ApplicationId[] getSupportedApplications() {
    return (ApplicationId[]) authApplicationIds.toArray();
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.cxdx.handlers.CxDxSessionCreationListener#stateChanged(org.jdiameter.api.app.AppSession, java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    DiameterActivityHandle dah = getActivityHandle(source.getSessionId());
    Object activity = getActivity(dah);
    if (activity != null) {
      if(source instanceof ServerShSession) {
        try{
          //damn, no common, do something unexpected
          StateChangeListener<AppSession> scl = (StateChangeListener<AppSession>) activity;
          scl.stateChanged(source, oldState, newState);
        }
        catch(Exception e) {
          tracer.warning("Failed to deliver state, for: " + dah + " on stateChanged( " + source + ", " + oldState + ", " + newState + " )", e);
        }
      }
    }
    else {
      tracer.warning("No activity for: " + dah + " on stateChanged( " + source + ", " + oldState + ", " + newState + " )");
    }
  }

  // Provider Implementation ---------------------------------------------

  private class CxDxProviderImpl implements CxDxProvider {

    protected DiameterCxDxResourceAdaptor ra;
    protected Validator validator = new ValidatorImpl();

    /**
     * Constructor.
     * 
     * @param cxdxResourceAdaptor The resource adaptor for this Provider.
     */
    public CxDxProviderImpl(DiameterCxDxResourceAdaptor cxdxResourceAdaptor) {
      this.ra = cxdxResourceAdaptor;
    }

    private DiameterActivity createActivity(Message message) throws CreateActivityException {
      DiameterActivity activity = activities.get(getActivityHandle(message.getSessionId()));
      if(activity == null) {
        if (message.isRequest()) {
          if(message.getCommandCode() == PushProfileRequest.COMMAND_CODE || message.getCommandCode() == RegistrationTerminationRequest.COMMAND_CODE) {
            return createCxDxClientSessionActivity((Request) message, false);
          }
          else {
            return createCxDxServerSessionActivity((Request) message);
          }
        }
        else {
          throw new IllegalStateException("Got answer, there should already be activity.");
        }
      }
      
      return activity;
    }

    private DiameterActivity createCxDxServerSessionActivity(Request request) throws CreateActivityException {
      ServerCxDxSession session = null;

      try {
        String sessionId = request == null ? null: request.getSessionId();
        session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(sessionId, ApplicationId.createByAuthAppId(DiameterCxDxAvpCodes.CXDX_VENDOR_ID, DiameterCxDxAvpCodes.CXDX_AUTH_APP_ID), ServerCxDxSession.class, request);

        if (session == null) {
          throw new CreateActivityException("Got NULL Session while creating Server Accounting Activity");
        }
      }
      catch (InternalException e) {
        throw new CreateActivityException("Internal exception while creating Server Accounting Activity", e);
      }
      catch (IllegalDiameterStateException e) {
        throw new CreateActivityException("Illegal Diameter State exception while creating Server Accounting Activity", e);
      }

      CxDxServerSessionImpl activity = new CxDxServerSessionImpl(ra.cxdxMessageFactory, ra.cxdxAvpFactory, session, (EventListener<Request, Answer>) session, (DiameterIdentity)null, (DiameterIdentity)null,stack);
      addActivity(activity, false);

      if(request != null) {
        if(request.getCommandCode() == LocationInfoRequest.COMMAND_CODE) {
          activity.fetchSessionData(new LocationInfoRequestImpl(request));
        }
        else if (request.getCommandCode() == MultimediaAuthenticationRequest.COMMAND_CODE) {
          activity.fetchSessionData(new MultimediaAuthenticationRequestImpl(request));
        }
        else if (request.getCommandCode() == ServerAssignmentRequest.COMMAND_CODE) {
          activity.fetchSessionData(new ServerAssignmentRequestImpl(request));
        }
        else if (request.getCommandCode() == UserAuthorizationRequest.COMMAND_CODE) {
          activity.fetchSessionData(new UserAuthorizationRequestImpl(request));
        }

        ((org.jdiameter.server.impl.app.cxdx.CxDxServerSessionImpl)session).processRequest(request);
      }

      return activity;
    }

    // Actual Provider Methods 

    public CxDxClientSessionActivity createCxDxClientSessionActivity() throws CreateActivityException {
      return createCxDxClientSessionActivity(null, false /*true*/);
    }

    public CxDxServerSessionActivity createCxDxServerSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException {
      try {
        ServerCxDxSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, ApplicationId.createByAuthAppId(DiameterCxDxAvpCodes.CXDX_VENDOR_ID, DiameterCxDxAvpCodes.CXDX_AUTH_APP_ID), ServerCxDxSession.class);
        CxDxServerSessionImpl activity = new CxDxServerSessionImpl(ra.cxdxMessageFactory, ra.cxdxAvpFactory, session, (EventListener<Request, Answer>) session, destinationHost, destinationRealm, stack);
        addActivity(activity, false);
        return activity;
      }
      catch (Exception e) {
        throw new CreateActivityException("Internal exception while creating Client Accounting Activity", e);
      }
    }

    /* (non-Javadoc)
     * @see net.java.slee.resource.diameter.cxdx.CxDxProvider#createCxDxClientSessionActivity(net.java.slee.resource.diameter.base.events.avp.DiameterIdentity, net.java.slee.resource.diameter.base.events.avp.DiameterIdentity)
     */
    public CxDxClientSessionActivity createCxDxClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException {
      try {

    	ClientCxDxSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, ApplicationId.createByAuthAppId(DiameterCxDxAvpCodes.CXDX_VENDOR_ID, DiameterCxDxAvpCodes.CXDX_AUTH_APP_ID), ClientCxDxSession.class);
      CxDxMessageFactoryImpl sessionMsgFactory = new CxDxMessageFactoryImpl(this.ra.baseMessageFactory,session.getSessions().get(0), stack, new DiameterIdentity[]{});

      // Set the first configured Application-Id as default for message factory
      ApplicationId firstAppId = authApplicationIds.get(0);
      sessionMsgFactory.setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

        CxDxClientSessionImpl activity = new CxDxClientSessionImpl(sessionMsgFactory, ra.cxdxAvpFactory, session, (EventListener<Request, Answer>) session, destinationHost, destinationRealm, ra.sleeEndpoint);
        addActivity(activity, false /*true*/);
        return activity;
      }
      catch (Exception e) {
        throw new CreateActivityException("Internal exception while creating Client Accounting Activity", e);
      }
    }

    private CxDxClientSessionActivity createCxDxClientSessionActivity(final Request request, final boolean suspend) throws CreateActivityException {
      try {
        String sessionId = request == null? null: request.getSessionId();
        ClientCxDxSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(sessionId, ApplicationId.createByAuthAppId(DiameterCxDxAvpCodes.CXDX_VENDOR_ID, DiameterCxDxAvpCodes.CXDX_AUTH_APP_ID), ClientCxDxSession.class);
        CxDxMessageFactoryImpl sessionMsgFactory = new CxDxMessageFactoryImpl(this.ra.baseMessageFactory,session.getSessions().get(0), stack, new DiameterIdentity[]{});

        // Set the first configured Application-Id as default for message factory
        ApplicationId firstAppId = authApplicationIds.get(0);
        sessionMsgFactory.setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

        CxDxClientSessionImpl activity = new CxDxClientSessionImpl(sessionMsgFactory, ra.cxdxAvpFactory, session, (EventListener<Request, Answer>) session, null, null, ra.sleeEndpoint);
        addActivity(activity, suspend);

        if(request != null) {
          if(request.getCommandCode() == PushProfileRequest.COMMAND_CODE) {
            activity.fetchSessionData(new PushProfileRequestImpl(request));
          }
          else if (request.getCommandCode() == RegistrationTerminationRequest.COMMAND_CODE) {
            activity.fetchSessionData(new RegistrationTerminationRequestImpl(request));
          }

          ((org.jdiameter.client.impl.app.cxdx.CxDxClientSessionImpl)session).processRequest(request);
        }

        return activity;
      }
      catch (Exception e) {
        throw new CreateActivityException("Internal exception while creating Client Accounting Activity", e);
      }
    }

    public CxDxMessageFactory getCxDxMessageFactory() {
      return ra.cxdxMessageFactory;
    }

    public CxDxAVPFactory getCxDxAVPFactory() {
      return ra.cxdxAvpFactory;
    }

    public DiameterIdentity[] getConnectedPeers() {
      return ra.getConnectedPeers();
    }

    public int getPeerCount() {
      return ra.getConnectedPeers().length;
    }

    public CxDxServerSessionActivity createCxDxServerSessionActivity() throws CreateActivityException {
      return createCxDxServerSessionActivity(null, null);
    }

    /* (non-Javadoc)
     * @see net.java.slee.resource.diameter.cxdx.CxDxProvider#getValidator()
     */
    @Override
    public Validator getValidator() {
      return this.validator;
    }
  }

  public DiameterIdentity[] getConnectedPeers() {
    if (this.stack != null) {
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

    return new DiameterIdentity[0];
  }

}
