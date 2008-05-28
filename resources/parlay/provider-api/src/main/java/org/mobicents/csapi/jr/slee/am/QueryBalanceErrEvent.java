package org.mobicents.csapi.jr.slee.am;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the request to query the balance failed and it reports the cause of failure to the application.
 * 
 * 
 */
public class QueryBalanceErrEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for QueryBalanceErrEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public QueryBalanceErrEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int queryId , org.csapi.am.TpBalanceQueryError cause ){
        super(tpServiceIdentifier);
        this.queryId = queryId;
        this.cause = cause;
    }

    /**
     * Returns the queryId
     * 
     */
    public int getQueryId() {
        return this.queryId;
    }
    /**
     * Returns the cause
     * 
     */
    public org.csapi.am.TpBalanceQueryError getCause() {
        return this.cause;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof QueryBalanceErrEvent)) {
            return false;
        } 
        QueryBalanceErrEvent queryBalanceErrEvent = (QueryBalanceErrEvent) o;
        if(!(this.getService() == queryBalanceErrEvent.getService())) {
            return false;
        }
        if(!(this.queryId == queryBalanceErrEvent.queryId)) {
            return false;
        }
        if ((this.cause != null) && (queryBalanceErrEvent.cause != null)) {
            if(!(this.cause.equals(queryBalanceErrEvent.cause)))  {
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

    private int queryId;
    private org.csapi.am.TpBalanceQueryError cause = null;

} // QueryBalanceErrEvent

