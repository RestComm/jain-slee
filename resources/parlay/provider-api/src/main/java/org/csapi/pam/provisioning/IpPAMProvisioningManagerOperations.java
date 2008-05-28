package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMProvisioningManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPAMProvisioningManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	byte[] getAuthToken(org.csapi.TpAttribute[] askerData) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	org.csapi.IpInterface obtainInterface(java.lang.String interfaceName) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNAVAILABLE_INTERFACE;
	org.csapi.pam.TpPAMAccessControlData getAccessControl(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void setAccessControl(java.lang.String identity, java.lang.String operation, org.csapi.pam.TpPAMAccessControlData newAccessControl, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
}
