package org.csapi.dsc;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpDataSessionControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpDataSessionControlManagerPOATie
	extends IpDataSessionControlManagerPOA
{
	private IpDataSessionControlManagerOperations _delegate;

	private POA _poa;
	public IpDataSessionControlManagerPOATie(IpDataSessionControlManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpDataSessionControlManagerPOATie(IpDataSessionControlManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.dsc.IpDataSessionControlManager _this()
	{
		return org.csapi.dsc.IpDataSessionControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.dsc.IpDataSessionControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.dsc.IpDataSessionControlManagerHelper.narrow(_this_object(orb));
	}
	public IpDataSessionControlManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpDataSessionControlManagerOperations delegate)
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

	public int enableNotifications(org.csapi.dsc.IpAppDataSessionControlManager appDataSessionControlManager) throws org.csapi.TpCommonExceptions
	{
		return _delegate.enableNotifications(appDataSessionControlManager);
	}

	public int createNotification(org.csapi.dsc.IpAppDataSessionControlManager appDataSessionControlManager, org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createNotification(appDataSessionControlManager,eventCriteria);
	}

	public org.csapi.dsc.TpDataSessionEventCriteria getNotification() throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions
	{
		return _delegate.getNotification();
	}

	public void disableNotifications() throws org.csapi.TpCommonExceptions
	{
_delegate.disableNotifications();
	}

	public org.csapi.dsc.TpDataSessionEventCriteriaResult[] getNotifications() throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions
	{
		return _delegate.getNotifications();
	}

	public void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions
	{
_delegate.destroyNotification(assignmentID);
	}

	public int createNotifications(org.csapi.dsc.IpAppDataSessionControlManager appDataSessionControlManager, org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createNotifications(appDataSessionControlManager,eventCriteria);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void changeNotification(int assignmentID, org.csapi.dsc.TpDataSessionEventCriteria eventCriteria) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
_delegate.changeNotification(assignmentID,eventCriteria);
	}

}
