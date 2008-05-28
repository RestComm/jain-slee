package org.mobicents.csapi.jr.slee.am;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This event indicates that the request to query the balance was successful and it reports the requested balance of an account to the application.
 * 
 * 
 */
public class QueryBalanceResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for QueryBalanceResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public QueryBalanceResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, int queryId , org.csapi.am.TpBalance[] balances ){
        super(tpServiceIdentifier);
        this.queryId = queryId;
        this.balances = balances;
    }

    /**
     * Returns the queryId
     * 
     */
    public int getQueryId() {
        return this.queryId;
    }
    /**
     * Returns the balances
     * 
     */
    public org.csapi.am.TpBalance[] getBalances() {
        return this.balances;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof QueryBalanceResEvent)) {
            return false;
        } 
        QueryBalanceResEvent queryBalanceResEvent = (QueryBalanceResEvent) o;
        if(!(this.getService() == queryBalanceResEvent.getService())) {
            return false;
        }
        if(!(this.queryId == queryBalanceResEvent.queryId)) {
            return false;
        }
        if ((this.balances != null) && (queryBalanceResEvent.balances != null)) {
            if(!(this.balances.equals(queryBalanceResEvent.balances)))  {
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
    private org.csapi.am.TpBalance[] balances = null;

} // QueryBalanceResEvent

