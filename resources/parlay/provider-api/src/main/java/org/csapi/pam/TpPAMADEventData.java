package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMADEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMADEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMADEventData(){}
	public java.lang.String[] AgentName;
	public java.lang.String[] AgentType;
	public TpPAMADEventData(java.lang.String[] AgentName, java.lang.String[] AgentType)
	{
		this.AgentName = AgentName;
		this.AgentType = AgentType;
	}
}
