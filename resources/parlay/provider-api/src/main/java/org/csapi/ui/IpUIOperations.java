package org.csapi.ui;

/**
 *	Generated from IDL interface "IpUI"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpUIOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int sendInfoReq(int userInteractionSessionID, org.csapi.ui.TpUIInfo info, java.lang.String language, org.csapi.ui.TpUIVariableInfo[] variableInfo, int repeatIndicator, int responseRequested) throws org.csapi.ui.P_ILLEGAL_ID,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.ui.P_ID_NOT_FOUND,org.csapi.P_INVALID_SESSION_ID;
	int sendInfoAndCollectReq(int userInteractionSessionID, org.csapi.ui.TpUIInfo info, java.lang.String language, org.csapi.ui.TpUIVariableInfo[] variableInfo, org.csapi.ui.TpUICollectCriteria criteria, int responseRequested) throws org.csapi.ui.P_ILLEGAL_ID,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.ui.P_ILLEGAL_RANGE,org.csapi.ui.P_ID_NOT_FOUND,org.csapi.P_INVALID_SESSION_ID,org.csapi.ui.P_INVALID_COLLECTION_CRITERIA;
	void release(int userInteractionSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setOriginatingAddress(int userInteractionSessionID, java.lang.String origin) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID;
	java.lang.String getOriginatingAddress(int userInteractionSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
