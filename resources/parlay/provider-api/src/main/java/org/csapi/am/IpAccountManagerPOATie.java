package org.csapi.am;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAccountManagerPOATie
	extends IpAccountManagerPOA
{
	private IpAccountManagerOperations _delegate;

	private POA _poa;
	public IpAccountManagerPOATie(IpAccountManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAccountManagerPOATie(IpAccountManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.am.IpAccountManager _this()
	{
		return org.csapi.am.IpAccountManagerHelper.narrow(_this_object());
	}
	public org.csapi.am.IpAccountManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.am.IpAccountManagerHelper.narrow(_this_object(orb));
	}
	public IpAccountManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAccountManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public void changeNotification(int assignmentID, org.csapi.am.TpChargingEventCriteria eventCriteria) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_INVALID_CRITERIA
	{
_delegate.changeNotification(assignmentID,eventCriteria);
	}

	public org.csapi.am.TpChargingEventCriteriaResult[] getNotification() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getNotification();
	}

	public int createNotification(org.csapi.am.IpAppAccountManager appAccountManager, org.csapi.am.TpChargingEventCriteria chargingEventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNKNOWN_SUBSCRIBER,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createNotification(appAccountManager,chargingEventCriteria);
	}

	public void disableNotifications() throws org.csapi.TpCommonExceptions
	{
_delegate.disableNotifications();
	}

	public void destroyNotification(int assignmentId) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.destroyNotification(assignmentId);
	}

	public int queryBalanceReq(org.csapi.TpAddress[] users) throws org.csapi.TpCommonExceptions,org.csapi.am.P_UNAUTHORIZED_APPLICATION,org.csapi.P_UNKNOWN_SUBSCRIBER
	{
		return _delegate.queryBalanceReq(users);
	}

	public int enableNotifications(org.csapi.am.IpAppAccountManager appAccountManager) throws org.csapi.TpCommonExceptions
	{
		return _delegate.enableNotifications(appAccountManager);
	}

	public int retrieveTransactionHistoryReq(org.csapi.TpAddress user, org.csapi.TpTimeInterval transactionInterval) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_TIME_AND_DATE_FORMAT,org.csapi.am.P_UNAUTHORIZED_APPLICATION,org.csapi.P_UNKNOWN_SUBSCRIBER
	{
		return _delegate.retrieveTransactionHistoryReq(user,transactionInterval);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

}
