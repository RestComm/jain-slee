/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor11;

import java.util.HashMap;

import javax.slee.facilities.Tracer;

public class TCKActivityMap {
    public TCKActivityMap(Tracer tracer) {
        this.vmStart = (int) System.currentTimeMillis();
        this.tracer = tracer;
    }

    // Public

    public synchronized TCKActivityHandleImpl allocateActivityHandle(Object activity) {
        TCKActivityHandleImpl ah = (TCKActivityHandleImpl) activityToHandle.get(activity);
        if (ah != null) {
            tracer.severe("Suspect TCK resource bug, two allocate invocations for the same TCKActivity object");
            return ah;
        }
        ah = new TCKActivityHandleImpl(vmStart, seq++);
        handleToActivity.put(ah, activity);
        activityToHandle.put(activity, ah);
        return ah;
    }

    public synchronized void deallocateActivityHandle(TCKActivityHandleImpl ah) {
        Object activity = handleToActivity.remove(ah);
        if (activity == null) {
            tracer.severe("unknown activity handle: " + ah);
        } else {
            activityToHandle.remove(activity);
        }
    }

    public synchronized TCKActivityHandleImpl getActivityHandle(Object activity) {
        if (tracer.isFinestEnabled())
            tracer.finest("getActivityHandle(): " + activity);
        return (TCKActivityHandleImpl) activityToHandle.get(activity);
    }

    public synchronized boolean isAllocated(Object activity) {
        return activityToHandle.containsKey(activity);
    }

    public synchronized Object getActivity(TCKActivityHandleImpl ah) {
        if (tracer.isFinestEnabled())
            tracer.finest("getActivity(): " + ah);
        return handleToActivity.get(ah);
    }

    public synchronized void clear() {
        handleToActivity.clear();
        activityToHandle.clear();
    }

    // Private

    private int seq = 0;
    private final int vmStart;
    private HashMap handleToActivity = new HashMap();
    private HashMap activityToHandle = new HashMap();
    private final Tracer tracer;
}
