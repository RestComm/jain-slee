package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpConfCallOperations
	extends org.csapi.cc.mmccs.IpMultiMediaCallOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.cccs.TpSubConfCallIdentifier[] getSubConferences(int conferenceSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.cc.cccs.TpSubConfCallIdentifier createSubConference(int conferenceSessionID, org.csapi.cc.cccs.IpAppSubConfCall appSubConference, org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void leaveMonitorReq(int conferenceSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.TpAddress getConferenceAddress(int conferenceSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
