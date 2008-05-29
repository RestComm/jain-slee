package org.csapi.cc.mmccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpMultiMediaCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpMultiMediaCallControlManagerPOATie
	extends IpMultiMediaCallControlManagerPOA
{
	private IpMultiMediaCallControlManagerOperations _delegate;

	private POA _poa;
	public IpMultiMediaCallControlManagerPOATie(IpMultiMediaCallControlManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpMultiMediaCallControlManagerPOATie(IpMultiMediaCallControlManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.mmccs.IpMultiMediaCallControlManager _this()
	{
		return org.csapi.cc.mmccs.IpMultiMediaCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpMultiMediaCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpMultiMediaCallControlManagerHelper.narrow(_this_object(orb));
	}
	public IpMultiMediaCallControlManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpMultiMediaCallControlManagerOperations delegate)
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

	public void destroyMediaNotification(int assignmentID) throws org.csapi.TpCommonExceptions
	{
_delegate.destroyMediaNotification(assignmentID);
	}

	public int createMediaNotification(org.csapi.cc.mmccs.IpAppMultiMediaCallControlManager appInterface, org.csapi.cc.mmccs.TpNotificationMediaRequest notificationMediaRequest) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createMediaNotification(appInterface,notificationMediaRequest);
	}

	public org.csapi.cc.TpNotificationRequested[] getNotification() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getNotification();
	}

	public void disableNotifications() throws org.csapi.TpCommonExceptions
	{
_delegate.disableNotifications();
	}

	public org.csapi.cc.mpccs.TpMultiPartyCallIdentifier createCall(org.csapi.cc.mpccs.IpAppMultiPartyCall appCall) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
		return _delegate.createCall(appCall);
	}

	public int setCallLoadControl(int duration, org.csapi.cc.TpCallLoadControlMechanism mechanism, org.csapi.cc.TpCallTreatment treatment, org.csapi.TpAddressRange addressRange) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN
	{
		return _delegate.setCallLoadControl(duration,mechanism,treatment,addressRange);
	}

	public int createNotification(org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager appCallControlManager, org.csapi.cc.TpCallNotificationRequest notificationRequest) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createNotification(appCallControlManager,notificationRequest);
	}

	public void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.destroyNotification(assignmentID);
	}

	public int enableNotifications(org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager appCallControlManager) throws org.csapi.TpCommonExceptions
	{
		return _delegate.enableNotifications(appCallControlManager);
	}

	public void changeMediaNotification(int assignmentID, org.csapi.cc.mmccs.TpNotificationMediaRequest notificationMediaRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
_delegate.changeMediaNotification(assignmentID,notificationMediaRequest);
	}

	public void changeNotification(int assignmentID, org.csapi.cc.TpCallNotificationRequest notificationRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
_delegate.changeNotification(assignmentID,notificationRequest);
	}

	public org.csapi.cc.mmccs.TpMediaNotificationRequested[] getMediaNotification() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getMediaNotification();
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public org.csapi.cc.TpNotificationRequestedSetEntry getNextNotification(boolean reset) throws org.csapi.TpCommonExceptions
	{
		return _delegate.getNextNotification(reset);
	}

}
