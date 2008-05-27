package org.mobicents.slee.resource.persistence.ra;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.slee.Address;
import javax.slee.InvalidStateException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;
import javax.transaction.SystemException;

//import org.jboss.logging.Logger;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;
import org.mobicents.slee.resource.persistence.ratype.PersistenceResourceAdaptorSbbInterface;

import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;

public class PersistenceResourceAdaptor implements ResourceAdaptor,
		Serializable, ActivityManipulation {

	static private transient Logger log;

	static {
		log = Logger.getLogger(PersistenceResourceAdaptor.class);
	}

	private String entityName = "PersistenceRA";

	private ResourceAdaptorState state = null;

	private transient Map activities = new ConcurrentReaderHashMap();

	private transient Map terminatedActivities = new ConcurrentReaderHashMap();

	private transient SleeEndpoint sleeEndpoint = null;

	private transient EventLookupFacility eventLookup = null;

	private transient BootstrapContext bootstrapContext = null;

	private transient SleeContainer serviceContainer = null;

	private transient SleeTransactionManager tm = null;

	private ActivityContextFactory activityContextFactory;

	private String PERSISTENCE_UNIT_NAME = "persistence-ra-unit";

	private String EMFS_MAP_NAME = "EMFS[" + this + "]";

	private String EMFS_SCOUNT_MAP_NAME = "EMFS_SCOUNT[" + this + "]";

	// Place to store EMFs and Subscription counts - tx aware map - this should be better tx aware
	CacheableMap emfsMap = new CacheableMap(EMFS_MAP_NAME); //NO GENERICS ;/

	CacheableMap emfsSubsriptionCounts = new CacheableMap(EMFS_SCOUNT_MAP_NAME);

	// LOCAL
	private PersistenceActivityContextInterfaceFactoryImp acif = null;

	// **** ENV METHOS
	// **HELP METHODS TO START/STOP ETC
	private void init(BootstrapContext bootstrapContext) {

		log.debug("PersistenceResourceAdaptor: init()");

		this.bootstrapContext = bootstrapContext;
		this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();

		// properties = new Properties();
		this.state = ResourceAdaptorState.UNCONFIGURED;
	}

	private void configure(Properties props) throws InvalidStateException {

		// ADD CONFIGURATION STEPS HERE

	}

	private void start() throws ResourceException {

		try {
			initializeNamingContext();

			activities = new ConcurrentReaderHashMap();
			// this.scanTransaction = new ScanTransaction(this);
			state = ResourceAdaptorState.ACTIVE;
		} catch (Exception e) {
			throw new ResourceException("Couldnt start Resource Adaptor["
					+ entityName + "]", e);
		}
	}

	private void initializeNamingContext() throws NamingException {
		SleeContainer container = SleeContainer.lookupFromJndi();

		serviceContainer = container;

		activityContextFactory = serviceContainer.getActivityContextFactory();

		tm = serviceContainer.getTransactionManager();

		ResourceAdaptorEntity resourceAdaptorEntity = ((ResourceAdaptorEntity) container
				.getResourceAdaptorEnitity(this.bootstrapContext
						.getEntityName()));

		ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
				.getInstalledResourceAdaptor().getRaType()
				.getResourceAdaptorTypeID();

		entityName = this.bootstrapContext.getEntityName();

		this.acif = new PersistenceActivityContextInterfaceFactoryImp(
				serviceContainer, entityName);

		resourceAdaptorEntity.getServiceContainer()
				.getActivityContextInterfaceFactories()
				.put(raTypeId, this.acif);

		try {
			if (this.acif != null) {
				// parse the string =
				// java:slee/resource/PersistenceRA/persistenceacif
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
						.getJndiName();

				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				log
						.debug("jndiName prefix =" + prefix + "; jndiName = "
								+ name);

				SleeContainer.registerWithJndi(prefix, name, this.acif);
			}
		} catch (IndexOutOfBoundsException e) {
			// not register with JNDI
			log.debug(e);
		}

	}

	public void stopping() {
		state = ResourceAdaptorState.STOPPING;
	}

	public void stop() {

		try {
			cleanNamingContext();
		} catch (NamingException e) {
			log.error("Cannot unbind naming context");
		}

		log.debug("Persistence Resource Adaptor stopped.");
	}

	public void stopEntityManagers() {

		Iterator it = activities.values().iterator();

		while (it.hasNext()) {

			SbbEntityManagerImpl sbei = (SbbEntityManagerImpl) it.next();
			if (sbei.isOpen()) {
				sbei.close();

			}
		}
		// emf.close();

	}

	private void cleanNamingContext() throws NamingException {
		try {
			if (this.acif != null) {
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.acif)
						.getJndiName();
				// remove "java:" prefix
				int begind = jndiName.indexOf(':');
				String javaJNDIName = jndiName.substring(begind + 1);
				SleeContainer.unregisterWithJndi(javaJNDIName);
			}
		} catch (IndexOutOfBoundsException e) {
			log.debug(e);
		}
	}

	// **** ACTIVITY MANIUPLATION METHODS
	
	// ***** RA METHODS

	// *** STATE METHODS

	public void entityCreated(BootstrapContext ctx) throws ResourceException {
		this.init(ctx);
	}

	public void entityActivated() throws ResourceException {
		try {
			try {
				this.configure(null);
			} catch (InvalidStateException e1) {

				e1.printStackTrace();
			}
			this.start();
		} catch (ResourceException e) {
			e.printStackTrace();
			throw new javax.slee.resource.ResourceException(
					"Failed to Activate Resource Adaptor!", e);
		}
	}

	public void entityDeactivated() {
		this.stop();
	}

	public void entityDeactivating() {
		this.stopping();
	}

	// *** OTHER
	public void activityEnded(ActivityHandle arg0) {
		// PersistenceActivityHandle pah=new PersistenceActivityHandle(em);
		activities.remove(arg0);
	}

	public void activityUnreferenced(ActivityHandle arg0) {

		((SbbEntityManagerImpl) activities.get(arg0)).close();
	}

	public void entityRemoved() {

	}

	public void eventProcessingFailed(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4, FailureReason arg5) {

	}

	public void eventProcessingSuccessful(ActivityHandle arg0, Object arg1,
			int arg2, Address arg3, int arg4) {

	}

	public Object getActivity(ActivityHandle arg0) {

		return activities.get(arg0);
	}

	public ActivityHandle getActivityHandle(Object arg0) {

		return null;
	}

	public Marshaler getMarshaler() {

		return null;
	}

	public Object getSBBResourceAdaptorInterface(String arg0) {

		// Late creation - so classes can be loaded into container if not in
		// jars packed with ra

		return new PersistenceResourceAdaptorSbbInterfaceImpl(this);
	}

	public void queryLiveness(ActivityHandle arg0) {

	}

	public void serviceActivated(String arg0) {

	}

	public void serviceDeactivated(String arg0) {

	}

	public void serviceInstalled(String arg0, int[] arg1, String[] arg2) {

	}

	public void serviceUninstalled(String arg0) {

	}

	public String getPersistanceUnitName() {

		return this.PERSISTENCE_UNIT_NAME;

	}

	public void setPersistenceUnitName(String name) {

		this.PERSISTENCE_UNIT_NAME = name;

	}

	
	
	public void addActivity(SbbEntityManagerImpl em) {

		PersistenceActivityHandle pah = new PersistenceActivityHandle(em);
		activities.put(pah, em);

	}

	public void removeActivity(SbbEntityManagerImpl em) {
		PersistenceActivityHandle pah = new PersistenceActivityHandle(em);
		// activities.remove(pah);

		try {
			this.sleeEndpoint.activityEnding(pah);
		} catch (NullPointerException e) {
			activities.remove(pah);
			e.printStackTrace();
		} catch (IllegalStateException e) {
			activities.remove(pah);
			e.printStackTrace();
		} catch (UnrecognizedActivityException e) {
			activities.remove(pah);
			e.printStackTrace();
		}
	}

	public EntityManagerFactory subscribeForEntityManagerFactoryForPU(String puName, Map emfProperties) {
		
		//in TX code
		EntityManagerFactory emf=null;
		
		
		boolean createdTx=false;
		try {
			if(!tm.isInTx())
			{
				tm.begin();
				createdTx=true;
				
			}
			
			
			
			if(!emfsMap.containsKey(puName))
			{
				if(emfProperties==null)
					emf=javax.persistence.Persistence.createEntityManagerFactory(puName);
				else
					emf=javax.persistence.Persistence.createEntityManagerFactory(puName,emfProperties);
				emfsMap.put(puName, emf);
				emfsSubsriptionCounts.put(puName, new Integer(1)); //No wrapping ;/
			}
			else
			{
				emf=(EntityManagerFactory) emfsMap.get(puName);
				Integer val=(Integer) emfsSubsriptionCounts.get(puName);
				emfsSubsriptionCounts.put(puName, new Integer(val+1));
			}
			
			
			
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			if(createdTx)
			{
				try {
					tm.commit();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		
		
		return emf;
	}

	public void unsubscribeForEntityManagerFactoryForPU(String puName) {
		
		
		
		boolean createdTx=false;
		try {
			if(!tm.isInTx())
			{
				tm.begin();
				createdTx=true;
				
			}
			
			
			if(!emfsMap.containsKey(puName))
				return;
			
			
			Integer val=(Integer) emfsSubsriptionCounts.get(puName);
			int newVal=val.intValue()-1;
			
			if(newVal==0)
			{
				//Its THE END for this emf, atleast till someone calls it again.
				
			}else
			{
				
				emfsSubsriptionCounts.put(puName, new Integer(newVal));
				
			}
			
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			if(createdTx)
			{
				try {
					tm.commit();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		
		
	}

	
	public boolean isEMFOpen(String puName)
	{
		boolean createdTx=false;
		try {
			if(!tm.isInTx())
			{
				tm.begin();
				createdTx=true;
				
			}
			
			
			
			if(emfsMap.containsKey(puName))
			{
				return ((EntityManagerFactory)emfsMap.get(puName)).isOpen();
				
				
			}else
			{
				return false;
			}
			
			
			
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			if(createdTx)
			{
				try {
					tm.commit();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		
		return false;
		
	}
	
	
}
