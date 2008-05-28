package org.mobicents.csapi.jr.slee.cc.mmccs;

/**
 * This interface shall be implemented by a Multi Media Call Control SCF.  Implementation of the superviseVolumeReq() method is optional.  The minimum required methods from IpMultiPartyCall are required.
 *
 * 
 * 
 */
public interface IpMultiMediaCallConnection extends org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection{


    /**
     *     The application calls this method to supervise a call. The application can set a granted data volume this call.
     *     @param volume Specifies the granted time in milliseconds for the connection.
    @param treatment Specifies how the network should react after the granted volume expired.

     */
    void superviseVolumeReq(org.csapi.cc.mmccs.TpCallSuperviseVolume volume,int treatment) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpMultiMediaCallConnection

