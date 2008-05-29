package org.csapi.fw.fw_service.service_lifecycle;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpServiceInstanceLifecycleManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpServiceInstanceLifecycleManagerPOATie
	extends IpServiceInstanceLifecycleManagerPOA
{
	private IpServiceInstanceLifecycleManagerOperations _delegate;

	private POA _poa;
	public IpServiceInstanceLifecycleManagerPOATie(IpServiceInstanceLifecycleManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpServiceInstanceLifecycleManagerPOATie(IpServiceInstanceLifecycleManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager _this()
	{
		return org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManagerHelper.narrow(_this_object(orb));
	}
	public IpServiceInstanceLifecycleManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpServiceInstanceLifecycleManagerOperations delegate)
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
	public org.csapi.IpService createServiceManager(java.lang.String application, org.csapi.fw.TpServiceProperty[] serviceProperties, java.lang.String serviceInstanceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_PROPERTY
	{
		return _delegate.createServiceManager(application,serviceProperties,serviceInstanceID);
	}

	public void destroyServiceManager(java.lang.String serviceInstance) throws org.csapi.TpCommonExceptions
	{
_delegate.destroyServiceManager(serviceInstance);
	}

}
