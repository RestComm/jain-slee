/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.collab;

import com.microjainslee.ra.jss7.event.Ss7MapEvent;
import com.microjainslee.ra.jss7.transport.Ss7Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPDialog;

/**
 * MAP protocol adapter — registers the MAP dialog listener + every MAP
 * service listener and republishes each jSS7 callback as an Ss7MapEvent.
 * Generated to cover the full MAP operation set; every service operation
 * arrives as Ss7MapEvent.Service (carrying the base message), dialog lifecycle
 * as Ss7MapEvent.Dialog.
 */
public final class MapProtocolAdapter implements Ss7ProtocolAdapter, org.restcomm.protocols.ss7.map.api.MAPDialogListener, org.restcomm.protocols.ss7.map.api.MAPServiceListener, org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener, org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSmsListener, org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener, org.restcomm.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandlingListener, org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener, org.restcomm.protocols.ss7.map.api.service.oam.MAPServiceOamListener, org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivationListener {

    private static final Logger LOG = LogManager.getLogger(MapProtocolAdapter.class);

    private MAPProvider provider;
    private Ss7EventPublisher publisher;

    @Override public String protocol() { return "MAP"; }

    @Override
    public void attach(Ss7Stack stack, Ss7EventPublisher publisher) {
        this.provider = stack.mapProvider();
        this.publisher = publisher;
        if (provider == null) {
            LOG.warn("[ra-jss7] MAP disabled — no provider on stack");
            return;
        }
        provider.addMAPDialogListener(this);
        try { var s = provider.getMAPServiceMobility(); s.addMAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] MAP service getMAPServiceMobility activate failed: {}", e.toString()); }
        try { var s = provider.getMAPServiceSms(); s.addMAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] MAP service getMAPServiceSms activate failed: {}", e.toString()); }
        try { var s = provider.getMAPServiceSupplementary(); s.addMAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] MAP service getMAPServiceSupplementary activate failed: {}", e.toString()); }
        try { var s = provider.getMAPServiceCallHandling(); s.addMAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] MAP service getMAPServiceCallHandling activate failed: {}", e.toString()); }
        try { var s = provider.getMAPServiceLsm(); s.addMAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] MAP service getMAPServiceLsm activate failed: {}", e.toString()); }
        try { var s = provider.getMAPServiceOam(); s.addMAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] MAP service getMAPServiceOam activate failed: {}", e.toString()); }
        try { var s = provider.getMAPServicePdpContextActivation(); s.addMAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] MAP service getMAPServicePdpContextActivation activate failed: {}", e.toString()); }
        LOG.info("[ra-jss7] MAP adapter attached (full listener coverage)");
    }

    @Override
    public void detach() { LOG.info("[ra-jss7] MAP adapter detached"); }

    // ── helpers ──────────────────────────────────────────────
    private void service(MAPMessage m) {
        if (publisher == null || m == null) return;
        String did = did(m.getMAPDialog());
        publisher.publish(did, new Ss7MapEvent.Service(did, m.getMessageType(), m));
    }

    private void dialog(MAPDialog d, Ss7MapEvent.Kind kind, String detail) {
        if (publisher == null) return;
        String did = did(d);
        publisher.publish(did, new Ss7MapEvent.Dialog(did, kind, detail));
    }

    private static String did(MAPDialog d) {
        return d == null || d.getLocalDialogId() == null ? "?" : String.valueOf(d.getLocalDialogId());
    }

    // ── generated MAP listener coverage ──────────────────
    @Override public void onDialogDelimiter(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog) { dialog(mapDialog, Ss7MapEvent.Kind.DELIMITER, null); }
    @Override public void onDialogRequest(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.primitives.AddressString destReference, org.restcomm.protocols.ss7.map.api.primitives.AddressString origReference, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { dialog(mapDialog, Ss7MapEvent.Kind.REQUEST, null); }
    @Override public void onDialogRequestEricsson(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.primitives.AddressString destReference, org.restcomm.protocols.ss7.map.api.primitives.AddressString origReference, org.restcomm.protocols.ss7.map.api.primitives.AddressString ericssonMsisdn, org.restcomm.protocols.ss7.map.api.primitives.AddressString ericssonVlrNo) { dialog(mapDialog, Ss7MapEvent.Kind.REQUEST, null); }
    @Override public void onDialogAccept(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { dialog(mapDialog, Ss7MapEvent.Kind.ACCEPT, null); }
    @Override public void onDialogReject(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason refuseReason, org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName alternativeApplicationContext, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { dialog(mapDialog, Ss7MapEvent.Kind.REJECT, null); }
    @Override public void onDialogUserAbort(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice userReason, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { dialog(mapDialog, Ss7MapEvent.Kind.USER_ABORT, null); }
    @Override public void onDialogProviderAbort(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason abortProviderReason, org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource abortSource, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { dialog(mapDialog, Ss7MapEvent.Kind.PROVIDER_ABORT, null); }
    @Override public void onDialogClose(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog) { dialog(mapDialog, Ss7MapEvent.Kind.CLOSE, null); }
    @Override public void onDialogNotice(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic mapNoticeProblemDiagnostic) { dialog(mapDialog, Ss7MapEvent.Kind.NOTICE, null); }
    @Override public void onDialogRelease(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog) { dialog(mapDialog, Ss7MapEvent.Kind.RELEASE, null); }
    @Override public void onDialogTimeout(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog) { dialog(mapDialog, Ss7MapEvent.Kind.TIMEOUT, null); }
    @Override public void onErrorComponent(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, Long invokeId, org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage mapErrorMessage) { dialog(mapDialog, Ss7MapEvent.Kind.NOTICE, "onErrorComponent"); }
    @Override public void onRejectComponent(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, Long invokeId, org.restcomm.protocols.ss7.tcap.asn.comp.Problem problem, boolean isLocalOriginated) { dialog(mapDialog, Ss7MapEvent.Kind.NOTICE, "onRejectComponent"); }
    @Override public void onInvokeTimeout(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, Long invokeId) { dialog(mapDialog, Ss7MapEvent.Kind.NOTICE, "onInvokeTimeout"); }
    @Override public void onMAPMessage(org.restcomm.protocols.ss7.map.api.MAPMessage mapMessage) { service(mapMessage); }
    @Override public void onUpdateLocationRequest(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest updateLocationRequestIndication) { service(updateLocationRequestIndication); }
    @Override public void onUpdateLocationResponse(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse updateLocationResponseIndication) { service(updateLocationResponseIndication); }
    @Override public void onCancelLocationRequest(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest cancelLocationRequest) { service(cancelLocationRequest); }
    @Override public void onCancelLocationResponse(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse cancelLocationResponse) { service(cancelLocationResponse); }
    @Override public void onSendIdentificationRequest(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest sendIdentificationRequest) { service(sendIdentificationRequest); }
    @Override public void onSendIdentificationResponse(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse sendIdentificationResponse) { service(sendIdentificationResponse); }
    @Override public void onUpdateGprsLocationRequest(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest updateGprsLocationRequest) { service(updateGprsLocationRequest); }
    @Override public void onUpdateGprsLocationResponse(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse updateGprsLocationResponse) { service(updateGprsLocationResponse); }
    @Override public void onPurgeMSRequest(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest purgeMSRequest) { service(purgeMSRequest); }
    @Override public void onPurgeMSResponse(org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse purgeMSResponse) { service(purgeMSResponse); }
    @Override public void onSendAuthenticationInfoRequest(org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest sendAuthenticationInfoRequestIndication) { service(sendAuthenticationInfoRequestIndication); }
    @Override public void onSendAuthenticationInfoResponse(org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse sendAuthenticationInfoResponseIndication) { service(sendAuthenticationInfoResponseIndication); }
    @Override public void onAuthenticationFailureReportRequest(org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest authenticationFailureReportRequestIndication) { service(authenticationFailureReportRequestIndication); }
    @Override public void onAuthenticationFailureReportResponse(org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse authenticationFailureReportResponseIndication) { service(authenticationFailureReportResponseIndication); }
    @Override public void onResetRequest(org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest resetRequestIndication) { service(resetRequestIndication); }
    @Override public void onForwardCheckSSIndicationRequest(org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest forwardCheckSSIndicationRequestIndication) { service(forwardCheckSSIndicationRequestIndication); }
    @Override public void onRestoreDataRequest(org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest restoreDataRequestIndication) { service(restoreDataRequestIndication); }
    @Override public void onRestoreDataResponse(org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse restoreDataResponseIndication) { service(restoreDataResponseIndication); }
    @Override public void onAnyTimeInterrogationRequest(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest anyTimeInterrogationRequest) { service(anyTimeInterrogationRequest); }
    @Override public void onAnyTimeInterrogationResponse(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse anyTimeInterrogationResponse) { service(anyTimeInterrogationResponse); }
    @Override public void onAnyTimeSubscriptionInterrogationRequest(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest anyTimeSubscriptionInterrogationRequest) { service(anyTimeSubscriptionInterrogationRequest); }
    @Override public void onAnyTimeSubscriptionInterrogationResponse(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse anyTimeSubscriptionInterrogationResponse) { service(anyTimeSubscriptionInterrogationResponse); }
    @Override public void onAnyTimeModificationRequest(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeModificationRequest anyTimeModificationRequest) { service(anyTimeModificationRequest); }
    @Override public void onAnyTimeModificationResponse(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeModificationResponse anyTimeModificationResponse) { service(anyTimeModificationResponse); }
    @Override public void onProvideSubscriberInfoRequest(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest provideSubscriberInfoRequest) { service(provideSubscriberInfoRequest); }
    @Override public void onProvideSubscriberInfoResponse(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse provideSubscriberInfoResponse) { service(provideSubscriberInfoResponse); }
    @Override public void onInsertSubscriberDataRequest(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest insertSubscriberDataRequest) { service(insertSubscriberDataRequest); }
    @Override public void onInsertSubscriberDataResponse(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse insertSubscriberDataResponse) { service(insertSubscriberDataResponse); }
    @Override public void onDeleteSubscriberDataRequest(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest deleteSubscriberDataRequest) { service(deleteSubscriberDataRequest); }
    @Override public void onDeleteSubscriberDataResponse(org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse deleteSubscriberDataResponse) { service(deleteSubscriberDataResponse); }
    @Override public void onCheckImeiRequest(org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest checkImeiRequest) { service(checkImeiRequest); }
    @Override public void onCheckImeiResponse(org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse checkImeiResponse) { service(checkImeiResponse); }
    @Override public void onActivateTraceModeRequest_Mobility(org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility activateTraceModeRequestMobilityIndication) { service(activateTraceModeRequestMobilityIndication); }
    @Override public void onActivateTraceModeResponse_Mobility(org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility activateTraceModeResponseMobilityIndication) { service(activateTraceModeResponseMobilityIndication); }
    @Override public void onForwardShortMessageRequest(org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest forwardShortMessageRequestIndication) { service(forwardShortMessageRequestIndication); }
    @Override public void onForwardShortMessageResponse(org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse forwardShortMessageResponseIndication) { service(forwardShortMessageResponseIndication); }
    @Override public void onMoForwardShortMessageRequest(org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest moForwardShortMessageRequestIndication) { service(moForwardShortMessageRequestIndication); }
    @Override public void onMoForwardShortMessageResponse(org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse moForwardShortMessageResponseIndication) { service(moForwardShortMessageResponseIndication); }
    @Override public void onMtForwardShortMessageRequest(org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest mtForwardShortMessageRequestIndication) { service(mtForwardShortMessageRequestIndication); }
    @Override public void onMtForwardShortMessageResponse(org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse mtForwardShortMessageResponseIndication) { service(mtForwardShortMessageResponseIndication); }
    @Override public void onSendRoutingInfoForSMRequest(org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest sendRoutingInfoForSMRequestIndication) { service(sendRoutingInfoForSMRequestIndication); }
    @Override public void onSendRoutingInfoForSMResponse(org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse sendRoutingInfoForSMResponseIndication) { service(sendRoutingInfoForSMResponseIndication); }
    @Override public void onReportSMDeliveryStatusRequest(org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest reportSMDeliveryStatusRequestIndication) { service(reportSMDeliveryStatusRequestIndication); }
    @Override public void onReportSMDeliveryStatusResponse(org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse reportSMDeliveryStatusResponseIndication) { service(reportSMDeliveryStatusResponseIndication); }
    @Override public void onInformServiceCentreRequest(org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest informServiceCentreRequestIndication) { service(informServiceCentreRequestIndication); }
    @Override public void onAlertServiceCentreRequest(org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest alertServiceCentreRequestIndication) { service(alertServiceCentreRequestIndication); }
    @Override public void onAlertServiceCentreResponse(org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse alertServiceCentreResponseIndication) { service(alertServiceCentreResponseIndication); }
    @Override public void onReadyForSMRequest(org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMRequest readyForSMRequest) { service(readyForSMRequest); }
    @Override public void onReadyForSMResponse(org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMResponse readyForSMResponse) { service(readyForSMResponse); }
    @Override public void onNoteSubscriberPresentRequest(org.restcomm.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest noteSubscriberPresentRequest) { service(noteSubscriberPresentRequest); }
    @Override public void onRegisterSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSRequest registerSSRequest) { service(registerSSRequest); }
    @Override public void onRegisterSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSResponse registerSSResponse) { service(registerSSResponse); }
    @Override public void onEraseSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSRequest eraseSSRequest) { service(eraseSSRequest); }
    @Override public void onEraseSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSResponse eraseSSResponse) { service(eraseSSResponse); }
    @Override public void onActivateSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSRequest activateSSRequest) { service(activateSSRequest); }
    @Override public void onActivateSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSResponse activateSSResponse) { service(activateSSResponse); }
    @Override public void onDeactivateSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest deactivateSSRequest) { service(deactivateSSRequest); }
    @Override public void onDeactivateSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse deactivateSSResponse) { service(deactivateSSResponse); }
    @Override public void onInterrogateSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest interrogateSSRequest) { service(interrogateSSRequest); }
    @Override public void onInterrogateSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse interrogateSSResponse) { service(interrogateSSResponse); }
    @Override public void onGetPasswordRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordRequest getPasswordRequest) { service(getPasswordRequest); }
    @Override public void onGetPasswordResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordResponse getPasswordResponse) { service(getPasswordResponse); }
    @Override public void onRegisterPasswordRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest registerPasswordRequest) { service(registerPasswordRequest); }
    @Override public void onRegisterPasswordResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse registerPasswordResponse) { service(registerPasswordResponse); }
    @Override public void onProcessUnstructuredSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest processUnstructuredSSRequest) { service(processUnstructuredSSRequest); }
    @Override public void onProcessUnstructuredSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse processUnstructuredSSResponse) { service(processUnstructuredSSResponse); }
    @Override public void onUnstructuredSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest unstructuredSSRequest) { service(unstructuredSSRequest); }
    @Override public void onUnstructuredSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse unstructuredSSResponse) { service(unstructuredSSResponse); }
    @Override public void onUnstructuredSSNotifyRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest unstructuredSSNotifyRequest) { service(unstructuredSSNotifyRequest); }
    @Override public void onUnstructuredSSNotifyResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse unstructuredSSNotifyResponse) { service(unstructuredSSNotifyResponse); }
    @Override public void onSendRoutingInformationRequest(org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest sendRoutingInformationRequest) { service(sendRoutingInformationRequest); }
    @Override public void onSendRoutingInformationResponse(org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse sendRoutingInformationResponse) { service(sendRoutingInformationResponse); }
    @Override public void onProvideRoamingNumberRequest(org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest provideRoamingNumberRequest) { service(provideRoamingNumberRequest); }
    @Override public void onProvideRoamingNumberResponse(org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse provideRoamingNumberResponse) { service(provideRoamingNumberResponse); }
    @Override public void onIstCommandRequest(org.restcomm.protocols.ss7.map.api.service.callhandling.IstCommandRequest istCommandRequest) { service(istCommandRequest); }
    @Override public void onIstCommandResponse(org.restcomm.protocols.ss7.map.api.service.callhandling.IstCommandResponse istCommandResponse) { service(istCommandResponse); }
    @Override public void onProvideSubscriberLocationRequest(org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest provideSubscriberLocationRequestIndication) { service(provideSubscriberLocationRequestIndication); }
    @Override public void onProvideSubscriberLocationResponse(org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse provideSubscriberLocationResponseIndication) { service(provideSubscriberLocationResponseIndication); }
    @Override public void onSubscriberLocationReportRequest(org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest subscriberLocationReportRequestIndication) { service(subscriberLocationReportRequestIndication); }
    @Override public void onSubscriberLocationReportResponse(org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse subscriberLocationReportResponseIndication) { service(subscriberLocationReportResponseIndication); }
    @Override public void onSendRoutingInfoForLCSRequest(org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest sendRoutingInfoForLCSRequestIndication) { service(sendRoutingInfoForLCSRequestIndication); }
    @Override public void onSendRoutingInfoForLCSResponse(org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse sendRoutingInfoForLCSResponseIndication) { service(sendRoutingInfoForLCSResponseIndication); }
    @Override public void onActivateTraceModeRequest_Oam(org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam activateTraceModeRequestOamIndication) { service(activateTraceModeRequestOamIndication); }
    @Override public void onActivateTraceModeResponse_Oam(org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeResponse_Oam activateTraceModeResponseOamIndication) { service(activateTraceModeResponseOamIndication); }
    @Override public void onSendImsiRequest(org.restcomm.protocols.ss7.map.api.service.oam.SendImsiRequest sendImsiRequestIndication) { service(sendImsiRequestIndication); }
    @Override public void onSendImsiResponse(org.restcomm.protocols.ss7.map.api.service.oam.SendImsiResponse sendImsiResponseIndication) { service(sendImsiResponseIndication); }
    @Override public void onSendRoutingInfoForGprsRequest(org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsRequest sendRoutingInfoForGprsRequest) { service(sendRoutingInfoForGprsRequest); }
    @Override public void onSendRoutingInfoForGprsResponse(org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsResponse sendRoutingInfoForGprsResponse) { service(sendRoutingInfoForGprsResponse); }
}
