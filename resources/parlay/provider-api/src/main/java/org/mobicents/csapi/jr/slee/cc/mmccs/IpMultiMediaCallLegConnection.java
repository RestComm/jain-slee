package org.mobicents.csapi.jr.slee.cc.mmccs;

/**
 * The Multi-Media call leg represents the signalling relationship between the call and an address. Associated with the signalling relationship there can be multiple media channels.  Media channels can be started and stopped by the terminals themselves. The application can monitor on these changes and influence them.								This interface shall be implemented by a Multi Media Call Control SCF.  The mediaStreamAllow() and mediaStreamMonitorReq() methods shall be implemented as a minimum requirement. The minimum required methods from IpCallLeg are also required.
 *
 * 
 * 
 */
public interface IpMultiMediaCallLegConnection extends org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection{


    /**
     *     This method can be used to allow setup of a media stream that was reported by a mediaStreamMonitorRes method.
     *     @param mediaStreamList Refers to the media streams (sessionIDs) as received in the mediaStreamMonitorRes() or in the reportMediaNotification() that is allowed to be established.

     */
    void mediaStreamAllow(int[] mediaStreamList) throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


    /**
     *     With this method the application can set monitors on the addition and subtraction of media streams. The monitors can either be general or restricted to certain types of codecs.
Monitoring on addition of media streams can be done in either interrupt of notify mode. In the first case the application has to allow or deny the establishment of the stream with mediaStreamAllow.
Monitoring on subtraction of media streams is only allowed in notify mode.
     *     @param mediaStreamEventCriteria Specifies the event specific criteria used by the application to define the event required. The mediaMonitorMode .is a parameter of TpMediaStreamRequest and  can be in interrupt or in notify mode. If in interrupt mode the application has to respond with mediaStreamAllow().

     */
    void mediaStreamMonitorReq(org.csapi.cc.mmccs.TpMediaStreamRequest[] mediaStreamEventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA,org.csapi.P_INVALID_EVENT_TYPE,javax.slee.resource.ResourceException;


    /**
     *     This method is used to return all currently established media streams for the leg.
     * 
     */
    org.csapi.cc.mmccs.TpMediaStream[] getMediaStreams() throws org.csapi.TpCommonExceptions,javax.slee.resource.ResourceException;


} // IpMultiMediaCallLegConnection

