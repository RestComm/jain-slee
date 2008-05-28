package org.csapi.ui;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppUIManagerPOATie
	extends IpAppUIManagerPOA
{
	private IpAppUIManagerOperations _delegate;

	private POA _poa;
	public IpAppUIManagerPOATie(IpAppUIManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppUIManagerPOATie(IpAppUIManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.ui.IpAppUIManager _this()
	{
		return org.csapi.ui.IpAppUIManagerHelper.narrow(_this_object());
	}
	public org.csapi.ui.IpAppUIManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.ui.IpAppUIManagerHelper.narrow(_this_object(orb));
	}
	public IpAppUIManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppUIManagerOperations delegate)
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
	public void userInteractionNotificationContinued()
	{
_delegate.userInteractionNotificationContinued();
	}

	public org.csapi.ui.IpAppUI reportNotification(org.csapi.ui.TpUIIdentifier userInteraction, org.csapi.ui.TpUIEventInfo eventInfo, int assignmentID)
	{
		return _delegate.reportNotification(userInteraction,eventInfo,assignmentID);
	}

	public void userInteractionNotificationInterrupted()
	{
_delegate.userInteractionNotificationInterrupted();
	}

	public org.csapi.ui.IpAppUI reportEventNotification(org.csapi.ui.TpUIIdentifier userInteraction, org.csapi.ui.TpUIEventNotificationInfo eventNotificationInfo, int assignmentID)
	{
		return _delegate.reportEventNotification(userInteraction,eventNotificationInfo,assignmentID);
	}

	public void userInteractionAborted(org.csapi.ui.TpUIIdentifier userInteraction)
	{
_delegate.userInteractionAborted(userInteraction);
	}

}
