/*
 * micro-jainslee 1.2.0
 * Dual-licensed: GPLv3 (Section A) OR Commercial License (Section B).
 * Copyright (c) 2026 Tran Nhan (nhanth87). All rights reserved.
 */

package com.microjainslee.ra.jss7.collab;

import com.microjainslee.ra.jss7.Ss7Address;
import com.microjainslee.ra.jss7.component.Ss7TcapComponent;
import com.microjainslee.ra.jss7.event.Ss7Event;
import com.microjainslee.ra.jss7.transport.Ss7Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DialogIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcap.asn.comp.Component;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

import java.util.ArrayList;
import java.util.List;

/**
 * TCAP dialog listener → generic {@link Ss7Event} bridge.
 *
 * <p>Handles the full TCAP dialog lifecycle (Begin/Continue/End/Uni/Abort/
 * Notice) and carries raw {@link Ss7TcapComponent}s. This is the decode path
 * for protocols with no jSS7 provider (INAP) and for raw TCAP applications;
 * MAP and CAP additionally publish fully-decoded events via their own
 * adapters.</p>
 */
public final class Ss7TcapListener implements Ss7ProtocolAdapter, TCListener {

    private static final Logger LOG = LogManager.getLogger(Ss7TcapListener.class);

    private TCAPProvider provider;
    private Ss7EventPublisher publisher;

    @Override public String protocol() { return "TCAP"; }

    @Override
    public void attach(Ss7Stack stack, Ss7EventPublisher publisher) {
        this.provider = stack.tcapProvider();
        this.publisher = publisher;
        this.provider.addTCListener(this);
        LOG.info("[ra-jss7] TCAP listener attached");
    }

    @Override
    public void detach() {
        if (provider != null) provider.removeTCListener(this);
        LOG.info("[ra-jss7] TCAP listener detached");
    }

    // ── inbound TCAP → SLEE ───────────────────────────────────

    @Override
    public void onTCBegin(TCBeginIndication ind) {
        Dialog d = ind.getDialog();
        int acVersion = ind.getApplicationContextName() == null ? 0 : 1;
        fire(d, new Ss7Event.TcapBegin(dialogId(d),
                addr(ind.getOriginatingAddress()), addr(ind.getDestinationAddress()),
                acVersion, components(ind), true, networkId(d)));
    }

    @Override
    public void onTCContinue(TCContinueIndication ind) {
        Dialog d = ind.getDialog();
        fire(d, new Ss7Event.TcapContinue(dialogId(d), addr(originating(ind)),
                components(ind), networkId(d)));
    }

    @Override
    public void onTCEnd(TCEndIndication ind) {
        Dialog d = ind.getDialog();
        fire(d, new Ss7Event.TcapEnd(dialogId(d), addr(originating(ind)),
                components(ind), networkId(d)));
    }

    @Override
    public void onTCUni(TCUniIndication ind) {
        Dialog d = ind.getDialog();
        fire(d, new Ss7Event.TcapUni(dialogId(d),
                addr(ind.getOriginatingAddress()), addr(ind.getDestinationAddress()),
                components(ind), networkId(d)));
    }

    @Override
    public void onTCUserAbort(TCUserAbortIndication ind) {
        Dialog d = ind.getDialog();
        fire(d, new Ss7Event.TcapAbort(dialogId(d), addr(originating(ind)), 0, networkId(d)));
    }

    @Override
    public void onTCPAbort(TCPAbortIndication ind) {
        Dialog d = ind.getDialog();
        int cause = ind.getPAbortCause() == null ? 0 : ind.getPAbortCause().getType();
        fire(d, new Ss7Event.TcapAbort(dialogId(d), addr(originating(ind)), cause, networkId(d)));
    }

    @Override
    public void onTCNotice(TCNoticeIndication ind) {
        Dialog d = ind.getDialog();
        fire(d, new Ss7Event.TcapNotice(dialogId(d), addr(ind.getRemoteAddress()), 0, networkId(d)));
    }

    @Override public void onDialogReleased(Dialog d) { /* handled by MAP/CAP + sweeper */ }
    @Override public void onInvokeTimeout(Invoke invoke) { /* no-op */ }
    @Override public void onDialogTimeout(Dialog d) { /* no-op */ }

    // ── helpers ───────────────────────────────────────────────

    private void fire(Dialog d, Ss7Event e) {
        if (publisher == null) return;
        publisher.publish(dialogId(d), e);
    }

    private static String dialogId(Dialog d) {
        return d == null || d.getLocalDialogId() == null ? "?" : String.valueOf(d.getLocalDialogId());
    }

    private static int networkId(Dialog d) {
        return d == null ? 0 : d.getNetworkId();
    }

    private static SccpAddress originating(DialogIndication ind) {
        Dialog d = ind.getDialog();
        return d == null ? null : d.getRemoteAddress();
    }

    private static Ss7Address addr(SccpAddress a) {
        if (a == null) return null;
        GlobalTitle gt = a.getGlobalTitle();
        String digits = gt == null ? "" : gt.getDigits();
        return new Ss7Address(digits == null ? "" : digits, 0, 1, 4,
                a.getSignalingPointCode(), a.getSubsystemNumber());
    }

    private static List<Ss7TcapComponent> components(DialogIndication ind) {
        List<Ss7TcapComponent> out = new ArrayList<>();
        Component[] comps = ind.getComponents();
        if (comps == null) return out;
        for (Component c : comps) {
            if (c == null) continue;
            Long invokeId = c.getInvokeId();
            switch (c.getType()) {
                case Invoke -> {
                    int op = 0;
                    if (c instanceof Invoke inv) {
                        OperationCode oc = inv.getOperationCode();
                        if (oc != null && oc.getLocalOperationCode() != null)
                            op = oc.getLocalOperationCode().intValue();
                    }
                    out.add(new Ss7TcapComponent.Invoke(invokeId, op, new byte[0], false, 0));
                }
                case ReturnResult, ReturnResultLast ->
                        out.add(new Ss7TcapComponent.ReturnResult(invokeId, 0, new byte[0], true));
                case ReturnError ->
                        out.add(new Ss7TcapComponent.ReturnError(invokeId, 0, new byte[0]));
                case Reject ->
                        out.add(new Ss7TcapComponent.Reject(invokeId, 0, false));
            }
        }
        return out;
    }
}
