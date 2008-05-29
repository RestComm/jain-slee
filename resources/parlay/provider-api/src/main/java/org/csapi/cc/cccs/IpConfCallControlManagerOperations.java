package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpConfCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpConfCallControlManagerOperations
	extends org.csapi.cc.mmccs.IpMultiMediaCallControlManagerOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.cccs.TpConfCallIdentifier createConference(org.csapi.cc.cccs.IpAppConfCall appConferenceCall, int numberOfSubConferences, org.csapi.cc.cccs.TpConfPolicy conferencePolicy, int numberOfParticipants, int duration) throws org.csapi.TpCommonExceptions;
	org.csapi.cc.cccs.TpConfSearchResult checkResources(org.csapi.cc.cccs.TpConfSearchCriteria searchCriteria) throws org.csapi.TpCommonExceptions;
	org.csapi.cc.cccs.TpResourceReservation reserveResources(org.csapi.cc.cccs.IpAppConfCallControlManager appInterface, java.lang.String startTime, int numberOfParticipants, int duration, org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions;
	void freeResources(org.csapi.cc.cccs.TpResourceReservation resourceReservation) throws org.csapi.TpCommonExceptions;
}
