package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpSubConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpSubConfCallOperations
	extends org.csapi.cc.mmccs.IpMultiMediaCallOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.cccs.TpSubConfCallIdentifier splitSubConference(int subConferenceSessionID, int[] callLegList, org.csapi.cc.cccs.IpAppSubConfCall appSubConferenceCall) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void mergeSubConference(int subConferenceCallSessionID, int targetSubConferenceCall) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void moveCallLeg(int subConferenceCallSessionID, int targetSubConferenceCall, int callLeg) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void inspectVideo(int subConferenceSessionID, int inspectedCallLeg) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void inspectVideoCancel(int subConferenceSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void appointSpeaker(int subConferenceSessionID, int speakerCallLeg) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void chairSelection(int subConferenceSessionID, int chairCallLeg) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void changeConferencePolicy(int subConferenceSessionID, org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
