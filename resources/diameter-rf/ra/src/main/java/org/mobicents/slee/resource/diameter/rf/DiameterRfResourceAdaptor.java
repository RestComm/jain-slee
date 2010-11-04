package org.mobicents.slee.resource.diameter.rf;

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
import javax.transaction.Transaction;

import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterException;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rf.RfAvpFactory;
import net.java.slee.resource.diameter.rf.RfClientSessionActivity;
import net.java.slee.resource.diameter.rf.RfMessageFactory;
import net.java.slee.resource.diameter.rf.RfProvider;
import net.java.slee.resource.diameter.rf.RfServerSessionActivity;
import net.java.slee.resource.diameter.rf.events.RfAccountingAnswer;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

import org.jboss.mx.util.MBeanServerLocator;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Peer;
import org.jdiameter.api.PeerTable;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.api.auth.ServerAuthSession;
import org.jdiameter.api.rf.ClientRfSession;
import org.jdiameter.api.rf.ServerRfSession;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.client.impl.app.rf.ClientRfSessionImpl;
import org.jdiameter.common.api.app.rf.IRfSessionFactory;
import org.jdiameter.server.impl.app.rf.ServerRfSessionImpl;
import org.mobicents.diameter.stack.DiameterListener;
import org.mobicents.diameter.stack.DiameterStackMultiplexerMBean;
import org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;
import org.mobicents.slee.resource.diameter.AbstractClusteredDiameterActivityManagement;
import org.mobicents.slee.resource.diameter.DiameterActivityManagement;
import org.mobicents.slee.resource.diameter.LocalDiameterActivityManagement;
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
import org.mobicents.slee.resource.diameter.rf.events.RfAccountingAnswerImpl;
import org.mobicents.slee.resource.diameter.rf.events.RfAccountingRequestImpl;
import org.mobicents.slee.resource.diameter.rf.handlers.RfSessionFactory;

/**
 * Diameter Rf Resource Adaptor
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DiameterRfResourceAdaptor implements ResourceAdaptor, DiameterListener, DiameterRAInterface,org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor<String, DiameterActivity> {

  private static final long serialVersionUID = 1L;

  // Config Properties Names ---------------------------------------------

  private static final String ACCT_APPLICATION_IDS = "acctApplicationIds";

  // Config Properties Values --------------------------------------------

  private List<ApplicationId> acctApplicationIds;

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

  protected DiameterBaseMarshaler marshaler = new DiameterBaseMarshaler();
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

  private ObjectName diameterMultiplexerObjectName = null;
  private DiameterStackMultiplexerMBean diameterMux = null;

  // Diameter Base Factories
  private DiameterMessageFactoryImpl baseMessageFactory;
  private DiameterAvpFactoryImpl baseAvpFactory;
//Rf Specific Factories
  private RfMessageFactoryImpl rfMessageFactory;
  private RfAvpFactory rfAvpFactory;
  private SessionFactory sessionFactory = null;

  

  private IRfSessionFactory rfSessionFactory;

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
  private transient DiameterActivityManagement activities;

  /**
   * A link to the DiameterProvider which then will be exposed to Sbbs
   */
  private RfProviderImpl raProvider = null;

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

  public DiameterRfResourceAdaptor() {
    // TODO: Initialize any default values.
  }

  // Lifecycle methods ---------------------------------------------------

  public void setResourceAdaptorContext(ResourceAdaptorContext context) {
    this.raContext = context;

    this.tracer = context.getTracer("DiameterRfResourceAdaptor");

    this.sleeEndpoint = context.getSleeEndpoint();
    this.eventLookup = context.getEventLookupFacility();
  }

  public void unsetResourceAdaptorContext() {
    this.raContext = null;

    this.tracer = null;

    this.sleeEndpoint = null;
    this.eventLookup = null;
  }

  // FT Lifecycle methods ------------------------------------------------

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
      tracer.fine("Diameter Rf RA :: raActive.");
    }

    try {
      if(tracer.isInfoEnabled()) {
        tracer.info("Activating Diameter Rf RA Entity");
      }

      this.diameterMultiplexerObjectName = new ObjectName("diameter.mobicents:service=DiameterStackMultiplexer");

      Object object = MBeanServerLocator.locateJBoss().invoke(this.diameterMultiplexerObjectName, "getMultiplexerMBean", new Object[]{}, new String[]{});

      if(object instanceof DiameterStackMultiplexerMBean) {
        this.diameterMux = (DiameterStackMultiplexerMBean) object;
      }

      this.raProvider = new RfProviderImpl(this);

      ///this.activities = new ConcurrentHashMap<ActivityHandle, DiameterActivity>();

      // Initialize the protocol stack
      initStack();

      //Init activities mgmt

      initActivitiesMgmt();
      // Initialize factories
      this.baseAvpFactory = new DiameterAvpFactoryImpl();
      this.baseMessageFactory = new DiameterMessageFactoryImpl(stack);

      this.rfMessageFactory = new RfMessageFactoryImpl(baseMessageFactory,null, stack);
      this.rfAvpFactory = new RfAvpFactoryImpl();
      // Register Accounting App Session Factories
      this.sessionFactory = this.stack.getSessionFactory();

      this.rfSessionFactory = new RfSessionFactory(this,sessionFactory);
  
      ((ISessionFactory) sessionFactory).registerAppFacory(ServerRfSession.class, rfSessionFactory);
      ((ISessionFactory) sessionFactory).registerAppFacory(ClientRfSession.class, rfSessionFactory);
    }
    catch (Exception e) {
      tracer.severe("Error Activating Diameter Rf RA Entity", e);
    }
  }

  public void raStopping() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Rf RA :: raStopping.");
    }

    try {
      diameterMux.unregisterListener(this);
    }
    catch (Exception e) {
      tracer.severe("Failed to unregister Rf RA from Diameter Mux.", e);
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
      tracer.info("Diameter Rf RA :: raStopping completed.");
    }
  }

  public void raInactive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Rf RA :: raInactive.");
    }

    //synchronized (this.activities) {
    //  activities.clear();
    //}
    activities = null;

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Rf RA :: raInactive completed.");
    }
  }

  public void raConfigure(ConfigProperties properties) {
    parseApplicationIds((String) properties.getProperty(ACCT_APPLICATION_IDS).getValue());
  }

  private void parseApplicationIds(String appIdsStr) {
    if(appIdsStr != null) {
      appIdsStr = appIdsStr.replaceAll(" ", "");

      String[] appIdsStrings  = appIdsStr.split(", ");

      acctApplicationIds = new ArrayList<ApplicationId>(appIdsStrings.length);

      for(String appId : appIdsStrings) {
        String[] vendorAndAppId = appId.split(":");
        acctApplicationIds.add(ApplicationId.createByAccAppId(Long.valueOf(vendorAndAppId[0]), Long.valueOf(vendorAndAppId[1]))); 
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
    tracer.info("Diameter Rf RA :: queryLiveness :: handle[" + handle + "].");
    if(!(handle instanceof DiameterActivityHandle)){ return ;}
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
      tracer.fine("Diameter Rf RA :: getActivity :: handle[" + handle + "].");
    }
    if(!(handle instanceof DiameterActivityHandle)){ return null;}
    return this.activities.get((DiameterActivityHandle)handle);
  }

  public ActivityHandle getActivityHandle(Object activity) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Rf RA :: getActivityHandle :: activity[" + activity + "].");
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
      tracer.info("Diameter Rf RA :: eventProcessingFailed :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "], reason[" + reason + "].");
    }
  }

  public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Rf RA :: eventProcessingSuccessful :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "].");
    }
  }

  public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Rf RA :: eventUnreferenced :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], service[" + service + "], flags[" + flags + "].");
    }
  }

  public void activityEnded(ActivityHandle handle) {
    tracer.info("Diameter Rf RA :: activityEnded :: handle[" + handle + ".");
    if(!(handle instanceof DiameterActivityHandle)){ return ;}
    if(this.activities != null) {
      synchronized (this.activities) {
        this.activities.remove((DiameterActivityHandle)handle);
      }
    }
  }

  public void activityUnreferenced(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Rf RA :: activityUnreferenced :: handle[" + handle + "].");
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
        	//fetch state for Server Activities
        	Object o = getActivity(handle);
            if(o == null) {
              Transaction t = raContext.getSleeTransactionManager().getTransaction();
              throw new IllegalStateException("TX[ "+t+" ] No activity for handle: "+handle);
            }
            if(o instanceof RfServerSessionActivityImpl && event instanceof RfAccountingRequest) {
              ((RfServerSessionActivityImpl)o).fetchSessionData((RfAccountingRequest)event,true);
            }
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

  @Override
  public void endActivity(DiameterActivityHandle arg0) {
    this.sleeEndpoint.endActivity(arg0);
  }

  @Override
  public void update(DiameterActivityHandle arg0, DiameterActivity arg1) {
    this.activities.update(arg0, arg1);
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
    case RfAccountingAnswer.commandCode: // ACR/ACA
      return isRequest ? new RfAccountingRequestImpl(message) : new RfAccountingAnswerImpl(message);

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
  private void addActivity(DiameterActivity ac) {
    try {
      // Inform SLEE that Activity Started
      DiameterActivityImpl activity = (DiameterActivityImpl) ac;
      sleeEndpoint.startActivity(activity.getActivityHandle(), activity, MARSHALABLE_ACTIVITY_FLAGS);

      // Set the listener
      activity.setSessionListener(this);

      // Put it into our activities map
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
    this.diameterMux.registerListener(this, getSupportedApplications());

    // Get the stack (should not mess with)
    this.stack = this.diameterMux.getStack();

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Rf RA :: Successfully initialized stack.");
    }
  }

  private void initActivitiesMgmt() {
    final DiameterRAInterface lst = this;
    if (this.ftRAContext.isLocal()) {
      if(tracer.isInfoEnabled()) {
        tracer.info(raContext.getEntityName() + " -- running in LOCAL mode.");
      }
      this.activities = new LocalDiameterActivityManagement();
    }
    else {
      tracer.info(raContext.getEntityName() + " -- running in CLUSTER mode.");
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
              // JIC: is this required?
              session = this.diameterStack.getSessionFactory().getNewSession(activity.getSessionId());
              performBeforeReturnOnBase(activity, session);
              return;
            }
            else if (activity instanceof RfServerSessionActivity) {
              RfServerSessionActivityImpl acc = (RfServerSessionActivityImpl) activity;
              ServerRfSession appSession = this.diameterStack.getSession(activity.getSessionId(), ServerRfSession.class);
              session = appSession.getSessions().get(0);
              performBeforeReturnOnBase(activity, session);
              performBeforeReturnRf(acc);
              acc.setSession(appSession);
            }
            else if (activity instanceof RfClientSessionActivity) {
              RfClientSessionActivityImpl acc = (RfClientSessionActivityImpl) activity;
              ClientRfSession appSession = this.diameterStack.getSession(activity.getSessionId(), ClientRfSession.class);
              session = appSession.getSessions().get(0);
              performBeforeReturnOnBase(activity, session);
              performBeforeReturnRf(acc);
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

        private void performBeforeReturnRf(RfClientSessionActivityImpl ac) {
          // we just have to set factory
          RfMessageFactoryImpl rfMessageFactory = new RfMessageFactoryImpl(baseMessageFactory, ac.getSessionId(),stack);
          ac.setRfMessageFactory(rfMessageFactory);
        }

        private void performBeforeReturnRf(RfServerSessionActivityImpl ac) {
          // we just have to set factory
          RfMessageFactoryImpl rfMessageFactory = new RfMessageFactoryImpl(baseMessageFactory,ac.getSessionId(), stack);
          ac.setRfMessageFactory(rfMessageFactory);
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

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.NetworkReqListener#processRequest(org.jdiameter.api.Request)
   */
  public Answer processRequest(Request request) {
    DiameterActivityImpl activity;

    try {
      activity = (DiameterActivityImpl) raProvider.createActivity(request);

      if(activity instanceof RfServerSessionActivityImpl) {
        RfServerSessionActivityImpl assai = (RfServerSessionActivityImpl)activity;
        ((ServerRfSessionImpl)assai.getSession()).processRequest(request);
      }
      else if(activity instanceof RfClientSessionActivityImpl) {
        RfClientSessionActivityImpl assai = (RfClientSessionActivityImpl)activity;
        ((ClientRfSessionImpl)assai.getSession()).processRequest(request);
      }
    }
    catch (CreateActivityException e) {
      tracer.severe("Failure trying to create Rf Activity.", e);
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
      tracer.fine("Diameter Rf RA :: receivedSuccessMessage :: " + "Request[" + request + "], Answer[" + answer + "].");
    }

    tracer.warning("Resource Adaptor should not receive this (receivedSuccessMessage), a session should exist to handle it.");

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
      tracer.info("Diameter Rf RA :: timeoutExpired :: " + "Request[" + request + "].");
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

  // Rf Session Creation Listener --------------------------------------

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.acc.ClientAccSession)
   */
  public void sessionCreated(ClientRfSession session) {
    DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(stack);

    RfClientSessionActivityImpl activity = new RfClientSessionActivityImpl(msgFactory, baseAvpFactory, session, null, null,stack);

    activity.setSessionListener(this);
    session.addStateChangeNotification(activity);
    addActivity(activity);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.acc.ServerAccSession)
   */
  public void sessionCreated(ServerRfSession session) {
    DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(stack);

    RfServerSessionActivityImpl activity = new RfServerSessionActivityImpl(msgFactory, baseAvpFactory, session, null, null, stack);

    session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.auth.ServerAuthSession)
   */
  public void sessionCreated(ServerAuthSession session) {
    tracer.severe("Unexpected Auth Session at Rf Resource Adaptor.");
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.auth.ClientAuthSession)
   */
  public void sessionCreated(ClientAuthSession session) {
    tracer.severe("Unexpected Auth Session at Rf Resource Adaptor.");
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionExists(java.lang.String)
   */
  public boolean sessionExists(String sessionId) {
    return this.activities.containsKey(getActivityHandle(sessionId));
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionDestroyed(java.lang.String, java.lang.Object)
   */
  public void sessionDestroyed(String sessionId, Object appSession) {
    try {
      this.sleeEndpoint.endActivity(getActivityHandle(sessionId));
    }
    catch (Exception e) {
      tracer.severe("Failure Ending Activity with Session-Id[" + sessionId + "]", e);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#getSupportedApplications()
   */
  public ApplicationId[] getSupportedApplications() {
    return (ApplicationId[]) acctApplicationIds.toArray(new ApplicationId[acctApplicationIds.size()]);
  }

  // Provider Implementation ---------------------------------------------

  private class RfProviderImpl implements RfProvider {

    protected DiameterRfResourceAdaptor ra;

    /**
     * Constructor.
     * 
     * @param rfResourceAdaptor The resource adaptor for this Provider.
     */
    public RfProviderImpl(DiameterRfResourceAdaptor rfResourceAdaptor) {
      this.ra = rfResourceAdaptor;
    }

    private DiameterActivity createActivity(Message message) throws CreateActivityException {
      String sessionId = message.getSessionId();
      DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);

      if (activities.containsKey(handle)) {
        return activities.get(handle);
      }
      else {
        if (message.isRequest()) {
          return createRfServerSessionActivity((Request) message);
        }
        else {
          AvpSet avps = message.getAvps();
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

          return createRfClientSessionActivity(destinationHost, destinationRealm);
        }
      }
    }

    private DiameterActivity createRfServerSessionActivity(Request request) throws CreateActivityException {
      ServerRfSession session = null;

      try {
        ApplicationId appId = request.getApplicationIdAvps().isEmpty() ? null : request.getApplicationIdAvps().iterator().next(); 
        session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(request.getSessionId(), appId, ServerRfSession.class, request);

        if (session == null) {
          throw new CreateActivityException("Got NULL Session while creating Server Accounting Activity");
        }
        sessionCreated(session);
      }
      catch (InternalException e) {
        throw new CreateActivityException("Internal exception while creating Server Accounting Activity", e);
      }
      catch (IllegalDiameterStateException e) {
        throw new CreateActivityException("Illegal Diameter State exception while creating Server Accounting Activity", e);
      }

      return (RfServerSessionActivityImpl) activities.get(getActivityHandle(session.getSessions().get(0).getSessionId()));
    }

    // Actual Provider Methods 

    public RfClientSessionActivity createRfClientSessionActivity() throws CreateActivityException {
      return createRfClientSessionActivity(null, null);
    }

    public RfClientSessionActivity createRfClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException {
      try {
        ClientRfSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, acctApplicationIds.get(0), ClientRfSession.class);
        if (session == null) {
          throw new CreateActivityException("Got NULL Session while creating Client Accounting Activity");
        }
        sessionCreated(session);
        return new RfClientSessionActivityImpl(baseMessageFactory, baseAvpFactory, session, destinationHost, destinationRealm, stack);
      }
      catch (Exception e) {
        throw new CreateActivityException("Internal exception while creating Client Accounting Activity", e);
      }
    }

    public RfMessageFactory getRfMessageFactory() {
      return ra.rfMessageFactory;
    }

    public RfAvpFactory getRfAvpFactory() {
      return ra.rfAvpFactory;
    }

    public RfAccountingAnswer sendRfAccountingRequest(RfAccountingRequest accountingRequest) {
      try {
        String sessionId = accountingRequest.getSessionId();
        DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);

        if (!activities.containsKey(handle)) {
          createActivity(((DiameterMessageImpl)accountingRequest).getGenericData());
        }

        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);

        return (RfAccountingAnswer) activity.sendSyncMessage(accountingRequest);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }

      // FIXME Throw unknown message exception?
      return null;
    }

    public DiameterIdentity[] getConnectedPeers() {
      return ra.getConnectedPeers();
    }

    public int getPeerCount() {
      return ra.getConnectedPeers().length;
    }
  }

  /**
   * @return
   */
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
