package org.mobicents.csapi.jr.slee.cc.mmccs;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports a call supervision event to the application when it has indicated its interest in these kind of events.
It is also called when the connection is terminated before the supervision event occurs.
 * 
 * 
 */
public class SuperviseVolumeResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SuperviseVolumeResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SuperviseVolumeResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpMultiMediaCallIdentifier tpMultiMediaCallIdentifier , int report , org.csapi.cc.mmccs.TpCallSuperviseVolume usedVolume ){
        super(tpServiceIdentifier);
        this.tpMultiMediaCallIdentifier = tpMultiMediaCallIdentifier;
        this.report = report;
        this.usedVolume = usedVolume;
    }

    /**
     * Returns the tpMultiMediaCallIdentifier
     * 
     */
    public TpMultiMediaCallIdentifier getTpMultiMediaCallIdentifier() {
        return this.tpMultiMediaCallIdentifier;
    }
    /**
     * Returns the report
     * 
     */
    public int getReport() {
        return this.report;
    }
    /**
     * Returns the usedVolume
     * 
     */
    public org.csapi.cc.mmccs.TpCallSuperviseVolume getUsedVolume() {
        return this.usedVolume;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SuperviseVolumeResEvent)) {
            return false;
        } 
        SuperviseVolumeResEvent superviseVolumeResEvent = (SuperviseVolumeResEvent) o;
        if(!(this.getService() == superviseVolumeResEvent.getService())) {
            return false;
        }
        if ((this.tpMultiMediaCallIdentifier != null) && (superviseVolumeResEvent.tpMultiMediaCallIdentifier != null)) {
            if(!(this.tpMultiMediaCallIdentifier.equals(superviseVolumeResEvent.tpMultiMediaCallIdentifier)))  {
                return false;
            }
        }
        if(!(this.report == superviseVolumeResEvent.report)) {
            return false;
        }
        if ((this.usedVolume != null) && (superviseVolumeResEvent.usedVolume != null)) {
            if(!(this.usedVolume.equals(superviseVolumeResEvent.usedVolume)))  {
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

    private TpMultiMediaCallIdentifier tpMultiMediaCallIdentifier = null;
    private int report;
    private org.csapi.cc.mmccs.TpCallSuperviseVolume usedVolume = null;

} // SuperviseVolumeResEvent

