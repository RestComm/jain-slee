package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpAppCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppCallLegOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void eventReportRes(int callLegSessionID, org.csapi.cc.TpCallEventInfo eventInfo);
	void eventReportErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication);
	void attachMediaRes(int callLegSessionID);
	void attachMediaErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication);
	void detachMediaRes(int callLegSessionID);
	void detachMediaErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication);
	void getInfoRes(int callLegSessionID, org.csapi.cc.TpCallLegInfoReport callLegInfoReport);
	void getInfoErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication);
	void routeErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication);
	void superviseRes(int callLegSessionID, int report, int usedTime);
	void superviseErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication);
	void callLegEnded(int callLegSessionID, org.csapi.cc.TpReleaseCause cause);
}
