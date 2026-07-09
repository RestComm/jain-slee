/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.collab;

import com.microjainslee.ra.jss7.event.Ss7CapEvent;
import com.microjainslee.ra.jss7.transport.Ss7Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;

/**
 * CAP protocol adapter — registers the CAP dialog listener + every CAP
 * service listener and republishes each jSS7 callback as an Ss7CapEvent.
 * Generated to cover the full CAP operation set; every service operation
 * arrives as Ss7CapEvent.Service (carrying the base message), dialog lifecycle
 * as Ss7CapEvent.Dialog.
 */
public final class CapProtocolAdapter implements Ss7ProtocolAdapter, org.restcomm.protocols.ss7.cap.api.CAPDialogListener, org.restcomm.protocols.ss7.cap.api.CAPServiceListener, org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener, org.restcomm.protocols.ss7.cap.api.service.gprs.CAPServiceGprsListener, org.restcomm.protocols.ss7.cap.api.service.sms.CAPServiceSmsListener {

    private static final Logger LOG = LogManager.getLogger(CapProtocolAdapter.class);

    private CAPProvider provider;
    private Ss7EventPublisher publisher;

    @Override public String protocol() { return "CAP"; }

    @Override
    public void attach(Ss7Stack stack, Ss7EventPublisher publisher) {
        this.provider = stack.capProvider();
        this.publisher = publisher;
        if (provider == null) {
            LOG.warn("[ra-jss7] CAP disabled — no provider on stack");
            return;
        }
        provider.addCAPDialogListener(this);
        try { var s = provider.getCAPServiceCircuitSwitchedCall(); s.addCAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] CAP service getCAPServiceCircuitSwitchedCall activate failed: {}", e.toString()); }
        try { var s = provider.getCAPServiceGprs(); s.addCAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] CAP service getCAPServiceGprs activate failed: {}", e.toString()); }
        try { var s = provider.getCAPServiceSms(); s.addCAPServiceListener(this); s.activate(); } catch (Exception e) { LOG.warn("[ra-jss7] CAP service getCAPServiceSms activate failed: {}", e.toString()); }
        LOG.info("[ra-jss7] CAP adapter attached (full listener coverage)");
    }

    @Override
    public void detach() { LOG.info("[ra-jss7] CAP adapter detached"); }

    // ── helpers ──────────────────────────────────────────────
    private void service(CAPMessage m) {
        if (publisher == null || m == null) return;
        String did = did(m.getCAPDialog());
        publisher.publish(did, new Ss7CapEvent.Service(did, m.getMessageType(), m));
    }

    private void dialog(CAPDialog d, Ss7CapEvent.Kind kind, String detail) {
        if (publisher == null) return;
        String did = did(d);
        publisher.publish(did, new Ss7CapEvent.Dialog(did, kind, detail));
    }

    private static String did(CAPDialog d) {
        return d == null || d.getLocalDialogId() == null ? "?" : String.valueOf(d.getLocalDialogId());
    }

    // ── generated CAP listener coverage ──────────────────
    @Override public void onDialogDelimiter(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog) { dialog(capDialog, Ss7CapEvent.Kind.DELIMITER, null); }
    @Override public void onDialogRequest(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog, org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber capGprsReferenceNumber) { dialog(capDialog, Ss7CapEvent.Kind.REQUEST, null); }
    @Override public void onDialogAccept(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog, org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber capGprsReferenceNumber) { dialog(capDialog, Ss7CapEvent.Kind.ACCEPT, null); }
    @Override public void onDialogUserAbort(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog, org.restcomm.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason generalReason, org.restcomm.protocols.ss7.cap.api.dialog.CAPUserAbortReason userReason) { dialog(capDialog, Ss7CapEvent.Kind.USER_ABORT, null); }
    @Override public void onDialogProviderAbort(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog, org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType abortCause) { dialog(capDialog, Ss7CapEvent.Kind.PROVIDER_ABORT, null); }
    @Override public void onDialogClose(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog) { dialog(capDialog, Ss7CapEvent.Kind.CLOSE, null); }
    @Override public void onDialogRelease(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog) { dialog(capDialog, Ss7CapEvent.Kind.RELEASE, null); }
    @Override public void onDialogTimeout(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog) { dialog(capDialog, Ss7CapEvent.Kind.TIMEOUT, null); }
    @Override public void onDialogNotice(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog, org.restcomm.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic noticeProblemDiagnostic) { dialog(capDialog, Ss7CapEvent.Kind.NOTICE, null); }
    @Override public void onErrorComponent(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog, Long invokeId, org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage capErrorMessage) { dialog(capDialog, Ss7CapEvent.Kind.NOTICE, "onErrorComponent"); }
    @Override public void onRejectComponent(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog, Long invokeId, org.restcomm.protocols.ss7.tcap.asn.comp.Problem problem, boolean isLocalOriginated) { dialog(capDialog, Ss7CapEvent.Kind.NOTICE, "onRejectComponent"); }
    @Override public void onInvokeTimeout(org.restcomm.protocols.ss7.cap.api.CAPDialog capDialog, Long invokeId) { dialog(capDialog, Ss7CapEvent.Kind.NOTICE, "onInvokeTimeout"); }
    @Override public void onCAPMessage(org.restcomm.protocols.ss7.cap.api.CAPMessage capMessage) { service(capMessage); }
    @Override public void onInitialDPRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest ind) { service(ind); }
    @Override public void onRequestReportBCSMEventRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest ind) { service(ind); }
    @Override public void onApplyChargingRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest ind) { service(ind); }
    @Override public void onEventReportBCSMRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest ind) { service(ind); }
    @Override public void onContinueRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest ind) { service(ind); }
    @Override public void onContinueWithArgumentRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest ind) { service(ind); }
    @Override public void onApplyChargingReportRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest ind) { service(ind); }
    @Override public void onReleaseCallRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest ind) { service(ind); }
    @Override public void onConnectRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest ind) { service(ind); }
    @Override public void onCallInformationRequestRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest ind) { service(ind); }
    @Override public void onCallInformationReportRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest ind) { service(ind); }
    @Override public void onActivityTestRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest ind) { service(ind); }
    @Override public void onActivityTestResponse(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestResponse ind) { service(ind); }
    @Override public void onAssistRequestInstructionsRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest ind) { service(ind); }
    @Override public void onEstablishTemporaryConnectionRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest ind) { service(ind); }
    @Override public void onDisconnectForwardConnectionRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest ind) { service(ind); }
    @Override public void onDisconnectLegRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest ind) { service(ind); }
    @Override public void onDisconnectLegResponse(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegResponse ind) { service(ind); }
    @Override public void onDisconnectForwardConnectionWithArgumentRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest ind) { service(ind); }
    @Override public void onConnectToResourceRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest ind) { service(ind); }
    @Override public void onResetTimerRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest ind) { service(ind); }
    @Override public void onFurnishChargingInformationRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest ind) { service(ind); }
    @Override public void onSendChargingInformationRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest ind) { service(ind); }
    @Override public void onSpecializedResourceReportRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest ind) { service(ind); }
    @Override public void onPlayAnnouncementRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest ind) { service(ind); }
    @Override public void onPromptAndCollectUserInformationRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest ind) { service(ind); }
    @Override public void onPromptAndCollectUserInformationResponse(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse ind) { service(ind); }
    @Override public void onCancelRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest ind) { service(ind); }
    @Override public void onInitiateCallAttemptRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest initiateCallAttemptRequest) { service(initiateCallAttemptRequest); }
    @Override public void onInitiateCallAttemptResponse(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptResponse initiateCallAttemptResponse) { service(initiateCallAttemptResponse); }
    @Override public void onMoveLegRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegRequest ind) { service(ind); }
    @Override public void onMoveLegResponse(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegResponse ind) { service(ind); }
    @Override public void onCollectInformationRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CollectInformationRequest ind) { service(ind); }
    @Override public void onSplitLegRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegRequest ind) { service(ind); }
    @Override public void onSplitLegResponse(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegResponse ind) { service(ind); }
    @Override public void onCallGapRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest ind) { service(ind); }
    @Override public void onInitialDpGprsRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest initialDpGprsRequestIndication) { service(initialDpGprsRequestIndication); }
    @Override public void onRequestReportGPRSEventRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest requestReportGPRSEventRequestIndication) { service(requestReportGPRSEventRequestIndication); }
    @Override public void onApplyChargingGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest applyChargingGPRSRequestInd) { service(applyChargingGPRSRequestInd); }
    @Override public void onEntityReleasedGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSRequest entityReleasedGPRSRequestInd) { service(entityReleasedGPRSRequestInd); }
    @Override public void onEntityReleasedGPRSResponse(org.restcomm.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSResponse entityReleasedGPRSResponseIndication) { service(entityReleasedGPRSResponseIndication); }
    @Override public void onConnectGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.ConnectGPRSRequest connectGPRSRequestIndication) { service(connectGPRSRequestIndication); }
    @Override public void onContinueGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.ContinueGPRSRequest continueGPRSRequestIndication) { service(continueGPRSRequestIndication); }
    @Override public void onReleaseGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.ReleaseGPRSRequest releaseGPRSRequestIndication) { service(releaseGPRSRequestIndication); }
    @Override public void onResetTimerGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest resetTimerGPRSRequestIndication) { service(resetTimerGPRSRequestIndication); }
    @Override public void onFurnishChargingInformationGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest furnishChargingInformationGPRSRequestIndication) { service(furnishChargingInformationGPRSRequestIndication); }
    @Override public void onCancelGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.CancelGPRSRequest cancelGPRSRequestIndication) { service(cancelGPRSRequestIndication); }
    @Override public void onSendChargingInformationGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest sendChargingInformationGPRSRequestIndication) { service(sendChargingInformationGPRSRequestIndication); }
    @Override public void onApplyChargingReportGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest applyChargingReportGPRSRequestIndication) { service(applyChargingReportGPRSRequestIndication); }
    @Override public void onApplyChargingReportGPRSResponse(org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSResponse applyChargingReportGPRSResponseIndication) { service(applyChargingReportGPRSResponseIndication); }
    @Override public void onEventReportGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSRequest eventReportGPRSRequestIndication) { service(eventReportGPRSRequestIndication); }
    @Override public void onEventReportGPRSResponse(org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSResponse eventReportGPRSResponseIndication) { service(eventReportGPRSResponseIndication); }
    @Override public void onActivityTestGPRSRequest(org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSRequest activityTestGPRSRequestIndication) { service(activityTestGPRSRequestIndication); }
    @Override public void onActivityTestGPRSResponse(org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSResponse activityTestGPRSResponseIndication) { service(activityTestGPRSResponseIndication); }
    @Override public void onConnectSMSRequest(org.restcomm.protocols.ss7.cap.api.service.sms.ConnectSMSRequest connectSMSRequestIndication) { service(connectSMSRequestIndication); }
    @Override public void onEventReportSMSRequest(org.restcomm.protocols.ss7.cap.api.service.sms.EventReportSMSRequest eventReportSMSRequestIndication) { service(eventReportSMSRequestIndication); }
    @Override public void onFurnishChargingInformationSMSRequest(org.restcomm.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest furnishChargingInformationSMSRequestIndication) { service(furnishChargingInformationSMSRequestIndication); }
    @Override public void onInitialDPSMSRequest(org.restcomm.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest initialDPSMSRequestIndication) { service(initialDPSMSRequestIndication); }
    @Override public void onReleaseSMSRequest(org.restcomm.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest releaseSMSRequestIndication) { service(releaseSMSRequestIndication); }
    @Override public void onRequestReportSMSEventRequest(org.restcomm.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest requestReportSMSEventRequestIndication) { service(requestReportSMSEventRequestIndication); }
    @Override public void onResetTimerSMSRequest(org.restcomm.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest resetTimerSMSRequestIndication) { service(resetTimerSMSRequestIndication); }
    @Override public void onContinueSMSRequest(org.restcomm.protocols.ss7.cap.api.service.sms.ContinueSMSRequest continueSMSRequestIndication) { service(continueSMSRequestIndication); }
}
