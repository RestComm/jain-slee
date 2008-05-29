package org.mobicents.csapi.jr.slee.am;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the request to retrieve the transaction history failed and it reports the cause of failure to the application.
 * 
 * 
 */
public class RetrieveTransactionHistoryErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for RetrieveTransactionHistoryErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public RetrieveTransactionHistoryErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int retrievalID , org.csapi.am.TpTransactionHistoryStatus transactionHistoryError ){
        super(tpServiceIdentifier);
        this.retrievalID = retrievalID;
        this.transactionHistoryError = transactionHistoryError;
    }

    /**
     * Returns the retrievalID
     * 
     */
    public int getRetrievalID() {
        return this.retrievalID;
    }
    /**
     * Returns the transactionHistoryError
     * 
     */
    public org.csapi.am.TpTransactionHistoryStatus getTransactionHistoryError() {
        return this.transactionHistoryError;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof RetrieveTransactionHistoryErrEvent)) {
            return false;
        } 
        RetrieveTransactionHistoryErrEvent retrieveTransactionHistoryErrEvent = (RetrieveTransactionHistoryErrEvent) o;
        if(!(this.getService() == retrieveTransactionHistoryErrEvent.getService())) {
            return false;
        }
        if(!(this.retrievalID == retrieveTransactionHistoryErrEvent.retrievalID)) {
            return false;
        }
        if ((this.transactionHistoryError != null) && (retrieveTransactionHistoryErrEvent.transactionHistoryError != null)) {
            if(!(this.transactionHistoryError.equals(retrieveTransactionHistoryErrEvent.transactionHistoryError)))  {
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
    private org.csapi.am.TpTransactionHistoryStatus transactionHistoryError = null;

} // RetrieveTransactionHistoryErrEvent

