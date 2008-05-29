package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppOAM"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppOAMPOATie
	extends IpAppOAMPOA
{
	private IpAppOAMOperations _delegate;

	private POA _poa;
	public IpAppOAMPOATie(IpAppOAMOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppOAMPOATie(IpAppOAMOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpAppOAM _this()
	{
		return org.csapi.fw.fw_application.integrity.IpAppOAMHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpAppOAM _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpAppOAMHelper.narrow(_this_object(orb));
	}
	public IpAppOAMOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppOAMOperations delegate)
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
	public java.lang.String systemDateTimeQuery(java.lang.String systemDateAndTime)
	{
		return _delegate.systemDateTimeQuery(systemDateAndTime);
	}

}
