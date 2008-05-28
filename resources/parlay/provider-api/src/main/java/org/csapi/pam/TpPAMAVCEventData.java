package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAVCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAVCEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAVCEventData(){}
	public java.lang.String[] IdentityName;
	public java.lang.String[] IdentityType;
	public org.csapi.pam.TpPAMContext[] PAMContext;
	public java.lang.String[] AttributeNames;
	public long ReportingPeriod;
	public TpPAMAVCEventData(java.lang.String[] IdentityName, java.lang.String[] IdentityType, org.csapi.pam.TpPAMContext[] PAMContext, java.lang.String[] AttributeNames, long ReportingPeriod)
	{
		this.IdentityName = IdentityName;
		this.IdentityType = IdentityType;
		this.PAMContext = PAMContext;
		this.AttributeNames = AttributeNames;
		this.ReportingPeriod = ReportingPeriod;
	}
}
