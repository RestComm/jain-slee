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

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.media.mscontrol.MediaEvent;
import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MsControlFactory;
import javax.media.mscontrol.resource.AllocationEvent;
import javax.media.mscontrol.resource.AllocationEventListener;
import javax.media.mscontrol.spi.DriverManager;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.ConfigProperties;
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
import javax.slee.resource.UnrecognizedActivityHandleException;
import org.mobicents.slee.resource.mediacontrol.local.FactoryLocal;
import org.mobicents.slee.resource.mediacontrol.local.MsActivity;

/**
 *
 * @author kulikov
 */
public class McResourceAdaptor implements ResourceAdaptor, MediaEventListener, AllocationEventListener {
    //Media control factory
    private MsControlFactory mscFactory;
    private ResourceAdaptorContext context;
    private SleeEndpoint sleeEndpoint;
    private EventLookupFacility eventLookupFacility;
    private Tracer tracer;    
    //the name of driver used
    private String driverName = "org.mobicents.Driver_1.0";    
    //references to configuration file
    private String configName = "msc.properties";    
    //Driver configuration
    private Properties config;
    private Address address = new Address(AddressPlan.IP, "127.0.0.1");

    protected ConcurrentHashMap<ActivityHandle, Object> activities = new ConcurrentHashMap();
    protected ConcurrentHashMap<String, ActivityHandle> handlers = new ConcurrentHashMap();
    
    private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;    
    /**
     * Gets the name of used driver.
     * 
     * @return the name of the driver
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * Assigns the name of driver.
     * 
     * @param driverName the name of the driver to be used.
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     * Gets the name of configuration file.
     * 
     * @return the name of the file with path counted from classpath
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * Assigns the name of the configuration file.
     * 
     * @param configName the name of file wich will be used for configuration.
     */
    public void setConfigName(String configName) {
        this.configName = configName;
        tracer.info("Assign config name: " + configName);
    }

    public void setResourceAdaptorContext(ResourceAdaptorContext context) {
        this.context = context;
        this.tracer = context.getTracer("MscResourceAdaptor");
        this.sleeEndpoint = context.getSleeEndpoint();
        this.eventLookupFacility = context.getEventLookupFacility();
    }

    public void unsetResourceAdaptorContext() {
    }

    public void raConfigure(ConfigProperties cfg) {
    }

    public void raUnconfigure() {
    }

    public void raActive() {
        try {
            config = new Properties();
            tracer.info("Loading resource adaptor configuration: " + configName);
            config.load(this.getClass().getResourceAsStream("/" + configName));
            tracer.info("Resource adaptor successfully configured");
            mscFactory = new FactoryLocal(DriverManager.getFactory(driverName, config), this);
            tracer.info("Successfully started driver " + driverName);
        } catch (Exception e) {
            tracer.severe("Can not activate driver[" + driverName + "]", e);
        }
    }

    public void raStopping() {
    }

    public void raInactive() {
    }

    public void raVerifyConfiguration(ConfigProperties config) throws InvalidConfigurationException {
    }

    public void raConfigurationUpdate(ConfigProperties config) {
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

    public void createActivity(ActivityHandle h, MsActivity a) {
        activities.put(h, a);
        handlers.put(a.getID(), h);
        try {
            this.sleeEndpoint.startActivity(h, a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void terminateActivity(ActivityHandle handle) {
        MsActivity activity = (MsActivity) activities.remove(handle);
        if (activity != null) {
            handlers.remove(activity.getID());
        }
        sleeEndpoint.endActivity(handle);
    }
    
    public Object getActivity(ActivityHandle handle) {
        return activities.get(handle);
    }

    public ActivityHandle getActivityHandle(Object activity) {
        ActivityHandle h = handlers.get(((MsActivity)activity).getID());
        return h;
    }

    public void administrativeRemove(ActivityHandle handle) {
    }

    public void eventProcessingSuccessful(ActivityHandle handle, FireableEventType evtTyppe, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
    }

    public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5, FailureReason arg6) {
    }

    public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
    }

    public void activityEnded(ActivityHandle handle) {
    }

    public void activityUnreferenced(ActivityHandle handle) {
    }

    protected void fireEvent(String eventName, ActivityHandle activityHandle, MediaEvent event) {
        tracer.info("Fire event: " + event);
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
            sleeEndpoint.fireEvent(activityHandle, eventID, event, address, null);

            if (tracer.isFineEnabled()) {
                tracer.fine("Fire event: " + eventName);
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

    public void onEvent(MediaEvent event) {
    }

    public void onEvent(AllocationEvent arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
