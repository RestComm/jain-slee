package org.mobicents.slee.resource.diameter.ro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.cca.CreditControlServerSession;
import net.java.slee.resource.diameter.cca.events.CreditControlAnswer;
import net.java.slee.resource.diameter.cca.events.CreditControlMessage;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;
import net.java.slee.resource.diameter.ro.RoAvpFactory;
import net.java.slee.resource.diameter.ro.RoClientSessionActivity;
import net.java.slee.resource.diameter.ro.RoMessageFactory;
import net.java.slee.resource.diameter.ro.RoProvider;

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
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;
import org.jdiameter.api.cca.ClientCCASession;
import org.jdiameter.api.cca.ServerCCASession;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.server.impl.app.cca.ServerCCASessionImpl;
import org.mobicents.diameter.stack.DiameterListener;
import org.mobicents.diameter.stack.DiameterStackMultiplexerMBean;
import org.mobicents.slee.resource.diameter.base.DiameterActivityHandle;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.base.EventIDFilter;
import org.mobicents.slee.resource.diameter.base.events.AbortSessionAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.AbortSessionRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.AccountingAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.AccountingRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.ReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ReAuthRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.SessionTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.SessionTerminationRequestImpl;
import org.mobicents.slee.resource.diameter.cca.CreditControlMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.cca.EventIDCache;
import org.mobicents.slee.resource.diameter.cca.events.CreditControlAnswerImpl;
import org.mobicents.slee.resource.diameter.cca.events.CreditControlRequestImpl;
import org.mobicents.slee.resource.diameter.cca.handlers.CCASessionCreationListener;
import org.mobicents.slee.resource.diameter.cca.handlers.CreditControlSessionFactory;

/**
 * Diameter Ro Resource Adaptor
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DiameterRoResourceAdaptor implements ResourceAdaptor, DiameterListener, CCASessionCreationListener {

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

  // Default Failure Handling
  protected int defaultDirectDebitingFailureHandling = 0;
  protected int defaultCreditControlFailureHandling = 0;

  // Validity and TxTimer values (in seconds)
  protected long defaultValidityTime = 30;
  protected long defaultTxTimerValue = 10;

  // Base Factories
  private DiameterAvpFactory baseAvpFactory = null;
  private DiameterMessageFactoryImpl baseMessageFactory;

  private SessionFactory sessionFactory = null;

  // Ro RA Factories
  protected CreditControlSessionFactory ccaSessionFactory = null;

  protected transient RoAvpFactory roAvpFactory = null;

  protected RoMessageFactory roMessageFactory;

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
  private transient ConcurrentHashMap<ActivityHandle, DiameterActivity> activities = null;

  /**
   * A link to the DiameterProvider which then will be exposed to Sbbs
   */
  protected transient RoProviderImpl raProvider = null;

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

  private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;

  public DiameterRoResourceAdaptor() {
    // TODO: Initialize any default values.
  }

  // Lifecycle methods ---------------------------------------------------

  public void setResourceAdaptorContext(ResourceAdaptorContext context) {
    this.raContext = context;

    this.tracer = context.getTracer("DiameterRoResourceAdaptor");

    this.sleeEndpoint = context.getSleeEndpoint();
    this.eventLookup = context.getEventLookupFacility();
  }

  public void unsetResourceAdaptorContext() {
    this.raContext = null;

    this.tracer = null;

    this.sleeEndpoint = null;
    this.eventLookup = null;
  }

  public void raActive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Ro RA :: raActive.");
    }

    try {
      if(tracer.isInfoEnabled()) {
        tracer.info("Activating Diameter Ro RA Entity");
      }

      this.diameterMultiplexerObjectName = new ObjectName("diameter.mobicents:service=DiameterStackMultiplexer");

      Object object = MBeanServerLocator.locateJBoss().invoke(this.diameterMultiplexerObjectName, "getMultiplexerMBean", new Object[]{}, new String[]{});

      if(object instanceof DiameterStackMultiplexerMBean) {
        this.diameterMux = (DiameterStackMultiplexerMBean) object;
      }

      this.raProvider = new RoProviderImpl(this);

      this.activities = new ConcurrentHashMap<ActivityHandle, DiameterActivity>();

      // Initialize the protocol stack
      initStack();

      // Initialize factories
      this.baseAvpFactory = new DiameterAvpFactoryImpl();
      this.baseMessageFactory = new DiameterMessageFactoryImpl(stack);

      this.roAvpFactory = new RoAvpFactoryImpl(baseAvpFactory);
      this.roMessageFactory = new RoMessageFactoryImpl(baseMessageFactory, null, stack, roAvpFactory);

      this.sessionFactory = this.stack.getSessionFactory();
      this.ccaSessionFactory = new CreditControlSessionFactory(sessionFactory, this, defaultDirectDebitingFailureHandling, defaultCreditControlFailureHandling,  defaultValidityTime, defaultTxTimerValue);

      // Register CCA App Session Factories
      ((ISessionFactory) sessionFactory).registerAppFacory(ServerCCASession.class, this.ccaSessionFactory);
      ((ISessionFactory) sessionFactory).registerAppFacory(ClientCCASession.class, this.ccaSessionFactory);
    }
    catch (Exception e) {
      tracer.severe("Error Activating Diameter Ro RA Entity", e);
    }
  }

  public void raStopping() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Ro RA :: raStopping.");
    }

    try {
      diameterMux.unregisterListener(this);
    }
    catch (Exception e) {
      tracer.severe("Failed to unregister Ro RA from Diameter Mux.", e);
    }

    synchronized (this.activities) {
      for (ActivityHandle activityHandle : activities.keySet()) {
        try {
          if(tracer.isInfoEnabled()) {
            tracer.info("Ending activity [" + activityHandle + "]");
          }

          activities.get(activityHandle).endActivity();
        }
        catch (Exception e) {
          tracer.severe("Error Deactivating Activity", e);
        }
      }
    }

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Ro RA :: raStopping completed.");
    }
  }

  public void raInactive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Ro RA :: raInactive.");
    }

    synchronized (this.activities) {
      activities.clear();
    }
    activities = null;

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Ro RA :: raInactive completed.");
    }
  }

  public void raConfigure(ConfigProperties properties) {
    parseApplicationIds((String) properties.getProperty(AUTH_APPLICATION_IDS).getValue());
  }

  private void parseApplicationIds(String appIdsStr) {
    if(appIdsStr != null) {
      appIdsStr = appIdsStr.replaceAll(" ", "");

      String[] appIdsStrings  = appIdsStr.split(", ");

      authApplicationIds = new ArrayList<ApplicationId>(appIdsStrings.length);

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
    // TODO Auto-generated method stub
    return null;
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
    tracer.info("Diameter Ro RA :: queryLiveness :: handle[" + handle + "].");

    DiameterActivityImpl activity = (DiameterActivityImpl) activities.get(handle);

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
      tracer.fine("Diameter Ro RA :: getActivity :: handle[" + handle + "].");
    }

    return this.activities.get(handle);
  }

  public ActivityHandle getActivityHandle(Object activity) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Ro RA :: getActivityHandle :: activity[" + activity + "].");
    }

    if (!(activity instanceof DiameterActivity)) {
      return null;
    }

    DiameterActivity inActivity = (DiameterActivity) activity;

    for (Map.Entry<ActivityHandle, DiameterActivity> activityInfo : this.activities.entrySet()) {
      Object curActivity = activityInfo.getValue();

      if (curActivity.equals(inActivity)) {
        return activityInfo.getKey();
      }
    }

    return null;
  }

  public void administrativeRemove(ActivityHandle handle) {
    // TODO what to do here?
  }

  // Optional callback methods -------------------------------------------

  public void eventProcessingFailed(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags, FailureReason reason) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Ro RA :: eventProcessingFailed :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "], reason[" + reason + "].");
    }
  }

  public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Ro RA :: eventProcessingSuccessful :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "].");
    }

    DiameterActivity activity = activities.get(handle);

    if(activity instanceof RoClientSessionActivityImpl) {
      RoClientSessionActivityImpl roClientActivity = (RoClientSessionActivityImpl) activity;

      if(roClientActivity.getTerminateAfterAnswer()) {
        roClientActivity.endActivity();
      }
    }
  }

  public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Ro RA :: eventUnreferenced :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], service[" + service + "], flags[" + flags + "].");
    }
  }

  public void activityEnded(ActivityHandle handle) {
    tracer.info("Diameter Ro RA :: activityEnded :: handle[" + handle + ".");

    if(this.activities != null) {
      synchronized (this.activities) {
        this.activities.remove(handle);
      }
    }
  }

  public void activityUnreferenced(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Ro RA :: activityUnreferenced :: handle[" + handle + "].");
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
        if (transacted){
          this.raContext.getSleeEndpoint().fireEventTransacted(handle, eventID, event, address, null, EVENT_FLAGS);
        }
        else {
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
    case CreditControlMessage.commandCode: // CCR/CCA
      return isRequest ? new CreditControlRequestImpl(message) : new CreditControlAnswerImpl(message);
    case AbortSessionAnswer.commandCode: // ASR/ASA
      return isRequest ? new AbortSessionRequestImpl(message) : new AbortSessionAnswerImpl(message);
    case SessionTerminationAnswer.commandCode: // STR/STA
      return isRequest ? new SessionTerminationRequestImpl(message) : new SessionTerminationAnswerImpl(message);
    case ReAuthAnswer.commandCode: // RAR/RAA
      return isRequest ? new ReAuthRequestImpl(message) : new ReAuthAnswerImpl(message);
    case AccountingAnswer.commandCode: // ACR/ACA
      return isRequest ? new AccountingRequestImpl(message) : new AccountingAnswerImpl(message);

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
      sleeEndpoint.startActivityTransacted(activity.getActivityHandle(), activity, ACTIVITY_FLAGS);

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

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Ro RA :: Successfully initialized stack.");
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
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Ro RA :: Got Request. Command-Code[" + request.getCommandCode() + "]");
    }

    // Here we receive initial request for which session does not exist!
    // Valid messages are:
    // * CCR - if we act as server, this is the message we receive
    // * NO other message should make it here, if it gets its an errro ?
    // FIXME: baranowb: check if ACR is vald here also
    if(request.getCommandCode() == CreditControlRequest.commandCode) {
      DiameterActivity activity;

      try {
        activity = raProvider.createActivity(request);

        if(activity == null) {
          tracer.severe("Diameter Ro RA :: Failed to create session, Command-Code: " + request.getCommandCode() + ", Session-Id: " + request.getSessionId());
        }
        else {
          // We can only have server session?, but for sake error catching
          if(activity instanceof CreditControlServerSession) {
            RoServerSessionActivityImpl session = (RoServerSessionActivityImpl) activity;
            ((ServerCCASessionImpl)session.getSession()).processRequest(request);
          }
        }
      }
      catch (CreateActivityException e) {
        tracer.severe("Failure trying to create Ro Activity.", e);
      }

      // Returning null so we can answer later
      return null;
    }
    else {
      if(tracer.isInfoEnabled()) {
        tracer.info("Diameter Ro RA :: Received unexpected Request. Either its not CCR or session should exist to handle this, Command-Code: " + request.getCommandCode() + ", Session-Id: " + request.getSessionId());
      }
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.EventListener#receivedSuccessMessage(org.jdiameter.api.Message, org.jdiameter.api.Message)
   */
  public void receivedSuccessMessage(Request request, Answer answer) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Ro RA :: receivedSuccessMessage :: " + "Request[" + request + "], Answer[" + answer + "].");
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
      tracer.info("Diameter Ro RA :: timeoutExpired :: " + "Request[" + request + "].");
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

  // Ro/CCA Session Creation Listener --------------------------------------

  public void sessionCreated(ClientCCASession ccClientSession) {
    // Make sure it's a new session and there's no activity created yet.
    if(this.getActivity(getActivityHandle(ccClientSession.getSessions().get(0).getSessionId())) != null) {
      tracer.warning("Activity found for created Credit-Control Client Session. Shouldn't exist. Aborting.");
      return;
    }

    // Get Message Factories (for Base and CCA)
    DiameterMessageFactoryImpl baseMsgFactory = new DiameterMessageFactoryImpl(ccClientSession.getSessions().get(0), this.stack);
    CreditControlMessageFactory ccaMsgFactory = new CreditControlMessageFactoryImpl(baseMsgFactory, ccClientSession.getSessions().get(0), this.stack, this.roAvpFactory);

    // Create Client Activity
    RoClientSessionActivityImpl activity = new RoClientSessionActivityImpl(ccaMsgFactory, this.roAvpFactory, ccClientSession, null, null, this.sleeEndpoint, stack);

    //FIXME: baranowb: add basic session mgmt for base? or do we relly on responses?
    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity);
  }

  public void sessionCreated(ServerCCASession ccServerSession)
  {
    // Make sure it's a new session and there's no activity created yet.
    if(this.getActivity(getActivityHandle(ccServerSession.getSessions().get(0).getSessionId())) != null) {
      tracer.warning("Activity found for created Credit-Control Server Session. Shouldn't exist. Aborting.");
      return;
    }

    // Get Message Factories (for Base and CCA)
    DiameterMessageFactoryImpl baseMsgFactory = new DiameterMessageFactoryImpl(ccServerSession.getSessions().get(0), this.stack);
    CreditControlMessageFactory ccaMsgFactory = new CreditControlMessageFactoryImpl(baseMsgFactory, ccServerSession.getSessions().get(0), this.stack, this.roAvpFactory);

    // Create Server Activity
    RoServerSessionActivityImpl activity = new RoServerSessionActivityImpl(ccaMsgFactory, this.roAvpFactory, ccServerSession, null, null, this.sleeEndpoint, stack);

    //FIXME: baranowb: add basic session mgmt for base? or do we relly on responses?
    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity);
  }

  public boolean sessionExists(String sessionId) {
    return this.activities.containsKey(new DiameterActivityHandle(sessionId));
  }

  public void sessionDestroyed(String sessionId, Object appSession) {
    try {
      this.sleeEndpoint.endActivity(getActivityHandle(sessionId));
    }
    catch (Exception e) {
      tracer.severe("Failure Ending Activity with Session-Id[" + sessionId + "]", e);
    }
  }

  // Provider Implementation ---------------------------------------------

  private class RoProviderImpl implements RoProvider {

    private DiameterRoResourceAdaptor ra;

    public RoProviderImpl(DiameterRoResourceAdaptor ra) {
      this.ra = ra;
    }

    public RoClientSessionActivity createRoClientSessionActivity() throws CreateActivityException {
      try {
        ClientCCASession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, authApplicationIds.get(0), ClientCCASession.class, new Object[]{});

        if (session == null) {
          tracer.severe("Failure creating Ro Client Session (null).");
          return null;
        }

        return  (RoClientSessionActivity) getActivity(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (Exception e) {
        throw new CreateActivityException(e);
      }
    }

    public RoClientSessionActivity createRoClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException {
      RoClientSessionActivityImpl clientSession = (RoClientSessionActivityImpl) this.createRoClientSessionActivity();

      clientSession.setDestinationHost(destinationHost);
      clientSession.setDestinationRealm(destinationRealm);

      return clientSession;
    }

    public RoAvpFactory getRoAvpFactory() {
      return this.ra.roAvpFactory;
    }

    public RoMessageFactory getRoMessageFactory() {
      return this.ra.roMessageFactory;
    }

    public CreditControlAnswer eventCreditControlRequest(CreditControlRequest ccr) throws IOException {
      try {
        String sessionId = ccr.getSessionId();
        DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);

        if (!activities.keySet().contains(handle)) {
          createActivity(((DiameterMessageImpl)ccr).getGenericData());
        }

        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);

        return (CreditControlAnswer) activity.sendSyncMessage(ccr);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }

      // FIXME Throw unknown message exception?
      return null;
    }

    public CreditControlAnswer initialCreditControlRequest(CreditControlRequest ccr) throws IOException {
      try {
        String sessionId = ccr.getSessionId();
        DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);

        if (!activities.keySet().contains(handle)) {
          createActivity(((DiameterMessageImpl)ccr).getGenericData());
        }

        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);

        return (CreditControlAnswer) activity.sendSyncMessage(ccr);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }

      // FIXME Throw unknown message exception?
      return null;
    }

    public CreditControlAnswer updateCreditControlRequest(CreditControlRequest ccr) throws IOException, IllegalArgumentException {
      try {
        String sessionId = ccr.getSessionId();
        DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);

        if (!activities.keySet().contains(handle)) {
          createActivity(((DiameterMessageImpl)ccr).getGenericData());
        }

        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);

        return (CreditControlAnswer) activity.sendSyncMessage(ccr);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }

      // FIXME Throw unknown message exception?
      return null;
    }

    public CreditControlAnswer terminationCreditControlRequest(CreditControlRequest ccr) throws IOException, IllegalArgumentException {
      try {
        String sessionId = ccr.getSessionId();
        DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);

        if (!activities.keySet().contains(handle)) {
          createActivity(((DiameterMessageImpl)ccr).getGenericData());
        }

        DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);

        return (CreditControlAnswer) activity.sendSyncMessage(ccr);
      }
      catch (Exception e) {
        tracer.severe("Failure sending sync request.", e);
      }

      // FIXME Throw unknown message exception?
      return null;
    }

    public byte[] marshalRoCreditControlRequest(CreditControlRequest ccr) {
      throw new UnsupportedOperationException();
    }

    public byte[] marshalRoCreditControlAnswer(CreditControlAnswer cca) {
      throw new UnsupportedOperationException();
    }

    public CreditControlRequest unmarshalRoCreditControlRequest(byte[] b) throws IOException, AvpNotAllowedException {
      throw new UnsupportedOperationException();
    }

    public CreditControlAnswer unmarshalRoCreditControlAnswer(byte[] b) throws IOException, AvpNotAllowedException {
      throw new UnsupportedOperationException();
    }

    private DiameterActivity createActivity(Message message) throws CreateActivityException {
      String sessionId = message.getSessionId();
      DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);

      if (activities.keySet().contains(handle)) {
        return activities.get(handle);
      }
      else {
        if (message.isRequest()) {
          return createRoServerSessionActivity((Request) message);
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

          return (DiameterActivity) createRoClientSessionActivity(destinationHost, destinationRealm);
        }
      }
    }

    private DiameterActivity createRoServerSessionActivity(Request message) throws CreateActivityException {
      try {
        ServerCCASession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(message.getSessionId(), authApplicationIds.get(0), ServerCCASession.class, new Object[]{});

        if (session == null) {
          tracer.severe("Failure creating Ro Server Session (null).");
          return null;
        }

        return  (DiameterActivity) getActivity(getActivityHandle(session.getSessions().get(0).getSessionId()));
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
