package org.csapi.fw.fw_enterprise_operator.service_subscription;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpClientAppManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpClientAppManagementPOATie
	extends IpClientAppManagementPOA
{
	private IpClientAppManagementOperations _delegate;

	private POA _poa;
	public IpClientAppManagementPOATie(IpClientAppManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpClientAppManagementPOATie(IpClientAppManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagement _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagementHelper.narrow(_this_object(orb));
	}
	public IpClientAppManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpClientAppManagementOperations delegate)
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
	public void modifySAG(org.csapi.fw.TpSag sag) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
_delegate.modifySAG(sag);
	}

	public void removeSAGMembers(java.lang.String sagID, java.lang.String[] clientAppIDList) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID
	{
_delegate.removeSAGMembers(sagID,clientAppIDList);
	}

	public org.csapi.fw.TpAddSagMembersConflict[] requestConflictInfo() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.requestConflictInfo();
	}

	public void addSAGMembers(java.lang.String sagID, java.lang.String[] clientAppIDs) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_ADDITION_TO_SAG,org.csapi.fw.P_INVALID_CLIENT_APP_ID
	{
_delegate.addSAGMembers(sagID,clientAppIDs);
	}

	public void createClientApp(org.csapi.fw.TpClientAppDescription clientAppDescription) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID
	{
_delegate.createClientApp(clientAppDescription);
	}

	public void deleteClientApp(java.lang.String clientAppID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID
	{
_delegate.deleteClientApp(clientAppID);
	}

	public void createSAG(org.csapi.fw.TpSag sag, java.lang.String[] clientAppIDs) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID
	{
_delegate.createSAG(sag,clientAppIDs);
	}

	public void modifyClientApp(org.csapi.fw.TpClientAppDescription clientAppDescription) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID
	{
_delegate.modifyClientApp(clientAppDescription);
	}

	public void deleteSAG(java.lang.String sagID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
_delegate.deleteSAG(sagID);
	}

}
