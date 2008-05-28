package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;
/**
 * This class is used to pass a reference to a ongoing GCCS call to the
 * User Interaction Service. Call related user interaction may be performed on
 * it.
 */
public class CallUITarget {
    private TpServiceIdentifier tpServiceIdentifier = null;
    private  TpCallIdentifier tpCallIdentifier = null;
    
    public TpServiceIdentifier getTpServiceIdentifier() {
        return tpServiceIdentifier;
    }

    public void setTpServiceIdentifier(TpServiceIdentifier serviceIdentifier) {
        tpServiceIdentifier = serviceIdentifier;
    }

    /**
     * @return Returns the _tpCallIdentifier.
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return tpCallIdentifier;
    }

    /**
     * @param callIdentifier The _tpCallIdentifier to set.
     */
    public void setTpCallIdentifier(TpCallIdentifier callIdentifier) {
        tpCallIdentifier = callIdentifier;
    }

    public CallUITarget(TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier) {
        super();
        this.tpServiceIdentifier = tpServiceIdentifier;
        this.tpCallIdentifier = tpCallIdentifier;
    }
}
