package org.mobicents.slee.resource.diameter.cca;

import static org.jdiameter.client.impl.helpers.Parameters.MessageTimeOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
import javax.slee.transaction.SleeTransactionManager;

import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
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
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.cca.events.CreditControlAnswerImpl;
import org.mobicents.slee.resource.diameter.cca.events.CreditControlRequestImpl;
import org.mobicents.slee.resource.diameter.cca.handlers.CCASessionCreationListener;
import org.mobicents.slee.resource.diameter.cca.handlers.CreditControlSessionFactory;

/**
 * Mobicents Diameter Credit-Control Application Resource Adaptor.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public class DiameterCCAResourceAdaptor implements ResourceAdaptor, DiameterListener, CCASessionCreationListener {

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

  /**
   * The EventLookupFacility is used to look up the event id of incoming events
   */
  private transient EventLookupFacility eventLookup = null;

  /**
   * The list of activites stored in this resource adaptor. If this resource
   * adaptor were a distributed and highly available solution, this storage
   * were one of the candidates for distribution.
   */
  private transient ConcurrentHashMap<ActivityHandle, DiameterActivity> activities = null;

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

  private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;

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

      this.activities = new ConcurrentHashMap<ActivityHandle, DiameterActivity>();

      initStack();

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
      tracer.info("Diameter CCA RA :: entityDeactivating completed.");
    }
  }

  public void raInactive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: entityDeactivated.");
    }

    synchronized (this.activities) {
      activities.clear();
    }
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
    tracer.info("Diameter CCA RA :: queryLiveness :: handle[" + handle + "].");

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
      tracer.fine("Diameter CCA RA :: getActivity :: handle[" + handle + "].");
    }

    return this.activities.get(handle);
  }

  public ActivityHandle getActivityHandle(Object activity) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: getActivityHandle :: activity[" + activity + "].");
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
      tracer.info("Diameter CCA RA :: eventProcessingFailed :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "], reason[" + reason + "].");
    }
  }

  public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter CCA RA :: eventProcessingSuccessful :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "].");
    }

    DiameterActivity activity = activities.get(handle);

    if(activity instanceof CreditControlClientSessionImpl) {
      CreditControlClientSessionImpl ccaClientActivity = (CreditControlClientSessionImpl) activity;

      if(ccaClientActivity.getTerminateAfterAnswer()) {
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

    if(this.activities != null) {
      synchronized (this.activities) {
        this.activities.remove(handle);
      }
    }
  }

  public void activityUnreferenced(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter CCA RA :: activityUnreferenced :: handle[" + handle + "].");
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
        else*/ {
        	if(getActivity(handle) instanceof CreditControlServerSessionImpl && event instanceof CreditControlRequest)
        	{
        		((CreditControlServerSessionImpl)getActivity(handle)).fetchCurrentState((CreditControlRequest)event);
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

  public void fireEvent(String sessionId, Message message) {
    DiameterMessage event = (DiameterMessage) createEvent(message);

    FireableEventType eventId = eventIdCache.getEventId(eventLookup, message);

    this.fireEvent(event, getActivityHandle(sessionId), eventId, null, true, message.isRequest());
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
  private void activityCreated(DiameterActivity ac) {
    try {
      // Inform SLEE that Activity Started
      DiameterActivityImpl activity = (DiameterActivityImpl) ac;
      sleeEndpoint.startActivity(activity.getActivityHandle(), activity, ACTIVITY_FLAGS);

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
    final SleeTransactionManager txManager = raContext.getSleeTransactionManager();

    boolean terminateTx = false;

    try {
      txManager.begin();
      terminateTx = true;

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

      terminateTx = false;
      txManager.commit();     
    }
    catch (Throwable e) {
      tracer.severe(e.getMessage(), e);

      if (terminateTx) {
        try {
          txManager.rollback();
        }
        catch (Throwable t) {
          tracer.severe(t.getMessage(), t);
        }
      }
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

  public void sessionCreated(ClientCCASession ccClientSession) {
    // Make sure it's a new session and there's no activity created yet.
    if(this.getActivity(getActivityHandle(ccClientSession.getSessions().get(0).getSessionId())) != null) {
      tracer.warning( "Activity found for created Credit-Control Client Session. Shouldn't exist. Aborting." );
      return;
    }

    // Get Message Factories (for Base and CCA)
    DiameterMessageFactoryImpl baseMsgFactory = new DiameterMessageFactoryImpl(ccClientSession.getSessions().get(0),this.stack);
    CreditControlMessageFactory ccaMsgFactory = new CreditControlMessageFactoryImpl(baseMsgFactory, ccClientSession.getSessions().get(0), this.stack, this.ccaAvpFactory);

    // Create Client Activity
    CreditControlClientSessionImpl activity = new CreditControlClientSessionImpl(ccaMsgFactory, this.ccaAvpFactory, ccClientSession, null, null, this.sleeEndpoint);

    //FIXME: baranowb: add basic session management for base? or do we rely on responses?
    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    activityCreated(activity);
  }

  public void sessionCreated(ServerCCASession ccServerSession) {
    // Make sure it's a new session and there's no activity created yet.
    if(this.getActivity(getActivityHandle(ccServerSession.getSessions().get(0).getSessionId())) != null) {
      tracer.warning( "Activity found for created Credit-Control Server Session. Shouldn't exist. Aborting." );
      return;
    }

    // Get Message Factories (for Base and CCA)
    DiameterMessageFactoryImpl baseMsgFactory = new DiameterMessageFactoryImpl(ccServerSession.getSessions().get(0),this.stack);
    CreditControlMessageFactory ccaMsgFactory = new CreditControlMessageFactoryImpl(baseMsgFactory, ccServerSession.getSessions().get(0), this.stack, this.ccaAvpFactory);

    // Create Server Activity
    CreditControlServerSessionImpl activity = new CreditControlServerSessionImpl(ccaMsgFactory,this.ccaAvpFactory,ccServerSession,null,null,this.sleeEndpoint);

    //FIXME: baranowb: add basic session management for base? or do we rely on responses?
    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    activityCreated(activity);
  }

  public boolean sessionExists(String sessionId) {
    return this.activities.containsKey(new DiameterActivityHandle(sessionId));
  }

  public void sessionDestroyed(String sessionId, Object appSession) {
    try {
      this.sleeEndpoint.endActivity(getActivityHandle(sessionId));
    }
    catch (Exception e) {
      tracer.severe( "Failure Ending Activity with Session-Id[" + sessionId + "]", e );
    }
  }

  // Credit Control Provider Implementation ------------------------------------

  private class CreditControlProviderImpl implements CreditControlProvider {

    protected DiameterCCAResourceAdaptor ra = null;

    public CreditControlProviderImpl(DiameterCCAResourceAdaptor ra) {
      super();
      this.ra = ra;
    }

    public CreditControlClientSession createClientSession() throws CreateActivityException {
      try {
        ClientCCASession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, ApplicationId.createByAuthAppId(CreditControlMessageFactory._CCA_VENDOR, CreditControlMessageFactory._CCA_AUTH_APP_ID), ClientCCASession.class, new Object[]{});

        if (session == null) {
          tracer.severe("Failure creating CCA Server Session (null).");
          return null;
        }

        return  (CreditControlClientSession) getActivity(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (Exception e) {
        throw new CreateActivityException(e);
      }
    }

    //This method should be called only for CCR
    protected DiameterActivity createActivity(Request request) throws CreateActivityException {
      try {
        Set<ApplicationId> appIds = request.getApplicationIdAvps();
        if(appIds == null || appIds.size() == 0) {
          throw new CreateActivityException("No App ids present in message");
        }

        ServerCCASession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(request.getSessionId(), ApplicationId.createByAuthAppId(CreditControlMessageFactory._CCA_VENDOR, CreditControlMessageFactory._CCA_AUTH_APP_ID), ServerCCASession.class, new Object[]{});

        if (session == null) {
          tracer.severe("Failure creating CCA Server Session (null).");
          return null;
        }

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
