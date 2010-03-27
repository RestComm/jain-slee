/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.slee.resource.diameter.base;

import static org.jdiameter.client.impl.helpers.Parameters.MessageTimeOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import javax.slee.transaction.SleeTransactionManager;

import net.java.slee.resource.diameter.base.AccountingClientSessionActivity;
import net.java.slee.resource.diameter.base.AccountingServerSessionActivity;
import net.java.slee.resource.diameter.base.AuthClientSessionActivity;
import net.java.slee.resource.diameter.base.AuthServerSessionActivity;
import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.DiameterProvider;
import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.CapabilitiesExchangeRequest;
import net.java.slee.resource.diameter.base.events.DeviceWatchdogRequest;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.DisconnectPeerRequest;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;

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
import org.jdiameter.api.RawSession;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;
import org.jdiameter.api.acc.ClientAccSession;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.api.auth.ServerAuthSession;
import org.jdiameter.client.api.IContainer;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.client.impl.app.acc.ClientAccSessionImpl;
import org.jdiameter.client.impl.app.auth.ClientAuthSessionImpl;
import org.jdiameter.client.impl.parser.MessageParser;
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.jdiameter.server.impl.app.acc.ServerAccSessionImpl;
import org.jdiameter.server.impl.app.auth.ServerAuthSessionImpl;
import org.mobicents.diameter.stack.DiameterListener;
import org.mobicents.diameter.stack.DiameterStackMultiplexerMBean;
import org.mobicents.slee.resource.diameter.base.events.AbortSessionAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.AbortSessionRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.AccountingAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.AccountingRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.CapabilitiesExchangeAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.CapabilitiesExchangeRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DeviceWatchdogAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DeviceWatchdogRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.DisconnectPeerAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DisconnectPeerRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.ReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ReAuthRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.SessionTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.SessionTerminationRequestImpl;
import org.mobicents.slee.resource.diameter.base.handlers.AccountingSessionFactory;
import org.mobicents.slee.resource.diameter.base.handlers.AuthorizationSessionFactory;
import org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener;

/**
 * Diameter Base Resource Adaptor
 * 
 * <br>
 * Super project: mobicents <br>
 * 1:20:00 AM May 9, 2008 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Erick Svenson
 */
public class DiameterBaseResourceAdaptor implements ResourceAdaptor, DiameterListener, BaseSessionCreationListener {

  private static final long serialVersionUID = 1L;

  // Config Properties Names ---------------------------------------------

  private static final String AUTH_APPLICATION_IDS = "authApplicationIds";

  private static final String ACCT_APPLICATION_IDS = "acctApplicationIds";

  // Config Properties Values --------------------------------------------

  private List<ApplicationId> authApplicationIds;

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

  private DiameterAvpFactory avpFactory = null;

  private DiameterMessageFactory messageFactory = null;

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
  private transient DiameterProviderImpl raProvider = null;

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

  private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;

  public DiameterBaseResourceAdaptor() {
    // TODO: Initialize any default values.
  }

  // Lifecycle methods ---------------------------------------------------

  public void setResourceAdaptorContext(ResourceAdaptorContext context) {
    this.raContext = context;

    this.tracer = context.getTracer("DiameterBaseResourceAdaptor");

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
      tracer.fine("Diameter Base RA :: raActive.");
    }

    try {
      if(tracer.isInfoEnabled()) {
        tracer.info("Activating Diameter Base RA Entity");
      }

      this.diameterMultiplexerObjectName = new ObjectName("diameter.mobicents:service=DiameterStackMultiplexer");

      Object object = MBeanServerLocator.locateJBoss().invoke(this.diameterMultiplexerObjectName, "getMultiplexerMBean", new Object[]{}, new String[]{});

      if(object instanceof DiameterStackMultiplexerMBean) {
        this.diameterMux = (DiameterStackMultiplexerMBean) object;
      }

      this.raProvider = new DiameterProviderImpl(this);

      this.activities = new ConcurrentHashMap<ActivityHandle, DiameterActivity>();

      // Initialize the protocol stack
      initStack();

      // Initialize factories
      this.messageFactory = new DiameterMessageFactoryImpl(stack);
      this.avpFactory = new DiameterAvpFactoryImpl();

      // Setup session factories
      this.sessionFactory = this.stack.getSessionFactory();
      this.accSessionFactory = AccountingSessionFactory.INSTANCE;
      this.accSessionFactory.registerListener(this, messageTimeout, sessionFactory);
      this.authSessionFactory = new AuthorizationSessionFactory(this,messageTimeout,sessionFactory);

      this.proxySessionFactory = new SessionFactory() {

        public <T extends AppSession> T getNewAppSession(ApplicationId applicationId, Class<? extends AppSession> userSession) throws InternalException {
          return (T)sessionFactory.getNewAppSession(applicationId, userSession);
        }

        public <T extends AppSession> T getNewAppSession(String sessionId, ApplicationId applicationId, Class<? extends AppSession> userSession) throws InternalException {
          return (T)sessionFactory.getNewAppSession(sessionId, applicationId, userSession);
        }

        public RawSession getNewRawSession() throws InternalException {
          try {
            return stack.getSessionFactory().getNewRawSession();
          }
          catch ( IllegalDiameterStateException e ) {
            tracer.severe( "Failure while obtaining Session Factory for new Raw Session.", e );
            return null;
          }
        }

        public Session getNewSession() throws InternalException {
          Session session = sessionFactory.getNewSession();
          sessionCreated(session);
          return session;
        }

        public Session getNewSession(String sessionId) throws InternalException {
          Session session = sessionFactory.getNewSession(sessionId);
          sessionCreated(session);
          return session;
        }
      };

      // Register Accounting App Session Factories
      ((ISessionFactory) sessionFactory).registerAppFacory(ServerAccSession.class, accSessionFactory);
      ((ISessionFactory) sessionFactory).registerAppFacory(ClientAccSession.class, accSessionFactory);

      // Register Authorization App Session Factories
      ((ISessionFactory) sessionFactory).registerAppFacory(ServerAuthSession.class, authSessionFactory);
      ((ISessionFactory) sessionFactory).registerAppFacory(ClientAuthSession.class, authSessionFactory);
    }
    catch (Exception e) {
      tracer.severe("Error Activating Diameter Base RA Entity", e);
    }
  }

  public void raStopping() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: raStopping.");
    }

    try{
      diameterMux.unregisterListener(this);
    }
    catch (Exception e) {
      tracer.severe("Failed to unregister Base RA from Diameter Mux.", e);
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
      tracer.info("Diameter Base RA :: STOPPING completed.");
    }
  }

  public void raInactive() {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: raInactive.");
    }

    synchronized (this.activities) {
      activities.clear();
    }
    activities = null;

    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Base RA :: INACTIVE completed.");
    }
  }

  public void raConfigure(ConfigProperties properties) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: raConfigure");
    }

    parseApplicationIds((String) properties.getProperty(AUTH_APPLICATION_IDS).getValue(), true);

    parseApplicationIds((String) properties.getProperty(ACCT_APPLICATION_IDS).getValue(), false);

    if(tracer.isInfoEnabled()) {
      tracer.info("RA supporting " + authApplicationIds + " " + acctApplicationIds);
    }
  }

  private void parseApplicationIds(String appIdsStr, boolean isAuthorization) {
    if(appIdsStr != null) {
      appIdsStr = appIdsStr.replaceAll(" ", "");

      String[] appIdsStrings  = appIdsStr.split(",");

      List<ApplicationId> appIds = new ArrayList<ApplicationId>();

      for(String appId : appIdsStrings) {
        String[] vendorAndAppId = appId.split(":");
        appIds.add(ApplicationId.createByAccAppId(Long.valueOf(vendorAndAppId[0]), Long.valueOf(vendorAndAppId[1]))); 
      }

      if(isAuthorization) {
        authApplicationIds = appIds;
      }
      else {
        acctApplicationIds = appIds;
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
    // TODO Verify configuration!
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
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Base RA :: queryLiveness :: handle[" + handle + "].");
    }

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
      tracer.fine("Diameter Base RA :: getActivity :: handle[" + handle + "].");
    }

    return this.activities.get(handle);
  }

  public ActivityHandle getActivityHandle(Object activity) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: getActivityHandle :: activity[" + activity + "].");
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
      tracer.info("Diameter Base RA :: eventProcessingFailed :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "], reason[" + reason + "].");
    }
  }

  public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Base RA :: eventProcessingSuccessful :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], flags[" + flags + "].");
    }

    DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);
    if(activity.isTerminateAfterProcessing()) {
      this.sleeEndpoint.endActivity(handle);
    }
  }

  public void eventUnreferenced(ActivityHandle handle, FireableEventType eventType, Object event, Address address, ReceivableService service, int flags) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: eventUnreferenced :: handle[" + handle + "], eventType[" + eventType + "], event[" + event + "], address[" + address + "], service[" + service + "], flags[" + flags + "].");
    }
  }

  public void activityEnded(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: activityEnded :: handle[" + handle + ".");
    }

    if(this.activities != null) {
      synchronized (this.activities) {
        this.activities.remove(handle);
      }
    }
  }

  public void activityUnreferenced(ActivityHandle handle) {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: activityUnreferenced :: handle[" + handle + "].");
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

    boolean isRequest = message.isRequest();

    switch (message.getCommandCode()) {
      case AbortSessionRequest.commandCode: // ASR/ASA
        return isRequest ? new AbortSessionRequestImpl(message) : new AbortSessionAnswerImpl(message);
      case AccountingRequest.commandCode: // ACR/ACA
        return isRequest ? new AccountingRequestImpl(message) : new AccountingAnswerImpl(message);
      case CapabilitiesExchangeRequest.commandCode: // CER/CEA
        return isRequest ? new CapabilitiesExchangeRequestImpl(message) : new CapabilitiesExchangeAnswerImpl(message);
      case DeviceWatchdogRequest.commandCode: // DWR/DWA
        return isRequest ? new DeviceWatchdogRequestImpl(message) : new DeviceWatchdogAnswerImpl(message);
      case DisconnectPeerRequest.commandCode: // DPR/DPA
        return isRequest ? new DisconnectPeerRequestImpl(message) : new DisconnectPeerAnswerImpl(message);
      case ReAuthRequest.commandCode: // RAR/RAA
        return isRequest ? new ReAuthRequestImpl(message) : new ReAuthAnswerImpl(message);
      case SessionTerminationRequest.commandCode: // STR/STA
        return isRequest ? new SessionTerminationRequestImpl(message) : new SessionTerminationAnswerImpl(message);
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
      sleeEndpoint.startActivity(activity.getActivityHandle(), activity, ACTIVITY_FLAGS);

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
    List<ApplicationId> allAppIds = new ArrayList<ApplicationId>();
    allAppIds.addAll(authApplicationIds);
    allAppIds.addAll(acctApplicationIds);

    this.diameterMux.registerListener( this, allAppIds.toArray(new ApplicationId[allAppIds.size()]));

    // Get the stack (should not mess with)
    this.stack = this.diameterMux.getStack();
    this.messageTimeout = stack.getMetaData().getConfiguration().getLongValue(MessageTimeOut.ordinal(), (Long) MessageTimeOut.defValue());

    // Obtain parser and store it in AvpUtilities
    MessageParser parser = ((IContainer)stack).getAssemblerFacility().getComponentInstance(MessageParser.class);
    AvpUtilities.setParser(parser);
    
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter Base RA :: Successfully initialized stack.");
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

  // Network Req Listener ------------------------------------------------

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.NetworkReqListener#processRequest(org.jdiameter.api.Request)
   */
  public Answer processRequest(Request request) {
    final SleeTransactionManager txManager = raContext.getSleeTransactionManager();

    boolean terminateTx = false;

    try {
      txManager.begin();
      terminateTx = true;

      DiameterActivityImpl activity = (DiameterActivityImpl) raProvider.createActivity(request);

      // Here we have either created activity or got old one, In cass of app activities, 
      // if we are here it means its initial, or something is wrong - stack is not firing
      // events into correct listener?
      //
      // If its a base - we have to fire manually

      if(activity instanceof AuthServerSessionActivityImpl) {
        AuthServerSessionActivityImpl assai = (AuthServerSessionActivityImpl)activity;
        ((ServerAuthSessionImpl)assai.getSession()).processRequest(request);
      }
      else if(activity instanceof AuthClientSessionActivityImpl) {
        AuthClientSessionActivityImpl assai = (AuthClientSessionActivityImpl)activity;
        ((ClientAuthSessionImpl)assai.getSession()).processRequest(request);
      }
      else if(activity instanceof AccountingServerSessionActivityImpl) {
        AccountingServerSessionActivityImpl assai = (AccountingServerSessionActivityImpl)activity;
        ((ServerAccSessionImpl)assai.getSession()).processRequest(request);
      }
      else if(activity instanceof AccountingClientSessionActivity) {
        AccountingClientSessionActivityImpl assai = (AccountingClientSessionActivityImpl)activity;
        ((ClientAccSessionImpl)assai.getSession()).processRequest(request);
      }
      else if(activity instanceof DiameterActivityImpl) {
        this.fireEvent(activity.getSessionId(), request);
      }
      else {
        //FIXME: Error?
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

    // returning null so we can answer later
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jdiameter.api.EventListener#receivedSuccessMessage(org.jdiameter.api.Message, org.jdiameter.api.Message)
   */
  public void receivedSuccessMessage(Request req, Answer ans) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: receivedSuccessMessage :: " + "Request[" + req + "], Answer[" + ans + "].");
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

  /*
   * (non-Javadoc)
   * 
   * @see org.jdiameter.api.EventListener#timeoutExpired(org.jdiameter.api.Message)
   */
  public void timeoutExpired(Request req) {
    if (tracer.isFineEnabled()) {
      tracer.fine("Diameter Base RA :: timeoutExpired :: " + "Request[" + req + "].");
    }

    // Message delivery timed out - we have to remove activity
    try {
      activities.get(getActivityHandle(req.getSessionId())).endActivity();
    }
    catch (Exception e) {
      tracer.severe("Failure processing timeout message.", e);
    }
  }

  // Base Session Creation Listener --------------------------------------

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.acc.ServerAccSession)
   */
  public void sessionCreated(ServerAccSession session) {
    DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(stack);

    AccountingServerSessionActivityImpl activity = new AccountingServerSessionActivityImpl(msgFactory, avpFactory, session, null, null, sleeEndpoint, stack);

    session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.auth.ServerAuthSession)
   */
  public void sessionCreated(ServerAuthSession session) {
    DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session.getSessions().get(0), stack, new DiameterIdentity[]{});

    AuthServerSessionActivityImpl activity = new AuthServerSessionActivityImpl(msgFactory, avpFactory, session, null, null, sleeEndpoint);

    session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.auth.ClientAuthSession)
   */
  public void sessionCreated(ClientAuthSession session) {
    DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session.getSessions().get(0), stack, new DiameterIdentity[]{});

    AuthClientSessionActivityImpl activity = new AuthClientSessionActivityImpl(msgFactory, avpFactory, session, null, null, sleeEndpoint);

    session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.acc.ClientAccSession)
   */
  public void sessionCreated(ClientAccSession session) {
    DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(stack);

    AccountingClientSessionActivityImpl activity = new AccountingClientSessionActivityImpl(msgFactory, avpFactory, session, null, null, sleeEndpoint);

    activity.setSessionListener(this);
    session.addStateChangeNotification(activity);
    addActivity(activity);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionCreated(org.jdiameter.api.Session)
   */
  public void sessionCreated(Session session) {
    DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session, stack, null, null);

    DiameterActivityImpl activity = new DiameterActivityImpl(msgFactory, avpFactory, session, this, null, null, sleeEndpoint);

    // TODO: Do we need to manage session?
    //session.addStateChangeNotification(activity);
    activity.setSessionListener(this);
    addActivity(activity);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#sessionDestroyed(java.lang.String, java.lang.Object)
   */
  public void sessionDestroyed(String sessionId, Object appSession) {
    try {
      ActivityHandle handle = getActivityHandle(sessionId);
      DiameterActivity activity = (DiameterActivity) this.getActivity(handle);
      if(activity instanceof AccountingClientSessionActivityImpl) {
        AccountingClientSessionActivityImpl accActivity = (AccountingClientSessionActivityImpl) activity;
        accActivity.setTerminateAfterProcessing(true);
      }
      else {
        this.sleeEndpoint.endActivity(handle);
      }
    }
    catch (Exception e) {
      tracer.severe( "Failure Ending Activity with Session-Id[" + sessionId + "]", e );
    }
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
   * @see org.mobicents.slee.resource.diameter.base.handlers.BaseSessionCreationListener#getSupportedApplications()
   */
  public ApplicationId[] getSupportedApplications() {

    List<ApplicationId> appIds = new ArrayList<ApplicationId>();
    appIds.addAll(authApplicationIds);
    appIds.addAll(acctApplicationIds);

    return appIds.toArray(new ApplicationId[appIds.size()]);
  }

  // Diameter Provider Implementation ------------------------------------

  private class DiameterProviderImpl implements DiameterProvider {

    protected final Tracer tracer = getRaContext().getTracer("DiameterProvider");;

    protected DiameterBaseResourceAdaptor ra;

    /**
     * Constructor.
     * 
     * @param ra The resource adaptor for this Provider.
     */
    public DiameterProviderImpl(DiameterBaseResourceAdaptor ra) {
      this.ra = ra;
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#createActivity()
     */
    public DiameterActivity createActivity() throws CreateActivityException {
      if(tracer.isFineEnabled()) {
        tracer.fine("Diameter Base RA :: createActivity");
      }

      return this.createActivity(null, null);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#createActivity(net.java.slee.resource.diameter.base.events.avp.DiameterIdentity, net.java.slee.resource.diameter.base.events.avp.DiameterIdentity)
     */
    public DiameterActivity createActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException {
      if(tracer.isFineEnabled()) {
        tracer.fine("Diameter Base RA :: createActivity :: destinationHost[" + destinationHost + "], destinationRealm[" + destinationRealm + "]");
      }

      return createActivity(destinationHost, destinationRealm, null);
    }

    // Authentication Activities -----------------------------------------

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#createAuthenticationActivity()
     */
    public AuthClientSessionActivity createAuthenticationClientSessionActivity() throws CreateActivityException {
      return this.createAuthenticationClientSessionActivity(null, null);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#createAuthenticationActivity(net.java.slee.resource.diameter.base.events.avp.DiameterIdentity, net.java.slee.resource.diameter.base.events.avp.DiameterIdentity)
     */
    public AuthClientSessionActivity createAuthenticationClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException {
      try {
        ClientAuthSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, authApplicationIds.get(0), ClientAuthSession.class);

        return (AuthClientSessionActivity) activities.get(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (InternalException e) {
        throw new CreateActivityException("Internal exception while creating Authentication Activity", e);
      }
      catch (IllegalDiameterStateException e) {
        throw new CreateActivityException("Illegal Diameter State exception while creating Authentication Activity", e);
      }
    }

    public AuthServerSessionActivity createAuthenticationServerActivity(Request request) throws CreateActivityException {
      ServerAuthSession session = null;

      try {
        session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, authApplicationIds.get(0), ServerAuthSession.class, new Object[]{request});

        return (AuthServerSessionActivity) activities.get(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (InternalException e) {
        throw new CreateActivityException("Internal exception while creating Server Authentication Activity", e);
      }
      catch (IllegalDiameterStateException e) {
        throw new CreateActivityException("Illegal Diameter State exception while creating Server Authentication Activity", e);
      }
    }

    // Accounting Activities ---------------------------------------------

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#createAccountingActivity()
     */
    public AccountingClientSessionActivity createAccountingClientSessionActivity() throws CreateActivityException {
      return this.createAccountingClientSessionActivity(null, null);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#createAccountingActivity(net.java.slee.resource.diameter.base.events.avp.DiameterIdentity, net.java.slee.resource.diameter.base.events.avp.DiameterIdentity)
     */
    public AccountingClientSessionActivity createAccountingClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException {
      try {
        // FIXME: alexandre: This must be fixed, we need way to specify Application-Id!
        ClientAccSession session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, acctApplicationIds.get(0), ClientAccSession.class);

        return (AccountingClientSessionActivity) activities.get(getActivityHandle(session.getSessions().get(0).getSessionId()));
      }
      catch (InternalException e) {
        throw new CreateActivityException("Internal exception while creating Client Accounting Activity", e);
      }
      catch (IllegalDiameterStateException e) {
        throw new CreateActivityException("Illegal Diameter State exception while creating Client Accounting Activity", e);
      }
    }

    public AccountingServerSessionActivity createAccountingServerActivity(Request req) throws CreateActivityException {
      ServerAccSession session = null;

      try {
        ApplicationId appId = req.getApplicationIdAvps().isEmpty() ? null : req.getApplicationIdAvps().iterator().next(); 
        session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(req.getSessionId(), appId, ServerAccSession.class, req);

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

      return (AccountingServerSessionActivity) activities.get(getActivityHandle(session.getSessions().get(0).getSessionId()));
    }

    /**
     * 
     * @param destinationHost
     * @param destinationRealm
     * @param sessionId
     * @return
     * @throws CreateActivityException
     */
    public DiameterActivity createActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm, String sessionId) throws CreateActivityException {
      try {
        Session session = sessionId != null ? proxySessionFactory.getNewSession(sessionId) : proxySessionFactory.getNewSession();

        return activities.get(getActivityHandle(session.getSessionId()));
      }
      catch (InternalException e) {
        throw new CreateActivityException("Failure obtaining session for creating activity.", e);
      }
    }

    /**
     * This method is for internal use only, it creates activities for
     * requests that do not fall in certain app range or no activitis were
     * found <br>
     * It should distinguish between initial requests, requests with
     * diferent domains etc. - respo for createing XXXServerSession or basic
     * diameter activity lies in this method
     * 
     * @param message
     * @return
     */
    DiameterActivity createActivity(Message message) throws CreateActivityException {
      String sessionId = message.getSessionId();
      DiameterActivityHandle handle = getActivityHandle(sessionId);

      if (activities.keySet().contains(handle)) {
        return activities.get(handle);
      }
      else {
        DiameterIdentity destinationHost = null;
        DiameterIdentity destinationRealm = null;

        AvpSet avps = message.getAvps();

        Avp raw = null;

        if((raw = avps.getAvp(Avp.DESTINATION_HOST)) != null) {
          try {
            destinationHost = new DiameterIdentity(raw.getDiameterIdentity());
          }
          catch (AvpDataException e) {
            tracer.severe("Unable to obtain Destination Host from Diameter Message.", e);
          }
        }

        if((raw = avps.getAvp(Avp.DESTINATION_REALM)) != null) {
          try {
            destinationRealm = new DiameterIdentity(raw.getDiameterIdentity());
          }
          catch (AvpDataException e) {
            tracer.severe("Unable to obtain Destination Realm from Diameter Message.", e);
          }
        }

        if (isMessageOfType(message, DiameterAvpCodes.AUTH_APPLICATION_ID)) {
          return createAuthenticationServerActivity((Request) message);
        }
        else if (isMessageOfType(message, DiameterAvpCodes.ACCT_APPLICATION_ID)) {
          return createAccountingServerActivity((Request) message);
        }
        else { // Base Activity
          return this.createActivity(destinationHost, destinationRealm, message.getSessionId());
        }
      }
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#getDiameterMessageFactory()
     */
    public DiameterMessageFactory getDiameterMessageFactory() {
      if (this.ra.messageFactory == null) {
        this.ra.messageFactory = new DiameterMessageFactoryImpl(this.ra.stack);
      }

      return this.ra.messageFactory;
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#getDiameterAvpFactory()
     */
    public DiameterAvpFactory getDiameterAvpFactory() {
      if (this.ra.avpFactory == null) {
        this.ra.avpFactory = new DiameterAvpFactoryImpl();
      }

      return this.ra.avpFactory;
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#sendSyncRequest(net.java.slee.resource.diameter.base.events.DiameterMessage)
     */
    public DiameterMessage sendSyncRequest(DiameterMessage message) throws IOException {
      try {
        if (message instanceof DiameterMessageImpl) {
          DiameterMessageImpl msg = (DiameterMessageImpl) message;

          String sessionId = message.getSessionId();
          DiameterActivityHandle handle = getActivityHandle(sessionId);

          if (!activities.keySet().contains(handle)) {
            createActivity(msg.getGenericData());
          }

          DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);

          return activity.sendSyncMessage(message);
        }
      }
      catch (JAvpNotAllowedException e) {
        throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
      }
      catch (Exception e) {
        throw new IOException("Failed to send message, due to: " + e.getMessage());
      }

      return null;
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#getConnectedPeers()
     */
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

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.base.DiameterProvider#getPeerCount()
     */
    public int getPeerCount() {
      return getConnectedPeers().length;
    }

    /**
     * Identifies if a certain message is of Acct or Auth type.
     * 
     * @param message
     * @param type
     * @return
     */
    private boolean isMessageOfType(Message message, int type) {
      try {
        Avp vendorSpecificAvp = null;  

        if(message.getAvps().getAvp(type) != null) {
          return true;
        }
        else if((vendorSpecificAvp = message.getAvps().getAvp(DiameterAvpCodes.VENDOR_SPECIFIC_APPLICATION_ID)) != null) {
          // 6.11.  Vendor-Specific-Application-Id AVP
          // The Vendor-Specific-Application-Id AVP (AVP Code 260) is of type Grouped and is used to 
          // advertise support of a vendor-specific Diameter Application.
          // Exactly one of the Auth-Application-Id and Acct-Application-Id AVPs MAY be present.
          return vendorSpecificAvp.getGrouped().getAvp(type) != null;
        }
      }
      catch (Exception ignore) {
        // ignore this... say it isn't
      }

      return false;
    }
  }

}