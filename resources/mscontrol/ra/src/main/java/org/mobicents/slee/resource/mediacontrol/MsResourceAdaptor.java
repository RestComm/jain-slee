/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.mediacontrol;

import java.io.Reader;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.media.mscontrol.Configuration;
import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MediaConfigException;
import javax.media.mscontrol.MediaEvent;
import javax.media.mscontrol.MediaObject;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.MsControlFactory;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.resource.video.VideoLayout;
import javax.media.mscontrol.spi.Driver;
import javax.media.mscontrol.spi.DriverManager;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ConfigProperties.Property;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireEventException;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.IllegalEventException;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.resource.StartActivityException;
import javax.slee.resource.UnrecognizedActivityHandleException;

import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.javax.media.mscontrol.spi.DriverImpl;
/**
 * 
 * @author baranowb
 */
public class MsResourceAdaptor implements ResourceAdaptor {

	/**
	 * RA config property which holds driver name.
	 */
	public static final String DRIVER = "driver.name";
	//flags for events.
	private static final int ACTIVITY_FLAGS = ActivityFlags.setRequestEndedCallback(ActivityFlags.REQUEST_ACTIVITY_UNREFERENCED_CALLBACK);
	
	// Media control factory
	private MsControlFactoryWrapper mscFactory;
	private Driver mscDriver;
	private ResourceAdaptorContext context;
	private SleeEndpoint sleeEndpoint;
	private EventLookupFacility eventLookupFacility;
	private Tracer tracer;

	// Driver configuration
	private Properties config = new Properties();
	private String driverName;
	private Map<MsActivityHandle, MsActivity> activities = new ConcurrentHashMap<MsActivityHandle, MsActivity>();
	
	/**
	 * 
	 */
	public MsResourceAdaptor() {
		super();
		this.mscFactory = new MsControlFactoryWrapper(this);
	}

	public void setResourceAdaptorContext(ResourceAdaptorContext context) {
		this.context = context;
		this.tracer = this.getTracer(this);
		this.sleeEndpoint = context.getSleeEndpoint();
		this.eventLookupFacility = context.getEventLookupFacility();
	}

	public void unsetResourceAdaptorContext() {
	}

	public void raConfigure(ConfigProperties cfg) {

			for(Property p:cfg.getProperties())
			{
				if(p.getName().equals(DRIVER))
				{
					this.driverName = (String)p.getValue(); //it must be string
				}else
				{
					//add to props.
					this.config.put(p.getName(), p.getValue());
				}
			}
		

	}

	public void raUnconfigure() {
		this.config.clear();
		this.driverName = null;
	}

	public void raActive() {
		try {
			
			this.mscDriver = DriverManager.getDriver(this.driverName);
			this.tracer.info("Created MSC Driver: " + mscDriver + ", from name: " + driverName);
			
			this.mscFactory.setFactory(this.mscDriver.getFactory(this.config));
			this.mscFactory.setActive(true);
			this.tracer.info("Successfully started MSC RA Entity:" + this.context.getEntityName());
		} catch (Exception e) {
			tracer.severe("Can not activate driver[" + driverName + "]", e);
		}
	}

	public void raStopping() {
		//set it before becoming inactive?
		this.mscFactory.setActive(false);
		Set<MsActivity> acs = new HashSet<MsActivity>(this.activities.values());
		for(MsActivity a:acs)
		{
			//release only on media session, rest will be released as children
			if(a instanceof MediaSession)
				a.release();
		}
		
	}
	

	public void raInactive() {

		if(this.activities.size()>0)
		{
			this.tracer.severe("Some activities still remain! "+this.activities);
		}
		this.activities.clear();
		//HACK, MSC does not expose any method to shutdown resources once driver is not used.
		//mobicents impl does.
		if(this.mscDriver instanceof DriverImpl)
		{
		    DriverImpl driverImpl = (DriverImpl)this.mscDriver;
		    driverImpl.shutdown();
		}
		this.mscDriver = null;
		this.mscFactory.setFactory(null);
	}

	public void raVerifyConfiguration(ConfigProperties cfg) throws InvalidConfigurationException {
		Property driverProperty = cfg.getProperty(DRIVER);
		if(driverProperty == null || driverProperty.getValue() == null || !driverProperty.getType().equals("java.lang.String"))
		{
			throw new InvalidConfigurationException("No driver property specified!");
		}

		String driverName = null;
		Properties config = new Properties();
		for(Property p:cfg.getProperties())
		{
			
			if(p.getName().equals(DRIVER))
			{
				driverName = (String)p.getValue(); //it must be string
			}else
			{
				//add to props.
				config.put(p.getName(), p.getValue());
			}
		}
		
		Driver d = DriverManager.getDriver(driverName);	
		if(d== null)
		{
			throw new InvalidConfigurationException("Failed to create driver for: "+driverName);
		}
		
	    //postpone interaction with Driver, since we dont want anything to be started.
//		try {
//			if(d.getFactory(config) == null)
//			{
//				throw new InvalidConfigurationException("Failed to create MscFactory for: "+config);
//			}
//		} catch (MsControlException e) {
//			throw new InvalidConfigurationException("Failed to create MscFactory for: "+config,e);
//		}
	}

	public void raConfigurationUpdate(ConfigProperties config) {
		throw new UnsupportedOperationException();
		
	}

	public Object getResourceAdaptorInterface(String name) {
		return mscFactory;
	}

	public Marshaler getMarshaler() {
		return null;
	}

	public void serviceActive(ReceivableService service) {
	}

	public void serviceStopping(ReceivableService service) {
	}

	public void serviceInactive(ReceivableService service) {
	}

	public void queryLiveness(ActivityHandle handle) {
	}

	public Object getActivity(ActivityHandle handle) {
		if (handle instanceof MsActivityHandle) {
			return activities.get(handle);
		} else {
			return null;
		}

	}

	public ActivityHandle getActivityHandle(Object activity) {
		if (activity instanceof MsActivity) {
			MsActivity mcActivity = (MsActivity) activity;
			return mcActivity.getActivityHandle();
		}

		return null;
	}

	public void administrativeRemove(ActivityHandle handle) {
	}

	public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType evtTyppe, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
	}

	public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5,
			FailureReason arg6) {
	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
	}

	public void activityEnded(ActivityHandle handle) {
		if (handle instanceof MsActivityHandle) {
			this.activities.remove(handle);
		}
	}

	public void activityUnreferenced(ActivityHandle handle) {
	}

	public void fireEvent(String eventName, ActivityHandle activityHandle, MediaEvent event) {
		tracer.info("Fire on: "+activityHandle+", event: " + eventName);
		FireableEventType eventID = null;
		try {
			EventTypeID eventTypeId = new EventTypeID(eventName, "org.mobicents", "1.0");
			eventID = eventLookupFacility.getFireableEventType(eventTypeId);
		} catch (FacilityException fe) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Caught a FacilityException: ");
			}
			fe.printStackTrace();
			throw new RuntimeException("JccResourceAdaptor.firEvent(): FacilityException caught. ", fe);
		} catch (UnrecognizedEventException ue) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Caught an UnrecognizedEventException: ");
			}
			ue.printStackTrace();
			throw new RuntimeException("JccResourceAdaptor.firEvent(): UnrecognizedEventException caught.", ue);
		}

		if (eventID == null) {
			if (tracer.isWarningEnabled()) {
				tracer.warning("Unknown event type: " + eventName);
			}
			return;
		}

		try {
			sleeEndpoint.fireEvent(activityHandle, eventID, event, null, null);

			if (tracer.isFineEnabled()) {
				tracer.fine("Fire on: "+activityHandle+", event: " + eventName);
			}
		} catch (IllegalStateException ise) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Caught an IllegalStateException: ");
			}
			ise.printStackTrace();
		} catch (ActivityIsEndingException aiee) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Caught an ActivityIsEndingException: ");
			}
			aiee.printStackTrace();
		} catch (UnrecognizedActivityException uaee) {
			if (tracer.isSevereEnabled()) {
				tracer.severe("Caught an UnrecognizedActivityException: ");
			}
			uaee.printStackTrace();
		} catch (UnrecognizedActivityHandleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalEventException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SLEEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FireEventException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param activityHandle
	 */
	public void endActivity(MsActivityHandle activityHandle) {
		this.sleeEndpoint.endActivity(activityHandle);
	}

	/**
	 * @param wrapper
	 * @throws StartActivityException
	 * @throws SLEEException
	 * @throws IllegalStateException
	 * @throws NullPointerException
	 * @throws ActivityAlreadyExistsException
	 */
	public void startActivity(MsActivity wrapper) throws ActivityAlreadyExistsException, NullPointerException, IllegalStateException, SLEEException,
			StartActivityException {
		MsActivityHandle handle = wrapper.getActivityHandle();
		sleeEndpoint.startActivitySuspended(handle, wrapper, ACTIVITY_FLAGS);
		this.activities.put(handle, wrapper);
	}

	public Tracer getTracer(Object o)
	{
		//hmm just to have single logging framework working...
		String name = getClass().getName();
		return this.context.getTracer(name);
	}
	
	//seems like it has to be in RA class?
	/**
	 * This wrapper is actually a provider of RA.
	 * 
	 * @author baranowb
	 * 
	 */
	public class MsControlFactoryWrapper implements MsControlFactory {

		protected MsControlFactory wrappedFactory;
		protected MsResourceAdaptor ra; //required to pass into other wrappers
		protected boolean active = false;

		/**
		 * @param wrappedFactory
		 * @param ra
		 */
		public MsControlFactoryWrapper(MsResourceAdaptor ra) {
			super();
			this.ra = ra;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.media.mscontrol.MsControlFactory#createMediaSession()
		 */
		
		public MediaSession createMediaSession() throws MsControlException {
			if(!active){
				throw new IllegalStateException("Factory is not ready!");
			}
			MediaSessionWrapper msw = new MediaSessionWrapper(this.wrappedFactory.createMediaSession(), this.ra);
			try {
				this.ra.startActivity(msw);
			} catch (Exception e) {
				throw new MsControlException("Failed to create MsControl resource.", e);
			}
			return msw;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.media.mscontrol.MsControlFactory#createParameters()
		 */
		
		public Parameters createParameters() {
			if(!active){
				throw new IllegalStateException("Factory is not ready!");
			}
			return this.wrappedFactory.createParameters();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.MsControlFactory#createVideoLayout(java.lang.String
		 * , java.io.Reader)
		 */
		
		public VideoLayout createVideoLayout(String mimeType, Reader xmlDef) throws MediaConfigException {
			if(!active){
				throw new IllegalStateException("Factory is not ready!");
			}
			return this.wrappedFactory.createVideoLayout(mimeType, xmlDef);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.MsControlFactory#getMediaConfig(javax.media.mscontrol
		 * .Configuration)
		 */
		
		public MediaConfig getMediaConfig(Configuration<?> configuration) throws MediaConfigException {
			if(!active){
				throw new IllegalStateException("Factory is not ready!");
			}
			return this.wrappedFactory.getMediaConfig(configuration);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.MsControlFactory#getMediaConfig(java.io.Reader)
		 */
		
		public MediaConfig getMediaConfig(Reader xmlDef) throws MediaConfigException {
			if(!active){
				throw new IllegalStateException("Factory is not ready!");
			}
			return this.wrappedFactory.getMediaConfig(xmlDef);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.media.mscontrol.MsControlFactory#getMediaObject(java.net.URI)
		 */
		
		public MediaObject getMediaObject(URI arg0) {
			// TODO
			throw new UnsupportedOperationException();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.MsControlFactory#getPresetLayout(java.lang.String)
		 */
		
		public VideoLayout getPresetLayout(String type) throws MediaConfigException {
			if(!active){
				throw new IllegalStateException("Factory is not ready!");
			}
			return this.wrappedFactory.getPresetLayout(type);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.media.mscontrol.MsControlFactory#getPresetLayouts(int)
		 */
		
		public VideoLayout[] getPresetLayouts(int numberOfLiveRegions) throws MediaConfigException {
			if(!active){
				throw new IllegalStateException("Factory is not ready!");
			}
			return this.wrappedFactory.getPresetLayouts(numberOfLiveRegions);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.media.mscontrol.MsControlFactory#getProperties()
		 */
		
		public Properties getProperties() {
			if(!active){
				throw new IllegalStateException("Factory is not ready!");
			}
			return this.wrappedFactory.getProperties();
		}

		/**
		 * @param factory
		 */
		public void setFactory(MsControlFactory factory) {
				this.wrappedFactory = factory;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

	}
}
