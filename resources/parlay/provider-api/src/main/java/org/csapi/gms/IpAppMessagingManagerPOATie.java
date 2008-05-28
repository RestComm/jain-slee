package org.csapi.gms;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppMessagingManagerPOATie
	extends IpAppMessagingManagerPOA
{
	private IpAppMessagingManagerOperations _delegate;

	private POA _poa;
	public IpAppMessagingManagerPOATie(IpAppMessagingManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppMessagingManagerPOATie(IpAppMessagingManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.gms.IpAppMessagingManager _this()
	{
		return org.csapi.gms.IpAppMessagingManagerHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpAppMessagingManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpAppMessagingManagerHelper.narrow(_this_object(orb));
	}
	public IpAppMessagingManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppMessagingManagerOperations delegate)
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
	public void mailboxTerminated(org.csapi.gms.IpMailbox mailbox, int mailboxSessionID)
	{
_delegate.mailboxTerminated(mailbox,mailboxSessionID);
	}

	public void messagingEventNotify(org.csapi.gms.IpMessagingManager messagingManager, org.csapi.gms.TpMessagingEventInfo eventInfo, int assignmentID)
	{
_delegate.messagingEventNotify(messagingManager,eventInfo,assignmentID);
	}

	public void messagingNotificationTerminated()
	{
_delegate.messagingNotificationTerminated();
	}

	public void mailboxFaultDetected(org.csapi.gms.IpMailbox mailbox, int mailboxSessionID, org.csapi.gms.TpMessagingFault fault)
	{
_delegate.mailboxFaultDetected(mailbox,mailboxSessionID,fault);
	}

}
