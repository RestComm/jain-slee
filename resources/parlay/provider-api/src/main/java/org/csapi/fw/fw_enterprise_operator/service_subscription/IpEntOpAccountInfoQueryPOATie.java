package org.csapi.fw.fw_enterprise_operator.service_subscription;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpEntOpAccountInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpEntOpAccountInfoQueryPOATie
	extends IpEntOpAccountInfoQueryPOA
{
	private IpEntOpAccountInfoQueryOperations _delegate;

	private POA _poa;
	public IpEntOpAccountInfoQueryPOATie(IpEntOpAccountInfoQueryOperations delegate)
	{
		_delegate = delegate;
	}
	public IpEntOpAccountInfoQueryPOATie(IpEntOpAccountInfoQueryOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountInfoQuery _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountInfoQueryHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountInfoQuery _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountInfoQueryHelper.narrow(_this_object(orb));
	}
	public IpEntOpAccountInfoQueryOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpEntOpAccountInfoQueryOperations delegate)
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
	public org.csapi.fw.TpEntOp describeEntOpAccount() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.describeEntOpAccount();
	}

}
