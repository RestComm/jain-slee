package org.csapi.ui;

/**
 *	Generated from IDL interface "IpUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpUIManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.ui.TpUIIdentifier createUI(org.csapi.ui.IpAppUI appUI, org.csapi.TpAddress userAddress) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions;
	org.csapi.ui.TpUICallIdentifier createUICall(org.csapi.ui.IpAppUICall appUI, org.csapi.ui.TpUITargetObject uiTargetObject) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions;
	int createNotification(org.csapi.ui.IpAppUIManager appUIManager, org.csapi.ui.TpUIEventCriteria eventCriteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
	void changeNotification(int assignmentID, org.csapi.ui.TpUIEventCriteria eventCriteria) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	org.csapi.ui.TpUIEventCriteriaResult[] getNotification() throws org.csapi.TpCommonExceptions;
	int enableNotifications(org.csapi.ui.IpAppUIManager appUIManager) throws org.csapi.TpCommonExceptions;
	void disableNotifications() throws org.csapi.TpCommonExceptions;
}
