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
import javax.slee.resource.ConfigProperties.Property;
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
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequestIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponseIndication;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.slee.resource.map.events.DialogAccept;
import org.mobicents.slee.resource.map.events.DialogClose;
import org.mobicents.slee.resource.map.events.DialogDelimiter;
import org.mobicents.slee.resource.map.events.DialogNotice;
import org.mobicents.slee.resource.map.events.DialogProviderAbort;
import org.mobicents.slee.resource.map.events.DialogReject;
import org.mobicents.slee.resource.map.events.DialogRequest;
import org.mobicents.slee.resource.map.events.DialogTimeout;
import org.mobicents.slee.resource.map.events.DialogUserAbort;
import org.mobicents.slee.resource.map.events.ErrorComponent;
import org.mobicents.slee.resource.map.events.InvokeTimeout;
import org.mobicents.slee.resource.map.events.ProviderErrorComponent;
import org.mobicents.slee.resource.map.events.RejectComponent;
import org.mobicents.slee.resource.map.events.service.suplementary.*;
import org.mobicents.slee.resource.map.events.service.lsm.*;
import org.mobicents.slee.resource.map.events.service.sms.*;
import org.mobicents.slee.resource.map.wrappers.MAPProviderWrapper;

/**
 * 
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class MAPResourceAdaptor implements ResourceAdaptor, MAPDialogListener, MAPServiceSupplementaryListener, MAPServiceLsmListener, MAPServiceSmsListener {
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
	protected MAPProviderWrapper mapProvider = null;
	protected MAPProvider realProvider = null; // so we dont have to "get"
	private Tracer tracer;
	private transient SleeEndpoint sleeEndpoint = null;

	// require ONLY for one method.... blah
	private ConcurrentHashMap<Long, MAPDialogActivityHandle> handlers = new ConcurrentHashMap<Long, MAPDialogActivityHandle>();
	private ConcurrentHashMap<MAPDialogActivityHandle, MAPDialog> activities = new ConcurrentHashMap<MAPDialogActivityHandle, MAPDialog>();

	private ResourceAdaptorContext resourceAdaptorContext;

	private EventIDCache eventIdCache = null;

	// name of config file
	private SccpProvider sccpProvider;

	// ////////////////////////////
	// Configuration parameters //
	// ////////////////////////////
	private static final int NO_TIMEOUT = -1;
	private static final String CONF_SSN = "ssn";
	private static final String CONF_SCCP_JNDI = "sccpJndi";
	private static final String CONF_TIMEOUT = "timeout";

	private int ssn;
	private String SccpJndi = null;
	private int timeoutCount = NO_TIMEOUT;
	private transient static final Address address = new Address(AddressPlan.IP, "localhost");

	public MAPResourceAdaptor() {
		this.mapProvider = new MAPProviderWrapper(this);
	}

	// ////////////////
	// RA callbacks //
	// ////////////////
	public void activityEnded(ActivityHandle activityHandle) {
		if (this.tracer.isFineEnabled()) {
			if (this.tracer.isFineEnabled()) {
				this.tracer.fine("Activity with handle " + activityHandle + " ended");
			}
		}
		MAPDialogActivityHandle mdah = (MAPDialogActivityHandle) activityHandle;

		this.handlers.remove(mdah.getDialogId());
		this.activities.remove(activityHandle);
	}

	public void activityUnreferenced(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void administrativeRemove(ActivityHandle arg0) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingFailed(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5,
			FailureReason arg6) {
		// TODO Auto-generated method stub

	}

	public void eventProcessingSuccessful(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1, Object arg2, Address arg3, ReceivableService arg4, int arg5) {
		// TODO Auto-generated method stub

	}

	public Object getActivity(ActivityHandle handle) {
		if (handle instanceof MAPDialogActivityHandle) {
			return this.activities.get(handle);
		} else {
			return null;
		}
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
			this.realProvider = this.mapStack.getMAPProvider();

			this.realProvider.addMAPDialogListener(this);
			this.realProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
			this.realProvider.getMAPServiceSms().addMAPServiceListener(this);
			this.realProvider.getMAPServiceLsm().addMAPServiceListener(this);

			this.sleeEndpoint = resourceAdaptorContext.getSleeEndpoint();

			this.realProvider.getMAPServiceSupplementary().acivate();
			this.realProvider.getMAPServiceSms().acivate();
			this.realProvider.getMAPServiceLsm().acivate();

			this.mapProvider.setWrappedProvider(this.realProvider);
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

			this.ssn = (Integer) properties.getProperty(CONF_SSN).getValue();
			this.SccpJndi = (String) properties.getProperty(CONF_SCCP_JNDI).getValue();
			this.timeoutCount = (Integer) properties.getProperty(CONF_TIMEOUT).getValue();

		} catch (Exception e) {
			tracer.severe("Configuring of MAP RA failed ", e);
		}
	}

	public void raInactive() {
		this.realProvider.getMAPServiceSupplementary().deactivate();
		this.realProvider.getMAPServiceLsm().deactivate();
		this.realProvider.getMAPServiceSms().deactivate();
		this.realProvider.getMAPServiceSupplementary().removeMAPServiceListener(this);
		this.realProvider.getMAPServiceLsm().removeMAPServiceListener(this);
		this.realProvider.getMAPServiceSms().removeMAPServiceListener(this);
		this.realProvider.removeMAPDialogListener(this);
		this.mapStack.stop();
	}

	public void raStopping() {
		// TODO Auto-generated method stub

	}

	public void raUnconfigure() {
		this.ssn = 0;
		this.SccpJndi = null;
		this.timeoutCount = NO_TIMEOUT;
	}

	public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
		try {

			if (tracer.isInfoEnabled()) {
				tracer.info("Verifying configuring MAPRA: " + this.resourceAdaptorContext.getEntityName());
			}
			this.ssn = (Integer) properties.getProperty(CONF_SSN).getValue();

			if (this.ssn < 0 || this.ssn == 0) {
				throw new InvalidConfigurationException("Subsystem Number should be greater than 0");
			}

			this.SccpJndi = (String) properties.getProperty(CONF_SCCP_JNDI).getValue();
			if (this.SccpJndi == null) {
				throw new InvalidConfigurationException("SCCP JNDI lookup name cannot be null");
			}

			Property p = properties.getProperty(CONF_TIMEOUT);
			if (p != null) {
				Integer i = (Integer) p.getValue();
				if (i < NO_TIMEOUT) {
					throw new InvalidConfigurationException("Wrong value of '" + CONF_TIMEOUT + "' property: " + i);
				}
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

	// //////////////////
	// Helper methods //
	// //////////////////

	public MAPDialogActivityHandle createActivity(MAPDialog mapDialog) throws ActivityAlreadyExistsException, NullPointerException, IllegalStateException,
			SLEEException, StartActivityException {
		MAPDialogActivityHandle handle = new MAPDialogActivityHandle(mapDialog);
		this.sleeEndpoint.startActivity(handle, mapDialog, ACTIVITY_FLAGS);
		this.handlers.put(mapDialog.getDialogId(), handle);
		this.activities.put(handle, mapDialog);
		return handle;

	}

	/**
	 * Private methods
	 */
	private void fireEvent(String eventName, ActivityHandle handle, Object event) {

		FireableEventType eventID = eventIdCache.getEventId(this.resourceAdaptorContext.getEventLookupFacility(), eventName);

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
				this.tracer.warning("Activity ended but no MAPDialogActivityHandle found for Dialog ID " + mapDialog.getDialogId());
			}
		} else {
			this.sleeEndpoint.endActivity(mapDialogActHndl);
			System.out.println("Ended " + mapDialogActHndl);
		}
	}

	// /////////////////
	// Event helpers //
	// /////////////////

	private MAPDialogActivityHandle onEvent(String eventName, MAPDialog mapDialog, Object event) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : %s for DialogId=%d", eventName, mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : %s but there is no Handler for this DialogId=%d", eventName, mapDialog.getDialogId()));
			return null;
		}
		handle.resetTimeoutCount();
		this.fireEvent(eventName, handle, event);
		return handle;
	}

	// ////////////////////
	// Dialog callbacks //
	// ////////////////////
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extension) {
		onEvent("ss7.map.DIALOG_ACCEPT", mapDialog, new DialogAccept(mapDialog, extension));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogClose(MAPDialog mapDialog) {

		MAPDialogActivityHandle handle = onEvent("ss7.map.DIALOG_CLOSE", mapDialog, new DialogClose(mapDialog));

		// End Activity
		if (handle != null)
			this.sleeEndpoint.endActivity(handle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		onEvent("ss7.map.DIALOG_DELIMITER", mapDialog, new DialogDelimiter(mapDialog));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		onEvent("ss7.map.DIALOG_NOTICE", mapDialog, new DialogNotice(mapDialog, noticeProblemDiagnostic));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource,
			MAPExtensionContainer extensionContainer) {

		MAPDialogActivityHandle handle = onEvent("ss7.map.DIALOG_PROVIDERABORT", mapDialog, new DialogProviderAbort(mapDialog, abortProviderReason,
				abortSource, extensionContainer));

		// End Activity
		if (handle != null)
			this.sleeEndpoint.endActivity(handle);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {

		MAPDialogActivityHandle handle = onEvent("ss7.map.DIALOG_REJECT", mapDialog, new DialogReject(mapDialog, refuseReason, providerError,
				alternativeApplicationContext, extensionContainer));

		// End Activity
		if (handle != null)
			this.sleeEndpoint.endActivity(handle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Received onDialogRequest id=%d ", mapDialog.getDialogId()));
		}

		try {
			MAPDialogActivityHandle handle = new MAPDialogActivityHandle(mapDialog);
			if (!this.activities.containsKey(handle)) {
				handle = createActivity(mapDialog);
			}

			DialogRequest event = new DialogRequest(mapDialog, destReference, origReference, extensionContainer);
			this.fireEvent("ss7.map.DIALOG_REQUEST", handle, event);
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

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
		MAPDialogActivityHandle handle = onEvent("ss7.map.DIALOG_USERABORT", mapDialog, new DialogUserAbort(mapDialog, userReason, extensionContainer));

		// ???
		// End Activity
		// if(handle!=null)
		// this.sleeEndpoint.endActivity(handle);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogResease(MAPDialog mapDialog) {

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : %s for DialogId=%d", "ss7.map.DIALOG_RELEASE", mapDialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(mapDialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : %s but there is no Handler for this DialogId=%d", "ss7.map.DIALOG_RELEASE", mapDialog.getDialogId()));
			return;
		}

		// End Activity
		this.sleeEndpoint.endActivity(handle);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogTimeout(MAPDialog dialog) {

		// TODO: done like that, since we want to process dialgo callbacks
		// before we fire event.
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogTimeout for DialogId=%d", dialog.getDialogId()));
		}

		MAPDialogActivityHandle handle = this.handlers.get(dialog.getDialogId());

		if (handle == null) {
			this.tracer.severe(String.format("Rx : DialogTimeout but there is no Handler for this DialogId=%d", dialog.getDialogId()));
			return;
		}

		if (this.timeoutCount == NO_TIMEOUT) {
			dialog.keepAlive();
		} else {
			handle.increateTimeoutCount();
			if (handle.getTimeoutCount() <= this.timeoutCount) {
				dialog.keepAlive();
			}
			// else, allow it to be terminated
		}
		DialogTimeout dt = new DialogTimeout(dialog);
		this.fireEvent("ss7.map.DIALOG_TIMEOUT", handle, dt);
	}

	// ///////////////////////
	// Component callbacks //
	// ///////////////////////
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInvokeTimeout(MAPDialog dialog, Long invokeId) {
		onEvent("ss7.map.INVOKE_TIMEOUT", dialog, new InvokeTimeout(dialog, invokeId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onErrorComponent(MAPDialog dialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		onEvent("ss7.map.ERROR_COMPONENT", dialog, new ErrorComponent(dialog, invokeId, mapErrorMessage));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProviderErrorComponent(MAPDialog dialog, Long invokeId, MAPProviderError mapProviderError) {
		onEvent("ss7.map.PROVIDER_ERROR_COMPONENT", dialog, new ProviderErrorComponent(dialog, invokeId, mapProviderError));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRejectComponent(MAPDialog dialog, Long invokeId, Problem problem) {
		onEvent("ss7.map.REJECT_COMPONENT", dialog, new RejectComponent(dialog, invokeId, problem));
	}

	// /////////////////////////
	// SERVICE: Suplementary //
	// /////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProcessUnstructuredSSRequestIndication(ProcessUnstructuredSSRequestIndication processUnstrSSInd) {
		onEvent("ss7.map.service.suplementary.PROCESS_UNSTRUCTURED_SS_REQUEST", processUnstrSSInd.getMAPDialog(), new ProcessUnstructuredSSRequest(
				processUnstrSSInd.getMAPDialog(), processUnstrSSInd));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProcessUnstructuredSSResponseIndication(ProcessUnstructuredSSResponseIndication unstrSSInd) {
		onEvent("ss7.map.service.suplementary.PROCESS_UNSTRUCTURED_SS_RESPONSE", unstrSSInd.getMAPDialog(),
				new ProcessUnstructuredSSResponse(unstrSSInd.getMAPDialog(), unstrSSInd));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUnstructuredSSRequestIndication(UnstructuredSSRequestIndication processUnstrSSInd) {
		onEvent("ss7.map.service.suplementary.UNSTRUCTURED_SS_REQUEST", processUnstrSSInd.getMAPDialog(),
				new UnstructuredSSRequest(processUnstrSSInd.getMAPDialog(), processUnstrSSInd));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUnstructuredSSResponseIndication(UnstructuredSSResponseIndication unstrSSInd) {
		onEvent("ss7.map.service.suplementary.UNSTRUCTURED_SS_RESPONSE", unstrSSInd.getMAPDialog(), new UnstructuredSSResponse(unstrSSInd.getMAPDialog(),
				unstrSSInd));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUnstructuredSSNotifyRequestIndication(UnstructuredSSNotifyRequestIndication unstrSSInd) {
		onEvent("ss7.map.service.suplementary.UNSTRUCTURED_SS_NOTIFY_REQUEST", unstrSSInd.getMAPDialog(),
				new UnstructuredSSNotifyRequest(unstrSSInd.getMAPDialog(), unstrSSInd));
	}

	// ////////////////
	// SERVICE: LSM //
	// ////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProvideSubscriberLocationRequestIndication(ProvideSubscriberLocationRequestIndication provideSubscriberLocationRequest) {
		onEvent("ss7.map.service.lsm.PROVIDE_SUBSCRIBER_LOCATION_REQUEST", provideSubscriberLocationRequest.getMAPDialog(),
				new ProvideSubscriberLocationRequest(provideSubscriberLocationRequest.getMAPDialog(), provideSubscriberLocationRequest));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProvideSubscriberLocationResponseIndication(ProvideSubscriberLocationResponseIndication provideSubscriberLocationResponse) {
		onEvent("ss7.map.service.lsm.PROVIDE_SUBSCRIBER_LOCATION_RESPONSE", provideSubscriberLocationResponse.getMAPDialog(),
				new ProvideSubscriberLocationResponse(provideSubscriberLocationResponse.getMAPDialog(), provideSubscriberLocationResponse));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSendRoutingInforForLCSRequestIndication(SendRoutingInfoForLCSRequestIndication sendRoutingInfoForLCSRequest) {
		onEvent("ss7.map.service.lsm.SEND_ROUTING_INFO_FOR_LCS_REQUEST", sendRoutingInfoForLCSRequest.getMAPDialog(), new SendRoutingInfoForLCSRequest(
				sendRoutingInfoForLCSRequest.getMAPDialog(), sendRoutingInfoForLCSRequest));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSendRoutingInforForLCSResponseIndication(SendRoutingInfoForLCSResponseIndication sendRoutingInfoForLCSResponse) {
		onEvent("ss7.map.service.lsm.SEND_ROUTING_INFO_FOR_LCS_RESPONSE", sendRoutingInfoForLCSResponse.getMAPDialog(), new SendRoutingInfoForLCSResponse(
				sendRoutingInfoForLCSResponse.getMAPDialog(), sendRoutingInfoForLCSResponse));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSubscriberLocationReportRequestIndication(SubscriberLocationReportRequestIndication subscriberLocationReportRequest) {
		onEvent("ss7.map.service.lsm.SUBSCRIBER_LOCATION_REPORT_REQUEST", subscriberLocationReportRequest.getMAPDialog(), new SubscriberLocationReportRequest(
				subscriberLocationReportRequest.getMAPDialog(), subscriberLocationReportRequest));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSubscriberLocationReportResponseIndication(SubscriberLocationReportResponseIndication subscriberLocationReportResponse) {
		onEvent("ss7.map.service.lsm.SUBSCRIBER_LOCATION_REPORT_RESPONSE", subscriberLocationReportResponse.getMAPDialog(),
				new SubscriberLocationReportResponse(subscriberLocationReportResponse.getMAPDialog(), subscriberLocationReportResponse));

	}

	// ////////////////
	// SERVICE: SMS //
	// ////////////////

	@Override
	public void onForwardShortMessageIndication(ForwardShortMessageRequestIndication forwardShortMessageRequest) {
		onEvent("ss7.map.service.sms.FORWARD_SHORT_MESSAGE_REQUEST", forwardShortMessageRequest.getMAPDialog(), new ForwardShortMessageRequest(
				forwardShortMessageRequest.getMAPDialog(), forwardShortMessageRequest));
	}

	@Override
	public void onForwardShortMessageRespIndication(ForwardShortMessageResponseIndication forwardShortMessageResponse) {
		onEvent("ss7.map.service.sms.FORWARD_SHORT_MESSAGE_RESPONSE", forwardShortMessageResponse.getMAPDialog(), new ForwardShortMessageResponse(
				forwardShortMessageResponse.getMAPDialog(), forwardShortMessageResponse));

	}

	@Override
	public void onMoForwardShortMessageIndication(MoForwardShortMessageRequestIndication moForwardShortMessageRequest) {
		onEvent("ss7.map.service.sms.MO_FORWARD_SHORT_MESSAGE_REQUEST", moForwardShortMessageRequest.getMAPDialog(), new MoForwardShortMessageRequest(
				moForwardShortMessageRequest.getMAPDialog(), moForwardShortMessageRequest));

	}

	@Override
	public void onMoForwardShortMessageRespIndication(MoForwardShortMessageResponseIndication moForwardShortMessageResponse) {
		onEvent("ss7.map.service.sms.MO_FORWARD_SHORT_MESSAGE_RESPONSE", moForwardShortMessageResponse.getMAPDialog(), new MoForwardShortMessageResponse(
				moForwardShortMessageResponse.getMAPDialog(), moForwardShortMessageResponse));
	}

	@Override
	public void onMtForwardShortMessageIndication(MtForwardShortMessageRequestIndication mtForwardShortMessageRequest) {
		onEvent("ss7.map.service.sms.MT_FORWARD_SHORT_MESSAGE_REQUEST", mtForwardShortMessageRequest.getMAPDialog(), new MtForwardShortMessageRequest(
				mtForwardShortMessageRequest.getMAPDialog(), mtForwardShortMessageRequest));
	}

	@Override
	public void onMtForwardShortMessageRespIndication(MtForwardShortMessageResponseIndication mtForwardShortMessageResponse) {
		onEvent("ss7.map.service.sms.MT_FORWARD_SHORT_MESSAGE_RESPONSE", mtForwardShortMessageResponse.getMAPDialog(), new MtForwardShortMessageResponse(
				mtForwardShortMessageResponse.getMAPDialog(), mtForwardShortMessageResponse));
	}

	@Override
	public void onSendRoutingInfoForSMIndication(SendRoutingInfoForSMRequestIndication sendRoutingInfoForSmRequest) {
		onEvent("ss7.map.service.sms.SEND_ROUTING_INFO_FOR_SM_REQUEST", sendRoutingInfoForSmRequest.getMAPDialog(), new SendRoutingInfoForSMRequest(
				sendRoutingInfoForSmRequest.getMAPDialog(), sendRoutingInfoForSmRequest));
	}

	@Override
	public void onSendRoutingInfoForSMRespIndication(SendRoutingInfoForSMResponseIndication sendRoutingInfoForSmResponse) {
		onEvent("ss7.map.service.sms.SEND_ROUTING_INFO_FOR_SM_RESPONSE", sendRoutingInfoForSmResponse.getMAPDialog(), new SendRoutingInfoForSMResponse(
				sendRoutingInfoForSmResponse.getMAPDialog(), sendRoutingInfoForSmResponse));
	}

	@Override
	public void onReportSMDeliveryStatusIndication(ReportSMDeliveryStatusRequestIndication reportSmDeliveryStatusRequest) {
		onEvent("ss7.map.service.sms.REPORT_SM_DELIVERY_STATUS_REQUEST", reportSmDeliveryStatusRequest.getMAPDialog(), new ReportSMDeliveryStatusRequest(
				reportSmDeliveryStatusRequest.getMAPDialog(), reportSmDeliveryStatusRequest));

	}

	@Override
	public void onReportSMDeliveryStatusRespIndication(ReportSMDeliveryStatusResponseIndication reportSmDeliveryStatusResponse) {
		onEvent("ss7.map.service.sms.REPORT_SM_DELIVERY_STATUS_RESPONSE", reportSmDeliveryStatusResponse.getMAPDialog(), new ReportSMDeliveryStatusResponse(
				reportSmDeliveryStatusResponse.getMAPDialog(), reportSmDeliveryStatusResponse));

	}

	@Override
	public void onInformServiceCentreIndication(InformServiceCentreRequestIndication informServiCecentreRequest) {
		onEvent("ss7.map.service.sms.INFORM_SERVICE_CENTER_REQUEST", informServiCecentreRequest.getMAPDialog(), new InformServiceCentreRequest(
				informServiCecentreRequest.getMAPDialog(), informServiCecentreRequest));
	}

	@Override
	public void onAlertServiceCentreIndication(AlertServiceCentreRequestIndication alertServiCecentreRequest) {
		onEvent("ss7.map.service.sms.ALERT_SERVICE_CENTER_REQUEST", alertServiCecentreRequest.getMAPDialog(), new AlertServiceCentreRequest(
				alertServiCecentreRequest.getMAPDialog(), alertServiCecentreRequest));

	}

	@Override
	public void onAlertServiceCentreRespIndication(AlertServiceCentreResponseIndication alertServiCecentreResponse) {
		onEvent("ss7.map.service.sms.ALERT_SERVICE_CENTER_RESPONSE", alertServiCecentreResponse.getMAPDialog(), new AlertServiceCentreResponse(
				alertServiCecentreResponse.getMAPDialog(), alertServiCecentreResponse));

	}

}
