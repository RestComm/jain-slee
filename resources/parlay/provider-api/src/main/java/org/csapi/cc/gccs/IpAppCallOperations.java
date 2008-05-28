package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpAppCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppCallOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void routeRes(int callSessionID, org.csapi.cc.gccs.TpCallReport eventReport, int callLegSessionID);
	void routeErr(int callSessionID, org.csapi.cc.TpCallError errorIndication, int callLegSessionID);
	void getCallInfoRes(int callSessionID, org.csapi.cc.gccs.TpCallInfoReport callInfoReport);
	void getCallInfoErr(int callSessionID, org.csapi.cc.TpCallError errorIndication);
	void superviseCallRes(int callSessionID, int report, int usedTime);
	void superviseCallErr(int callSessionID, org.csapi.cc.TpCallError errorIndication);
	void callFaultDetected(int callSessionID, org.csapi.cc.gccs.TpCallFault fault);
	void getMoreDialledDigitsRes(int callSessionID, java.lang.String digits);
	void getMoreDialledDigitsErr(int callSessionID, org.csapi.cc.TpCallError errorIndication);
	void callEnded(int callSessionID, org.csapi.cc.gccs.TpCallEndedReport report);
}
