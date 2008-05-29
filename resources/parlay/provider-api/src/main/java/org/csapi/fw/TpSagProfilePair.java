package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpSagProfilePair"
 *	@author JacORB IDL compiler 
 */

public final class TpSagProfilePair
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpSagProfilePair(){}
	public java.lang.String Sag;
	public java.lang.String ServiceProfile;
	public TpSagProfilePair(java.lang.String Sag, java.lang.String ServiceProfile)
	{
		this.Sag = Sag;
		this.ServiceProfile = ServiceProfile;
	}
}
