package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpAppMultiMediaCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppMultiMediaCallLegOperations
	extends org.csapi.cc.mpccs.IpAppCallLegOperations
{
	/* constants */
	/* operations  */
	void mediaStreamMonitorRes(int callLegSessionID, org.csapi.cc.mmccs.TpMediaStream[] streams, org.csapi.cc.mmccs.TpMediaStreamEventType type);
}
