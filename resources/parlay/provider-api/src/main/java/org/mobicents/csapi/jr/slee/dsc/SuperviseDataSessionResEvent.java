package org.mobicents.csapi.jr.slee.dsc;

import org.mobicents.csapi.jr.slee.ParlayConnection;

/**
 *    This asynchronous event reports a data session supervision event to the application.  In addition, it may also be used to notify the application of a newly negotiated set of Quality of Service parameters during the active life of the data session.
 * 
 * 
 */
public class SuperviseDataSessionResEvent extends org.mobicents.csapi.jr.slee.ResourceEvent{

    /**
     * Constructor for SuperviseDataSessionResEvent
     * @param tpServiceIdentifier the service this event is related to
     */
    public SuperviseDataSessionResEvent(org.mobicents.csapi.jr.slee.TpServiceIdentifier tpServiceIdentifier, TpDataSessionIdentifier tpDataSessionIdentifier , int report , org.csapi.dsc.TpDataSessionSuperviseVolume usedVolume , org.csapi.TpDataSessionQosClass qualityOfService ){
        super(tpServiceIdentifier);
        this.tpDataSessionIdentifier = tpDataSessionIdentifier;
        this.report = report;
        this.usedVolume = usedVolume;
        this.qualityOfService = qualityOfService;
    }

    /**
     * Returns the tpDataSessionIdentifier
     * 
     */
    public TpDataSessionIdentifier getTpDataSessionIdentifier() {
        return this.tpDataSessionIdentifier;
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
    public org.csapi.dsc.TpDataSessionSuperviseVolume getUsedVolume() {
        return this.usedVolume;
    }
    /**
     * Returns the qualityOfService
     * 
     */
    public org.csapi.TpDataSessionQosClass getQualityOfService() {
        return this.qualityOfService;
    }

    /**
     * Indicates whether some other object is 'equal to' this one.
     */
    public boolean equals(Object o) {
        if (o == null) { 
            return false;
        }
        if(!(o instanceof SuperviseDataSessionResEvent)) {
            return false;
        } 
        SuperviseDataSessionResEvent superviseDataSessionResEvent = (SuperviseDataSessionResEvent) o;
        if(!(this.getService() == superviseDataSessionResEvent.getService())) {
            return false;
        }
        if ((this.tpDataSessionIdentifier != null) && (superviseDataSessionResEvent.tpDataSessionIdentifier != null)) {
            if(!(this.tpDataSessionIdentifier.equals(superviseDataSessionResEvent.tpDataSessionIdentifier)))  {
                return false;
            }
        }
        if(!(this.report == superviseDataSessionResEvent.report)) {
            return false;
        }
        if ((this.usedVolume != null) && (superviseDataSessionResEvent.usedVolume != null)) {
            if(!(this.usedVolume.equals(superviseDataSessionResEvent.usedVolume)))  {
                return false;
            }
        }
        if ((this.qualityOfService != null) && (superviseDataSessionResEvent.qualityOfService != null)) {
            if(!(this.qualityOfService.equals(superviseDataSessionResEvent.qualityOfService)))  {
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

    private TpDataSessionIdentifier tpDataSessionIdentifier = null;
    private int report;
    private org.csapi.dsc.TpDataSessionSuperviseVolume usedVolume = null;
    private org.csapi.TpDataSessionQosClass qualityOfService = null;

} // SuperviseDataSessionResEvent

