package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMAvailability"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPAMAvailabilityOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.pam.TpPAMAvailabilityProfile[] getAvailability(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	org.csapi.pam.TpPAMPreferenceData getPreference(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
	void setPreference(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, java.lang.String operation, org.csapi.pam.TpPAMPreferenceData newPreference, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL;
}
