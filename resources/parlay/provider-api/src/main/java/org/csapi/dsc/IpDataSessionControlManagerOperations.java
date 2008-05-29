package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpDataSessionControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpDataSessionControlManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int createNotification(org.csapi.dsc.IpAppDataSessionControlManager appDataSessionControlManager, org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions;
	void changeNotification(int assignmentID, org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	org.csapi.dsc.TpDataSessionEventCriteria getNotification() throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions;
	int enableNotifications(org.csapi.dsc.IpAppDataSessionControlManager appDataSessionControlManager) throws org.csapi.TpCommonExceptions;
	void disableNotifications() throws org.csapi.TpCommonExceptions;
	org.csapi.dsc.TpDataSessionEventCriteriaResult[] getNotifications() throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions;
	int createNotifications(org.csapi.dsc.IpAppDataSessionControlManager appDataSessionControlManager, org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
}
