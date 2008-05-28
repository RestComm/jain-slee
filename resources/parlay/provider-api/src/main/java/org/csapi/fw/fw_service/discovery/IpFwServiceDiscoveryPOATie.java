package org.csapi.fw.fw_service.discovery;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpFwServiceDiscovery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpFwServiceDiscoveryPOATie
	extends IpFwServiceDiscoveryPOA
{
	private IpFwServiceDiscoveryOperations _delegate;

	private POA _poa;
	public IpFwServiceDiscoveryPOATie(IpFwServiceDiscoveryOperations delegate)
	{
		_delegate = delegate;
	}
	public IpFwServiceDiscoveryPOATie(IpFwServiceDiscoveryOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.discovery.IpFwServiceDiscovery _this()
	{
		return org.csapi.fw.fw_service.discovery.IpFwServiceDiscoveryHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.discovery.IpFwServiceDiscovery _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.discovery.IpFwServiceDiscoveryHelper.narrow(_this_object(orb));
	}
	public IpFwServiceDiscoveryOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpFwServiceDiscoveryOperations delegate)
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
	public java.lang.String[] listServiceTypes() throws org.csapi.TpCommonExceptions
	{
		return _delegate.listServiceTypes();
	}

	public org.csapi.fw.TpService[] listRegisteredServices() throws org.csapi.TpCommonExceptions
	{
		return _delegate.listRegisteredServices();
	}

	public org.csapi.fw.TpService[] discoverService(java.lang.String serviceTypeName, org.csapi.fw.TpServiceProperty[] desiredPropertyList, int max) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_INVALID_PROPERTY,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE
	{
		return _delegate.discoverService(serviceTypeName,desiredPropertyList,max);
	}

	public org.csapi.fw.TpServiceTypeDescription describeServiceType(java.lang.String name) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE
	{
		return _delegate.describeServiceType(name);
	}

}
