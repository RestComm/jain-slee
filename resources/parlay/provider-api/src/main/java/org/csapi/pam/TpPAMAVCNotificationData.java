package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAVCNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAVCNotificationData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAVCNotificationData(){}
	public java.lang.String Identity;
	public org.csapi.pam.TpPAMAvailabilityProfile[] Availability;
	public TpPAMAVCNotificationData(java.lang.String Identity, org.csapi.pam.TpPAMAvailabilityProfile[] Availability)
	{
		this.Identity = Identity;
		this.Availability = Availability;
	}
}
