package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpAppMultiPartyCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppMultiPartyCallOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void getInfoRes(int callSessionID, org.csapi.cc.TpCallInfoReport callInfoReport);
	void getInfoErr(int callSessionID, org.csapi.cc.TpCallError errorIndication);
	void superviseRes(int callSessionID, int report, int usedTime);
	void superviseErr(int callSessionID, org.csapi.cc.TpCallError errorIndication);
	void callEnded(int callSessionID, org.csapi.cc.TpCallEndedReport report);
	void createAndRouteCallLegErr(int callSessionID, org.csapi.cc.mpccs.TpCallLegIdentifier callLegReference, org.csapi.cc.TpCallError errorIndication);
}
