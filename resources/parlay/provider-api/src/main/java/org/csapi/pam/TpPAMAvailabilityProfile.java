package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAvailabilityProfile"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAvailabilityProfile
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAvailabilityProfile(){}
	public java.lang.String PrivacyCode;
	public org.csapi.pam.TpPAMPresenceData AvailabilityData;
	public TpPAMAvailabilityProfile(java.lang.String PrivacyCode, org.csapi.pam.TpPAMPresenceData AvailabilityData)
	{
		this.PrivacyCode = PrivacyCode;
		this.AvailabilityData = AvailabilityData;
	}
}
