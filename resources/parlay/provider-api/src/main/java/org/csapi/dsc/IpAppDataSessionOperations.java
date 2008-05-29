package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpAppDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppDataSessionOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void connectRes(int dataSessionID, org.csapi.dsc.TpDataSessionReport eventReport, int assignmentID);
	void connectErr(int dataSessionID, org.csapi.dsc.TpDataSessionError errorIndication, int assignmentID);
	void superviseDataSessionRes(int dataSessionID, int report, org.csapi.dsc.TpDataSessionSuperviseVolume usedVolume, org.csapi.TpDataSessionQosClass qualityOfService);
	void superviseDataSessionErr(int dataSessionID, org.csapi.dsc.TpDataSessionError errorIndication);
	void dataSessionFaultDetected(int dataSessionID, org.csapi.dsc.TpDataSessionFault fault);
}
