package org.csapi.pam.provisioning;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMProvisioningManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMProvisioningManagerPOATie
	extends IpPAMProvisioningManagerPOA
{
	private IpPAMProvisioningManagerOperations _delegate;

	private POA _poa;
	public IpPAMProvisioningManagerPOATie(IpPAMProvisioningManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMProvisioningManagerPOATie(IpPAMProvisioningManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.provisioning.IpPAMProvisioningManager _this()
	{
		return org.csapi.pam.provisioning.IpPAMProvisioningManagerHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMProvisioningManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMProvisioningManagerHelper.narrow(_this_object(orb));
	}
	public IpPAMProvisioningManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMProvisioningManagerOperations delegate)
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
	public byte[] getAuthToken(org.csapi.TpAttribute[] askerData) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.getAuthToken(askerData);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void setAccessControl(java.lang.String identity, java.lang.String operation, org.csapi.pam.TpPAMAccessControlData newAccessControl, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.setAccessControl(identity,operation,newAccessControl,authToken);
	}

	public org.csapi.pam.TpPAMAccessControlData getAccessControl(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.getAccessControl(identity,authToken);
	}

	public org.csapi.IpInterface obtainInterface(java.lang.String interfaceName) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACE
	{
		return _delegate.obtainInterface(interfaceName);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
