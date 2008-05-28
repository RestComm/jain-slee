package org.csapi.cs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppChargingManagerPOATie
	extends IpAppChargingManagerPOA
{
	private IpAppChargingManagerOperations _delegate;

	private POA _poa;
	public IpAppChargingManagerPOATie(IpAppChargingManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppChargingManagerPOATie(IpAppChargingManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cs.IpAppChargingManager _this()
	{
		return org.csapi.cs.IpAppChargingManagerHelper.narrow(_this_object());
	}
	public org.csapi.cs.IpAppChargingManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cs.IpAppChargingManagerHelper.narrow(_this_object(orb));
	}
	public IpAppChargingManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppChargingManagerOperations delegate)
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
	public void sessionAborted(int sessionID)
	{
_delegate.sessionAborted(sessionID);
	}

}
