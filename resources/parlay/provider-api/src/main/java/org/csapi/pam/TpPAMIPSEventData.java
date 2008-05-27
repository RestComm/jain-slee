package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMIPSEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMIPSEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMIPSEventData(){}
	public java.lang.String[] IdentityName;
	public java.lang.String[] IdentityType;
	public java.lang.String[] AttributeNames;
	public long ReportingPeriod;
	public TpPAMIPSEventData(java.lang.String[] IdentityName, java.lang.String[] IdentityType, java.lang.String[] AttributeNames, long ReportingPeriod)
	{
		this.IdentityName = IdentityName;
		this.IdentityType = IdentityType;
		this.AttributeNames = AttributeNames;
		this.ReportingPeriod = ReportingPeriod;
	}
}
