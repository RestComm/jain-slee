package org.csapi.ui;

/**
 *	Generated from IDL interface "IpAppUI"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppUIOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void sendInfoRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response);
	void sendInfoErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error);
	void sendInfoAndCollectRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response, java.lang.String collectedInfo);
	void sendInfoAndCollectErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error);
	void userInteractionFaultDetected(int userInteractionSessionID, org.csapi.ui.TpUIFault fault);
}
