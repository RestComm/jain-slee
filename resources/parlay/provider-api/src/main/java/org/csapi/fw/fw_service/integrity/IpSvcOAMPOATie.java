package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpSvcOAM"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpSvcOAMPOATie
	extends IpSvcOAMPOA
{
	private IpSvcOAMOperations _delegate;

	private POA _poa;
	public IpSvcOAMPOATie(IpSvcOAMOperations delegate)
	{
		_delegate = delegate;
	}
	public IpSvcOAMPOATie(IpSvcOAMOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpSvcOAM _this()
	{
		return org.csapi.fw.fw_service.integrity.IpSvcOAMHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpSvcOAM _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpSvcOAMHelper.narrow(_this_object(orb));
	}
	public IpSvcOAMOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpSvcOAMOperations delegate)
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
	public java.lang.String systemDateTimeQuery(java.lang.String systemDateAndTime) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_TIME_AND_DATE_FORMAT
	{
		return _delegate.systemDateTimeQuery(systemDateAndTime);
	}

}
