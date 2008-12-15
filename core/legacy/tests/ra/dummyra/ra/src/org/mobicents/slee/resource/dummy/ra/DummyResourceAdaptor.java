/*
 * DummyResourceAdaptor.java
 *
 * Created on 14 Декабрь 2006 г., 10:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.dummy.ra;

import java.io.DataOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import java.util.*;


import javax.naming.NamingException;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.SleeEndpoint;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;

import java.net.Socket;
import java.net.ServerSocket;



import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceEventImpl;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;



import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.DeferredEvent;
import org.mobicents.slee.runtime.SleeInternalEndpoint;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorState;

import javax.slee.EventTypeID;
import javax.slee.FactoryException;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.management.SleeState;
import javax.slee.nullactivity.NullActivity;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.resource.ActivityHandle;

import javax.slee.resource.ActivityHandle;
import javax.slee.*;
import javax.transaction.SystemException;

/**
 *
 * @author Oleg Kulikov
 * @author baranowb
 */
public class DummyResourceAdaptor implements ResourceAdaptor, Serializable {
    
    private Byte byteProperty;
    private Boolean booleanProperty;
    private Integer intProperty;
    private Long longProperty;
    private Short shortProperty;
    private Float floatProperty;
    private Double doubleProperty;
    private String stringProperty;
    private Date dateProperty;
    
    private ServerSocket serverSocket;
    
    private static Logger log=Logger.getLogger(DummyResourceAdaptor.class);
    
    
//  STUFF FOR TCK EVENTS, WHICH WILL BE FIRED TO TEST RA AND CONTAINER
	public static final String TCK_EVENT_NAME = "com.opencloud.sleetck.lib.resource.events.TCKResourceEventX.X";

	public static final String TCK_VENDOR = "jain.slee.tck";

	public static final String TCK_VERSION = "1.0";

	public static final ComponentKey TckX1Key = new ComponentKey(
			TCK_EVENT_NAME + 1, TCK_VENDOR, TCK_VERSION);

	public static final ComponentKey TckX2Key = new ComponentKey(
			TCK_EVENT_NAME + 2, TCK_VENDOR, TCK_VERSION);

	public static final ComponentKey TckX3Key = new ComponentKey(
			TCK_EVENT_NAME + 3, TCK_VENDOR, TCK_VERSION);
    
	private static ActivityContextInterface nullAci = null;
	private transient EventLookupFacility eventLookup;
    //EVENT FILTERING PART
	private Map eventIDsOfServicesInstalled = new ConcurrentHashMap(31);
	private Map myComponentKeys = new ConcurrentHashMap(31);

	private Map eventResourceOptionsOfServicesInstalled = new ConcurrentHashMap(
			31);

	private ActivityContextFactory activityContextFactory;
	private transient SleeTransactionManager tm = null;
    
	private ComponentKey raTypeKey = null;

	private ComponentKey raKey = null;
    
	private static SleeInternalEndpoint sleeInternalEndpoint = SleeContainer
	.lookupFromJndi().getSleeEndpoint();

	private transient SleeContainer serviceContainer;
	private transient BootstrapContext bootstrapContext;
	
	private DummyActivityContextInterfaceFactoryImpl acif;
	
    /** Creates a new instance of DummyResourceAdaptor */
    public DummyResourceAdaptor() {
    }

    public Byte getByteProperty() {
        return byteProperty;
    }
    
    public void setByteProperty(Byte byteProperty) {
        this.byteProperty = byteProperty;
    }
    

    public Boolean getBooleanProperty() {
        return booleanProperty;
    }
    
    public void setBooleanProperty(Boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }
    
    public Integer getIntProperty() {
        return intProperty;
    }
    
    public void setIntProperty(Integer intProperty) {
        this.intProperty = intProperty;
    }

    public Short getShortProperty() {
        return shortProperty;
    }
    
    public void setShortProperty(Short shortProperty) {
        this.shortProperty = shortProperty;
    }
    
    public Long getLongProperty() {
        return longProperty;
    }
    
    public void setLongProperty(Long longProperty) {
        this.longProperty = longProperty;
    }

    public Float getFloatProperty() {
        return floatProperty;
    }
    
    public void setFloatProperty(Float floatProperty) {
        this.floatProperty = floatProperty;
    }
    
    public Double getDoubleProperty() {
        return doubleProperty;
    }
    
    public void setDoubleProperty(Double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }
    
    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }
    
    public Date getDateProperty() {
        return dateProperty;
    }
    
    public void setDateProperty(Date dateProperty) {
        this.dateProperty = dateProperty;
    }
    
    public void entityCreated(BootstrapContext bootstrapContext) throws ResourceException {
    	log.info(" === +++ ===");
        new Thread(new TestReporter()).start();
        this.eventLookup = bootstrapContext.getEventLookupFacility();
        this.bootstrapContext = bootstrapContext;
        SleeContainer container = SleeContainer.lookupFromJndi();
		serviceContainer = container;
    }

    public void entityRemoved() {
    }

    public void entityActivated() throws ResourceException {
    	log.info("___ === ___");
    	
    	try {

			
    		ResourceAdaptorEntity resourceAdaptorEntity = serviceContainer.getResourceManagement().getResourceAdaptorEntity(this.bootstrapContext
					.getEntityName());
			
			log.info("___ = 1 = ___");
			
			// TCK LIKE TEST
			raTypeKey = resourceAdaptorEntity.getInstalledResourceAdaptor()
					.getRaType().getResourceAdaptorTypeID().getComponentKey();
			raKey = resourceAdaptorEntity.getInstalledResourceAdaptor()
					.getKey().getComponentKey();

			
			log.info("___ = 2 = ___");
			SleeContainer container = SleeContainer.lookupFromJndi();
			serviceContainer = container;
			activityContextFactory = serviceContainer.getActivityContextFactory();
			tm = serviceContainer.getTransactionManager();
			
			ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
					.getInstalledResourceAdaptor().getRaType()
					.getResourceAdaptorTypeID();
			log.info("___ = 3 = ___");
			acif=new DummyActivityContextInterfaceFactoryImpl(container,this.bootstrapContext.getEntityName());
			resourceAdaptorEntity.getServiceContainer().getResourceManagement()
					.getActivityContextInterfaceFactories()
					.put(raTypeId, acif);
			String entityName = this.bootstrapContext.getEntityName();
			log.info("___ = 4 = ___");
			try {
				if (acif != null) {

					String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) acif)
							.getJndiName();
					int begind = jndiName.indexOf(':');
					int toind = jndiName.lastIndexOf('/');
					String prefix = jndiName.substring(begind + 1, toind);
					String name = jndiName.substring(toind + 1);
					log
							.debug("jndiName prefix =" + prefix + "; jndiName = "
									+ name);
					log.info("___ = 5 = ___");
					SleeContainer.registerWithJndi(prefix, name, acif);
				}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			
			
			
				log.info("___ = 6 = ___");
			
			
		} catch (Exception ex) {
			
			throw new ResourceException(ex.getMessage());
		}
	
    	
    	
    }

    public void entityDeactivating() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            
        }
    }

    public void entityDeactivated() {
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
			e.printStackTrace();
		}
		nullAci = null;
    }

    public void eventProcessingSuccessful(ActivityHandle activityHandle, Object object, int i, Address address, int i0) {
    }

    public void eventProcessingFailed(ActivityHandle activityHandle, Object object, int i, Address address, int i0, FailureReason failureReason) {
    }

    public void activityEnded(ActivityHandle activityHandle) {
    }

    public void activityUnreferenced(ActivityHandle activityHandle) {
    }

    public void queryLiveness(ActivityHandle activityHandle) {
    }

    public Object getActivity(ActivityHandle handle) {
        return ((DummyActivityHandle) handle).getActivity();
    }

    public ActivityHandle getActivityHandle(Object object) {
        return new DummyActivityHandle("unknown");
    }

    public Object getSBBResourceAdaptorInterface(String string) {
        return this;
    }

    public Marshaler getMarshaler() {
        return null;
    }

    public void serviceInstalled(String serviceID, int[] eventIDs, String[] resourceOptions) {
    	log.info("------------------- serviceInstalled");
    	fireX1(serviceID, eventIDs, resourceOptions);
		// STORE SOME INFORMATION FOR LATER
    	log.info("------------------- fired");
    	eventIDsOfServicesInstalled.put(serviceID, eventIDs);
		eventResourceOptionsOfServicesInstalled.put(serviceID, resourceOptions);
    }

    public void serviceUninstalled(String serviceID) {
    	
    	eventIDsOfServicesInstalled.remove(serviceID);
		eventResourceOptionsOfServicesInstalled.remove(serviceID);
    	
    }

    public void serviceActivated(String string) {
    }

    public void serviceDeactivated(String string) {
    }
 
    private class TestReporter implements Runnable {
        public void run() {
            try {
                serverSocket = new ServerSocket(9201);
                Socket socket = serverSocket.accept();
            
                DataOutputStream in = new DataOutputStream(socket.getOutputStream());
                in.writeBoolean(booleanProperty.booleanValue());
                in.writeByte(byteProperty.byteValue());
                in.writeDouble(doubleProperty.doubleValue());
                in.writeFloat(floatProperty.floatValue());
                in.writeInt(intProperty.intValue());
                in.writeLong(longProperty.longValue());
                in.writeShort(shortProperty.shortValue());
                in.writeUTF(stringProperty);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    private void fireX1(String serviceID, int[] eventIDs,
			String[] resourceOptions) {
    	
    	SleeTransactionManager tm = SleeContainer.lookupFromJndi().getTransactionManager();
    	boolean newTx = tm.requireTransaction();
    	boolean rb = true;
    	
    	try {
    	
		Object[] message = makeMessage(serviceID, eventIDs, resourceOptions);

		TCKResourceEventX event1 = new TCKResourceEventImpl(12,
				TCKResourceEventX.X1, message, null);
		
			if (nullAci == null) {
				// check container state is not stopping
				if (SleeContainer.lookupFromJndi().getSleeState().equals(SleeState.STOPPING)) {
					return;
				}
				nullAci = retrieveNullActivityContext();
			}
				new DeferredEvent(eventLookup.getEventID(
						TckX1Key.getName(),TckX1Key.getVendor(),TckX1Key.getVersion()), event1, ((ActivityContextInterfaceImpl)nullAci).getActivityContext(), null);
			log.info("==== FIRED ====");
			
			rb = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			try {
				if (newTx) {
					if (rb) {
						tm.rollback();	
					}
					else {
						tm.commit();
					}
				}
				else {
					if (rb) {
						tm.setRollbackOnly();
					}
				}
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void fireX2(String serviceID, int[] eventIDs,
			String[] resourceOptions) {
		
		SleeTransactionManager tm = SleeContainer.lookupFromJndi().getTransactionManager();
    	boolean newTx = tm.requireTransaction();
    	boolean rb = true;
    	
    	try {
    		
    	
		Object[] message = makeMessage(serviceID, eventIDs, resourceOptions);

		TCKResourceEventX event1 = new TCKResourceEventImpl(12,
				TCKResourceEventX.X2, message, null);
		
			if (nullAci == null) {
				// check container state is not stopping
				if (SleeContainer.lookupFromJndi().getSleeState().equals(SleeState.STOPPING)) {
					return;
				}
				nullAci = retrieveNullActivityContext();
			}
			
			sleeInternalEndpoint.enqueueEvent(new DeferredEvent(eventLookup.getEventID(
					TckX2Key.getName(),TckX2Key.getVendor(),TckX2Key.getVersion()), event1, ((ActivityContextInterfaceImpl)nullAci).getActivityContext(), null));
		
			rb = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			try {
				if (newTx) {
					if (rb) {
						tm.rollback();	
					}
					else {
						tm.commit();
					}
				}
				else {
					if (rb) {
						tm.setRollbackOnly();
					}
				}
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void fireX3(String serviceID, int[] eventIDs,
			String[] resourceOptions) {
		
		SleeTransactionManager tm = SleeContainer.lookupFromJndi().getTransactionManager();
    	boolean newTx = tm.requireTransaction();
    	boolean rb = true;
    	
    	try {
    		
    	
		Object[] message = makeMessage(serviceID, eventIDs, resourceOptions);

		TCKResourceEventX event1 = new TCKResourceEventImpl(12,
				TCKResourceEventX.X3, message, null);
		
			if (nullAci == null) {
				// check container state is not stopping
				if (SleeContainer.lookupFromJndi().getSleeState().equals(SleeState.STOPPING)) {
					return;
				}
				nullAci = retrieveNullActivityContext();
			}
			
			sleeInternalEndpoint.enqueueEvent(new DeferredEvent(eventLookup.getEventID(
					TckX3Key.getName(),TckX3Key.getVendor(),TckX3Key.getVersion()), event1, ((ActivityContextInterfaceImpl)nullAci).getActivityContext(), null));
			
					rb = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			try {
				if (newTx) {
					if (rb) {
						tm.rollback();	
					}
					else {
						tm.commit();
					}
				}
				else {
					if (rb) {
						tm.setRollbackOnly();
					}
				}
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
    
    
	/**
	 * Encapsulates JNDI lookups for creation of nullActivityContextInterface.
	 * 
	 * @return New NullActivityContextInterface.
	 */
	protected static ActivityContextInterface retrieveNullActivityContext() {
		ActivityContextInterface localACI = null;
		NullActivityFactory nullACFactory = null;
		NullActivityContextInterfaceFactory nullActivityContextFactory = null;


		SleeContainer cotainer = SleeContainer.lookupFromJndi();

		nullACFactory = cotainer.getNullActivityFactory();

		NullActivity na = nullACFactory.createNullActivity();

		nullActivityContextFactory = cotainer
				.getNullActivityContextInterfaceFactory();

		try {

			localACI = nullActivityContextFactory
					.getActivityContextInterface(na);

		} catch (TransactionRequiredLocalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecognizedActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return localACI;
	}
    
	private Object[] makeMessage(String serviceID, int[] eventIDs,
			String[] resourceOptions) {
		Object message[] = new Object[7];
		String[] options = new String[resourceOptions.length];
		int[] ids = new int[eventIDs.length];
		
		for (int i = 0; i < ids.length; i++) {
			options[i] = resourceOptions[i];
			ids[i] = eventIDs[i];
			String tmp[]=null;
			try {
				tmp = eventLookup.getEventType(eventIDs[i]);
			} catch (FacilityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecognizedEventException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(tmp==null)
			{}
			else
			{
			
			}
		}
		message[0] = serviceID;
		message[1] = ids;
		message[2] = options;

		HashMap componentKeysToServiceIDs = new HashMap();
		Iterator it = myComponentKeys.keySet().iterator();

		while (it.hasNext()) {
			Object key = it.next();
			componentKeysToServiceIDs.put(key, new HashSet(
					(Set) myComponentKeys.get(key)));
		}
		message[3] = raTypeKey;
		message[4] = raKey;
		message[5] = componentKeysToServiceIDs;
		message[6] = null;
		return message;
	}
    
}
