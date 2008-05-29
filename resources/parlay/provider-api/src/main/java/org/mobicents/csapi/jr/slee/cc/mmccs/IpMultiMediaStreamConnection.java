package org.mobicents.csapi.jr.slee.cc.mmccs;

/**
 * The Multi Media Stream Interface represents a bi-directional information stream associated with a call leg. Currently, the only available method is to subtract the media stream.  This interface and the subtract() method shall be implemented by a Multi Media Call Control SCF.
 *
 * 
 * 
 */
public interface IpMultiMediaStreamConnection extends org.mobicents.csapi.jr.slee.IpServiceConnection {


    /**
     *     This method can be used to subtract the multi-media stream.
     * 
     */
    void subtract() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpMultiMediaStreamConnection

