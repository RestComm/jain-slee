package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpAppPAMPreferenceCheck"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppPAMPreferenceCheckOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.pam.TpPAMAvailabilityProfile[] computeAvailability(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, java.lang.String[] attributeNames, byte[] authToken);
}
