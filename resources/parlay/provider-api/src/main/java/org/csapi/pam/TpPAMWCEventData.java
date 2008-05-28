package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMWCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMWCEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMWCEventData(){}
	public org.csapi.pam.TpPAMEventName[] Events;
	public java.lang.String[] IdentityName;
	public java.lang.String[] IdentityType;
	public long ReportingPeriod;
	public TpPAMWCEventData(org.csapi.pam.TpPAMEventName[] Events, java.lang.String[] IdentityName, java.lang.String[] IdentityType, long ReportingPeriod)
	{
		this.Events = Events;
		this.IdentityName = IdentityName;
		this.IdentityType = IdentityType;
		this.ReportingPeriod = ReportingPeriod;
	}
}
