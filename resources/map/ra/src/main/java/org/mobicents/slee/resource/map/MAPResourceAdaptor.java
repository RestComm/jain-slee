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

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.MAPMessage;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.mobicents.protocols.ss7.map.api.dialog.MAPDialogState;
import org.mobicents.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderError;
import org.mobicents.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.AnyTimeInterrogationRequest;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.AnyTimeInterrogationResponse;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPDialogSubscriberInformation;
import org.mobicents.protocols.ss7.map.api.service.subscriberInformation.MAPServiceSubscriberInformationListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.slee.resource.map.events.DialogAccept;
import org.mobicents.slee.resource.map.events.DialogClose;
import org.mobicents.slee.resource.map.events.DialogDelimiter;
import org.mobicents.slee.resource.map.events.DialogNotice;
import org.mobicents.slee.resource.map.events.DialogProviderAbort;
import org.mobicents.slee.resource.map.events.DialogReject;
import org.mobicents.slee.resource.map.events.DialogRelease;
import org.mobicents.slee.resource.map.events.DialogRequest;
import org.mobicents.slee.resource.map.events.DialogTimeout;
import org.mobicents.slee.resource.map.events.DialogUserAbort;
import org.mobicents.slee.resource.map.events.ErrorComponent;
import org.mobicents.slee.resource.map.events.InvokeTimeout;
import org.mobicents.slee.resource.map.events.MAPEvent;
import org.mobicents.slee.resource.map.events.ProviderErrorComponent;
import org.mobicents.slee.resource.map.events.RejectComponent;
import org.mobicents.slee.resource.map.service.lsm.wrappers.MAPDialogLsmWrapper;
import org.mobicents.slee.resource.map.service.lsm.wrappers.ProvideSubscriberLocationRequestWrapper;
import org.mobicents.slee.resource.map.service.lsm.wrappers.ProvideSubscriberLocationResponseWrapper;
import org.mobicents.slee.resource.map.service.lsm.wrappers.SendRoutingInfoForLCSRequestWrapper;
import org.mobicents.slee.resource.map.service.lsm.wrappers.SendRoutingInfoForLCSResponseWrapper;
import org.mobicents.slee.resource.map.service.lsm.wrappers.SubscriberLocationReportRequestWrapper;
import org.mobicents.slee.resource.map.service.lsm.wrappers.SubscriberLocationReportResponseWrapper;
import org.mobicents.slee.resource.map.service.mobility.authentication.wrapper.SendAuthenticationInfoRequestWrapper;
import org.mobicents.slee.resource.map.service.mobility.authentication.wrapper.SendAuthenticationInfoResponseWrapper;
import org.mobicents.slee.resource.map.service.mobility.wrappers.MAPDialogMobilityWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.AlertServiceCentreRequestWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.AlertServiceCentreResponseWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.ForwardShortMessageRequestWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.ForwardShortMessageResponseWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.InformServiceCentreRequestWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.MAPDialogSmsWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.MoForwardShortMessageRequestWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.MoForwardShortMessageResponseWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.MtForwardShortMessageRequestWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.MtForwardShortMessageResponseWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.ReportSMDeliveryStatusRequestWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.ReportSMDeliveryStatusResponseWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.SendRoutingInfoForSMRequestWrapper;
import org.mobicents.slee.resource.map.service.sms.wrappers.SendRoutingInfoForSMResponseWrapper;
import org.mobicents.slee.resource.map.service.subscriberInformation.wrappers.AnyTimeInterrogationRequestWrapper;
import org.mobicents.slee.resource.map.service.subscriberInformation.wrappers.AnyTimeInterrogationResponseWrapper;
import org.mobicents.slee.resource.map.service.subscriberInformation.wrappers.MAPDialogSubscriberInformationWrapper;
import org.mobicents.slee.resource.map.service.supplementary.wrappers.MAPDialogSupplementaryWrapper;
import org.mobicents.slee.resource.map.service.supplementary.wrappers.ProcessUnstructuredSSRequestWrapper;
import org.mobicents.slee.resource.map.service.supplementary.wrappers.ProcessUnstructuredSSResponseWrapper;
import org.mobicents.slee.resource.map.service.supplementary.wrappers.UnstructuredSSNotifyRequestWrapper;
import org.mobicents.slee.resource.map.service.supplementary.wrappers.UnstructuredSSNotifyResponseWrapper;
import org.mobicents.slee.resource.map.service.supplementary.wrappers.UnstructuredSSRequestWrapper;
import org.mobicents.slee.resource.map.service.supplementary.wrappers.UnstructuredSSResponseWrapper;
import org.mobicents.slee.resource.map.wrappers.MAPDialogWrapper;
import org.mobicents.slee.resource.map.wrappers.MAPProviderWrapper;

/**
 * 
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class MAPResourceAdaptor implements ResourceAdaptor, MAPDialogListener, MAPServiceSupplementaryListener,
		MAPServiceLsmListener, MAPServiceSmsListener, MAPServiceSubscriberInformationListener,
		MAPServiceMobilityListener {
	/**
	 * for all events we are interested in knowing when the event failed to be
	 * processed
	 */
	public static final int DEFAULT_EVENT_FLAGS = EventFlags.REQUEST_PROCESSING_FAILED_CALLBACK;

	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;// .NO_FLAGS;

	/**
	 * This is local proxy of provider.
	 */
	protected MAPProviderWrapper mapProvider = null;
	protected MAPProvider realProvider = null; // so we dont have to "get"
	private Tracer tracer;
	private transient SleeEndpoint sleeEndpoint = null;

	private ResourceAdaptorContext resourceAdaptorContext;

	private EventIDCache eventIdCache = null;

	/**
	 * tells the RA if an event with a specified ID should be filtered or not
	 */
	private final EventIDFilter eventIDFilter = new EventIDFilter();

	// ////////////////////////////
	// Configuration parameters //
	// ////////////////////////////
	private static final String CONF_MAP_JNDI = "mapJndi";

	private String mapJndi = null;
	private transient static final Address address = new Address(AddressPlan.IP, "localhost");

	public MAPResourceAdaptor() {
		this.mapProvider = new MAPProviderWrapper(this);
	}

	// ////////////////
	// RA callbacks //
	// ////////////////
	public void activityEnded(ActivityHandle activityHandle) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("Activity with handle " + activityHandle + " ended");
		}
		MAPDialogActivityHandle mdah = (MAPDialogActivityHandle) activityHandle;
		final MAPDialogWrapper dw = mdah.getActivity();
		mdah.setActivity(null);

		if (dw != null) {
			dw.clear();
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
		return ((MAPDialogActivityHandle) handle).getActivity();
	}

	public ActivityHandle getActivityHandle(Object activity) {
		if (activity instanceof MAPDialogWrapper) {
			final MAPDialogWrapper wrapper = ((MAPDialogWrapper) activity);
			if (wrapper.getRa() == this) {
				return wrapper.getActivityHandle();
			}
		}

		return null;
	}

	public Marshaler getMarshaler() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getResourceAdaptorInterface(String arg0) {
		return this.mapProvider;
	}

	public void queryLiveness(ActivityHandle activityHandle) {
		final MAPDialogActivityHandle handle = ((MAPDialogActivityHandle) activityHandle);
		final MAPDialogWrapper mapDialog = handle.getActivity();
		if (mapDialog == null || mapDialog.getWrappedDialog() == null
				|| mapDialog.getState() == MAPDialogState.EXPUNGED) {
			sleeEndpoint.endActivity(handle);
		}
	}

	public void raActive() {

		try {
			InitialContext ic = new InitialContext();
			this.realProvider = (MAPProvider) ic.lookup(this.mapJndi);
			tracer.info("Sucssefully connected to MAP service[" + this.mapJndi + "]");

			this.realProvider.addMAPDialogListener(this);
			this.realProvider.getMAPServiceSupplementary().addMAPServiceListener(this);
			this.realProvider.getMAPServiceSms().addMAPServiceListener(this);
			this.realProvider.getMAPServiceLsm().addMAPServiceListener(this);
			this.realProvider.getMapServiceSubscriberInformation().addMAPServiceListener(this);
			this.realProvider.getMAPServiceMobility().addMAPServiceListener(this);

			this.sleeEndpoint = resourceAdaptorContext.getSleeEndpoint();

			this.realProvider.getMAPServiceSupplementary().acivate();
			this.realProvider.getMAPServiceSms().acivate();
			this.realProvider.getMAPServiceLsm().acivate();
			this.realProvider.getMapServiceSubscriberInformation().acivate();
			this.realProvider.getMAPServiceMobility().acivate();

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
			this.mapJndi = (String) properties.getProperty(CONF_MAP_JNDI).getValue();
		} catch (Exception e) {
			tracer.severe("Configuring of MAP RA failed ", e);
		}
	}

	public void raInactive() {
		this.realProvider.getMAPServiceSupplementary().deactivate();
		this.realProvider.getMAPServiceLsm().deactivate();
		this.realProvider.getMAPServiceSms().deactivate();
		this.realProvider.getMapServiceSubscriberInformation().deactivate();
		this.realProvider.getMAPServiceMobility().deactivate();

		this.realProvider.getMAPServiceSupplementary().removeMAPServiceListener(this);
		this.realProvider.getMAPServiceLsm().removeMAPServiceListener(this);
		this.realProvider.getMAPServiceSms().removeMAPServiceListener(this);
		this.realProvider.getMapServiceSubscriberInformation().removeMAPServiceListener(this);
		this.realProvider.getMAPServiceMobility().removeMAPServiceListener(this);
		
		this.realProvider.removeMAPDialogListener(this);
	}

	public void raStopping() {
		// TODO Auto-generated method stub

	}

	public void raUnconfigure() {
		this.mapJndi = null;
	}

	public void raVerifyConfiguration(ConfigProperties properties) throws InvalidConfigurationException {
		try {

			if (tracer.isInfoEnabled()) {
				tracer.info("Verifying configuring MAPRA: " + this.resourceAdaptorContext.getEntityName());
			}

			this.mapJndi = (String) properties.getProperty(CONF_MAP_JNDI).getValue();
			if (this.mapJndi == null) {
				throw new InvalidConfigurationException("MAP JNDI lookup name cannot be null");
			}

		} catch (Exception e) {
			throw new InvalidConfigurationException("Failed to test configuration options!", e);
		}
	}

	public void serviceActive(ReceivableService receivableService) {
		eventIDFilter.serviceActive(receivableService);
	}

	public void serviceInactive(ReceivableService receivableService) {
		eventIDFilter.serviceInactive(receivableService);
	}

	public void serviceStopping(ReceivableService receivableService) {
		eventIDFilter.serviceStopping(receivableService);
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
	public void startActivity(MAPDialogWrapper mapDialogWrapper) throws ActivityAlreadyExistsException,
			NullPointerException, IllegalStateException, SLEEException, StartActivityException {
		this.sleeEndpoint.startActivity(mapDialogWrapper.getActivityHandle(), mapDialogWrapper, ACTIVITY_FLAGS);
	}

	public void startSuspendedActivity(MAPDialogWrapper mapDialogWrapper) throws ActivityAlreadyExistsException,
			NullPointerException, IllegalStateException, SLEEException, StartActivityException {
		this.sleeEndpoint.startActivitySuspended(mapDialogWrapper.getActivityHandle(), mapDialogWrapper,
				ActivityFlags.REQUEST_ENDED_CALLBACK);
	}

	/**
	 * Private methods
	 */
	private void fireEvent(String eventName, ActivityHandle handle, Object event) {

		FireableEventType eventID = eventIdCache.getEventId(this.resourceAdaptorContext.getEventLookupFacility(),
				eventName);

		if (eventIDFilter.filterEvent(eventID)) {
			if (tracer.isFineEnabled()) {
				tracer.fine("Event " + (eventID == null ? "null" : eventID.getEventType()) + " filtered");
			}
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

	// /////////////////
	// Event helpers //
	// /////////////////

	private MAPDialogActivityHandle onEvent(String eventName, MAPDialogWrapper dw, MAPEvent event) {
		if (dw == null) {
			this.tracer.severe(String.format("Firing %s but MAPDialogWrapper userObject is null", eventName));
			return null;
		}

		if (this.tracer.isFineEnabled()) {
			this.tracer
					.fine(String.format("Firing %s for DialogId=%d", eventName, dw.getWrappedDialog().getDialogId()));
		}

		this.fireEvent(eventName, dw.getActivityHandle(), event);
		return dw.getActivityHandle();
	}

	// ////////////////////
	// Dialog callbacks //
	// ////////////////////
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extension) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		DialogAccept dialogAccept = new DialogAccept(mapDialogWrapper, extension);
		onEvent(dialogAccept.getEventTypeName(), mapDialogWrapper, dialogAccept);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		DialogClose dialogClose = new DialogClose(mapDialogWrapper);
		MAPDialogActivityHandle handle = onEvent(dialogClose.getEventTypeName(), mapDialogWrapper, dialogClose);

		// End Activity
		// if (handle != null)
		// this.sleeEndpoint.endActivity(handle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		DialogDelimiter dialogDelimiter = new DialogDelimiter(mapDialogWrapper);
		onEvent(dialogDelimiter.getEventTypeName(), mapDialogWrapper, dialogDelimiter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		DialogNotice dialogNotice = new DialogNotice(mapDialogWrapper, noticeProblemDiagnostic);
		onEvent(dialogNotice.getEventTypeName(), mapDialogWrapper, dialogNotice);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		DialogProviderAbort dialogProviderAbort = new DialogProviderAbort(mapDialogWrapper, abortProviderReason,
				abortSource, extensionContainer);
		MAPDialogActivityHandle handle = onEvent(dialogProviderAbort.getEventTypeName(), mapDialogWrapper,
				dialogProviderAbort);

		// End Activity
		// if (handle != null)
		// this.sleeEndpoint.endActivity(handle);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, MAPProviderError providerError,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		DialogReject dialogReject = new DialogReject(mapDialogWrapper, refuseReason, providerError,
				alternativeApplicationContext, extensionContainer);
		MAPDialogActivityHandle handle = onEvent(dialogReject.getEventTypeName(), mapDialogWrapper, dialogReject);

		// End Activity
		// if (handle != null)
		// this.sleeEndpoint.endActivity(handle);
	}

	private void handleDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer, IMSI eriImsi, AddressString eriVlrNo) {
		try {

			if (this.tracer.isFineEnabled()) {
				this.tracer.fine(String.format("Received onDialogRequest id=%d ", mapDialog.getDialogId()));
			}

			MAPDialogActivityHandle activityHandle = new MAPDialogActivityHandle(mapDialog.getDialogId());
			MAPDialogWrapper mapDialogWrapper = null;
			MAPApplicationContext mapApplicationContext = mapDialog.getApplicationContext();
			switch (mapApplicationContext.getApplicationContextName()) {
			// -- USSD
			case networkUnstructuredSsContext:
				mapDialogWrapper = new MAPDialogSupplementaryWrapper((MAPDialogSupplementary) mapDialog,
						activityHandle, this);

				break;
			// -- SMS
			case shortMsgAlertContext:
			case shortMsgMORelayContext:
			case shortMsgMTRelayContext:
			case shortMsgGatewayContext:
				mapDialogWrapper = new MAPDialogSmsWrapper((MAPDialogSms) mapDialog, activityHandle, this);
				break;
			// -- Location Service Management (lsm)
			case locationSvcEnquiryContext:
				mapDialogWrapper = new MAPDialogLsmWrapper((MAPDialogLsm) mapDialog, activityHandle, this);
				break;
			// -- Subscriber Information Service
			case anyTimeEnquiryContext:
			case locationSvcGatewayContext:
				mapDialogWrapper = new MAPDialogSubscriberInformationWrapper(
						(MAPDialogSubscriberInformation) mapDialog, activityHandle, this);
				break;
			case infoRetrievalContext:
				mapDialogWrapper = new MAPDialogMobilityWrapper((MAPDialogMobility) mapDialog, activityHandle, this);
				break;
			default:
				this.tracer.severe(String.format("Received onDialogRequest id=%d for unknown MAPApplicationContext=%s",
						mapDialog.getDialogId(), mapApplicationContext.getApplicationContextName()));
				return;

			}
			DialogRequest event = new DialogRequest(mapDialogWrapper, destReference, origReference, extensionContainer,
					eriImsi, eriVlrNo);
			mapDialog.setUserObject(mapDialogWrapper);
			this.startActivity(mapDialogWrapper);
			this.fireEvent(event.getEventTypeName(), mapDialogWrapper.getActivityHandle(), event);

		} catch (Exception e) {
			this.tracer.severe(String.format(
					"Exception when trying to fire event DIALOG_REQUEST for received DialogRequest=%s ", mapDialog), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {
		this.handleDialogRequest(mapDialog, destReference, origReference, extensionContainer, null, null);
	}

	@Override
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			IMSI eriImsi, AddressString eriVlrNo) {
		this.handleDialogRequest(mapDialog, destReference, origReference, null, eriImsi, eriVlrNo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {

		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		DialogUserAbort dialogUserAbort = new DialogUserAbort(mapDialogWrapper, userReason, extensionContainer);
		MAPDialogActivityHandle handle = onEvent(dialogUserAbort.getEventTypeName(), mapDialogWrapper, dialogUserAbort);

		// End Activity
		// if (handle != null)
		// this.sleeEndpoint.endActivity(handle);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogRelease(MAPDialog mapDialog) {
		try {

			MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
			DialogRelease dialogUserAbort = new DialogRelease(mapDialogWrapper);
			MAPDialogActivityHandle handle = onEvent(dialogUserAbort.getEventTypeName(), mapDialogWrapper,
					dialogUserAbort);

			// End Activity
			this.sleeEndpoint.endActivity(handle);
		} catch (Exception e) {
			this.tracer.severe(String.format(
					"onDialogRelease : Exception while trying to end activity for MAPDialog=%s", mapDialog), e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {

		// TODO: done like that, since we want to process dialgo callbacks
		// before we fire event.
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine(String.format("Rx : onDialogTimeout for DialogId=%d", mapDialog.getDialogId()));
		}

		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		DialogTimeout dt = new DialogTimeout(mapDialogWrapper);
		onEvent(dt.getEventTypeName(), mapDialogWrapper, dt);
	}

	// ///////////////////////
	// Component callbacks //
	// ///////////////////////
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		InvokeTimeout invokeTimeout = new InvokeTimeout(mapDialogWrapper, invokeId);
		onEvent(invokeTimeout.getEventTypeName(), mapDialogWrapper, invokeTimeout);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		ErrorComponent errorComponent = new ErrorComponent(mapDialogWrapper, invokeId, mapErrorMessage);
		onEvent(errorComponent.getEventTypeName(), mapDialogWrapper, errorComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProviderErrorComponent(MAPDialog mapDialog, Long invokeId, MAPProviderError mapProviderError) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		ProviderErrorComponent providerErrorComponent = new ProviderErrorComponent(mapDialogWrapper, invokeId,
				mapProviderError);
		onEvent(providerErrorComponent.getEventTypeName(), mapDialogWrapper, providerErrorComponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem) {
		MAPDialogWrapper mapDialogWrapper = (MAPDialogWrapper) mapDialog.getUserObject();
		RejectComponent rejectComponent = new RejectComponent(mapDialogWrapper, invokeId, problem);
		onEvent(rejectComponent.getEventTypeName(), mapDialogWrapper, rejectComponent);
	}

	// /////////////////////////
	// SERVICE: Suplementary //
	// /////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest processUnstrSSInd) {
		MAPDialogSupplementaryWrapper mapDialogSupplementaryWrapper = (MAPDialogSupplementaryWrapper) processUnstrSSInd
				.getMAPDialog().getUserObject();
		ProcessUnstructuredSSRequestWrapper event = new ProcessUnstructuredSSRequestWrapper(
				mapDialogSupplementaryWrapper, processUnstrSSInd);
		onEvent(event.getEventTypeName(), mapDialogSupplementaryWrapper, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse processUnstructuredSSResponse) {
		MAPDialogSupplementaryWrapper mapDialogSupplementaryWrapper = (MAPDialogSupplementaryWrapper) processUnstructuredSSResponse
				.getMAPDialog().getUserObject();
		ProcessUnstructuredSSResponseWrapper event = new ProcessUnstructuredSSResponseWrapper(
				mapDialogSupplementaryWrapper, processUnstructuredSSResponse);
		onEvent(event.getEventTypeName(), mapDialogSupplementaryWrapper, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUnstructuredSSRequest(UnstructuredSSRequest unstructuredSSRequest) {
		MAPDialogSupplementaryWrapper mapDialogSupplementaryWrapper = (MAPDialogSupplementaryWrapper) unstructuredSSRequest
				.getMAPDialog().getUserObject();
		UnstructuredSSRequestWrapper event = new UnstructuredSSRequestWrapper(mapDialogSupplementaryWrapper,
				unstructuredSSRequest);
		onEvent(event.getEventTypeName(), mapDialogSupplementaryWrapper, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUnstructuredSSResponse(UnstructuredSSResponse unstructuredSSResponse) {
		MAPDialogSupplementaryWrapper mapDialogSupplementaryWrapper = (MAPDialogSupplementaryWrapper) unstructuredSSResponse
				.getMAPDialog().getUserObject();
		UnstructuredSSResponseWrapper event = new UnstructuredSSResponseWrapper(mapDialogSupplementaryWrapper,
				unstructuredSSResponse);
		onEvent(event.getEventTypeName(), mapDialogSupplementaryWrapper, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstructuredSSNotifyRequest) {
		MAPDialogSupplementaryWrapper mapDialogSupplementaryWrapper = (MAPDialogSupplementaryWrapper) unstructuredSSNotifyRequest
				.getMAPDialog().getUserObject();
		UnstructuredSSNotifyRequestWrapper event = new UnstructuredSSNotifyRequestWrapper(
				mapDialogSupplementaryWrapper, unstructuredSSNotifyRequest);
		onEvent(event.getEventTypeName(), mapDialogSupplementaryWrapper, event);
	}

	@Override
	public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstructuredSSNotifyResponse) {
		MAPDialogSupplementaryWrapper mapDialogSupplementaryWrapper = (MAPDialogSupplementaryWrapper) unstructuredSSNotifyResponse
				.getMAPDialog().getUserObject();
		UnstructuredSSNotifyResponseWrapper event = new UnstructuredSSNotifyResponseWrapper(
				mapDialogSupplementaryWrapper, unstructuredSSNotifyResponse);
		onEvent(event.getEventTypeName(), mapDialogSupplementaryWrapper, event);
	}

	// ////////////////
	// SERVICE: LSM //
	// ////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest provideSubscriberLocationRequest) {
		MAPDialogLsmWrapper mapDialogLsmWrapper = (MAPDialogLsmWrapper) provideSubscriberLocationRequest.getMAPDialog()
				.getUserObject();
		ProvideSubscriberLocationRequestWrapper event = new ProvideSubscriberLocationRequestWrapper(
				mapDialogLsmWrapper, provideSubscriberLocationRequest);
		onEvent(event.getEventTypeName(), mapDialogLsmWrapper, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse provideSubscriberLocationResponse) {
		MAPDialogLsmWrapper mapDialogLsmWrapper = (MAPDialogLsmWrapper) provideSubscriberLocationResponse
				.getMAPDialog().getUserObject();
		ProvideSubscriberLocationResponseWrapper event = new ProvideSubscriberLocationResponseWrapper(
				mapDialogLsmWrapper, provideSubscriberLocationResponse);
		onEvent(event.getEventTypeName(), mapDialogLsmWrapper, event);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSendRoutingInforForLCSRequest(SendRoutingInfoForLCSRequest sendRoutingInfoForLCSRequest) {
		MAPDialogLsmWrapper mapDialogLsmWrapper = (MAPDialogLsmWrapper) sendRoutingInfoForLCSRequest.getMAPDialog()
				.getUserObject();
		SendRoutingInfoForLCSRequestWrapper event = new SendRoutingInfoForLCSRequestWrapper(mapDialogLsmWrapper,
				sendRoutingInfoForLCSRequest);
		onEvent(event.getEventTypeName(), mapDialogLsmWrapper, event);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSendRoutingInforForLCSResponse(SendRoutingInfoForLCSResponse sendRoutingInfoForLCSResponse) {
		MAPDialogLsmWrapper mapDialogLsmWrapper = (MAPDialogLsmWrapper) sendRoutingInfoForLCSResponse.getMAPDialog()
				.getUserObject();
		SendRoutingInfoForLCSResponseWrapper event = new SendRoutingInfoForLCSResponseWrapper(mapDialogLsmWrapper,
				sendRoutingInfoForLCSResponse);
		onEvent(event.getEventTypeName(), mapDialogLsmWrapper, event);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest subscriberLocationReportRequest) {
		MAPDialogLsmWrapper mapDialogLsmWrapper = (MAPDialogLsmWrapper) subscriberLocationReportRequest.getMAPDialog()
				.getUserObject();
		SubscriberLocationReportRequestWrapper event = new SubscriberLocationReportRequestWrapper(mapDialogLsmWrapper,
				subscriberLocationReportRequest);
		onEvent(event.getEventTypeName(), mapDialogLsmWrapper, event);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse subscriberLocationReportResponse) {
		MAPDialogLsmWrapper mapDialogLsmWrapper = (MAPDialogLsmWrapper) subscriberLocationReportResponse.getMAPDialog()
				.getUserObject();
		SubscriberLocationReportResponseWrapper event = new SubscriberLocationReportResponseWrapper(
				mapDialogLsmWrapper, subscriberLocationReportResponse);
		onEvent(event.getEventTypeName(), mapDialogLsmWrapper, event);

	}

	// ///////////////
	// SERVICE : SUBSCRIBER INFORMATION
	// //////////////
	public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest anyTimeInterrogationRequest) {
		MAPDialogSubscriberInformationWrapper mapDialogSubscriberInformationWrapper = (MAPDialogSubscriberInformationWrapper) anyTimeInterrogationRequest
				.getMAPDialog().getUserObject();
		AnyTimeInterrogationRequestWrapper event = new AnyTimeInterrogationRequestWrapper(
				mapDialogSubscriberInformationWrapper, anyTimeInterrogationRequest);
		onEvent(event.getEventTypeName(), mapDialogSubscriberInformationWrapper, event);
	}

	public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse anyTimeInterrogationResponse) {
		MAPDialogSubscriberInformationWrapper mapDialogSubscriberInformationWrapper = (MAPDialogSubscriberInformationWrapper) anyTimeInterrogationResponse
				.getMAPDialog().getUserObject();
		AnyTimeInterrogationResponseWrapper event = new AnyTimeInterrogationResponseWrapper(
				mapDialogSubscriberInformationWrapper, anyTimeInterrogationResponse);
		onEvent(event.getEventTypeName(), mapDialogSubscriberInformationWrapper, event);
	}

	// ////////////////
	// SERVICE: SMS //
	// ////////////////

	@Override
	public void onForwardShortMessageRequest(ForwardShortMessageRequest forwardShortMessageRequest) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) forwardShortMessageRequest.getMAPDialog()
				.getUserObject();
		ForwardShortMessageRequestWrapper event = new ForwardShortMessageRequestWrapper(mapDialogSmsWrapper,
				forwardShortMessageRequest);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	@Override
	public void onForwardShortMessageResponse(ForwardShortMessageResponse forwardShortMessageResponse) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) forwardShortMessageResponse.getMAPDialog()
				.getUserObject();
		ForwardShortMessageResponseWrapper event = new ForwardShortMessageResponseWrapper(mapDialogSmsWrapper,
				forwardShortMessageResponse);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);

	}

	@Override
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwardShortMessageRequest) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) moForwardShortMessageRequest.getMAPDialog()
				.getUserObject();
		MoForwardShortMessageRequestWrapper event = new MoForwardShortMessageRequestWrapper(mapDialogSmsWrapper,
				moForwardShortMessageRequest);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);

	}

	@Override
	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwardShortMessageResponse) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) moForwardShortMessageResponse.getMAPDialog()
				.getUserObject();
		MoForwardShortMessageResponseWrapper event = new MoForwardShortMessageResponseWrapper(mapDialogSmsWrapper,
				moForwardShortMessageResponse);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	@Override
	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwardShortMessageRequest) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) mtForwardShortMessageRequest.getMAPDialog()
				.getUserObject();
		MtForwardShortMessageRequestWrapper event = new MtForwardShortMessageRequestWrapper(mapDialogSmsWrapper,
				mtForwardShortMessageRequest);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	@Override
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwardShortMessageResponse) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) mtForwardShortMessageResponse.getMAPDialog()
				.getUserObject();
		MtForwardShortMessageResponseWrapper event = new MtForwardShortMessageResponseWrapper(mapDialogSmsWrapper,
				mtForwardShortMessageResponse);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	@Override
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMRequest) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) sendRoutingInfoForSMRequest.getMAPDialog()
				.getUserObject();
		SendRoutingInfoForSMRequestWrapper event = new SendRoutingInfoForSMRequestWrapper(mapDialogSmsWrapper,
				sendRoutingInfoForSMRequest);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	@Override
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSmResponse) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) sendRoutingInfoForSmResponse.getMAPDialog()
				.getUserObject();
		SendRoutingInfoForSMResponseWrapper event = new SendRoutingInfoForSMResponseWrapper(mapDialogSmsWrapper,
				sendRoutingInfoForSmResponse);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	@Override
	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSmDeliveryStatusRequest) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) reportSmDeliveryStatusRequest.getMAPDialog()
				.getUserObject();
		ReportSMDeliveryStatusRequestWrapper event = new ReportSMDeliveryStatusRequestWrapper(mapDialogSmsWrapper,
				reportSmDeliveryStatusRequest);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);

	}

	@Override
	public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSmDeliveryStatusResponse) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) reportSmDeliveryStatusResponse.getMAPDialog()
				.getUserObject();
		ReportSMDeliveryStatusResponseWrapper event = new ReportSMDeliveryStatusResponseWrapper(mapDialogSmsWrapper,
				reportSmDeliveryStatusResponse);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);

	}

	@Override
	public void onInformServiceCentreRequest(InformServiceCentreRequest informServiCecentreRequest) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) informServiCecentreRequest.getMAPDialog()
				.getUserObject();
		InformServiceCentreRequestWrapper event = new InformServiceCentreRequestWrapper(mapDialogSmsWrapper,
				informServiCecentreRequest);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	@Override
	public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiCecentreRequest) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) alertServiCecentreRequest.getMAPDialog()
				.getUserObject();
		AlertServiceCentreRequestWrapper event = new AlertServiceCentreRequestWrapper(mapDialogSmsWrapper,
				alertServiCecentreRequest);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	@Override
	public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiCecentreResponse) {
		MAPDialogSmsWrapper mapDialogSmsWrapper = (MAPDialogSmsWrapper) alertServiCecentreResponse.getMAPDialog()
				.getUserObject();
		AlertServiceCentreResponseWrapper event = new AlertServiceCentreResponseWrapper(mapDialogSmsWrapper,
				alertServiCecentreResponse);
		onEvent(event.getEventTypeName(), mapDialogSmsWrapper, event);
	}

	// ///////////////
	// SERVICE : MOBILITY
	// //////////////
	public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
		MAPDialogMobilityWrapper mapDialogMobilityWrapper = (MAPDialogMobilityWrapper) ind.getMAPDialog()
				.getUserObject();
		SendAuthenticationInfoRequestWrapper event = new SendAuthenticationInfoRequestWrapper(mapDialogMobilityWrapper,
				ind);
		onEvent(event.getEventTypeName(), mapDialogMobilityWrapper, event);
	}

	public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
		MAPDialogMobilityWrapper mapDialogMobilityWrapper = (MAPDialogMobilityWrapper) ind.getMAPDialog()
				.getUserObject();
		SendAuthenticationInfoResponseWrapper event = new SendAuthenticationInfoResponseWrapper(
				mapDialogMobilityWrapper, ind);
		onEvent(event.getEventTypeName(), mapDialogMobilityWrapper, event);
	}

	public void onUpdateLocationRequest(UpdateLocationRequest ind) {
		// TODO
	}

	public void onUpdateLocationResponse(UpdateLocationResponse ind) {
		// TODO
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.MAPServiceListener#onMAPMessage(org
	 * .mobicents.protocols.ss7.map.api.MAPMessage)
	 */
	@Override
	public void onMAPMessage(MAPMessage arg0) {
		// Do nothing
	}

}
