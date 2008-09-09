package org.mobicents.slee.resource.diameter.base;

import static org.jdiameter.client.impl.helpers.Parameters.MessageTimeOut;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.slee.Address;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.AccountingClientSessionActivity;
import net.java.slee.resource.diameter.base.AccountingServerSessionActivity;
import net.java.slee.resource.diameter.base.AuthClientSessionActivity;
import net.java.slee.resource.diameter.base.AuthServerSessionActivity;
import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterActivityContextInterfaceFactory;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.DiameterProvider;
import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.CapabilitiesExchangeAnswer;
import net.java.slee.resource.diameter.base.events.DeviceWatchdogAnswer;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.DisconnectPeerAnswer;
import net.java.slee.resource.diameter.base.events.ErrorAnswer;
import net.java.slee.resource.diameter.base.events.ExtensionDiameterMessage;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpDataException;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.Configuration;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Network;
import org.jdiameter.api.NetworkReqListener;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.Peer;
import org.jdiameter.api.PeerTable;
import org.jdiameter.api.Request;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.Session;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.Stack;
import org.jdiameter.api.acc.ClientAccSession;
import org.jdiameter.api.acc.ClientAccSessionListener;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.api.acc.ServerAccSessionListener;
import org.jdiameter.api.acc.events.AccountAnswer;
import org.jdiameter.api.acc.events.AccountRequest;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.api.auth.ClientAuthSessionListener;
import org.jdiameter.api.auth.ServerAuthSession;
import org.jdiameter.api.auth.ServerAuthSessionListener;
import org.jdiameter.api.auth.events.AbortSessionRequest;
import org.jdiameter.api.auth.events.ReAuthRequest;
import org.jdiameter.api.auth.events.SessionTermAnswer;
import org.jdiameter.api.auth.events.SessionTermRequest;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.client.impl.app.acc.ClientAccSessionImpl;
import org.jdiameter.client.impl.app.auth.ClientAuthSessionImpl;
import org.jdiameter.common.api.app.IAppSessionFactory;
import org.jdiameter.common.api.app.auth.IAuthMessageFactory;
import org.jdiameter.common.impl.app.AppAnswerEventImpl;
import org.jdiameter.common.impl.app.AppRequestEventImpl;
import org.jdiameter.server.impl.StackImpl;
import org.jdiameter.server.impl.app.acc.ServerAccSessionImpl;
import org.jdiameter.server.impl.app.auth.ServerAuthSessionImpl;
import org.jdiameter.server.impl.helpers.XMLConfiguration;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;
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
import org.mobicents.slee.resource.diameter.base.events.avp.DiameterIdentityAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.util.AvpDictionary;

/**
 * Diameter Resource Adaptor
 * 
 * <br>
 * Super project: mobicents <br>
 * 1:20:00 AM May 9, 2008 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Erick Svenson
 */
public class DiameterBaseResourceAdaptor implements ResourceAdaptor, NetworkReqListener, Serializable, EventListener<Request, Answer> {
  
	private static final long serialVersionUID = 1L;

	private static transient Logger logger = Logger.getLogger(DiameterBaseResourceAdaptor.class);

	private ResourceAdaptorState state;

	private Stack stack;
	private SessionFactory sessionFactory = null;
	private long messageTimeout = 5000;
	private DiameterAvpFactoryImpl diameterAvpFactory = new DiameterAvpFactoryImpl();
	/**
	 * The BootstrapContext provides the resource adaptor with the required
	 * capabilities in the SLEE to execute its work. The bootstrap context is
	 * implemented by the SLEE. The BootstrapContext object holds references to
	 * a number of objects that are of interest to many resource adaptors. For
	 * further information see JSLEE v1.1 Specification Page 305. The
	 * bootstrapContext will be set in entityCreated() method.
	 */
	private transient BootstrapContext bootstrapContext = null;

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
	 * The activity context interface factory defined in
	 * DiameterRAActivityContextInterfaceFactoryImpl
	 */
	private transient DiameterActivityContextInterfaceFactory acif = null;

	/**
	 * A link to the DiameterProvider which then will be exposed to Sbbs
	 */
	private transient DiameterProviderImpl raProvider = null;

	private static final Map<Integer, String> events;
	private static HashSet<Integer> accEventCodes = new HashSet<Integer>();
	private static HashSet<Integer> authEventCodes = new HashSet<Integer>();
	
	static
	{
		Map<Integer, String> eventsTemp = new HashMap<Integer, String>();

		eventsTemp.put(AbortSessionAnswer.commandCode, "AbortSession");
		eventsTemp.put(AccountingAnswer.commandCode, "Accounting");
		eventsTemp.put(CapabilitiesExchangeAnswer.commandCode, "CapabilitiesExchange");
		eventsTemp.put(DeviceWatchdogAnswer.commandCode, "DeviceWatchdog");
		eventsTemp.put(DisconnectPeerAnswer.commandCode, "DisconnectPeer");
		eventsTemp.put(ReAuthAnswer.commandCode, "ReAuth");
		eventsTemp.put(SessionTerminationAnswer.commandCode, "SessionTermination");
		eventsTemp.put(ErrorAnswer.commandCode, "Error");
		
		// FIXME: baranowb - make sure its compilant with xml
		eventsTemp.put(ExtensionDiameterMessage.commandCode, "ExtensionDiameter");
		
		events = Collections.unmodifiableMap(eventsTemp);
		
		authEventCodes.add(AbortSessionAnswer.commandCode);
		authEventCodes.add(ReAuthAnswer.commandCode);
		authEventCodes.add(SessionTerminationAnswer.commandCode);
		
		accEventCodes.add(AccountingAnswer.commandCode);
	}

	public DiameterBaseResourceAdaptor()
	{
		logger.info("Diameter Base RA :: DiameterBaseResourceAdaptor.");
		
    try
    {
      logger.info( "Parsing AVP Dictionary file..." );
      AvpDictionary.INSTANCE.parseDictionary( this.getClass().getResourceAsStream("dictionary.xml") );
      logger.info( "AVP Dictionary file successfuly parsed!" );
    }
    catch ( Exception e )
    {
      logger.error( "Error while parsing dictionary file.", e );
    }
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the SLEE
	 * has completed activity end processing for the activity represented by the
	 * activity handle. The resource adaptor should release any resource related
	 * to this activity as the SLEE will not ask for it again.
	 */
	public void activityEnded(ActivityHandle handle)
	{
		logger.info("Diameter Base RA :: activityEnded :: handle[" + handle + ".");
		
		if(this.activities != null)
		{
  		synchronized (this.activities)
  		{
  			this.activities.remove(handle);
  		}
		}
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that the
	 * activity’s Activity Context object is no longer attached to any SBB
	 * entities and is no longer referenced by any SLEE Facilities. This enables
	 * the resource adaptor to implicitly end the Activity object.
	 */
	public void activityUnreferenced(ActivityHandle handle)
	{
		logger.info("Diameter Base RA :: activityUnreferenced :: handle[" + handle + "].");

		this.activityEnded(handle);
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
	 * Specification does not include entityActivated(). However, the API
	 * description of JSLEE v1.1 does include this method already. So, the
	 * documentation follows the code. <br>
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor activation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.activate() calls this
	 * method entityActivated(). This method signals the resource adaptor the
	 * transition from state "INACTIVE" to state "ACTIVE".
	 */
	public void entityActivated() throws ResourceException
	{
		logger.info("Diameter Base RA :: entityActivated.");

		try
		{
			logger.info("Activating Diameter Base RA Entity");

			this.raProvider = new DiameterProviderImpl(this);

			initializeNamingContext();

			this.activities = new ConcurrentHashMap();

			this.state = ResourceAdaptorState.CONFIGURED;
		}
		catch (Exception e)
		{
			logger.error("Error Configuring Diameter Base RA Entity", e);
		}

		try
		{
			// Initialize the protocol stack
			initStack();

			// Resource Adaptor ready to rumble!
			this.state = ResourceAdaptorState.ACTIVE;
			this.sessionFactory = this.stack.getSessionFactory();

			// Register Accounting App Session Factories
			((ISessionFactory) sessionFactory).registerAppFacory(
					ServerAccSession.class, new AccountingSessionFactory(this));
			((ISessionFactory) sessionFactory).registerAppFacory(
					ClientAccSession.class, new AccountingSessionFactory(this));

			// Register Authorization App Session Factories
			((ISessionFactory) sessionFactory).registerAppFacory(
					ServerAuthSession.class, new AuthorizationSessionFactory(
							this));
			((ISessionFactory) sessionFactory).registerAppFacory(
					ClientAuthSession.class, new AuthorizationSessionFactory(
							this));
		}
		catch (Exception e)
		{
			logger.error("Error Activating Diameter Base RA Entity", e);
		}
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 298 for further information. <br>
	 * This method is called by the SLEE when a resource adaptor object instance
	 * is bootstrapped, either when a resource adaptor entity is created or
	 * during SLEE startup. The SLEE implementation will construct the resource
	 * adaptor object and then invoke the entityCreated method before any other
	 * operations can be invoked on the resource adaptor object.
	 */
	public void entityCreated(BootstrapContext bootstrapContext) throws ResourceException
	{
		logger.info("Diameter Base RA :: entityCreated :: bootstrapContext[" + bootstrapContext + "].");

		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();

		this.state = ResourceAdaptorState.UNCONFIGURED;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor The JSLEE v1.1
	 * Specification does not include entityDeactivated(). However, the API
	 * description of JSLEE v1.1 does include this method already. So, the
	 * documentation follows the code. <br>
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor deactivation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.deactivate() calls this
	 * method entityDeactivated(). The method call is done AFTER the call to
	 * entityDeactivating(). This method signals the resource adaptor the
	 * transition from state "STOPPING" to state "INACTIVE".
	 */
	public void entityDeactivated()
	{
		logger.info("Diameter Base RA :: entityDeactivated.");

		logger.info("Diameter Base RA :: Cleaning RA Activities.");

		synchronized (this.activities)
		{
			activities.clear();
		}
		activities = null;

		logger.info("Diameter Base RA :: Cleaning naming context.");

		try
		{
			cleanNamingContext();
		}
		catch (NamingException e)
		{
			logger.error("Diameter Base RA :: Cannot unbind naming context.");
		}

		// Stop the stack
		try
		{
			stack.stop(5, TimeUnit.SECONDS);
		}
		catch (Exception e)
		{
			logger.error("Diameter Base RA :: Failure while stopping ");
		}

		logger.info("Diameter Base RA :: RA Stopped.");
	}

	/**
	 * This method is called in context of project Mobicents in context of
	 * resource adaptor deactivation. More precisely,
	 * org.mobicents.slee.resource.ResourceAdaptorEntity.deactivate() calls this
	 * method entityDeactivating() PRIOR to invoking entityDeactivated(). This
	 * method signals the resource adaptor the transition from state "ACTIVE" to
	 * state "STOPPING".
	 */
	public void entityDeactivating()
	{
		logger.info("Diameter Base RA :: entityDeactivating.");
		
		this.state = ResourceAdaptorState.STOPPING;
		
		try 
		{
			Network network = stack.unwrap(Network.class);

			Iterator<ApplicationId> appIdsIt = stack.getMetaData().getLocalPeer().getCommonApplications().iterator();
			
			while(appIdsIt.hasNext())
			{
				network.removeNetworkReqListener(appIdsIt.next());
				
				// Update the iterator (avoid ConcurrentModificationException)
				appIdsIt = stack.getMetaData().getLocalPeer().getCommonApplications().iterator();
			}
		}
		catch (InternalException e) 
		{
			logger.error("", e);
		}

		synchronized (this.activities)
		{
			for (ActivityHandle activityHandle : activities.keySet())
			{
				try
				{
					logger.info("Ending activity [" + activityHandle + "]");

					activities.get(activityHandle).endActivity();

				}
				catch (Exception e)
				{
					logger.error("Error Deactivating Activity", e);
				}
			}

		}
		
		logger.info("Diameter Base RA :: entityDeactivating completed.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 299 for further information. <br>
	 * This method is called by the SLEE when a resource adaptor object instance
	 * is being removed, either when a resource adaptor entity is deleted or
	 * during SLEE shutdown. When receiving this invocation the resource adaptor
	 * object is expected to close any system resources it has allocated.
	 */
	public void entityRemoved()
	{
		// Stop the stack
		this.stack.destroy();

		// Clean up!
		this.acif = null;
		this.activities = null;
		this.bootstrapContext = null;
		this.eventLookup = null;
		this.raProvider = null;
		this.sleeEndpoint = null;
		this.stack = null;

		logger.info("Diameter Base RA :: entityRemoved.");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 300 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor object that the
	 * specified event was processed unsuccessfully by the SLEE. Event
	 * processing can fail if, for example, the SLEE doesn’t have enough
	 * resource to process the event, a SLEE node fails during event processing
	 * or a system level failure prevents the SLEE from committing transactions.
	 */
	public void eventProcessingFailed(ActivityHandle handle, Object event, int eventID, Address address, int flags, FailureReason reason)
	{
		logger.info("Diameter Base RA :: eventProcessingFailed :: handle[" + handle + "], event[" + event + "], eventID[" + eventID + "], address[" + address + "], flags[" + flags + 
		    "], reason[" + reason + "].");
		

	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 300 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor object that the
	 * specified event was processed successfully by the SLEE. An event is
	 * considered to be processed successfully if the SLEE has attempted to
	 * deliver the event to all interested SBBs.
	 */
	public void eventProcessingSuccessful(ActivityHandle handle, Object event, int eventID, Address address, int flags)
	{
		logger.info("Diameter Base RA :: eventProcessingSuccessful :: handle[" + handle + "], event[" + event + "], eventID[" + eventID + "], address[" + address + "], flags[" + 
		    flags + "].");


	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to get access to the underlying activity for
	 * an activity handle. The resource adaptor is expected to pass back a
	 * non-null object.
	 */
	public Object getActivity(ActivityHandle handle)
	{
		logger.info("Diameter Base RA :: getActivity :: handle[" + handle + "].");

		return this.activities.get(handle);
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to get an activity handle for an activity
	 * created by the underlying resource. This method is invoked by the SLEE
	 * when it needs to construct an activity context for an activity via an
	 * activity context interface factory method invoked by an SBB.
	 */
	public ActivityHandle getActivityHandle(Object activity)
	{
		logger.info("Diameter Base RA :: getActivityHandle :: activity[" + activity + "].");

		if (!(activity instanceof DiameterActivity))
			return null;

		DiameterActivity inActivity = (DiameterActivity) activity;
		
		for (Map.Entry<ActivityHandle, DiameterActivity> activityInfo : this.activities.entrySet())
		{
			Object curActivity = activityInfo.getValue();
			
			if (curActivity.equals(inActivity))
				return activityInfo.getKey();
		}

		return null;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 302 for further information. <br>
	 * The SLEE calls this method to get reference to the Marshaler object. The
	 * resource adaptor implements the Marshaler interface. The Marshaler is
	 * used by the SLEE to convert between object and distributable forms of
	 * events and event handles.
	 */
	public Marshaler getMarshaler()
	{
		logger.info("Diameter Base RA :: getMarshaler");

		return null;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 302 for further information. <br>
	 * The SLEE calls this method to get access to the underlying resource
	 * adaptor interface that enables the SBB to invoke the resource adaptor, to
	 * send messages for example.
	 */
	public Object getSBBResourceAdaptorInterface(String className)
	{
		logger.info("Diameter Base RA :: getSBBResourceAdaptorInterface :: className[" + className + "].");

		return this.raProvider;
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 301 for further information. <br>
	 * The SLEE calls this method to query if a specific activity belonging to
	 * this resource adaptor object is alive.
	 */
	public void queryLiveness(ActivityHandle handle)
	{
		logger.info("Diameter Base RA :: queryLiveness :: handle[" + handle + "].");

		DiameterActivityImpl activity = (DiameterActivityImpl) activities.get(handle);

		if (activity != null && !activity.isValid())
		{
			try
			{
				sleeEndpoint.activityEnding(handle);
			}
			catch (Exception e)
			{
				logger.error("", e);
			}
		}
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 303 for further information. <br>
	 * The SLEE calls this method to inform the resource adaptor that a service
	 * has been activated and is interested in the event types associated to the
	 * service key. The service must be installed with the resource adaptor via
	 * the serviceInstalled method before it can be activated.
	 */
	public void serviceActivated(String serviceKey)
	{
	  logger.info("Diameter Base RA :: serviceActivated :: serviceKey[" + serviceKey + "].");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 304 for further information. <br>
	 * The SLEE calls this method to inform the SLEE that a service has been
	 * deactivated and is no longer interested in the event types associated to
	 * the service key.
	 */
	public void serviceDeactivated(String serviceKey)
	{
		logger.info("Diameter Base RA :: serviceDeactivated :: serviceKey[" + serviceKey + "].");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 302 for further information. <br>
	 * The SLEE calls this method to signify to the resource adaptor that a
	 * service has been installed and is interested in a specific set of events.
	 * The SLEE passes an event filter which identifies a set of event types
	 * that services in the SLEE are interested in. The SLEE calls this method
	 * once a service is installed.
	 */
	public void serviceInstalled(String serviceKey, int[] eventIDs, String[] resourceOptions)
	{
		logger.info("Diameter Base RA :: serviceInstalled :: serviceKey[" + serviceKey + "], eventIDs[" + eventIDs + "], resourceOptions[" + resourceOptions + "].");
	}

	/**
	 * implements javax.slee.resource.ResourceAdaptor Please refer to JSLEE v1.1
	 * Specification Page 303 for further information. <br>
	 * The SLEE calls this method to signify that a service has been
	 * un-installed in the SLEE. The event types associated to the service key
	 * are no longer of interest to a particular application.
	 */
	public void serviceUninstalled(String serviceKey)
	{
		logger.info("Diameter Base RA :: serviceUninstalled :: serviceKey[" + serviceKey + "].");
	}

	/**
	 * Set up the JNDI naming context
	 */
	private void initializeNamingContext() throws NamingException
	{
		// get the reference to the SLEE container from JNDI
		SleeContainer container = SleeContainer.lookupFromJndi();

		// get the entities name
		String entityName = bootstrapContext.getEntityName();

		ResourceAdaptorEntity resourceAdaptorEntity;

		try
		{
			resourceAdaptorEntity = ((ResourceAdaptorEntity) container.getResourceAdaptorEntity(entityName));
		}
		catch (UnrecognizedResourceAdaptorEntityException uraee)
		{
			throw new NamingException("Failure setting up Naming Context. RA Entity not found.");
		}

		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity.getInstalledResourceAdaptor().getRaType().getResourceAdaptorTypeID();

		// create the ActivityContextInterfaceFactory
		acif = new DiameterActivityContextInterfaceFactoryImpl(resourceAdaptorEntity.getServiceContainer(), entityName);

		// set the ActivityContextInterfaceFactory
		resourceAdaptorEntity.getServiceContainer().getActivityContextInterfaceFactories().put(raTypeId, acif);

		try
		{
			if (this.acif != null)
			{
				// parse the string = java:slee/resources/RAFrameRA/raframeacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) acif).getJndiName();
				
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);

				logger.info("Diameter Base RA :: Registering in JNDI :: Prefix[" + prefix + "], Name[" + name + "].");

				SleeContainer.registerWithJndi(prefix, name, this.acif);
				
				logger.info("Diameter Base RA :: Registered in JNDI successfully.");
			}
		}
		catch (IndexOutOfBoundsException iobe)
		{
			logger.info("Failure initializing name context.", iobe);
		}
	}

	/**
	 * Clean the JNDI naming context
	 */
	private void cleanNamingContext() throws NamingException
	{
		try
		{
			if (this.acif != null)
			{
				// parse the string = java:slee/resources/RAFrameRA/raframeacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif).getJndiName();

				// remove "java:" prefix
				int begind = jndiName.indexOf(':');
				String javaJNDIName = jndiName.substring(begind + 1);

				logger.info("Diameter Base RA :: Unregistering from JNDI :: Name[" + javaJNDIName + "].");

				SleeContainer.unregisterWithJndi(javaJNDIName);
				
				logger.info("Diameter Base RA :: Unregistered from JNDI successfully.");
			}
		}
		catch (IndexOutOfBoundsException iobe)
		{
			logger.error("Failure cleaning name context.", iobe);
		}
	}

	/**
	 * Initializes the RA Diameter Stack.
	 * 
	 * @throws Exception
	 */
	private synchronized void initStack() throws Exception
	{
		InputStream is = null;

		try
		{
			// Create and configure stack
			this.stack = new StackImpl();

			// Get configuration
			String configFile = "jdiameter-config.xml";
			is = this.getClass().getResourceAsStream(configFile);

			// Load the configuration
			Configuration config = new XMLConfiguration(is);

			this.stack.init(config);

			Network network = stack.unwrap(Network.class);

			Set<ApplicationId> appIds = stack.getMetaData().getLocalPeer().getCommonApplications();

			// FIXME: This should come from config.. adding manually
			// <ApplicationID>
			// <VendorId value="193"/>
			// <AuthApplId value="0"/>
			// <AcctApplId value="19302"/>
			// </ApplicationID>
			appIds.add(ApplicationId.createByAccAppId(193L, 19302L));

			// <ApplicationID>
			// <VendorId value="193"/>
			// <AuthApplId value="19301"/>
			// <AcctApplId value="0"/>
			// </ApplicationID>
			appIds.add(ApplicationId.createByAuthAppId(193L, 19301L));

			logger.info("Diameter Base RA :: Supporting " + appIds.size() + " applications.");

			for (ApplicationId appId : appIds)
			{
				logger.info("Diameter Base RA :: Adding Listener for [" + appId + "].");
				network.addNetworkReqListener(this, appId);
			}

			// Set message timeout accordingly to stack definition
			this.messageTimeout = stack.getMetaData().getConfiguration().getLongValue(MessageTimeOut.ordinal(), (Long) MessageTimeOut.defValue());
			
			this.stack.start();
		}
		finally
		{
			if (is != null)
				is.close();

			is = null;
		}

		logger.info("Diameter Base RA :: Successfully initialized stack.");
	}

	/**
	 * RA Entry Point
	 */
	public Answer processRequest(Request request)
	{
		DiameterActivity activity;

		try
		{
			activity = raProvider.createActivity(request);

			// Will it be handled by specific app handler?
			//FIXME: baranowb: add acc/auth app id check - for instance STR will be handled here wrong?
			if( !authEventCodes.contains(request.getCommandCode()) && !accEventCodes.contains(request.getCommandCode()) )
			{
				activityCreated(activity);
				DiameterActivityHandle handle = createActivityHandle(activity);

				fireEvent(handle, events.get(request.getCommandCode()) + "Request", request, null);
			}
		}
		catch (CreateActivityException e)
		{
			logger.error("", e);
		}

		// returning null so we can answer later
		return null;
	}

	/**
	 * Create the Diameter Activity Handle for an activity
	 * 
	 * @param request
	 *            the Request object to create the activity handle from
	 * @return a DiameterActivityHandle for the provided request
	 */
	private DiameterActivityHandle createActivityHandle(DiameterActivity di)
	{
		return ((DiameterActivityImpl) di).getActivityHandle();
	}

	/**
	 * Method for performing tasks when activity is created, such as informing
	 * SLEE about it and storing into internal map.
	 * 
	 * @param ac
	 *            the activity that has been created
	 */
	private void activityCreated(DiameterActivity ac)
	{
		try
		{
			// Inform SLEE that Activity Started
			DiameterActivityImpl activity = (DiameterActivityImpl) ac;
			sleeEndpoint.activityStarted(activity.getActivityHandle());

			// Put it into our activites map
			activities.put(activity.getActivityHandle(), activity);

			logger.info("Activity started [" + activity.getActivityHandle() + "]");
		}
		catch (Exception e)
		{
			logger.error("Error creating activity", e);
			
			throw new RuntimeException("Error creating activity", e);
		}
	}

	/**
	 * Create Event object from request/answer
	 * 
	 * @param request
	 *            the request to create the event from, if any.
	 * @param answer
	 *            the answer to create the event from, if any.
	 * @return a DiameterMessage object wrapping the request/answer
	 * @throws OperationNotSupportedException
	 */
	public DiameterMessage createEvent(Request request, Answer answer) throws OperationNotSupportedException
	{
		if (request == null && answer == null)
			return null;

		int commandCode = (request != null ? request.getCommandCode() : answer.getCommandCode());

		switch (commandCode)
		{
		case AbortSessionAnswer.commandCode: // ASR/ASA
			return request != null ? new AbortSessionRequestImpl(request) : new AbortSessionAnswerImpl(answer);
		case AccountingAnswer.commandCode: // ACR/ACA
			return request != null ? new AccountingRequestImpl(request) : new AccountingAnswerImpl(answer);
		case CapabilitiesExchangeAnswer.commandCode: // CER/CEA
			return request != null ? new CapabilitiesExchangeRequestImpl(request) : new CapabilitiesExchangeAnswerImpl(answer);
		case DeviceWatchdogAnswer.commandCode: // DWR/DWA
			return request != null ? new DeviceWatchdogRequestImpl(request) : new DeviceWatchdogAnswerImpl(answer);
		case DisconnectPeerAnswer.commandCode: // DPR/DPA
			return request != null ? new DisconnectPeerRequestImpl(request) : new DisconnectPeerAnswerImpl(answer);
		case ReAuthAnswer.commandCode: // RAR/RAA
			return request != null ? new ReAuthRequestImpl(request) : new ReAuthAnswerImpl(answer);
		case SessionTerminationAnswer.commandCode: // STR/STA
			return request != null ? new SessionTerminationRequestImpl(request) : new SessionTerminationAnswerImpl(answer);
		case ErrorAnswer.commandCode:
			if (answer != null)
			{
				return new ErrorAnswerImpl(answer);
			}
			else
			{
				throw new IllegalArgumentException("ErrorAnswer code set on request: " + request);
			}
		case ExtensionDiameterMessage.commandCode:
			// FIXME: baranowb - is this correct ?
			return new ExtensionDiameterMessageImpl(request != null ? request : answer);

		default:
			throw new OperationNotSupportedException("Not supported message code:" + commandCode + "\n" + (request != null ? request : answer));
		}
	}

	private void fireEvent(Request request, Answer answer)
	{
		String sessionId = (request != null ? request.getSessionId() : answer.getSessionId());
		
		int commandCode = (request != null ? request.getCommandCode() : answer.getCommandCode());
		String postfix = (request != null ? "Request" : "Answer");

		this.fireEvent(new DiameterActivityHandle(sessionId), events.get(commandCode) + postfix, request, answer);
	}

	/**
	 * Method for firing event to SLEE
	 * 
	 * @param handle
	 *            the handle for the activity where event will be fired on
	 * @param name
	 *            the unqualified Event name
	 * @param request
	 *            the request that will be wrapped in the event, if any
	 * @param answer
	 *            the answer that will be wrapped in the event, if any
	 */
	private void fireEvent(ActivityHandle handle, String name, Request request, Answer answer)
	{
		try
		{
			int eventID = eventLookup.getEventID("net.java.slee.resource.diameter.base.events." + name, "java.net", "0.8");

			DiameterMessage event = (DiameterMessage) createEvent(request, answer);

			sleeEndpoint.fireEvent(handle, event, eventID, null);
		}
		catch (Exception e)
		{
			logger.warn("Can not send event", e);
		}
	}

	/**
	 * Method for obtaining the Peers the RA is currently conneceted to.
	 * 
	 * @return an array of DiameterIdentity AVPs representing the peers.
	 */
	public DiameterIdentityAvp[] getConnectedPeers()
	{
		if (this.stack != null)
		{
			try
			{
				// Get the list of peers from the stack
				List<Peer> peers = stack.unwrap(PeerTable.class).getPeerTable();
				
				DiameterIdentityAvp[] result = new DiameterIdentityAvp[peers.size()];

				int i = 0;

				// Get each peer from the list and make a DiameterIdentityAvp
				for (Peer peer : peers)
				{
					DiameterIdentityAvp identity = new DiameterIdentityAvpImpl(0, 0, 0, 0, peer.getUri().toString().getBytes());

					result[i++] = identity;
				}

				return result;
			}
			catch (Exception e)
			{
				logger.error("Failure getting peer list.", e);
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdiameter.api.EventListener#receivedSuccessMessage(org.jdiameter.api.Message,
	 *      org.jdiameter.api.Message)
	 */
	public void receivedSuccessMessage(Request req, Answer ans)
	{
	  logger.info("Diameter Base RA :: receivedSuccessMessage :: " + "Request[" + req + "], Answer[" + ans + "].");

		try
    {
      logger.info( "Received Message Result-Code: " + ans.getResultCode().getUnsigned32() );
    }
    catch ( AvpDataException ignore )
    {
      // ignore, this was just for informational purposes...
    }
		// FIXME: alexandre: what should we do here? end activity?
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdiameter.api.EventListener#timeoutExpired(org.jdiameter.api.Message)
	 */
	public void timeoutExpired(Request req)
	{
	  logger.info("Diameter Base RA :: timeoutExpired :: " + "Request[" + req + "].");
	  
		// Message delivery timed out - we have to remove activity
		DiameterActivityHandle ah = new DiameterActivityHandle(req.getSessionId());

		try
		{
			activities.get(ah).endActivity();
		}
		catch (Exception e)
		{
			logger.error("Failure processing timeout message.", e);
		}
	}

	// ################################
	// ## ACCOUNTING SESSION FACTORY ##
	// ################################

	private class AccountingSessionFactory implements IAppSessionFactory, ServerAccSessionListener, StateChangeListener, ClientAccSessionListener {

		DiameterBaseResourceAdaptor ra;

		public AccountingSessionFactory(DiameterBaseResourceAdaptor ra)
		{
			this.ra = ra;
		}

		public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args)
		{
			try
			{
				if (aClass == ServerAccSession.class)
				{
					Request request = (Request) args[0];
					
					return new ServerAccSessionImpl(stack.getSessionFactory().getNewSession(request.getSessionId()), request, this, messageTimeout, true, new StateChangeListener[] {this});
				}
				else
				{
					if (aClass == ClientAccSession.class)
					{
						return sessionId == null ? new ClientAccSessionImpl(stack.getSessionFactory(), this, applicationId) : new ClientAccSessionImpl(stack.getSessionFactory(), sessionId, 
						    this, applicationId);
					}
				}

			}
			catch (Exception e)
			{
				logger.error("Failure to obtain new Accounting Session.", e);
			}

			return null;
		}

		public void doAccRequestEvent(ServerAccSession appSession, AccountRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException
		{
			logger.info("Diameter Base RA :: doAccRequestEvent :: appSession[" + appSession + "], Request[" + request + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(request.getCommandCode()) + "Request", (Request) request.getMessage(), null);
		}

		public void doAccAnswerEvent(ClientAccSession appSession, AccountRequest request, AccountAnswer answer) throws InternalException, IllegalDiameterStateException, 
		    RouteException, OverloadException
		{
			logger.info("doAccAnswerEvent :: appSession[" + appSession + "], request[" + request + "], answer[" + answer + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(answer.getCommandCode()) + "Answer", null, (Answer) answer.getMessage());
		}

		public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException,
		    OverloadException
		{
			logger.info("Diameter Base RA :: doOtherEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			if (answer != null)
			{
				this.ra.fireEvent(handle, events.get(answer.getCommandCode()) + "Answer", null, (Answer) answer.getMessage());
			}
			else
			{
				this.ra.fireEvent(handle, events.get(request.getCommandCode()) + "Request", (Request) request.getMessage(), null);
			}
		}

		public void stateChanged(Enum oldState, Enum newState)
		{
			logger.info("Diameter Base RA :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
		}

	}

	// ###################################
	// ## AUTHORIZATION SESSION FACTORY ##
	// ###################################

	private class AuthorizationSessionFactory implements IAppSessionFactory, IAuthMessageFactory, ServerAuthSessionListener, StateChangeListener, ClientAuthSessionListener {

	  private long authAppId = 19301L;
		private DiameterBaseResourceAdaptor ra;

		private boolean stateless = true;
		
		public AuthorizationSessionFactory(DiameterBaseResourceAdaptor ra)
		{
			this.ra = ra;
		}

		public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args)
		{
		  try 
		  {
		    if (aClass == ServerAuthSession.class) 
		    {
		      Request request =(Request) args[0];
		      return new ServerAuthSessionImpl(stack.getSessionFactory().getNewSession(request.getSessionId()), request, this,  this, messageTimeout, stateless, this);
		    }
		    else
		    {
		      if (aClass == ClientAuthSession.class)
		        return sessionId == null ?
		            new ClientAuthSessionImpl(stateless, this, stack.getSessionFactory(), this) :
		            new ClientAuthSessionImpl(stateless, sessionId, this, stack.getSessionFactory(), this) ;
		    }
		  }
		  catch (Exception e)
		  {
		    logger.error( "", e );
		  }
		  
		  return null;
		}

		public void stateChanged(Enum oldState, Enum newState)
		{
			logger.info("Diameter Base RA :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
		}

		public void doAbortSessionRequestEvent(ClientAuthSession appSession, AbortSessionRequest asr) throws InternalException, IllegalDiameterStateException, RouteException,
		    OverloadException
		{
			logger.info("Diameter Base RA :: doAbortSessionRequestEvent :: appSession[" + appSession + "], ASR[" + asr + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(asr.getCommandCode()) + "Request", (Request) asr.getMessage(), null);
		}

		public void doAbortSessionAnswerEvent(ServerAuthSession appSession, org.jdiameter.api.auth.events.AbortSessionAnswer asa) throws InternalException,
		    IllegalDiameterStateException, RouteException, OverloadException
		{
			logger.info("Diameter Base RA :: doAbortSessionAnswerEvent :: appSession[" + appSession + "], ASA[" + asa + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(asa.getCommandCode()) + "Answer", null, (Answer) asa.getMessage());
		}

		public void doSessionTerminationRequestEvent(ServerAuthSession appSession, SessionTermRequest str) throws InternalException, IllegalDiameterStateException, RouteException, 
		    OverloadException 
		{
			logger.info("Diameter Base RA :: doSessionTerminationRequestEvent :: appSession[" + appSession + "], STA[" + str + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(str.getCommandCode()) + "Request", (Request) str.getMessage(), null);
		}

		public void doSessionTerminationAnswerEvent(ClientAuthSession appSession, SessionTermAnswer sta) throws InternalException, IllegalDiameterStateException, RouteException,
		    OverloadException
		{
			logger.info("Diameter Base RA :: doSessionTerminationAnswerEvent :: appSession[" + appSession + "], STA[" + sta + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(sta.getCommandCode()) + "Answer", null, (Answer) sta.getMessage());
		}

		public void doAuthRequestEvent(ServerAuthSession appSession, AppRequestEvent request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException
		{
			logger.info("Diameter Base RA :: doAuthRequestEvent :: appSession[" + appSession + "], Request[" + request + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(request.getCommandCode()) + "Request", (Request) request.getMessage(), null);
		}

		public void doAuthAnswerEvent(ClientAuthSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, 
		    RouteException, OverloadException
		{
		  logger.info("Diameter Base RA :: doAuthAnswerEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(answer.getCommandCode()) + "Answer", null, (Answer) answer.getMessage());
		}

		public void doReAuthRequestEvent(ClientAuthSession appSession, ReAuthRequest rar) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException
		{
			logger.info("Diameter Base RA :: doReAuthRequestEvent :: appSession[" + appSession + "], RAR[" + rar + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(rar.getCommandCode()) + "Request", (Request) rar.getMessage(), null);
		}

		public void doReAuthAnswerEvent(ServerAuthSession appSession, ReAuthRequest rar, org.jdiameter.api.auth.events.ReAuthAnswer raa) throws InternalException,
		    IllegalDiameterStateException, RouteException, OverloadException 
		{
			logger.info("Diameter Base RA :: doReAuthAnswerEvent :: appSession[" + appSession + "], RAR[" + rar + "], RAA[" + raa + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			this.ra.fireEvent(handle, events.get(raa.getCommandCode()) + "Answer", null, (Answer) raa.getMessage());
		}

		public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, 
		    RouteException, OverloadException 
		{
			logger.info("Diameter Base RA :: doOtherEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");

			DiameterActivityHandle handle = new DiameterActivityHandle(appSession.getSessions().get(0).getSessionId());

			if (answer != null)
			{
				this.ra.fireEvent(handle, events.get(answer.getCommandCode()) + "Answer", null, (Answer) answer.getMessage());
			}
			else
			{
				this.ra.fireEvent(handle, events.get(request.getCommandCode()) + "Request", (Request) request.getMessage(), null);
			}
		}

		public AppAnswerEvent createAuthAnswer(Answer answer) {
			return new AppAnswerEventImpl(answer);
		}

		public AppRequestEvent createAuthRequest(Request request) {
			return new AppRequestEventImpl(request);
		}

		public ApplicationId getApplicationId() {
			return ApplicationId.createByAuthAppId(authAppId);
		}

		public int getAuthMessageCommandCode() {
			// FIXME: alexandre: what to use here?
			return 0;
		}

		public void setStateless(boolean stateless)
		{
		  this.stateless = stateless;
		}
		
		public boolean getStateless()
		{
		  return this.stateless;
		}
	}

	// ##############
	// ## PROVIDER ##
	// ##############

	private class DiameterProviderImpl implements DiameterProvider {

		protected final Logger logger = Logger.getLogger(DiameterProviderImpl.class);

		protected DiameterBaseResourceAdaptor ra;
		protected DiameterAvpFactory avpFactory = null;
		protected DiameterMessageFactory messageFactory = null;

		public DiameterProviderImpl(DiameterBaseResourceAdaptor ra) 
		{
			this.ra = ra;
		}

		public DiameterActivity createActivity() throws CreateActivityException
		{
			logger.info("Diameter Base RA :: createActivity");

			return this.createActivity(null, null);
		}

		public DiameterActivity createActivity(DiameterIdentityAvp destinationHost, DiameterIdentityAvp destinationRealm) throws CreateActivityException
		{
			logger.info("Diameter Base RA :: createActivity :: destinationHost[" + destinationHost + "], destinationRealm[" + destinationRealm + "]");

			return createActivity(destinationHost, destinationRealm, null);
		}

		public DiameterActivity createActivity(DiameterIdentityAvp destinationHost, DiameterIdentityAvp destinationRealm, String sessionId) throws CreateActivityException
		{
			Session session = null;

			try
			{
				if (sessionId != null)
				{
					session = sessionFactory.getNewSession(sessionId);
				}
				else
				{
					session = sessionFactory.getNewSession();
				}
			}
			catch (InternalException e)
			{
				logger.error("", e);
				return null;
			}

			DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session, stack, destinationHost, destinationRealm);

			DiameterActivityImpl activity = new DiameterActivityImpl(msgFactory, diameterAvpFactory, session, ra, messageTimeout, destinationHost, destinationRealm, sleeEndpoint);

			activityCreated(activity);

			return activity;
		}

		public AccountingClientSessionActivity createAccountingActivity(DiameterIdentityAvp destinationHost, DiameterIdentityAvp destinationRealm) throws CreateActivityException
		{
			ClientAccSession session = null;

			// FIXME: alexandre: replaced by stack constructor. needs review.
      // DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session.getSessions().get(0), destinationHost, destinationRealm);
			
			DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(stack);
			
			AccountingClientSessionActivityImpl activity = new AccountingClientSessionActivityImpl(msgFactory, diameterAvpFactory, session, ra, messageTimeout, 
			    destinationHost, destinationRealm, sleeEndpoint);
			
			activityCreated(activity);
			
			return activity;
		}

		public AccountingClientSessionActivity createAccountingActivity() throws CreateActivityException
		{
			return this.createAccountingActivity(null, null);
		}

		public AuthClientSessionActivity createAuthenticationActivity(DiameterIdentityAvp destinationHost, DiameterIdentityAvp destinationRealm) throws CreateActivityException
		{

			ClientAuthSession session;
			try {
				//FIXME: baranowb: AppId should be taken from stack conf?
				 session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, ApplicationId.createByAuthAppId(193L, 19301L), ClientAuthSession.class, null);
				DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session.getSessions().get(0), stack, destinationHost, destinationRealm);
				
				AuthClientSessionActivityImpl activity = new AuthClientSessionActivityImpl(msgFactory, diameterAvpFactory, session, ra, messageTimeout, 
				    destinationHost, destinationRealm, sleeEndpoint);
				
				activityCreated(activity);
				
				return activity;
			} catch (InternalException e) {
				
				e.printStackTrace();
				throw new CreateActivityException(e);
			} catch (IllegalDiameterStateException e) {
				
				e.printStackTrace();
				throw new CreateActivityException(e);
			}
			

		}

		public AuthClientSessionActivity createAuthenticationActivity()
				throws CreateActivityException {
			return this.createAuthenticationActivity(null, null);
		}

		AccountingServerSessionActivity createAccountingServerActivity(Request req) throws CreateActivityException
		{
			ServerAccSession session = null;

			try
			{
				session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, null, ServerAccSession.class, req);
				
				if (session == null)
				{
					logger.error("Failure creating Accounting Server Session (null).");
					return null;
				}
			}
			catch (InternalException e)
			{
				logger.error("", e);
				return null;
			}
			catch (IllegalDiameterStateException e)
			{
				logger.error("", e);
				return null;
			}

			DiameterIdentityAvp destinationHost = null;
			DiameterIdentityAvp destinationRealm = null;

			AvpSet avps = req.getAvps();

			Avp raw = null;

			if ((raw = avps.getAvp(Avp.DESTINATION_HOST)) != null)
			{
				try
				{
					destinationHost = new DiameterIdentityAvpImpl(Avp.DESTINATION_HOST, 0, 0, 0, raw.getRaw());
				}
				catch (AvpDataException e)
				{
					logger.error("", e);
				}
			}

			if ((raw = avps.getAvp(Avp.DESTINATION_REALM)) != null)
			{
				try
				{
					destinationRealm = new DiameterIdentityAvpImpl(Avp.DESTINATION_REALM, 0, 0, 0, raw.getRaw());
				}
				catch (AvpDataException e)
				{
					logger.error("", e);
				}
			}

			// FIXME: alexandre: replaced with stack constructor. review!
			DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(stack);
			
			AccountingServerSessionActivityImpl activity = new AccountingServerSessionActivityImpl(msgFactory, diameterAvpFactory, session, ra, messageTimeout,
			    destinationHost, destinationRealm, sleeEndpoint, stack);
			
			activityCreated(activity);

			return activity;
		}

		AccountingServerSessionActivity createAccountingServerActivity(DiameterIdentityAvp destinationHost, DiameterIdentityAvp destinationRealm) throws CreateActivityException
		{
			ServerAccSession session = null;

			try 
			{
				//FIXME: baranowb: 19302 - shouldnt it be taken from stack conf?
				session = sessionFactory.getNewAppSession(ApplicationId.createByAccAppId(19302), ServerAccSession.class);
				
				if (session == null)
				{
					logger.error("Failure creating Accounting Server Session (null).");
					
					return null;
				}
			}
			catch (InternalException e)
			{
				logger.error("", e);
				return null;
			}

			DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session.getSessions().get(0), stack, destinationHost, destinationRealm);
			
			AccountingServerSessionActivityImpl activity = new AccountingServerSessionActivityImpl( msgFactory, diameterAvpFactory, session, ra, messageTimeout,
			    destinationHost, destinationRealm, sleeEndpoint, stack);
			
			activityCreated(activity);

			return activity;
		}

		AccountingServerSessionActivity createAccountingServerActivity() throws CreateActivityException 
		{
			return this.createAccountingServerActivity(null, null);
		}

		AuthServerSessionActivity createAuthenticationServerActivity(DiameterIdentityAvp destinationHost, DiameterIdentityAvp destinationRealm) throws CreateActivityException
		{
			ServerAuthSession session = null;

			try {
				//FIXME: baranowb: AppId should be taken from stack conf?
				 session = ((ISessionFactory) stack.getSessionFactory()).getNewAppSession(null, ApplicationId.createByAuthAppId(193L, 19301L), ServerAuthSession.class, null);
				DiameterMessageFactoryImpl msgFactory = new DiameterMessageFactoryImpl(session.getSessions().get(0), stack, destinationHost, destinationRealm);
				
				AuthServerSessionActivityImpl activity = new AuthServerSessionActivityImpl(msgFactory, diameterAvpFactory, session, ra, messageTimeout,
				    destinationHost, destinationRealm, sleeEndpoint);
				
				activityCreated(activity);
				
				return activity;
			} catch (InternalException e) {
				
				e.printStackTrace();
				throw new CreateActivityException(e);
			} catch (IllegalDiameterStateException e) {
				
				e.printStackTrace();
				throw new CreateActivityException(e);
			}
			
		}

		AuthServerSessionActivity createAuthenticationServerActivity() throws CreateActivityException
		{
			return this.createAuthenticationServerActivity(null, null);
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
		DiameterActivity createActivity(Message message) throws CreateActivityException 
		{
			String sessionId = message.getSessionId();
			DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);
			
			if (activities.keySet().contains(handle))
			{
				return activities.get(handle);
			}
			else 
			{
				DiameterIdentityAvp destinationHost = null;
				DiameterIdentityAvp destinationRealm = null;

				AvpSet avps = message.getAvps();

				Avp raw = null;

				if((raw = avps.getAvp(Avp.DESTINATION_HOST)) != null) 
				{
					try 
					{
						destinationHost = new DiameterIdentityAvpImpl(Avp.DESTINATION_HOST, 0, 0, 0, raw.getRaw());
					}
					catch (AvpDataException e) 
					{
						logger.error("", e);
					}
				}

				if((raw = avps.getAvp(Avp.DESTINATION_REALM)) != null)
				{
					try 
					{
						destinationRealm = new DiameterIdentityAvpImpl(Avp.DESTINATION_REALM, 0, 0, 0, raw.getRaw());
					}
					catch (AvpDataException e)
					{
						logger.error("", e);
					}
				}

				if (authEventCodes.contains(message.getCommandCode()))
				{
					return createAuthenticationServerActivity(destinationHost, destinationRealm);
				}
				else if (accEventCodes.contains(message.getCommandCode()))
				{
					return createAccountingServerActivity((Request) message);
				}
				else // Base Activity
				{
					return this.createActivity(destinationHost, destinationRealm, message.getSessionId());
				}
			}

		}

		public DiameterMessageFactory getDiameterMessageFactory()
		{
			if (this.messageFactory == null)
			{
				this.messageFactory = new DiameterMessageFactoryImpl(this.ra.stack);
			}

			return this.messageFactory;
		}

		public DiameterAvpFactory getDiameterAvpFactory()
		{
			if (this.avpFactory == null)
			{
				this.avpFactory = new DiameterAvpFactoryImpl();
			}
			
			return this.avpFactory;
		}

		public DiameterMessage sendSyncRequest(DiameterMessage message) throws IOException
		{
			try
			{
				if (message instanceof DiameterMessageImpl) 
				{
					DiameterMessageImpl msg = (DiameterMessageImpl) message;
					
					// FIXME: baranowb - get session from RA! THIS WILL BE COMPLICATED IN CASE WE HAVE RELLY AGENT!
					// here we can have leaks ;[
					
					String sessionId = message.getSessionId();
					DiameterActivityHandle handle = new DiameterActivityHandle(sessionId);

					if (!activities.keySet().contains(handle))
					{
						createActivity(msg.getGenericData());
					}

					DiameterActivityImpl activity = (DiameterActivityImpl) getActivity(handle);
					
					return activity.sendSyncMessage(message);
				}
			}
			catch (Exception e)
			{
				logger.error("Failure sending sync request.", e);
			}

			// FIXME Throw unknown message exception?
			return null;
		}

		public DiameterIdentityAvp[] getConnectedPeers() {
			return this.ra.getConnectedPeers();
		}

		public int getPeerCount() {
			return getConnectedPeers().length;
		}
	}
}
