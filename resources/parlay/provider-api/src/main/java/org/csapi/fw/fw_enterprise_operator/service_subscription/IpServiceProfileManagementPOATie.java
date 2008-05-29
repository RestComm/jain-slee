package org.csapi.fw.fw_enterprise_operator.service_subscription;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpServiceProfileManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpServiceProfileManagementPOATie
	extends IpServiceProfileManagementPOA
{
	private IpServiceProfileManagementOperations _delegate;

	private POA _poa;
	public IpServiceProfileManagementPOATie(IpServiceProfileManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpServiceProfileManagementPOATie(IpServiceProfileManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagement _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagementHelper.narrow(_this_object(orb));
	}
	public IpServiceProfileManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpServiceProfileManagementOperations delegate)
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
	public void assign(java.lang.String sagID, java.lang.String serviceProfileID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID
	{
_delegate.assign(sagID,serviceProfileID);
	}

	public org.csapi.fw.TpAssignSagToServiceProfileConflict[] requestConflictInfo() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.requestConflictInfo();
	}

	public void deleteServiceProfile(java.lang.String serviceProfileID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID
	{
_delegate.deleteServiceProfile(serviceProfileID);
	}

	public void modifyServiceProfile(org.csapi.fw.TpServiceProfile serviceProfile) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID
	{
_delegate.modifyServiceProfile(serviceProfile);
	}

	public void deassign(java.lang.String sagID, java.lang.String serviceProfileID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID
	{
_delegate.deassign(sagID,serviceProfileID);
	}

	public java.lang.String createServiceProfile(org.csapi.fw.TpServiceProfileDescription serviceProfileDescription) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.createServiceProfile(serviceProfileDescription);
	}

}
