package org.csapi.fw.fw_enterprise_operator.service_subscription;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpClientAppInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpClientAppInfoQueryPOATie
	extends IpClientAppInfoQueryPOA
{
	private IpClientAppInfoQueryOperations _delegate;

	private POA _poa;
	public IpClientAppInfoQueryPOATie(IpClientAppInfoQueryOperations delegate)
	{
		_delegate = delegate;
	}
	public IpClientAppInfoQueryPOATie(IpClientAppInfoQueryOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQuery _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQueryHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQuery _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQueryHelper.narrow(_this_object(orb));
	}
	public IpClientAppInfoQueryOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpClientAppInfoQueryOperations delegate)
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
	public java.lang.String describeSAG(java.lang.String sagID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.describeSAG(sagID);
	}

	public org.csapi.fw.TpClientAppDescription describeClientApp(java.lang.String clientAppID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID
	{
		return _delegate.describeClientApp(clientAppID);
	}

	public java.lang.String[] listSAGMembers(java.lang.String sagID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.listSAGMembers(sagID);
	}

	public java.lang.String[] listSAGs() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.listSAGs();
	}

	public java.lang.String[] listClientAppMembership(java.lang.String clientAppID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID
	{
		return _delegate.listClientAppMembership(clientAppID);
	}

	public java.lang.String[] listClientApps() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.listClientApps();
	}

}
