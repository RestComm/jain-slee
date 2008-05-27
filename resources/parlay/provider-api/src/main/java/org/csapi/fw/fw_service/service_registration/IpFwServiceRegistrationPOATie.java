package org.csapi.fw.fw_service.service_registration;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpFwServiceRegistration"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpFwServiceRegistrationPOATie
	extends IpFwServiceRegistrationPOA
{
	private IpFwServiceRegistrationOperations _delegate;

	private POA _poa;
	public IpFwServiceRegistrationPOATie(IpFwServiceRegistrationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpFwServiceRegistrationPOATie(IpFwServiceRegistrationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration _this()
	{
		return org.csapi.fw.fw_service.service_registration.IpFwServiceRegistrationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.service_registration.IpFwServiceRegistrationHelper.narrow(_this_object(orb));
	}
	public IpFwServiceRegistrationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpFwServiceRegistrationOperations delegate)
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
	public void announceServiceAvailability(java.lang.String serviceID, org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager serviceInstanceLifecycleManagerRef) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID
	{
_delegate.announceServiceAvailability(serviceID,serviceInstanceLifecycleManagerRef);
	}

	public org.csapi.fw.TpServiceDescription describeService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID
	{
		return _delegate.describeService(serviceID);
	}

	public void unannounceService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID
	{
_delegate.unannounceService(serviceID);
	}

	public java.lang.String registerService(java.lang.String serviceTypeName, org.csapi.fw.TpServiceProperty[] servicePropertyList) throws org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLE,org.csapi.fw.P_MISSING_MANDATORY_PROPERTY,org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE,org.csapi.fw.P_DUPLICATE_PROPERTY_NAME,org.csapi.fw.P_PROPERTY_TYPE_MISMATCH
	{
		return _delegate.registerService(serviceTypeName,servicePropertyList);
	}

	public void unregisterService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID
	{
_delegate.unregisterService(serviceID);
	}

}
