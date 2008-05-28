package org.csapi.ui;

/**
 *	Generated from IDL interface "IpUICall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpUICallOperations
	extends org.csapi.ui.IpUIOperations
{
	/* constants */
	/* operations  */
	int recordMessageReq(int userInteractionSessionID, org.csapi.ui.TpUIInfo info, org.csapi.ui.TpUIMessageCriteria criteria) throws org.csapi.ui.P_ILLEGAL_ID,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.ui.P_ID_NOT_FOUND,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_INVALID_CRITERIA;
	int deleteMessageReq(int usrInteractionSessionID, int messageID) throws org.csapi.ui.P_ILLEGAL_ID,org.csapi.TpCommonExceptions,org.csapi.ui.P_ID_NOT_FOUND,org.csapi.P_INVALID_SESSION_ID;
	void abortActionReq(int userInteractionSessionID, int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
