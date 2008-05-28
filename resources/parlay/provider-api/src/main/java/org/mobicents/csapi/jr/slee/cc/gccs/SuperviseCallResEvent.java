package org.mobicents.csapi.jr.slee.cc.gccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports a call supervision event to the application when it has indicated its interest in this kind of event.
It is also called when the connection is terminated before the supervision event occurs.
 * 
 * 
 */
public class SuperviseCallResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SuperviseCallResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SuperviseCallResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpCallIdentifier tpCallIdentifier , int report , int usedTime ){
        super(tpServiceIdentifier);
        this.tpCallIdentifier = tpCallIdentifier;
        this.report = report;
        this.usedTime = usedTime;
    }

    /**
     * Returns the tpCallIdentifier
     * 
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return this.tpCallIdentifier;
    }
    /**
     * Returns the report
     * 
     */
    public int getReport() {
        return this.report;
    }
    /**
     * Returns the usedTime
     * 
     */
    public int getUsedTime() {
        return this.usedTime;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SuperviseCallResEvent)) {
            return false;
        } 
        SuperviseCallResEvent superviseCallResEvent = (SuperviseCallResEvent) o;
        if(!(this.getService() == superviseCallResEvent.getService())) {
            return false;
        }
        if ((this.tpCallIdentifier != null) && (superviseCallResEvent.tpCallIdentifier != null)) {
            if(!(this.tpCallIdentifier.equals(superviseCallResEvent.tpCallIdentifier)))  {
                return false;
            }
        }
        if(!(this.report == superviseCallResEvent.report)) {
            return false;
        }
        if(!(this.usedTime == superviseCallResEvent.usedTime)) {
            return false;
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

    private TpCallIdentifier tpCallIdentifier = null;
    private int report;
    private int usedTime;

} // SuperviseCallResEvent

