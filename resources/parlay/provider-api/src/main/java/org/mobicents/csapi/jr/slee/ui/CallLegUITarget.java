package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
/**
 * This class is used to pass a reference to a ongoing call leg to the
 * User Interaction Service. Call related user interaction may be performed on
 * it.
 */
public class CallLegUITarget {
    private TpServiceIdentifier tpServiceIdentifier = null;
    private TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier = null;
    private TpCallLegIdentifier tpCallLegIdentifier = null;
    /**
     * @return Returns the tpMultiPartyCallIdentifier.
     */
    public TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier() {
        return tpMultiPartyCallIdentifier;
    }
    /**
     * @param tpMultiPartyCallIdentifier The tpMultiPartyCallIdentifier to set.
     */
    public void setTpMultiPartyCallIdentifier(TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier) {
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
    }
    /**
     * @return Returns the tpCallLegIdentifier.
     */
    public TpCallLegIdentifier getTpCallLegIdentifier() {
        return tpCallLegIdentifier;
    }
    /**
     * @param tpCallLegIdentifier The tpCallLegIdentifier to set.
     */
    public void setTpCallLegIdentifier(TpCallLegIdentifier tpCallLegIdentifier) {
        this.tpCallLegIdentifier = tpCallLegIdentifier;
    }
    /**
     * @return Returns the tpServiceIdentifier.
     */
    public TpServiceIdentifier getTpServiceIdentifier() {
        return tpServiceIdentifier;
    }
    /**
     * @param tpServiceIdentifier The tpServiceIdentifier to set.
     */
    public void setTpServiceIdentifier(TpServiceIdentifier tpServiceIdentifier) {
        this.tpServiceIdentifier = tpServiceIdentifier;
    }
    public CallLegUITarget(TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier, TpCallLegIdentifier tpCallLegIdentifier) {
        super();
        this.tpServiceIdentifier = tpServiceIdentifier;
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
    }
  
    

}
