package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAPSEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAPSEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAPSEventData(){}
	public java.lang.String[] AgentName;
	public java.lang.String[] AgentType;
	public java.lang.String[] AttributeNames;
	public long ReportingPeriod;
	public TpPAMAPSEventData(java.lang.String[] AgentName, java.lang.String[] AgentType, java.lang.String[] AttributeNames, long ReportingPeriod)
	{
		this.AgentName = AgentName;
		this.AgentType = AgentType;
		this.AttributeNames = AttributeNames;
		this.ReportingPeriod = ReportingPeriod;
	}
}
