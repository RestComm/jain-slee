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
package org.mobicents.slee.resource.diameter.cca;

import static org.jdiameter.client.impl.helpers.Parameters.MessageTimeOut;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.management.ObjectName;
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
import javax.slee.resource.ConfigProperties.Property;
import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import net.java.slee.resource.diameter.Validator;
import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterException;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.cca.CreditControlClientSession;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.cca.CreditControlProvider;
import net.java.slee.resource.diameter.cca.CreditControlServerSession;
import net.java.slee.resource.diameter.cca.events.CreditControlMessage;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;

import org.jboss.mx.util.MBeanServerLocator;

import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Message;
import org.jdiameter.api.Peer;
import org.jdiameter.api.PeerTable;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;
import org.jdiameter.api.cca.ClientCCASession;
import org.jdiameter.api.cca.ServerCCASession;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.server.impl.app.cca.ServerCCASessionImpl;
import org.mobicents.diameter.stack.DiameterListener;
import org.mobicents.diameter.stack.DiameterStackMultiplexerMBean;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;
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
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;
import org.mobicents.slee.resource.diameter.cca.events.CreditControlAnswerImpl;
import org.mobicents.slee.resource.diameter.cca.events.CreditControlRequestImpl;
import org.mobicents.slee.resource.diameter.cca.handlers.CreditControlSessionFactory;

/**
 * Mobicents Diameter Credit-Control Application Resource Adaptor.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public class DiameterCCAResourceAdaptor implements ResourceAdaptor, DiameterListener,DiameterRAInterface, FaultTolerantResourceAdaptor<String, DiameterActivity> {

  private static final long serialVersionUID = 1L;

  // Config Properties Names ---------------------------------------------

  private static final String AUTH_APPLICATION_IDS = "authApplicationIds";
  private static final String DEFAULT_VALIDITY_TIME = "defaultValidityTime";
  private static final String DEFAULT_TX_TIMER_VALUE = "defaultTxTimerValue";

  // Config Properties Values --------------------------------------------

  private List<ApplicationId> authApplicationIds;

  // Validity and TxTimer values (in seconds)
  protected long defaultValidityTime = 30;
  protected long defaultTxTimerValue = 10;

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

  // Diameter Specific Properties ----------------------------------------

  private Stack stack;
  private SessionFactory sessionFactory = null;
  private long messageTimeout = 5000;

  private ObjectName diameterMultiplexerObjectName = null;
  private DiameterStackMultiplexerMBean diameterMux = null;

  private DiameterAvpFactory baseAvpFactory = null;
  private CreditControlAVPFactory ccaAvpFactory = null;

  private DiameterMessageFactory baseMessageFactory = null;
  private CreditControlMessageFactory ccaMessageFactory = null;

  protected CreditControlSessionFactory ccaSessionFactory = null;

  protected DiameterBaseMarshaler marshaler = new DiameterBaseMarshaler();

  /**
   * The EventLookupFacility is used to look up the event id of incoming events
   */
  private transient EventLookupFacility eventLookup = null;

  /**
   * The list of activities stored in this resource adaptor. If this resource
   * adaptor were a distributed and highly available solution, this storage
   * were one of the candidates for distribution.
   */
  //private transient ConcurrentHashMap<ActivityHandle, DiameterActivity> activities = null;
  private DiameterActivityManagement activities;

  /**
   * A link to the CreditControlProvider which then will be exposed to Sbbs
   */
  protected transient CreditControlProviderImpl raProvider = null;

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

  private static final int DEFAULT_ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;
  private static final int MARSHALABLE_ACTIVITY_FLAGS = ActivityFlags.setSleeMayMarshal(DEFAULT_ACTIVITY_FLAGS);

  // Aux vars ------------------------------------------------------------

  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[]{};
  private static final String[] EMPTY_STRING_ARRAY = new String[]{};

  public DiameterCCAResourceAdaptor() {
    // TODO: Initialize any default values.
  }

  // Lifecycle methods ---------------------------------------------------

  public void setResourceAdaptorContext(ResourceAdaptorContext context) {
    this.raContext = context;

    this.tracer = context.getTracer("DiameterCCAResourceAdaptor");

    this.sleeEndpoint = context.getSleeEndpoint();
    this.eventLookup = context.getEventLookupFacility();
  }

  public void unsetResourceAdaptorContext() {
    this.raContext = null;

    this.tracer = null;

    this.sleeEndpoint = null;
    this.eventLookup = null;
  }

  // FT Lifecycle methods ----------------------------------------------------- 

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor#setFaultTolerantResourceAdaptorContext
   * (org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext)
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

  // FT methods ---------------------------------------------------------------

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

  // Lifecycle again
  public void raActive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: raActive.");
    }

    try {
      if(tracer.isInfoEnabled()) {
        tracer.info("Activating Diameter CCA RA Entity");
      }

      this.diameterMultiplexerObjectName = new ObjectName("diameter.mobicents:service=DiameterStackMultiplexer");

      Object object = MBeanServerLocator.locateJBoss().invoke(this.diameterMultiplexerObjectName, "getMultiplexerMBean", EMPTY_OBJECT_ARRAY, EMPTY_STRING_ARRAY);

      if(object instanceof DiameterStackMultiplexerMBean) {
        this.diameterMux = (DiameterStackMultiplexerMBean) object;
      }

      this.raProvider = new CreditControlProviderImpl(this);

      // Initialize stack
      initStack();

      // Initialize activities mgmt
      initActivitiesMgmt();

      // Initialize factories
      this.baseAvpFactory = new DiameterAvpFactoryImpl();
      this.ccaAvpFactory = new CreditControlAVPFactoryImpl(baseAvpFactory);

      this.baseMessageFactory = new DiameterMessageFactoryImpl(stack);
      this.ccaMessageFactory = new CreditControlMessageFactoryImpl(baseMessageFactory, null, stack, ccaAvpFactory);

      // Setup session factories
      this.sessionFactory = this.stack.getSessionFactory();

      this.ccaSessionFactory = new CreditControlSessionFactory(sessionFactory, this, defaultDirectDebitingFailureHandling, defaultCreditControlFailureHandling,  defaultValidityTime, defaultTxTimerValue);

      // Register CCA App Session Factories
      ((ISessionFactory) sessionFactory).registerAppFacory(ServerCCASession.class, this.ccaSessionFactory);
      ((ISessionFactory) sessionFactory).registerAppFacory(ClientCCASession.class, this.ccaSessionFactory);
    }
    catch (Exception e) {
      tracer.severe("Error Activating Diameter CCA RA Entity", e);
      throw new RuntimeException(e);//??
    }
  }

  public void raStopping() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: raStopping.");
    }

    try {
      diameterMux.unregisterListener(this);
    }
    catch (Exception e) {
      tracer.severe("Failed to unregister CCA RA from Diameter Mux.", e);
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
      tracer.info("Diameter CCA RA :: entityDeactivating completed.");
    }
  }

  public void raInactive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: entityDeactivated.");
    }

    //synchronized (this.activities) {
    //  activities.clear();
    //}
    activities = null;

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter CCA RA :: INACTIVE completed.");
    }
  }

  public void raConfigure(ConfigProperties properties) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: raConfigure");
    }

    parseApplicationIds((String) properties.getProperty(AUTH_APPLICATION_IDS).getValue());

    Property dvtProp = properties.getProperty(DEFAULT_VALIDITY_TIME);
    defaultValidityTime = (Long) (dvtProp != null ? dvtProp.getValue() : defaultValidityTime);

    Property dttProp = properties.getProperty(DEFAULT_TX_TIMER_VALUE);
    defaultTxTimerValue = (Long) (dttProp != null ? dttProp.getValue() : defaultTxTimerValue);

    if(tracer.isInfoEnabled()) {
      tracer.info("RA supporting " + authApplicationIds);
    }
  }

  private void parseApplicationIds(String appIdsStr) {
    if(appIdsStr != null) {
      appIdsStr = appIdsStr.replaceAll(" ", "");

      String[] appIdsStrings  = appIdsStr.split(",");

      authApplicationIds = new ArrayList<ApplicationId>();

      for(String appId : appIdsStrings) {
        String[] vendorAndAppId = appId.split(":");
        authApplicationIds.add(ApplicationId.createByAuthAppId(Long.valueOf(vendorAndAppId[0]), Long.valueOf(vendorAndAppId[1]))); 
      }
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
    return marshaler;
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
    tracer.info("Diameter CCA RA :: queryLiveness :: handle[" + handle + "].");

    if(!(handle instanceof DiameterActivityHandle)) {
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
      tracer.fine("Diameter CCA RA :: getActivity :: handle[" + handle + "].");
    }

    if(!(handle instanceof DiameterActivityHandle)){
      return null;
    }

    return this.activities.get((DiameterActivityHandle)handle);
  }

  public ActivityHandle getActivityHandle(Object activity) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: getActivityHandle :: activity[" + activity + "].");
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
      tracer.info("Diameter CCA RA :: eventProcessingFailed :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "], reason[" + reason + "].");
    }
  }

  public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter CCA RA :: eventProcessingSuccessful :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "].");
    }

    if(!(handle instanceof DiameterActivityHandle)) {
      return;
    }

    DiameterActivity activity = activities.get((DiameterActivityHandle)handle);

    if(activity instanceof CreditControlClientSessionImpl) {
      CreditControlClientSessionImpl ccaClientActivity = (CreditControlClientSessionImpl) activity;

      if(ccaClientActivity.isTerminateAfterProcessing()) {
        ccaClientActivity.endActivity();
      }
    }
  }

  public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: eventUnreferenced :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], service[" + service + "], flags[" + flags + "].");
    }
  }

  public void activityEnded(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: activityEnded :: handle[" + handle + ".");
    }

    if(!(handle instanceof DiameterActivityHandle)){
      return;
    }

    if(this.activities != null) {
      synchronized (this.activities) {
        this.activities.remove((DiameterActivityHandle)handle);
      }
    }
  }

  public void activityUnreferenced(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: activityUnreferenced :: handle[" + handle + "].");
    }

    if(!(handle instanceof DiameterActivityHandle)){
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
        Object o = getActivity(handle);
        if(o == null) {
          Transaction t = raContext.getSleeTransactionManager().getTransaction();
          throw new IllegalStateException("TX[ "+t+" ] No activity for handle: "+handle);
        }
        if(o instanceof CreditControlServerSessionImpl && event instanceof CreditControlRequest) {
          ((CreditControlServerSessionImpl)o).fetchCurrentState((CreditControlRequest)event);
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

  public void fireEvent(String sessionId, Message message) {
    DiameterMessage event = (DiameterMessage) createEvent(message);

    FireableEventType eventId = eventIdCache.getEventId(eventLookup, message);

    this.fireEvent(event, getActivityHandle(sessionId), eventId, null, true, message.isRequest());
  }

  @Override
  public void endActivity(DiameterActivityHandle arg0) {
    sleeEndpoint.endActivity(arg0);
  }

  @Override
  public void update(DiameterActivityHandle arg0, DiameterActivity arg1) {
    activities.update(arg0, arg1);
  }

  @Override
  public ApplicationId[] getSupportedApplications() {
    // not used
    return null;
  }
  /**
   * Create Event object from a JDiameter message (request or answer)
   * 
   * @return a DiameterMessage object wrapping the request/answer
   */
  private DiameterMessage createEvent(Message message) {
    if (message == null) {
      throw new NullPointerException("Message argument cannot be null while creating event.");
    }

    if (message.isError()) {
      return new ErrorAnswerImpl(message);
    }

    switch (message.getCommandCode()) {
    case CreditControlMessage.commandCode: // CCR/CCA
      return message.isRequest() ? new CreditControlRequestImpl(message) : new CreditControlAnswerImpl(message);
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
    // Register in the Mux as app listener.
    this.diameterMux.registerListener(this, (ApplicationId[]) authApplicationIds.toArray(new ApplicationId[authApplicationIds.size()]));

    // Get the stack (should not mess with)
    this.stack = this.diameterMux.getStack();
    this.messageTimeout = this.stack.getMetaData().getConfiguration().getLongValue(MessageTimeOut.ordinal(), (Long) MessageTimeOut.defValue());

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter CCA RA :: Successfully initialized stack.");
    }
  }

  private void initActivitiesMgmt() {
    final DiameterRAInterface lst = this;
    if (this.ftRAContext.isLocal()) {
      // local mgmt;
      if(tracer.isInfoEnabled()) {
        tracer.info(raContext.getEntityName() + " -- running in local mode.");
      }
      this.activities = new LocalDiameterActivityManagement();
    }
    else {
      if(tracer.isInfoEnabled()) {
        tracer.info(raContext.getEntityName() + " -- running in cluster mode.");
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
              session = this.diameterStack.getSessionFactory().getNewSession(activity.getSessionId());
              performBeforeReturnOnBase(activity, session);
              return;
            }
            else if (activity instanceof CreditControlClientSession) {
              CreditControlClientSessionImpl acc = (CreditControlClientSessionImpl) activity;
              ClientCCASession appSession = this.diameterStack.getSession(activity.getSessionId(), ClientCCASession.class);
              session = appSession.getSessions().get(0);
              performBeforeReturnOnBase(activity, session);
              performBeforeReturnCC(activity, session);
              acc.setSession(appSession);
            }
            else if (activity instanceof CreditControlServerSession) {
              CreditControlServerSessionImpl acc = (CreditControlServerSessionImpl) activity;
              ServerCCASession appSession = this.diameterStack.getSession(activity.getSessionId(), ServerCCASession.class);
              session = appSession.getSessions().get(0);
              performBeforeReturnOnBase(activity, session);
              performBeforeReturnCC(activity, session);
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

        private void performBeforeReturnOnBase(DiameterActivityImpl ac, Session session) {
          DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session, stack, new DiameterIdentity[] {});
          ac.setAvpFactory(baseAvpFactory);
          ac.setMessageFactory(msgFactory);
          ac.setCurrentWorkingSession(session);
          ac.setSessionListener(lst);
        }

        private void performBeforeReturnCC(DiameterActivityImpl ac, Session session) {
          CreditControlSessionImpl ccs = (CreditControlSessionImpl) ac;
          ccs.setCCAAvpFactory(ccaAvpFactory);
          ccs.setCCAMessageFactory(ccaMessageFactory);
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
    }
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

  public Answer processRequest(Request request) {
    // Here we receive initial request for which session does not exist!
    // Valid messages are:
    // * CCR - if we act as server, this is the message we receive
    // * NO other message should make it here, if it gets its an error ?
    //FIXME: baranowb: check if ACR is vald here also
    if(request.getCommandCode() == CreditControlRequest.commandCode) {
      DiameterActivity activity;

      try {
        activity = raProvider.createActivity(request);

        if(activity == null) {
          tracer.severe("Diameter CCA RA :: Failed to create session, Command-Code: " + request.getCommandCode() + ", Session-Id: " + request.getSessionId());
        }
        else {
          // We can only have server session?, but for sake error catching
          if(activity instanceof CreditControlServerSession) {
            CreditControlServerSessionImpl session = (CreditControlServerSessionImpl) activity;
            ((ServerCCASessionImpl)session.getSession()).processRequest(request);
          }
        }
      }
      catch (CreateActivityException e) {
        tracer.severe("Failure trying to create CCA Activity.", e);
      }
    }
    else {
      tracer.severe("Diameter CCA RA :: Received unexpected Request. Either its not CCR or session should exist to handle this, Command-Code: "+request.getCommandCode()+", Session-Id: "+request.getSessionId());
    }

    // Returning null so we can answer later
    return null;
  }

  public void receivedSuccessMessage(Request request, Answer answer) {
    // No answer should make it here, session should exist. It's an error, report it. 
    tracer.severe("Diameter CCA RA :: Received unexpected Answer - RA should not get this, session should exist to handle it. Command-Code: " + answer.getCommandCode() + ", Session-Id: " + answer.getSessionId());
  }

  public void timeoutExpired(Request request) {
    // No timeout should make it here, session should exist. It's an error, report it. 
    tracer.severe("Diameter CCA RA :: Received Timeout Message - RA should not get this, session should exist to handle it. Command-Code: " + request.getCommandCode() + ", Session-Id: " + request.getSessionId());
  }

  // CCA Session Creation Listener --------------------------------------

  private void sessionCreated(ClientCCASession ccClientSession) {
    // Make sure it's a new session and there's no activity created yet.
    if(this.getActivity(getActivityHandle(ccClientSession.getSessions().get(0).getSessionId())) != null) {
      tracer.warning( "Activity found for created Credit-Control Client Session. Shouldn't exist. Aborting." );
      return;
    }

    // Get Message Factories (for Base and CCA)
    DiameterMessageFactoryImpl baseMsgFactory = new DiameterMessageFactoryImpl(ccClientSession.getSessions().get(0),this.stack);
    CreditControlMessageFactory ccaMsgFactory = new CreditControlMessageFactoryImpl(baseMsgFactory, ccClientSession.getSessions().get(0), this.stack, this.ccaAvpFactory);

    // Create Client Activity
    CreditControlClientSessionImpl activity = new CreditControlClientSessionImpl(ccaMsgFactory, this.ccaAvpFactory, ccClientSession, null, null);

    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    activityCreated(activity, true);
  }

  private void sessionCreated(ServerCCASession ccServerSession) {
    // Make sure it's a new session and there's no activity created yet.
    if(this.getActivity(getActivityHandle(ccServerSession.getSessions().get(0).getSessionId())) != null) {
      tracer.warning( "Activity found for created Credit-Control Server Session. Shouldn't exist. Aborting." );
      return;
    }

    // Get Message Factories (for Base and CCA)
    DiameterMessageFactoryImpl baseMsgFactory = new DiameterMessageFactoryImpl(ccServerSession.getSessions().get(0),this.stack);
    CreditControlMessageFactory ccaMsgFactory = new CreditControlMessageFactoryImpl(baseMsgFactory, ccServerSession.getSessions().get(0), this.stack, this.ccaAvpFactory);

    // Create Server Activity
    CreditControlServerSessionImpl activity = new CreditControlServerSessionImpl(ccaMsgFactory,this.ccaAvpFactory,ccServerSession,null,null);

    activity.setSessionListener(this);
    activityCreated(activity, false);
  }

  // Credit Control Provider Implementation ------------------------------------

  private class CreditControlProviderImpl implements CreditControlProvider {

    protected DiameterCCAResourceAdaptor ra = null;
    protected Validator validator = new ValidatorImpl();
    public CreditControlProviderImpl(DiameterCCAResourceAdaptor ra) {
      super();
      this.ra = ra;
    }

    public CreditControlClientSession createClientSession() throws CreateActivityException {

      Transaction suspended = null;
      try {
        //SUSPEND tx, in case of call from SBB it will be tx calls, meaning activity wont be visible until
        //this tx ends. can cause "no activity" in ra fire!
        if(raContext.getSleeTransactionManager().getTransaction() != null) {
          suspended = raContext.getSleeTransactionManager().suspend();
        }

        ClientCCASession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, ApplicationId.createByAuthAppId(CreditControlMessageFactory._CCA_VENDOR, CreditControlMessageFactory._CCA_AUTH_APP_ID), ClientCCASession.class, new Object[]{});

        if (session == null) {
          tracer.severe("Failure creating CCA Client Session (null).");
          return null;
        }
        sessionCreated(session);
        return  (CreditControlClientSession) getActivity(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (Exception e) {
        throw new CreateActivityException(e);
      }
      finally {
        if(suspended != null) {
          try {
            raContext.getSleeTransactionManager().resume(suspended);
          }
          catch (InvalidTransactionException e) {
            tracer.severe("Error resuming transaction (" + suspended + ").", e);
          }
          catch (IllegalStateException e) {
            tracer.severe("Error resuming transaction (" + suspended + ").", e);
          }
          catch (SystemException e) {
            tracer.severe("Error resuming transaction (" + suspended + ").", e);
          }
        }
      }
    }

    //This method should be called only for CCR
    protected DiameterActivity createActivity(Request request) throws CreateActivityException {
      try {
        Set<ApplicationId> appIds = request.getApplicationIdAvps();
        if(appIds == null || appIds.size() == 0) {
          throw new CreateActivityException("No App ids present in message");
        }
        ISessionFactory fct = ((ISessionFactory) stack.getSessionFactory());
        ServerCCASession session = fct.getNewAppSession(request.getSessionId(), ApplicationId.createByAuthAppId(CreditControlMessageFactory._CCA_VENDOR, CreditControlMessageFactory._CCA_AUTH_APP_ID), ServerCCASession.class, new Object[]{});

        if (session == null) {
          tracer.severe("Failure creating CCA Server Session (null).");
          return null;
        }
        sessionCreated(session);
        return (DiameterActivity) getActivity(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (Exception e) {
        throw new CreateActivityException(e);
      }
    }

    public CreditControlClientSession createClientSession(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException {
      CreditControlClientSessionImpl clientSession=(CreditControlClientSessionImpl) this.createClientSession();

      clientSession.setDestinationHost(destinationHost);
      clientSession.setDestinationRealm(destinationRealm);

      return clientSession;
    }

    public CreditControlAVPFactory getCreditControlAVPFactory() {
      return ccaAvpFactory;
    }

    public CreditControlMessageFactory getCreditControlMessageFactory() {
      return ccaMessageFactory;
    }

    public DiameterIdentity[] getConnectedPeers() {
      if (ra.stack != null) {
        try {
          // Get the list of peers from the stack
          List<Peer> peers = ra.stack.unwrap(PeerTable.class).getPeerTable();

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
      DiameterIdentity[] connectedPeers = getConnectedPeers();
      return connectedPeers != null ? getConnectedPeers().length : -1;
    }

    /* (non-Javadoc)
     * @see net.java.slee.resource.diameter.cca.CreditControlProvider#getValidator()
     */
    @Override
    public Validator getValidator() {
      return this.validator;
    }
  }

  // Provisioning --------------------------------------------------------

  protected int defaultDirectDebitingFailureHandling = 0;
  protected int defaultCreditControlFailureHandling = 0;

  //////////////////////////
  // Provisioning Methods //
  //////////////////////////

  public long getMessageTimeout() {
    return messageTimeout;
  }

  public void setMessageTimeout(long messageTimeout) {
    this.messageTimeout = messageTimeout;
  }

  public int getDefaultDirectDebitingFailureHandling() {
    return defaultDirectDebitingFailureHandling;
  }

  public void setDefaultDirectDebitingFailureHandling(int defaultDirectDebitingFailureHandling) {
    this.defaultDirectDebitingFailureHandling = defaultDirectDebitingFailureHandling;
  }

  public int getDefaultCreditControlFailureHandling() {
    return defaultCreditControlFailureHandling;
  }

  public void setDefaultCreditControlFailureHandling(int defaultCreditControlFailureHandling) {
    this.defaultCreditControlFailureHandling = defaultCreditControlFailureHandling;
  }

  public long getDefaultValidityTime() {
    return defaultValidityTime;
  }

  public void setDefaultValidityTime(long defaultValidityTime) {
    this.defaultValidityTime = defaultValidityTime;
  }

  public long getDefaultTxTimerValue() {
    return defaultTxTimerValue;
  }

  public void setDefaultTxTimerValue(long defaultTxTimerValue) {
    this.defaultTxTimerValue = defaultTxTimerValue;
  }

}
