package org.mobicents.csapi.jr.slee.ui;

import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
/**
 * This class is used to pass a reference to a ongoing multiparty call to the
 * User Interaction Service. Call related user interaction may be performed on
 * it.
 */
public class MultiPartyCallUITarget {
    
    private TpServiceIdentifier tpServiceIdentifier = null;
    private TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier = null;

    public TpServiceIdentifier getTpServiceIdentifier() {
        return tpServiceIdentifier;
    }

    public MultiPartyCallUITarget(TpServiceIdentifier tpServiceIdentifier, TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier) {
        super();
        this.tpServiceIdentifier = tpServiceIdentifier;
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
    }

    public void setTpServiceIdentifier(TpServiceIdentifier serviceIdentifier) {
        this.tpServiceIdentifier = serviceIdentifier;
    }

    /**
     * @return Returns the tpMultiPartyCallIdentifier.
     */
    public TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier() {
        return tpMultiPartyCallIdentifier;
    }

    /**
     * @param tpMultiPartyCallIdentifier
     *            The tpMultiPartyCallIdentifier to set.
     */
    public void setTpMultiPartyCallIdentifier(
            TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier) {
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
    }
}
