package org.csapi.fw.fw_enterprise_operator.service_subscription;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpEntOpAccountManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpEntOpAccountManagementPOATie
	extends IpEntOpAccountManagementPOA
{
	private IpEntOpAccountManagementOperations _delegate;

	private POA _poa;
	public IpEntOpAccountManagementPOATie(IpEntOpAccountManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpEntOpAccountManagementPOATie(IpEntOpAccountManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountManagement _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountManagementHelper.narrow(_this_object(orb));
	}
	public IpEntOpAccountManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpEntOpAccountManagementOperations delegate)
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
	public void deleteEntOpAccount() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
_delegate.deleteEntOpAccount();
	}

	public void modifyEntOpAccount(org.csapi.fw.TpProperty[] enterpriseOperatorProperties) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY
	{
_delegate.modifyEntOpAccount(enterpriseOperatorProperties);
	}

}
