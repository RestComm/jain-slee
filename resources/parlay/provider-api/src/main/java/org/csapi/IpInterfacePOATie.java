package org.csapi;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpInterface"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpInterfacePOATie
	extends IpInterfacePOA
{
	private IpInterfaceOperations _delegate;

	private POA _poa;
	public IpInterfacePOATie(IpInterfaceOperations delegate)
	{
		_delegate = delegate;
	}
	public IpInterfacePOATie(IpInterfaceOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.IpInterface _this()
	{
		return org.csapi.IpInterfaceHelper.narrow(_this_object());
	}
	public org.csapi.IpInterface _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.IpInterfaceHelper.narrow(_this_object(orb));
	}
	public IpInterfaceOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpInterfaceOperations delegate)
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
}
