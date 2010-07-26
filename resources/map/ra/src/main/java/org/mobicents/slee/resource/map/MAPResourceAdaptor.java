package org.mobicents.slee.resource.map;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.SLEEException;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
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

import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPServiceListener;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAcceptInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPCloseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPOpenInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseInfo;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortInfo;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.protocols.ss7.sccp.SccpProvider;

/**
 * 
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class MAPResourceAdaptor implements ResourceAdaptor, MAPDialogListener, MAPServiceListener {
	/**
	 * for all events we are interested in knowing when the event failed to be processed
	 */
	public static final int DEFAULT_EVENT_FLAGS = EventFlags.REQUEST_PROCESSING_FAILED_CALLBACK;

	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;// .NO_FLAGS;
	private static final String _CONFIG_OPT_NAME_CONF = "configName";

	private MAPStack mapStack = null;
	/**
	 * This is local proxy of provider.
	 */
	protected MAPProviderWrapper mapProviderWrapper = null;
	private Tracer tracer;
	private transient SleeEndpoint sleeEndpoint = null;

	private ConcurrentHashMap<Long, MAPDialogActivityHandle> handlers = new ConcurrentHashMap<Long, MAPDialogActivityHandle>();
	private ConcurrentHashMap<MAPDialogActivityHandle, MAPDialog> activities = new ConcurrentHashMap<MAPDialogActivityHandle, MAPDialog>();
	

	private ResourceAdaptorContext resourceAdaptorContext;

	private EventIDCache eventIdCache = null;

	//name of config file
	private String configName;


	private transient static final Address address = new Address(AddressPlan.IP, "localhost");

	public MAPResourceAdaptor() {
		// TODO Auto-generated constructor stub
	}

	public void activityEnded(ActivityHandle activityHandle) {
		if (this.tracer.isFineEnabled()) {
			if (this.tracer.isFineEnabled()) {
				this.tracer.fine("Activity with handle " + activityHandle + " ended");
			}
		}
		if (activityHandle instanceof MAPDialogActivityHandle) {
			MAPDialogActivityHandle mdah = (MAPDialogActivityHandle) activityHandle;

			this.handlers.remove(mdah.getMAPDialog().getDialogId());
			this.activities.remove(activityHandle);
		}
	}

	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void administrativeRemove(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public Object getActivity(ActivityHandle handle) {
		return ((MAPDialogActivityHandle) handle).getMAPDialog();
	}

	public ActivityHandle getActivityHandle(Object arg0) {
		Long id = ((MAPDialog) arg0).getDialogId();
		return handlers.get(id);
	}

	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getResourceAdaptorInterface(String arg0) {
		return this.mapProviderWrapper;
	}

	public void queryLiveness(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void raActive() {

		try {
			
			this.mapStack.start();
			org.mobicents.protocols.ss7.map.api.MAPProvider mapProvider = this.mapStack.getMAPProvider();

			this.mapProviderWrapper = new MAPProviderWrapper(mapProvider, this);

			mapProvider.addMAPDialogListener(this);
			mapProvider.addMAPServiceListener(this);

			this.sleeEndpoint = resourceAdaptorContext.getSleeEndpoint();
		} catch (Exception e) {
			this.tracer.severe("Failed to activate MAP RA ", e);
		}
	}

	public void raConfigurationUpdate(ConfigProperties arg0) {
		// TODO Auto-generated method stub

	}

	public void raConfigure(ConfigProperties arg0) {
		try {
			if (tracer.isInfoEnabled()) {
				tracer.info("Configuring MAPRA: " + this.resourceAdaptorContext.getEntityName());
			}
			Properties properties = new Properties();
			properties.load(getClass().getResourceAsStream("/" + configName));
			tracer.info("Loaded properties: " + properties);


			
			this.mapStack = new MAPStackImpl();
			this.mapStack.configure(properties);
			
			
		} catch (UnsatisfiedLinkError ex) {
			tracer.warning("JCC Resource adaptor is not attached to baord driver", ex);
		} catch (Exception e) {
			tracer.severe("Can not start Jcc Provider: ", e);

		}
	}

	public void raInactive() {
		org.mobicents.protocols.ss7.map.api.MAPProvider mapProvider = this.mapStack.getMAPProvider();
		mapProvider.removeMAPDialogListener(this);
		mapProvider.removeMAPServiceListener(this);
		this.mapStack.stop();

	}

	public void raStopping() {
		// TODO Auto-generated method stub

	}

	public void raUnconfigure() {
		// TODO Auto-generated method stub

	}

	public void raVerifyConfiguration(ConfigProperties cps) throws InvalidConfigurationException {
		try {

			if (tracer.isInfoEnabled()) {
				tracer.info("Verifyin configuring MAPRA: " + this.resourceAdaptorContext.getEntityName());
			}
			this.configName = (String) cps.getProperty(_CONFIG_OPT_NAME_CONF).getValue();

			if (this.configName == null) {
				throw new InvalidConfigurationException("No name set for configuration file.");
			}

			if (null == getClass().getResource("/" + configName)) {
				throw new InvalidConfigurationException("Configuration file: " + configName + ", can not be located.");
			}

		} catch (InvalidConfigurationException e) {
			throw e;
		} catch (Exception e) {
			throw new InvalidConfigurationException("Failed to test configuration options!", e);
		}

	}

	public void serviceActive(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	public void serviceInactive(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	public void serviceStopping(ReceivableService arg0) {
		// TODO Auto-generated method stub

	}

	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) {
		this.resourceAdaptorContext = raContext;
		this.tracer = resourceAdaptorContext.getTracer(MAPResourceAdaptor.class.getSimpleName());

		this.eventIdCache = new EventIDCache(this.tracer);
	}

	public void unsetResourceAdaptorContext() {
		this.resourceAdaptorContext = null;
	}

	/**
	 * MAPDialogListener methods
	 */
	public void onMAPAcceptInfo(MAPAcceptInfo mapAcceptInfo) {
		MAPDialog mapDialog = mapAcceptInfo.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Received MAPAcceptInfo for DialogId " + mapDialog.getDialogId());
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe("Received MAPAcceptInfo but there is no Handler for this Dialog");
			return;
		}

		this.fireEvent("org.mobicents.protocols.ss7.map.ACCEPT_INFO", handle, mapAcceptInfo);
	}

	public void onMAPCloseInfo(MAPCloseInfo mapCloseInfo) {
		MAPDialog mapDialog = mapCloseInfo.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Received MAPCloseInfo for DialogId " + mapDialog.getDialogId());
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe("Received MAPCloseInfo but there is no Handler for this Dialog");
			return;
		}

		this.fireEvent("org.mobicents.protocols.ss7.map.CLOSE_INFO", handle, mapCloseInfo);
		// close
		this.sleeEndpoint.endActivity(handle);
	}

	public void onMAPOpenInfo(MAPOpenInfo mapOpenInfo) {
		MAPDialog mapDialog = mapOpenInfo.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Received MAPOpenInfo for DialogId " + mapDialog.getDialogId());
		}

		try {
			MAPDialogActivityHandle handle = startActivity(mapDialog);
			this.fireEvent("org.mobicents.protocols.ss7.map.OPEN_INFO", handle, mapOpenInfo);
		} catch (ActivityAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SLEEException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StartActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private MAPDialogActivityHandle startActivity(MAPDialog mapDialog) throws ActivityAlreadyExistsException,
			NullPointerException, IllegalStateException, SLEEException, StartActivityException {
		MAPDialogActivityHandle handle = new MAPDialogActivityHandle(mapDialog);
		this.sleeEndpoint.startActivity(handle, mapDialog, ACTIVITY_FLAGS);
		this.handlers.put(mapDialog.getDialogId(), handle);
		this.activities.put(handle, mapDialog);
		return handle;

	}

	public void onMAPProviderAbortInfo(MAPProviderAbortInfo mapProviderAbortInfo) {
		MAPDialog mapDialog = mapProviderAbortInfo.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Received MAPProviderAbortInfo for DialogId " + mapDialog.getDialogId());
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe("Received MAPProviderAbortInfo but there is no Handler for this Dialog");
			return;
		}

		this.fireEvent("org.mobicents.protocols.ss7.map.PROVIDER_ABORT_INFO", handle, mapProviderAbortInfo);

		// close
		this.sleeEndpoint.endActivity(handle);

	}

	public void onMAPRefuseInfo(MAPRefuseInfo arg0) {
		// TODO Auto-generated method stub

	}

	public void onMAPUserAbortInfo(MAPUserAbortInfo mapUserAbortInfo) {
		MAPDialog mapDialog = mapUserAbortInfo.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Received MAPUserAbortInfo for DialogId " + mapDialog.getDialogId());
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe("Received MAPUserAbortInfo but there is no Handler for this Dialog");
			return;
		}

		this.fireEvent("org.mobicents.protocols.ss7.map.USER_ABORT_INFO", handle, mapUserAbortInfo);
		// close
		this.sleeEndpoint.endActivity(handle);
	}

	/**
	 * MAPServiceListener methods
	 */
	public void onProcessUnstructuredSSIndication(ProcessUnstructuredSSIndication processUnstrSSInd) {
		MAPDialog mapDialog = processUnstrSSInd.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Received ProcessUnstructuredSSIndication for DialogId " + mapDialog.getDialogId());
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe("Received ProcessUnstructuredSSIndication but there is no Handler for this Dialog");
			return;
		}

		this.fireEvent("org.mobicents.protocols.ss7.map.PROCESS_UNSTRUCTURED_SS_REQUEST_INDICATION", handle,
				processUnstrSSInd);
	}

	public void onUnstructuredSSIndication(UnstructuredSSIndication unstrSSInd) {
		MAPDialog mapDialog = unstrSSInd.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Received UnstructuredSSIndication for DialogId " + mapDialog.getDialogId());
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe("Received UnstructuredSSIndication but there is no Handler for this Dialog");
			return;
		}

		this.fireEvent("org.mobicents.protocols.ss7.map.UNSTRUCTURED_SS_REQUEST_INDICATION", handle, unstrSSInd);
	}

	/**
	 * Private methods
	 */
	private void fireEvent(String eventName, ActivityHandle handle, Object event) {

		FireableEventType eventID = eventIdCache.getEventId(this.resourceAdaptorContext.getEventLookupFacility(),
				eventName);

		if (eventID == null) {
			tracer.severe("Event id for " + eventID + " is unknown, cant fire!!!");
		} else {
			try {
				sleeEndpoint.fireEvent(handle, eventID, event, address, null);
			} catch (UnrecognizedActivityHandleException e) {
				this.tracer.severe("Error while firing event", e);
			} catch (IllegalEventException e) {
				this.tracer.severe("Error while firing event", e);
			} catch (ActivityIsEndingException e) {
				this.tracer.severe("Error while firing event", e);
			} catch (NullPointerException e) {
				this.tracer.severe("Error while firing event", e);
			} catch (SLEEException e) {
				this.tracer.severe("Error while firing event", e);
			} catch (FireEventException e) {
				this.tracer.severe("Error while firing event", e);
			}
		}
	}

	protected void endActivity(MAPDialog mapDialog) {
		MAPDialogActivityHandle mapDialogActHndl = this.handlers.get(mapDialog.getDialogId());
		if (mapDialogActHndl == null) {
			if (this.tracer.isWarningEnabled()) {
				this.tracer.warning("Activity ended but no MAPDialogActivityHandle found for Dialog ID "
						+ mapDialog.getDialogId());
			}
		} else {
			this.sleeEndpoint.endActivity(mapDialogActHndl);
		}
	}

}
