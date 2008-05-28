package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAAEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAAEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMAAEventData(){}
	public java.lang.String[] IdentityName;
	public java.lang.String[] IdentityType;
	public java.lang.String[] AgentName;
	public java.lang.String[] AgentType;
	public TpPAMAAEventData(java.lang.String[] IdentityName, java.lang.String[] IdentityType, java.lang.String[] AgentName, java.lang.String[] AgentType)
	{
		this.IdentityName = IdentityName;
		this.IdentityType = IdentityType;
		this.AgentName = AgentName;
		this.AgentType = AgentType;
	}
}
