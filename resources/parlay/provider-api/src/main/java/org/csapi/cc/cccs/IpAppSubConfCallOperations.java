package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpAppSubConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppSubConfCallOperations
	extends org.csapi.cc.mmccs.IpAppMultiMediaCallOperations
{
	/* constants */
	/* operations  */
	void chairSelection(int subConferenceSessionID, int callLegSessionID);
	void floorRequest(int subConferenceSessionID, int callLegSessionID);
}
