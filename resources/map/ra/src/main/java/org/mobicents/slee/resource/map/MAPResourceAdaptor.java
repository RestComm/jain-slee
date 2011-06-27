/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.map;

import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;
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
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;

/**
 * 
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class MAPResourceAdaptor implements ResourceAdaptor, MAPDialogListener, MAPServiceSupplementaryListener {
	/**
	 * for all events we are interested in knowing when the event failed to be
	 * processed
	 */
	public static final int DEFAULT_EVENT_FLAGS = EventFlags.REQUEST_PROCESSING_FAILED_CALLBACK;

	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;// .NO_FLAGS;

	private MAPStackImpl mapStack = null;
	/**
	 * This is local proxy of provider.
	 */
	protected MAPProvider mapProvider = null;

	private Tracer tracer;
	private transient SleeEndpoint sleeEndpoint = null;

	private ConcurrentHashMap<Long, MAPDialogActivityHandle> handlers = new ConcurrentHashMap<Long, MAPDialogActivityHandle>();
	private ConcurrentHashMap<MAPDialogActivityHandle, MAPDialog> activities = new ConcurrentHashMap<MAPDialogActivityHandle, MAPDialog>();

	private ResourceAdaptorContext resourceAdaptorContext;

	private EventIDCache eventIdCache = null;

	// name of config file
	private SccpProvider sccpProvider;

	private int ssn;
	private String SccpJndi = null;

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
		MAPDialogActivityHandle mdah = (MAPDialogActivityHandle) activityHandle;

		this.handlers.remove(mdah.getMAPDialog().getDialogId());
		this.activities.remove(activityHandle);
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
		return this.mapProvider;
	}

	public void queryLiveness(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void raActive() {

		try {
			InitialContext ic = new InitialContext();
			sccpProvider = (SccpProvider) ic.lookup(this.SccpJndi);
			tracer.info("Sucssefully connected to SCCP service[" + this.SccpJndi + "]");

			this.mapStack = new MAPStackImpl(sccpProvider, this.ssn);

			this.mapStack.start();
			this.mapProvider = this.mapStack.getMAPProvider();

			this.mapProvider.addMAPDialogListener(this);
			this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(this);

			this.sleeEndpoint = resourceAdaptorContext.getSleeEndpoint();

			this.mapProvider.getMAPServiceSupplementary().acivate();
		} catch (Exception e) {
			this.tracer.severe("Failed to activate MAP RA ", e);
		}
	}

	public void raConfigurationUpdate(ConfigProperties properties) {
		raConfigure(properties);
	}

	public void raConfigure(ConfigProperties properties) {
		try {
			if (tracer.isInfoEnabled()) {
				tracer.info("Configuring MAPRA: " + this.resourceAdaptorContext.getEntityName());
			}

			this.ssn = (Integer) properties.getProperty("ssn").getValue();
			this.SccpJndi = (String) properties.getProperty("sccpJndi").getValue();

		} catch (Exception e) {
			tracer.severe("Configuring of MAP RA failed ", e);
		}
	}

	public void raInactive() {
		this.mapProvider.getMAPServiceSupplementary().deactivate();
		this.mapProvider.getMAPServiceSupplementary().removeMAPServiceListener(this);
		this.mapProvider.removeMAPDialogListener(this);
		this.mapStack.stop();
	}

	public void raStopping() {
		// TODO Auto-generated method stub

	}

	public void raUnconfigure() {
		this.ssn = 0;
		this.SccpJndi = null;
	}

	public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
		try {

			if (tracer.isInfoEnabled()) {
				tracer.info("Verifyin configuring MAPRA: " + this.resourceAdaptorContext.getEntityName());
			}
			this.ssn = (Integer) properties.getProperty("ssn").getValue();

			if (this.ssn < 0 || this.ssn == 0) {
				throw new InvalidConfigurationException("Subsystem Number should be greater than 0");
			}

			this.SccpJndi = (String) properties.getProperty("sccpJndi").getValue();
			if (this.SccpJndi == null) {
				throw new InvalidConfigurationException("SCCP JNDI lookup name cannot be null");
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

	private MAPDialogActivityHandle startActivity(MAPDialog mapDialog) throws ActivityAlreadyExistsException,
			NullPointerException, IllegalStateException, SLEEException, StartActivityException {
		MAPDialogActivityHandle handle = new MAPDialogActivityHandle(mapDialog);
		this.sleeEndpoint.startActivity(handle, mapDialog, ACTIVITY_FLAGS);
		this.handlers.put(mapDialog.getDialogId(), handle);
		this.activities.put(handle, mapDialog);
		return handle;

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
			System.out.println("Ended " + mapDialogActHndl);
		}
	}

	protected MAPDialog getMAPDialog(Long dialogId) {
		MAPDialogActivityHandle handle = this.handlers.get(dialogId);
		if (handle != null) {
			handle.getMAPDialog();
		}

		return null;
	}

	/**
	 * MAPDialogListener methods
	 */
	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extension) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogAccept for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogAccept but there is no Handler for this DialogId=%d",
					mapDialog.getDialogId()));
			return;
		}

		DialogAccept accept = new DialogAccept(handle.getMAPDialog(), extension);

		this.fireEvent("org.mobicents.protocols.ss7.map.DIALOG_ACCEPT", handle, accept);

	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogClose for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogClose but there is no Handler for this DialogId=%d",
					mapDialog.getDialogId()));
			return;
		}

		DialogClose close = new DialogClose(handle.getMAPDialog());

		this.fireEvent("org.mobicents.protocols.ss7.map.DIALOG_CLOSE", handle, close);

		// End Activity
		this.sleeEndpoint.endActivity(handle);
	}

	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogDelimiter for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogDelimiter but there is no Handler for this DialogId=%d",
					mapDialog.getDialogId()));
			return;
		}

		DialogDelimiter delimiter = new DialogDelimiter(handle.getMAPDialog());

		this.fireEvent("org.mobicents.protocols.ss7.map.DIALOG_DELIMITER", handle, delimiter);
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogNotice for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogNotice but there is no Handler for this DialogId=%d",
					mapDialog.getDialogId()));
			return;
		}

		DialogNotice notice = new DialogNotice(handle.getMAPDialog(), noticeProblemDiagnostic);

		this.fireEvent("org.mobicents.protocols.ss7.map.DIALOG_NOTICE", handle, notice);
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogProviderAbort for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogProviderAbort but there is no Handler for this DialogId=%d",
					mapDialog.getDialogId()));
			return;
		}

		DialogProviderAbort abort = new DialogProviderAbort(handle.getMAPDialog(), abortProviderReason, abortSource,
				extensionContainer);

		this.fireEvent("org.mobicents.protocols.ss7.map.DIALOG_PROVIDERABORT", handle, abort);

		// End Activity
		this.sleeEndpoint.endActivity(handle);
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogReject for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogReject but there is no Handler for this DialogId=%d",
					mapDialog.getDialogId()));
			return;
		}

		DialogReject reject = new DialogReject(handle.getMAPDialog(), refuseReason, providerError,
				alternativeApplicationContext, extensionContainer);

		this.fireEvent("org.mobicents.protocols.ss7.map.DIALOG_REJECT", handle, reject);

		// End Activity
		this.sleeEndpoint.endActivity(handle);
	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Received onDialogRequest id=%d ", mapDialog.getDialogId()));
		}

		switch (mapDialog.getApplicationContext()) {
		case networkUnstructuredSsContextV2:
			try {
				MAPDialogActivityHandle handle = startActivity(mapDialog);
				DialogRequest event = new DialogRequest(handle.getMAPDialog(), destReference, origReference,
						extensionContainer);
				this.fireEvent("org.mobicents.protocols.ss7.map.DIALOG_REQUEST", handle, event);
			} catch (ActivityAlreadyExistsException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (SLEEException e) {
				e.printStackTrace();
			} catch (StartActivityException e) {
				e.printStackTrace();
			}
			break;
		default:
			this.tracer.severe(String.format("Received unknown MAP ApplicationContext=%s",
					mapDialog.getApplicationContext()));
			// TODO : Abort Dialog?
			return;
		}

	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogUserAbort for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogUserAbort but there is no Handler for this DialogId=%d",
					mapDialog.getDialogId()));
			return;
		}

		DialogUserAbort abort = new DialogUserAbort(handle.getMAPDialog(), userReason, extensionContainer);

		this.fireEvent("org.mobicents.protocols.ss7.map.DIALOG_USERABORT", handle, abort);
	}

	@Override
	public void onDialogResease(MAPDialog mapDialog) {

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogResease for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogResease but there is no Handler for this DialogId=%d",
					mapDialog.getDialogId()));
			return;
		}

		// End Activity
		this.sleeEndpoint.endActivity(handle);

	}

	/**
	 * MAPServiceSupplementaryListener Methods
	 */

	@Override
	public void onProcessUnstructuredSSIndication(ProcessUnstructuredSSIndication processUnstrSSInd) {
		MAPDialog mapDialog = processUnstrSSInd.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Rx : ProcessUnstructuredSSIndication for DialogId " + mapDialog.getDialogId());
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe("Rx : ProcessUnstructuredSSIndication but there is no Handler for this Dialog");
			return;
		}

		this.fireEvent("org.mobicents.protocols.ss7.map.PROCESS_UNSTRUCTURED_SS_REQUEST_INDICATION", handle,
				processUnstrSSInd);
	}

	@Override
	public void onUnstructuredSSIndication(UnstructuredSSIndication unstrSSInd) {
		MAPDialog mapDialog = unstrSSInd.getMAPDialog();

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Rx : UnstructuredSSIndication for DialogId " + mapDialog.getDialogId());
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe("Rx : UnstructuredSSIndication but there is no Handler for this Dialog");
			return;
		}

		this.fireEvent("org.mobicents.protocols.ss7.map.UNSTRUCTURED_SS_REQUEST_INDICATION", handle, unstrSSInd);
	}
}
