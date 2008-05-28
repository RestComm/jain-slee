package org.csapi.ui;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpUIManagerPOATie
	extends IpUIManagerPOA
{
	private IpUIManagerOperations _delegate;

	private POA _poa;
	public IpUIManagerPOATie(IpUIManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpUIManagerPOATie(IpUIManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.ui.IpUIManager _this()
	{
		return org.csapi.ui.IpUIManagerHelper.narrow(_this_object());
	}
	public org.csapi.ui.IpUIManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.ui.IpUIManagerHelper.narrow(_this_object(orb));
	}
	public IpUIManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpUIManagerOperations delegate)
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

	public void changeNotification(int assignmentID, org.csapi.ui.TpUIEventCriteria eventCriteria) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
_delegate.changeNotification(assignmentID,eventCriteria);
	}

	public org.csapi.ui.TpUIEventCriteriaResult[] getNotification() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getNotification();
	}

	public void disableNotifications() throws org.csapi.TpCommonExceptions
	{
_delegate.disableNotifications();
	}

	public void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.destroyNotification(assignmentID);
	}

	public org.csapi.ui.TpUICallIdentifier createUICall(org.csapi.ui.IpAppUICall appUI, org.csapi.ui.TpUITargetObject uiTargetObject) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions
	{
		return _delegate.createUICall(appUI,uiTargetObject);
	}

	public int createNotification(org.csapi.ui.IpAppUIManager appUIManager, org.csapi.ui.TpUIEventCriteria eventCriteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createNotification(appUIManager,eventCriteria);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public int enableNotifications(org.csapi.ui.IpAppUIManager appUIManager) throws org.csapi.TpCommonExceptions
	{
		return _delegate.enableNotifications(appUIManager);
	}

	public org.csapi.ui.TpUIIdentifier createUI(org.csapi.ui.IpAppUI appUI, org.csapi.TpAddress userAddress) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions
	{
		return _delegate.createUI(appUI,userAddress);
	}

}
