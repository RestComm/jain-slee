package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMultiMediaCallLegOperations
	extends org.csapi.cc.mpccs.IpCallLegOperations
{
	/* constants */
	/* operations  */
	void mediaStreamAllow(int callLegSessionID, int[] mediaStreamList) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void mediaStreamMonitorReq(int callLegSessionID, org.csapi.cc.mmccs.TpMediaStreamRequest[] mediaStreamEventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_INVALID_CRITERIA;
	org.csapi.cc.mmccs.TpMediaStream[] getMediaStreams(int callLegSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
