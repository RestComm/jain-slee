package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpFwOAM"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpFwOAMPOATie
	extends IpFwOAMPOA
{
	private IpFwOAMOperations _delegate;

	private POA _poa;
	public IpFwOAMPOATie(IpFwOAMOperations delegate)
	{
		_delegate = delegate;
	}
	public IpFwOAMPOATie(IpFwOAMOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpFwOAM _this()
	{
		return org.csapi.fw.fw_service.integrity.IpFwOAMHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpFwOAM _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpFwOAMHelper.narrow(_this_object(orb));
	}
	public IpFwOAMOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpFwOAMOperations delegate)
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
	public java.lang.String systemDateTimeQuery(java.lang.String clientDateAndTime) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_TIME_AND_DATE_FORMAT
	{
		return _delegate.systemDateTimeQuery(clientDateAndTime);
	}

}
