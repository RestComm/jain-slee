package org.csapi.fw.fw_enterprise_operator.service_subscription;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpServiceProfileInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpServiceProfileInfoQueryPOATie
	extends IpServiceProfileInfoQueryPOA
{
	private IpServiceProfileInfoQueryOperations _delegate;

	private POA _poa;
	public IpServiceProfileInfoQueryPOATie(IpServiceProfileInfoQueryOperations delegate)
	{
		_delegate = delegate;
	}
	public IpServiceProfileInfoQueryPOATie(IpServiceProfileInfoQueryOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQueryHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQueryHelper.narrow(_this_object(orb));
	}
	public IpServiceProfileInfoQueryOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpServiceProfileInfoQueryOperations delegate)
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
	public java.lang.String[] listServiceProfiles() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.listServiceProfiles();
	}

	public org.csapi.fw.TpServiceProfileDescription describeServiceProfile(java.lang.String serviceProfileID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID
	{
		return _delegate.describeServiceProfile(serviceProfileID);
	}

	public java.lang.String[] listAssignedMembers(java.lang.String serviceProfileID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID
	{
		return _delegate.listAssignedMembers(serviceProfileID);
	}

}
