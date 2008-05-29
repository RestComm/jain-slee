package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMGMCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMGMCEventData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMGMCEventData(){}
	public java.lang.String[] GroupName;
	public java.lang.String[] GroupType;
	public TpPAMGMCEventData(java.lang.String[] GroupName, java.lang.String[] GroupType)
	{
		this.GroupName = GroupName;
		this.GroupType = GroupType;
	}
}
