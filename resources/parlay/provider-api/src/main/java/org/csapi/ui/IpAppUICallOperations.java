package org.csapi.ui;

/**
 *	Generated from IDL interface "IpAppUICall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppUICallOperations
	extends org.csapi.ui.IpAppUIOperations
{
	/* constants */
	/* operations  */
	void recordMessageRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response, int messageID);
	void recordMessageErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error);
	void deleteMessageRes(int usrInteractionSessionID, org.csapi.ui.TpUIReport response, int assignmentID);
	void deleteMessageErr(int usrInteractionSessionID, org.csapi.ui.TpUIError error, int assignmentID);
	void abortActionRes(int userInteractionSessionID, int assignmentID);
	void abortActionErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error);
}
