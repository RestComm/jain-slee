package org.csapi.ui;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppUICall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppUICallPOATie
	extends IpAppUICallPOA
{
	private IpAppUICallOperations _delegate;

	private POA _poa;
	public IpAppUICallPOATie(IpAppUICallOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppUICallPOATie(IpAppUICallOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.ui.IpAppUICall _this()
	{
		return org.csapi.ui.IpAppUICallHelper.narrow(_this_object());
	}
	public org.csapi.ui.IpAppUICall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.ui.IpAppUICallHelper.narrow(_this_object(orb));
	}
	public IpAppUICallOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppUICallOperations delegate)
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
	public void abortActionErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error)
	{
_delegate.abortActionErr(userInteractionSessionID,assignmentID,error);
	}

	public void deleteMessageErr(int usrInteractionSessionID, org.csapi.ui.TpUIError error, int assignmentID)
	{
_delegate.deleteMessageErr(usrInteractionSessionID,error,assignmentID);
	}

	public void sendInfoAndCollectRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response, java.lang.String collectedInfo)
	{
_delegate.sendInfoAndCollectRes(userInteractionSessionID,assignmentID,response,collectedInfo);
	}

	public void sendInfoErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error)
	{
_delegate.sendInfoErr(userInteractionSessionID,assignmentID,error);
	}

	public void recordMessageErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error)
	{
_delegate.recordMessageErr(userInteractionSessionID,assignmentID,error);
	}

	public void sendInfoRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response)
	{
_delegate.sendInfoRes(userInteractionSessionID,assignmentID,response);
	}

	public void userInteractionFaultDetected(int userInteractionSessionID, org.csapi.ui.TpUIFault fault)
	{
_delegate.userInteractionFaultDetected(userInteractionSessionID,fault);
	}

	public void abortActionRes(int userInteractionSessionID, int assignmentID)
	{
_delegate.abortActionRes(userInteractionSessionID,assignmentID);
	}

	public void recordMessageRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response, int messageID)
	{
_delegate.recordMessageRes(userInteractionSessionID,assignmentID,response,messageID);
	}

	public void sendInfoAndCollectErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error)
	{
_delegate.sendInfoAndCollectErr(userInteractionSessionID,assignmentID,error);
	}

	public void deleteMessageRes(int usrInteractionSessionID, org.csapi.ui.TpUIReport response, int assignmentID)
	{
_delegate.deleteMessageRes(usrInteractionSessionID,response,assignmentID);
	}

}
