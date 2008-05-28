package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpAppConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppConfCallOperations
	extends org.csapi.cc.mmccs.IpAppMultiMediaCallOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.mpccs.IpAppCallLeg partyJoined(int conferenceSessionID, org.csapi.cc.mpccs.TpCallLegIdentifier callLeg, org.csapi.cc.cccs.TpJoinEventInfo eventInfo);
	void leaveMonitorRes(int conferenceSessionID, int callLeg);
}
