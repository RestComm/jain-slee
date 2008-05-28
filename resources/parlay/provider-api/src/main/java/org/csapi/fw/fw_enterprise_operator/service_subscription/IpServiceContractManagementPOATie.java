package org.csapi.fw.fw_enterprise_operator.service_subscription;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpServiceContractManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpServiceContractManagementPOATie
	extends IpServiceContractManagementPOA
{
	private IpServiceContractManagementOperations _delegate;

	private POA _poa;
	public IpServiceContractManagementPOATie(IpServiceContractManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpServiceContractManagementPOATie(IpServiceContractManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractManagement _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractManagementHelper.narrow(_this_object(orb));
	}
	public IpServiceContractManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpServiceContractManagementOperations delegate)
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
	public java.lang.String createServiceContract(org.csapi.fw.TpServiceContractDescription serviceContractDescription) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.createServiceContract(serviceContractDescription);
	}

	public void modifyServiceContract(org.csapi.fw.TpServiceContract serviceContract) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID,org.csapi.fw.P_ACCESS_DENIED
	{
_delegate.modifyServiceContract(serviceContract);
	}

	public void deleteServiceContract(java.lang.String serviceContractID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID,org.csapi.fw.P_ACCESS_DENIED
	{
_delegate.deleteServiceContract(serviceContractID);
	}

}
