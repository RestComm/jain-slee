package org.csapi.am;

/**
 *	Generated from IDL interface "IpAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAccountManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int createNotification(org.csapi.am.IpAppAccountManager appAccountManager, org.csapi.am.TpChargingEventCriteria chargingEventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_INVALID_CRITERIA;
	void destroyNotification(int assignmentId) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
	int queryBalanceReq(org.csapi.TpAddress[] users) throws org.csapi.TpCommonExceptions,org.csapi.am.P_UNAUTHORIZED_APPLICATION,org.csapi.P_UNKNOWN_SUBSCRIBER;
	void changeNotification(int assignmentID, org.csapi.am.TpChargingEventCriteria eventCriteria) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_INVALID_CRITERIA;
	org.csapi.am.TpChargingEventCriteriaResult[] getNotification() throws org.csapi.TpCommonExceptions;
	int retrieveTransactionHistoryReq(org.csapi.TpAddress user, org.csapi.TpTimeInterval transactionInterval) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_TIME_AND_DATE_FORMAT,org.csapi.am.P_UNAUTHORIZED_APPLICATION,org.csapi.P_UNKNOWN_SUBSCRIBER;
	int enableNotifications(org.csapi.am.IpAppAccountManager appAccountManager) throws org.csapi.TpCommonExceptions;
	void disableNotifications() throws org.csapi.TpCommonExceptions;
}
