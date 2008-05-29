package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAUEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAUEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAUEventData(){}
	public java.lang.String[] IdentityName;
	public java.lang.String[] IdentityType;
	public java.lang.String[] AgentName;
	public java.lang.String[] AgentType;
	public TpPAMAUEventData(java.lang.String[] IdentityName, java.lang.String[] IdentityType, java.lang.String[] AgentName, java.lang.String[] AgentType)
	{
		this.IdentityName = IdentityName;
		this.IdentityType = IdentityType;
		this.AgentName = AgentName;
		this.AgentType = AgentType;
	}
}
