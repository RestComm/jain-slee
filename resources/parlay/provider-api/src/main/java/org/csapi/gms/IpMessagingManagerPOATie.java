package org.csapi.gms;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpMessagingManagerPOATie
	extends IpMessagingManagerPOA
{
	private IpMessagingManagerOperations _delegate;

	private POA _poa;
	public IpMessagingManagerPOATie(IpMessagingManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpMessagingManagerPOATie(IpMessagingManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.gms.IpMessagingManager _this()
	{
		return org.csapi.gms.IpMessagingManagerHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpMessagingManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpMessagingManagerHelper.narrow(_this_object(orb));
	}
	public IpMessagingManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpMessagingManagerOperations delegate)
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
	public void disableMessagingNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.disableMessagingNotification(assignmentID);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public int enableMessagingNotification(org.csapi.gms.IpAppMessagingManager appInterface, org.csapi.gms.TpMessagingEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.enableMessagingNotification(appInterface,eventCriteria);
	}

	public org.csapi.gms.TpMailboxIdentifier openMailbox(org.csapi.TpAddress mailboxID, java.lang.String authenticationInfo) throws org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION,org.csapi.gms.P_GMS_INVALID_MAILBOX,org.csapi.TpCommonExceptions
	{
		return _delegate.openMailbox(mailboxID,authenticationInfo);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
