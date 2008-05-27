package org.mobicents.csapi.jr.slee.am;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the request to retrieve the transaction history was successful and it returns the requested transaction history.
 * 
 * 
 */
public class RetrieveTransactionHistoryResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for RetrieveTransactionHistoryResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public RetrieveTransactionHistoryResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int retrievalID , org.csapi.am.TpTransactionHistory[] transactionHistory ){
        super(tpServiceIdentifier);
        this.retrievalID = retrievalID;
        this.transactionHistory = transactionHistory;
    }

    /**
     * Returns the retrievalID
     * 
     */
    public int getRetrievalID() {
        return this.retrievalID;
    }
    /**
     * Returns the transactionHistory
     * 
     */
    public org.csapi.am.TpTransactionHistory[] getTransactionHistory() {
        return this.transactionHistory;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof RetrieveTransactionHistoryResEvent)) {
            return false;
        } 
        RetrieveTransactionHistoryResEvent retrieveTransactionHistoryResEvent = (RetrieveTransactionHistoryResEvent) o;
        if(!(this.getService() == retrieveTransactionHistoryResEvent.getService())) {
            return false;
        }
        if(!(this.retrievalID == retrieveTransactionHistoryResEvent.retrievalID)) {
            return false;
        }
        if ((this.transactionHistory != null) && (retrieveTransactionHistoryResEvent.transactionHistory != null)) {
            if(!(this.transactionHistory.equals(retrieveTransactionHistoryResEvent.transactionHistory)))  {
                return false;
            }
        }
        if (this.hashCode() != o.hashCode()) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hashcode value for the object.
     */
    public int hashCode() {
        return 1;
    }

    // VARIABLES
    // .......................................................

    private int retrievalID;
    private org.csapi.am.TpTransactionHistory[] transactionHistory = null;

} // RetrieveTransactionHistoryResEvent

