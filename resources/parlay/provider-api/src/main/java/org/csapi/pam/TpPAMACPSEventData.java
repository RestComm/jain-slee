package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMACPSEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACPSEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMACPSEventData(){}
	public java.lang.String[] AgentName;
	public java.lang.String[] AgentType;
	public java.lang.String[] Capabilities;
	public java.lang.String[] AttributeNames;
	public long ReportingPeriod;
	public TpPAMACPSEventData(java.lang.String[] AgentName, java.lang.String[] AgentType, java.lang.String[] Capabilities, java.lang.String[] AttributeNames, long ReportingPeriod)
	{
		this.AgentName = AgentName;
		this.AgentType = AgentType;
		this.Capabilities = Capabilities;
		this.AttributeNames = AttributeNames;
		this.ReportingPeriod = ReportingPeriod;
	}
}
