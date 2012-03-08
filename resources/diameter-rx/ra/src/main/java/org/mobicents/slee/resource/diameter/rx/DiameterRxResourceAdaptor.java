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

package org.mobicents.slee.resource.diameter.rx;

import java.io.IOException;
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
import javax.slee.transaction.SleeTransactionManager;

import net.java.slee.resource.diameter.Validator;
import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterException;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rx.RxAvpFactory;
import net.java.slee.resource.diameter.rx.RxClientSessionActivity;
import net.java.slee.resource.diameter.rx.RxMessageFactory;
import net.java.slee.resource.diameter.rx.RxProvider;
import net.java.slee.resource.diameter.rx.RxServerSessionActivity;
import net.java.slee.resource.diameter.rx.events.AAAnswer;
import net.java.slee.resource.diameter.rx.events.AARequest;
import net.java.slee.resource.diameter.rx.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.rx.events.AbortSessionRequest;
import net.java.slee.resource.diameter.rx.events.ReAuthAnswer;
import net.java.slee.resource.diameter.rx.events.ReAuthRequest;
import net.java.slee.resource.diameter.rx.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.rx.events.SessionTerminationRequest;

import org.jboss.mx.util.MBeanServerLocator;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.Message;
import org.jdiameter.api.Peer;
import org.jdiameter.api.PeerTable;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;
import org.jdiameter.api.rx.ClientRxSession;
import org.jdiameter.api.rx.ServerRxSession;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.server.impl.app.rx.ServerRxSessionImpl;
import org.mobicents.diameter.stack.DiameterListener;
import org.mobicents.diameter.stack.DiameterStackMultiplexerMBean;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;
import org.mobicents.slee.resource.cluster.ReplicatedData;
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
import org.mobicents.slee.resource.diameter.rx.events.AAAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.AARequestImpl;
import org.mobicents.slee.resource.diameter.rx.events.AbortSessionAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.AbortSessionRequestImpl;
import org.mobicents.slee.resource.diameter.rx.events.ReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.ReAuthRequestImpl;
import org.mobicents.slee.resource.diameter.rx.events.SessionTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.SessionTerminationRequestImpl;
import org.mobicents.slee.resource.diameter.rx.handlers.RxSessionFactory;

/**
 * Diameter Rx Resource Adaptor
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class DiameterRxResourceAdaptor implements ResourceAdaptor, DiameterListener, DiameterRAInterface, FaultTolerantResourceAdaptor<String, DiameterActivity> {

  private static final long serialVersionUID = 1L;

  // Config Properties Names ---------------------------------------------
  private static final String AUTH_APPLICATION_IDS = "authApplicationIds";
  private static final int DEFAULT_ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;
  private static final int MARSHALABLE_ACTIVITY_FLAGS = ActivityFlags.setSleeMayMarshal(DEFAULT_ACTIVITY_FLAGS);
  // for all events we are interested in knowing when the event failed to be processed
  private static final int EVENT_FLAGS = getEventFlags();

  // caches the eventIDs, avoiding lookup in container.
  public final EventIDCache eventIdCache = new EventIDCache();
  protected DiameterBaseMarshaler marshaler = new DiameterBaseMarshaler();
  protected int defaultDirectDebitingFailureHandling;
  protected int defaultCreditControlFailureHandling;

  // Validity and TxTimer values (in seconds)
  protected long defaultValidityTime = 30;
  protected long defaultTxTimerValue = 10;

  // activity stuff
  protected long activityRemoveDelay = 30000;

  // Rx RA Factories
  protected RxSessionFactory rxSessionFactory;
  protected RxAvpFactory rxAvpFactory;
  protected RxMessageFactory rxMessageFactory;
  protected transient RxProviderImpl raProvider;

  // tells the RA if an event with a specified ID should be filtered or not.
  private final EventIDFilter eventIDFilter = new EventIDFilter();
  private List<ApplicationId> authApplicationIds;

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
  private transient SleeEndpoint sleeEndpoint;
  private Tracer tracer;

  // Diameter Specific Properties ----------------------------------------
  private Stack stack;
  private ObjectName diameterMultiplexerObjectName;
  private DiameterStackMultiplexerMBean diameterMux;

  // Base Factories
  private DiameterAvpFactory baseAvpFactory;
  private DiameterMessageFactoryImpl baseMessageFactory;
  private SessionFactory sessionFactory;
  // the EventLookupFacility is used to look up the event id of incoming events.
  private transient EventLookupFacility eventLookup;

  /**
   * The list of activities stored in this resource adaptor. If this resource
   * adaptor were a distributed and highly available solution, this storage
   * were one of the candidates for distribution.
   */
  private transient DiameterActivityManagement activities;

  /**
   * Static method for constructing an event flags object for this RA.
   * @return the generated event flags.
   */
  private static int getEventFlags() {
    int eventFlags = EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK;
    eventFlags = EventFlags.setRequestProcessingFailedCallback(eventFlags);
    eventFlags = EventFlags.setRequestProcessingSuccessfulCallback(eventFlags);
    return eventFlags;
  }

  public DiameterRxResourceAdaptor() {
    // TODO: Initialize any default values.
  }

  // Lifecycle methods ---------------------------------------------------
  public void setResourceAdaptorContext(final ResourceAdaptorContext context) {
    this.raContext = context;

    this.tracer = context.getTracer("DiameterRxResourceAdaptor");

    this.sleeEndpoint = context.getSleeEndpoint();
    this.eventLookup = context.getEventLookupFacility();
    this.raProvider = new RxProviderImpl(this);
  }

  /**
   * Reset all context references.
   */
  public void unsetResourceAdaptorContext() {
    this.raContext = null;

    this.tracer = null;

    this.sleeEndpoint = null;
    this.eventLookup = null;
  }

  // FT Lifecycle methods ------------------------------------------------
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFaultTolerantResourceAdaptorContext(final FaultTolerantResourceAdaptorContext<String, DiameterActivity> ctx) {
    this.ftRAContext = ctx;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unsetFaultTolerantResourceAdaptorContext() {
    this.ftRAContext = null;
    //clear this.activities ??
  }

  // FT methods ----------------------------------------------------------
  /**
   * {@inheritDoc}
   */
  @Override
  public void dataRemoved(final String arg0) {
    this.activities.remove(getActivityHandle(arg0));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void failOver(final String arg0) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ApplicationId[] getSupportedApplications() {
    //FIXME: fill this , Alex what should go here? Seconded. :)
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void raActive() {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Rx RA :: raActive.");
    }

    try {
      if (tracer.isInfoEnabled()) {
        tracer.info("Activating Diameter Rx RA Entity");
      }

      this.diameterMultiplexerObjectName = new ObjectName("diameter.mobicents:service=DiameterStackMultiplexer");

      final Object object = MBeanServerLocator.locateJBoss().invoke(this.diameterMultiplexerObjectName,
          "getMultiplexerMBean", new Object[]{}, new String[]{});

      if (object instanceof DiameterStackMultiplexerMBean) {
        this.diameterMux = (DiameterStackMultiplexerMBean) object;
      }

      // Initialize the protocol stack
      initStack();

      //Initialize activities mgmt
      initActivitiesMgmt();

      // Initialize factories
      this.baseAvpFactory = new DiameterAvpFactoryImpl();
      this.baseMessageFactory = new DiameterMessageFactoryImpl(stack);

      this.rxAvpFactory = new RxAvpFactoryImpl(baseAvpFactory);
      this.rxMessageFactory = new RxMessageFactoryImpl(baseMessageFactory, null, stack);

      // Set the first configured Application-Id as default for message factory
      ApplicationId firstAppId = authApplicationIds.get(0);
      ((RxMessageFactoryImpl)this.rxMessageFactory).setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

      this.sessionFactory = this.stack.getSessionFactory();
      this.rxSessionFactory = new RxSessionFactory(this, sessionFactory);

      // Register Rx App Session Factories
      ((ISessionFactory) sessionFactory).registerAppFacory(ServerRxSession.class, this.rxSessionFactory);
      ((ISessionFactory) sessionFactory).registerAppFacory(ClientRxSession.class, this.rxSessionFactory);
    }
    catch (Exception e) {
      tracer.severe("Error Activating Diameter Rx RA Entity", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void raStopping() {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Rx RA :: raStopping.");
    }

    try {
      diameterMux.unregisterListener(this);
    }
    catch (Exception e) {
      tracer.severe("Failed to unregister Rx RA from Diameter Mux.", e);
    }

    if (tracer.isInfoEnabled()) {
      tracer.info("Diameter Rx RA :: raStopping completed.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void raInactive() {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Rx RA :: raInactive.");
    }

    activities = null;

    if (tracer.isInfoEnabled()) {
      tracer.info("Diameter Rx RA :: raInactive completed.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void raConfigure(ConfigProperties properties) {
    parseApplicationIds((String) properties.getProperty(AUTH_APPLICATION_IDS).getValue());
  }

  private void parseApplicationIds(final String appIdsStr) {
    if (appIdsStr != null) {
      final String trimmedString = appIdsStr.replaceAll(" ", "");

      final String[] appIdsStrings = trimmedString.split(",");

      authApplicationIds = new ArrayList<ApplicationId>(appIdsStrings.length);

      for (String appId : appIdsStrings) {
        final String[] vendorAndAppId = appId.split(":");
        authApplicationIds.add(ApplicationId.createByAuthAppId(Long.valueOf(vendorAndAppId[0]), Long.valueOf(vendorAndAppId[1])));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void raUnconfigure() {
    // Clean up!
    this.activities = null;
    this.raContext = null;
    this.eventLookup = null;
    this.raProvider = null;
    this.sleeEndpoint = null;
    this.stack = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void raVerifyConfiguration(final ConfigProperties properties) throws InvalidConfigurationException {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void raConfigurationUpdate(final ConfigProperties properties) {
  }

  // Interface access methods --------------------------------------------
  public Object getResourceAdaptorInterface(final String className) {
    return raProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Marshaler getMarshaler() {
    return marshaler;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void serviceActive(final ReceivableService serviceInfo) {
    eventIDFilter.serviceActive(serviceInfo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void serviceStopping(final ReceivableService serviceInfo) {
    eventIDFilter.serviceStopping(serviceInfo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void serviceInactive(final ReceivableService serviceInfo) {
    eventIDFilter.serviceInactive(serviceInfo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void queryLiveness(final ActivityHandle handle) {
    tracer.info("Diameter Rx RA :: queryLiveness :: handle[" + handle + "].");

    final DiameterActivityImpl activity = (DiameterActivityImpl) activities.get((DiameterActivityHandle) handle);

    if (activity != null && !activity.isValid()) {
      try {
        sleeEndpoint.endActivity(handle);
      }
      catch (Exception e) {
        tracer.severe("Failure ending non-live activity.", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getActivity(ActivityHandle handle) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Rx RA :: getActivity :: handle[" + handle + "].");
    }

    return this.activities.get((DiameterActivityHandle) handle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActivityHandle getActivityHandle(final Object activity) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Rx RA :: getActivityHandle :: activity[" + activity + "].");
    }

    if (!(activity instanceof DiameterActivity)) {
      return null;
    }

    final DiameterActivityImpl inActivity = (DiameterActivityImpl) activity;

    return inActivity.getActivityHandle();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void administrativeRemove(final ActivityHandle handle) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void eventProcessingFailed(final ActivityHandle handle, final FireableEventType eventType, final Object event, final Address address,
      final ReceivableService service, final int flags, final FailureReason reason) {
    if (tracer.isInfoEnabled()) {
      tracer.info("Diameter Rx RA :: eventProcessingFailed :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address["
          + address + "], flags[" + flags + "], reason[" + reason + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }

    processAfterEventDelivery(handle, eventType, event, address, service, flags);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void eventProcessingSuccessful(final ActivityHandle handle, final FireableEventType eventType, final Object event, final Address address,
      final ReceivableService service, final int flags) {
    if (tracer.isInfoEnabled()) {
      tracer.info("Diameter Rx RA :: eventProcessingSuccessful :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address["
          + address + "], flags[" + flags + "].");
    }

    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }

    processAfterEventDelivery(handle, eventType, event, address, service, flags);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void eventUnreferenced(final ActivityHandle handle, final FireableEventType eventType, final Object event, final Address address,
      final ReceivableService service, final int flags) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Rx RA :: eventUnreferenced :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address["
          + address + "], service[" + service + "], flags[" + flags + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }

    processAfterEventDelivery(handle, eventType, event, address, service, flags);
  }

  private void processAfterEventDelivery(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service,
      int flags) {
    DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);
    if (activity != null) {
      synchronized (activity) {
        if (activity.isTerminateAfterProcessing()) {
          activity.endActivity();
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void activityEnded(ActivityHandle handle) {
    tracer.info("Diameter Rx RA :: activityEnded :: handle[" + handle + ".");

    if (this.activities != null) {
      synchronized (this.activities) {
        this.activities.remove((DiameterActivityHandle) handle);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void activityUnreferenced(final ActivityHandle handle) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Rx RA :: activityUnreferenced :: handle[" + handle + "].");
    }

    //this.activityEnded(handle);
    if (handle instanceof DiameterActivityHandle) {
      this.endActivity((DiameterActivityHandle) handle);
    }
  }

  public boolean fireEvent(final Object event, final ActivityHandle handle, final FireableEventType eventID, final Address address,
      final boolean useFiltering, final boolean transacted) {

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
        final Object o = getActivity(handle);
        if (o == null) {
          throw new IllegalStateException("No activity for handle: " + handle);
        }

        if (o instanceof RxServerSessionActivityImpl && event instanceof AARequest) {
          ((RxServerSessionActivityImpl) o).fetchCurrentState((AARequest) event);
        }

        if (o instanceof RxServerSessionActivityImpl && event instanceof SessionTerminationRequest) {
          ((RxServerSessionActivityImpl) o).fetchCurrentState((SessionTerminationRequest) event);
        }

        this.raContext.getSleeEndpoint().fireEvent(handle, eventID, event, address, null, EVENT_FLAGS);

        return true;
      }
      catch (Exception e) {
        tracer.severe("Error firing event.", e);
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fireEvent(final String sessionId, final Message message) {
    final DiameterMessage event = (DiameterMessage) createEvent(message);

    final FireableEventType eventId = eventIdCache.getEventId(eventLookup, message);

    this.fireEvent(event, getActivityHandle(sessionId), eventId, null, true, message.isRequest());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endActivity(DiameterActivityHandle arg0) {
    sleeEndpoint.endActivity(arg0);
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
  public void update(DiameterActivityHandle arg0, DiameterActivity arg1) {
    activities.update(arg0, arg1);
  }

  /**
   * Create Event object from a JDiameter message (request or answer).
   *
   * @return a DiameterMessage object wrapping the request/answer
   * @throws OperationNotSupportedException
   */
  private DiameterMessage createEvent(final Message message) {
    if (message == null) {
      throw new NullPointerException("Message argument cannot be null while creating event.");
    }

    final int commandCode = message.getCommandCode();

    if (message.isError()) {
      return new ErrorAnswerImpl(message);
    }

    final boolean isRequest = message.isRequest();

    switch (commandCode) {
      case AARequest.commandCode: // AAR/AAA
        return isRequest ? new AARequestImpl(message) : new AAAnswerImpl(message);
      case AbortSessionAnswer.commandCode: // ASR/ASA
        return isRequest ? new AbortSessionRequestImpl(message) : new AbortSessionAnswerImpl(message);
      case SessionTerminationAnswer.commandCode: // STR/STA
        return isRequest ? new SessionTerminationRequestImpl(message) : new SessionTerminationAnswerImpl(message);
      case ReAuthAnswer.commandCode: // RAR/RAA
        return isRequest ? new ReAuthRequestImpl(message) : new ReAuthAnswerImpl(message);
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
  private void addActivity(final DiameterActivity ac, boolean suspended) {
    try {
      // Inform SLEE that Activity Started
      final DiameterActivityImpl activity = (DiameterActivityImpl) ac;

      if (suspended) {
        sleeEndpoint.startActivitySuspended(activity.getActivityHandle(), activity, MARSHALABLE_ACTIVITY_FLAGS);
      }
      else {
        sleeEndpoint.startActivity(activity.getActivityHandle(), activity, MARSHALABLE_ACTIVITY_FLAGS);
      }

      // Set the listener
      activity.setSessionListener(this);

      // Put it into our activities map
      activities.put(activity.getActivityHandle(), activity);

      if (tracer.isInfoEnabled()) {
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

    if (tracer.isInfoEnabled()) {
      tracer.info("Diameter Rx RA :: Successfully initialized stack.");
    }
  }

  private void initActivitiesMgmt() {
    final DiameterRAInterface lst = this;
    if (this.ftRAContext.isLocal()) {
      // local mgmt;
      if (tracer.isInfoEnabled()) {
        tracer.info(raContext.getEntityName() + " -- running in LOCAL mode.");
      }
      this.activities = new LocalDiameterActivityManagement(this.raContext, activityRemoveDelay);
    }
    else {
      if (tracer.isInfoEnabled()) {
        tracer.info(raContext.getEntityName() + " -- running in CLUSTER mode.");
      }
      final org.mobicents.slee.resource.cluster.ReplicatedData<String, DiameterActivity> clusteredData = this.ftRAContext.getReplicateData(true);
      // get special one
      this.activities = new ClusteredDiameterActivityManagementImpl(this.ftRAContext, activityRemoveDelay,this.raContext.getTracer(""), stack, this.raContext.getSleeTransactionManager(), clusteredData, lst);
    }
  }

  /**
   * Create the Diameter Activity Handle for an given session id.
   *
   * @param sessionId the session identifier to create the activity handle from
   * @return a DiameterActivityHandle for the provided sessionId
   */
  protected DiameterActivityHandle getActivityHandle(final String sessionId) {
    return new DiameterActivityHandle(sessionId);
  }

  // NetworkReqListener Implementation -----------------------------------
  public Answer processRequest(final Request request) {
    if (tracer.isInfoEnabled()) {
      tracer.info("Diameter Rx RA :: Got Request. Command-Code[" + request.getCommandCode() + "]");
    }

    // Here we receive initial request for which session does not exist!
    // Valid messages are:
    // * CCR - if we act as server, this is the message we receive
    // * NO other message should make it here, if it gets its an errro ?
    // FIXME: baranowb: check if ACR is vald here also
    if (request.getCommandCode() == AARequest.commandCode) {
      DiameterActivity activity;

      try {
        activity = raProvider.createActivity(request);

        if (activity == null) {
          tracer.severe("Diameter Rx RA :: Failed to create session, Command-Code: " + request.getCommandCode() + ", Session-Id: "
              + request.getSessionId());
        }
        else {
          if (activity instanceof RxServerSessionActivity) {
            final RxServerSessionActivityImpl session = (RxServerSessionActivityImpl) activity;
            ((ServerRxSessionImpl) session.getSession()).processRequest(request);
          }
        }
      }
      catch (CreateActivityException e) {
        tracer.severe("Failure trying to create Rx Activity.", e);
      }

      // Returning null so we can answer later
      return null;
    }
    else {
      if (tracer.isInfoEnabled()) {
        tracer.info("Diameter Rx RA :: Received unexpected Request. Either its not CCR or session should exist to handle this, Command-Code: "
            + request.getCommandCode() + ", Session-Id: " + request.getSessionId());
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void receivedSuccessMessage(final Request request, final Answer answer) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Rx RA :: receivedSuccessMessage :: " + "Request[" + request + "], Answer[" + answer + "].");
    }

    tracer.warning("Resource Adaptor should not receive this (receivedSuccessMessage), a session should exist to handle it.");

    try {
      if (tracer.isInfoEnabled()) {
        tracer.info("Received Message Result-Code: " + answer.getResultCode().getUnsigned32());
      }
    }
    catch (AvpDataException ignore) {
      // ignore, this was just for informational purposes...
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void timeoutExpired(final Request request) {
    if (tracer.isInfoEnabled()) {
      tracer.info("Diameter Rx RA :: timeoutExpired :: " + "Request[" + request + "].");
    }

    tracer.warning("Resource Adaptor should not receive this (timeoutExpired), a session should exist to handle it.");

    try {
      // Message delivery timed out - we have to remove activity
      ((DiameterActivity) getActivity(getActivityHandle(request.getSessionId()))).endActivity();
    }
    catch (Exception e) {
      tracer.severe("Failure processing timeout message.", e);
    }
  }

  // Rx/CCA Session Creation Listener --------------------------------------
  public void sessionCreated(final ClientRxSession rxClientSession) {
    // Make sure it's a new session and there's no activity created yet.
    if (this.getActivity(getActivityHandle(rxClientSession.getSessions().get(0).getSessionId())) != null) {
      tracer.warning("Activity found for created Rx Client Session. Shouldn't exist. Aborting.");
      return;
    }

    // Create Client Activity
    final RxClientSessionActivityImpl activity = new RxClientSessionActivityImpl(rxMessageFactory, rxAvpFactory, rxClientSession, null, null, stack);

    //FIXME: baranowb: add basic session mgmt for base? or do we relly on responses?
    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity, false /*true*/);
  }

  public void sessionCreated(final ServerRxSession rxServerSession) {
    // Make sure it's a new session and there's no activity created yet.
    if (this.getActivity(getActivityHandle(rxServerSession.getSessions().get(0).getSessionId())) != null) {
      tracer.warning("Activity found for created Rx Server Session. Shouldn't exist. Aborting.");
      return;
    }

    // Create Server Activity
    final RxServerSessionActivityImpl activity = new RxServerSessionActivityImpl(rxMessageFactory, rxAvpFactory, rxServerSession, null, null, stack);

    //FIXME: baranowb: add basic session mgmt for base? or do we relly on responses?
    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity, false);
  }

  public boolean sessionExists(final String sessionId) {
    return this.activities.containsKey(getActivityHandle(sessionId));
  }

  public void sessionDestroyed(final String sessionId, final Object appSession) {
    try {
      this.sleeEndpoint.endActivity(getActivityHandle(sessionId));
    }
    catch (Exception e) {
      tracer.severe("Failure Ending Activity with Session-Id[" + sessionId + "]", e);
    }
  }

  // Provider Implementation ---------------------------------------------
  private class RxProviderImpl implements RxProvider {

    private DiameterRxResourceAdaptor ra;
    private Validator validator = new ValidatorImpl();

    public RxProviderImpl(final DiameterRxResourceAdaptor ra) {
      this.ra = ra;
    }

    public RxClientSessionActivity createRxClientSessionActivity() throws CreateActivityException {
      try {
        final ClientRxSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, authApplicationIds.get(0),
            ClientRxSession.class, new Object[]{});
        sessionCreated(session);
        if (session == null) {
          tracer.severe("Failure creating Rx Client Session (null).");
          return null;
        }

        return (RxClientSessionActivity) getActivity(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (Exception e) {
        throw new CreateActivityException(e);
      }
    }

    public RxClientSessionActivity createRxClientSessionActivity(final DiameterIdentity destinationHost, final DiameterIdentity destinationRealm)
    throws CreateActivityException {
      final RxClientSessionActivityImpl clientSession = (RxClientSessionActivityImpl) this.createRxClientSessionActivity();

      clientSession.setDestinationHost(destinationHost);
      clientSession.setDestinationRealm(destinationRealm);

      return clientSession;
    }

    public RxAvpFactory getRxAvpFactory() {
      return this.ra.rxAvpFactory;
    }

    public RxMessageFactory getRxMessageFactory() {
      return this.ra.rxMessageFactory;
    }

    public AAAnswer sendAARequest(final AARequest aar) throws IOException {
      try {
        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(getActivityHandle(aar.getSessionId()));

        if (activity == null) {
          activity = (DiameterActivityImpl) createActivity(((DiameterMessageImpl) aar).getGenericData());
        }

        return (AAAnswer) activity.sendSyncMessage(aar);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }

      // FIXME Throw unknown message exception?
      return null;
    }

    @Override
    public net.java.slee.resource.diameter.rx.events.AbortSessionAnswer sendAbortSessionRequest(AbortSessionRequest ccr) throws IOException {
      try {
        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(getActivityHandle(ccr.getSessionId()));

        if (activity == null) {
          activity = (DiameterActivityImpl) createActivity(((DiameterMessageImpl) ccr).getGenericData());
        }

        return (AbortSessionAnswer) activity.sendSyncMessage(ccr);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }

      // FIXME Throw unknown message exception?
      return null;
    }

    @Override
    public net.java.slee.resource.diameter.rx.events.SessionTerminationAnswer sendSessionTerminationRequest(SessionTerminationRequest ccr)
    throws IOException {
      try {
        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(getActivityHandle(ccr.getSessionId()));

        if (activity == null) {
          activity = (DiameterActivityImpl) createActivity(((DiameterMessageImpl) ccr).getGenericData());
        }

        return (SessionTerminationAnswer) activity.sendSyncMessage(ccr);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }
      // FIXME Throw unknown message exception?
      return null;
    }

    @Override
    public net.java.slee.resource.diameter.rx.events.ReAuthAnswer sendReAuthRequest(ReAuthRequest ccr) throws IOException {
      try {
        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(getActivityHandle(ccr.getSessionId()));

        if (activity == null) {
          activity = (DiameterActivityImpl) createActivity(((DiameterMessageImpl) ccr).getGenericData());
        }

        return (ReAuthAnswer) activity.sendSyncMessage(ccr);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }
      // FIXME Throw unknown message exception?
      return null;
    }

    private DiameterActivity createActivity(final Message message) throws CreateActivityException {
      DiameterActivity activity = activities.get(getActivityHandle(message.getSessionId()));
      if (activity == null) {
        if (message.isRequest()) {
          return createRxServerSessionActivity((Request) message);
        }
        else {
          final AvpSet avps = message.getAvps();
          Avp avp = null;

          DiameterIdentity destinationHost = null;
          DiameterIdentity destinationRealm = null;

          if ((avp = avps.getAvp(Avp.DESTINATION_HOST)) != null) {
            try {
              destinationHost = new DiameterIdentity(avp.getDiameterIdentity());
            }
            catch (AvpDataException e) {
              tracer.severe("Failed to extract Destination-Host from Message.", e);
            }
          }

          if ((avp = avps.getAvp(Avp.DESTINATION_REALM)) != null) {
            try {
              destinationRealm = new DiameterIdentity(avp.getDiameterIdentity());
            }
            catch (AvpDataException e) {
              tracer.severe("Failed to extract Destination-Realm from Message.", e);
            }
          }

          return (DiameterActivity) createRxClientSessionActivity(destinationHost, destinationRealm);
        }
      }

      return activity;
    }

    private DiameterActivity createRxServerSessionActivity(final Request message) throws CreateActivityException {
      try {
        final ServerRxSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(message.getSessionId(),
            authApplicationIds.get(0), ServerRxSession.class, new Object[]{});
        sessionCreated(session);
        if (session == null) {
          tracer.severe("Failure creating Rx Server Session (null).");
          return null;
        }

        return (DiameterActivity) getActivity(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (Exception e) {
        throw new CreateActivityException(e);
      }
    }

    public DiameterIdentity[] getConnectedPeers() {
      return ra.getConnectedPeers();
    }

    public int getPeerCount() {
      return ra.getConnectedPeers().length;
    }

    @Override
    public Validator getValidator() {
      return this.validator;
    }
  }

  public DiameterIdentity[] getConnectedPeers() {
    if (this.stack != null) {
      try {
        // Get the list of peers from the stack
        final List<Peer> peers = stack.unwrap(PeerTable.class).getPeerTable();

        final DiameterIdentity[] result = new DiameterIdentity[peers.size()];

        int i = 0;

        // Get each peer from the list and make a DiameterIdentity
        for (Peer peer : peers) {
          final DiameterIdentity identity = new DiameterIdentity(peer.getUri().toString());

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

  private class ClusteredDiameterActivityManagementImpl extends AbstractClusteredDiameterActivityManagement {

    private final DiameterRAInterface lst;

    /**
     * @param ftRAContext
     * @param delay
     * @param tracer
     * @param diameterStack
     * @param sleeTxManager
     * @param replicatedData
     */
    public ClusteredDiameterActivityManagementImpl(final FaultTolerantResourceAdaptorContext ftRAContext, final long delay, final Tracer tracer, final Stack diameterStack,
        final SleeTransactionManager sleeTxManager,final  ReplicatedData<String, DiameterActivity> replicatedData, final DiameterRAInterface lst) {
      super(ftRAContext, delay, tracer, diameterStack, sleeTxManager, replicatedData);
      this.lst = lst;
    }

    @Override
    protected void performBeforeReturn(final DiameterActivityImpl activity) {
      try {
        Session session = null;
        if (activity.getClass().equals(DiameterActivityImpl.class)) {
          session = this.diameterStack.getSessionFactory().getNewSession(activity.getSessionId());
          performBeforeReturnOnBase(activity, session);
          return;
        }
        else if (activity instanceof RxClientSessionActivity) {
          final RxClientSessionActivityImpl acc = (RxClientSessionActivityImpl) activity;
          final ClientRxSession appSession = this.diameterStack.getSession(activity.getSessionId(), ClientRxSession.class);
          session = appSession.getSessions().get(0);
          performBeforeReturnOnBase(activity, session);
          performBeforeReturnRx(acc, session);
          performBeforeReturnCC(acc);
          acc.setSession(appSession);
        }
        else if (activity instanceof RxServerSessionActivity) {
          final RxServerSessionActivityImpl acc = (RxServerSessionActivityImpl) activity;
          final ServerRxSession appSession = this.diameterStack.getSession(activity.getSessionId(), ServerRxSession.class);
          session = appSession.getSessions().get(0);
          performBeforeReturnOnBase(activity, session);
          performBeforeReturnRx(acc, session);
          performBeforeReturnCC(acc);
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

    // Two calls are required since Rx relies on CCA. CCA does not know anything about Rx so it needs its fields created.
    private void performBeforeReturnCC(final RxServerSessionActivityImpl acc) {
    }

    private void performBeforeReturnCC(final RxClientSessionActivityImpl acc) {
    }

    private void performBeforeReturnRx(final RxServerSessionActivityImpl acc, Session session) {
      RxMessageFactoryImpl messageFactory = new RxMessageFactoryImpl(baseMessageFactory, session.getSessionId(), stack);

      // Set the first configured Application-Id as default for message factory
      ApplicationId firstAppId = authApplicationIds.get(0);
      messageFactory.setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

      acc.setRxMessageFactory(messageFactory);
    }

    private void performBeforeReturnRx(final RxClientSessionActivityImpl acc, Session session) {
      RxMessageFactoryImpl messageFactory = new RxMessageFactoryImpl(baseMessageFactory, session.getSessionId(), stack);

      // Set the first configured Application-Id as default for message factory
      ApplicationId firstAppId = authApplicationIds.get(0);
      messageFactory.setApplicationId(firstAppId.getVendorId(), firstAppId.getAuthAppId());

      acc.setRxMessageFactory(messageFactory);
    }

    private void performBeforeReturnOnBase(final DiameterActivityImpl ac, final Session session) {
      final DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session, stack, new DiameterIdentity[]{});
      ac.setAvpFactory(baseAvpFactory);
      ac.setMessageFactory(msgFactory);
      ac.setCurrentWorkingSession(session);
      ac.setSessionListener(lst);
    }

    @Override
    public DiameterActivity get(final DiameterActivityHandle handle) {
      return super.get(handle);
    }

    @Override
    public void put(final DiameterActivityHandle handle, final DiameterActivity activity) {
      super.put(handle, activity);
    }

    @Override
    public DiameterActivity remove(final DiameterActivityHandle handle) {
      return super.remove(handle);
    }
  }
}
