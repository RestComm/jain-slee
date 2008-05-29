package org.csapi.fw.fw_application.discovery;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpServiceDiscovery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpServiceDiscoveryPOATie
	extends IpServiceDiscoveryPOA
{
	private IpServiceDiscoveryOperations _delegate;

	private POA _poa;
	public IpServiceDiscoveryPOATie(IpServiceDiscoveryOperations delegate)
	{
		_delegate = delegate;
	}
	public IpServiceDiscoveryPOATie(IpServiceDiscoveryOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.discovery.IpServiceDiscovery _this()
	{
		return org.csapi.fw.fw_application.discovery.IpServiceDiscoveryHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.discovery.IpServiceDiscovery _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.discovery.IpServiceDiscoveryHelper.narrow(_this_object(orb));
	}
	public IpServiceDiscoveryOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpServiceDiscoveryOperations delegate)
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
	public org.csapi.fw.TpService[] listSubscribedServices() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.listSubscribedServices();
	}

	public java.lang.String[] listServiceTypes() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.listServiceTypes();
	}

	public org.csapi.fw.TpService[] discoverService(java.lang.String serviceTypeName, org.csapi.fw.TpServiceProperty[] desiredPropertyList, int max) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE
	{
		return _delegate.discoverService(serviceTypeName,desiredPropertyList,max);
	}

	public org.csapi.fw.TpServiceTypeDescription describeServiceType(java.lang.String name) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE
	{
		return _delegate.describeServiceType(name);
	}

}
