package org.csapi.ui;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppUI"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppUIPOATie
	extends IpAppUIPOA
{
	private IpAppUIOperations _delegate;

	private POA _poa;
	public IpAppUIPOATie(IpAppUIOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppUIPOATie(IpAppUIOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.ui.IpAppUI _this()
	{
		return org.csapi.ui.IpAppUIHelper.narrow(_this_object());
	}
	public org.csapi.ui.IpAppUI _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.ui.IpAppUIHelper.narrow(_this_object(orb));
	}
	public IpAppUIOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppUIOperations delegate)
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
	public void userInteractionFaultDetected(int userInteractionSessionID, org.csapi.ui.TpUIFault fault)
	{
_delegate.userInteractionFaultDetected(userInteractionSessionID,fault);
	}

	public void sendInfoAndCollectRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response, java.lang.String collectedInfo)
	{
_delegate.sendInfoAndCollectRes(userInteractionSessionID,assignmentID,response,collectedInfo);
	}

	public void sendInfoErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error)
	{
_delegate.sendInfoErr(userInteractionSessionID,assignmentID,error);
	}

	public void sendInfoAndCollectErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error)
	{
_delegate.sendInfoAndCollectErr(userInteractionSessionID,assignmentID,error);
	}

	public void sendInfoRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response)
	{
_delegate.sendInfoRes(userInteractionSessionID,assignmentID,response);
	}

}
