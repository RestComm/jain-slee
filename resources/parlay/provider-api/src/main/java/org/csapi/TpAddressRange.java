package org.csapi;

/**
 *	Generated from IDL definition of struct "TpAddressRange"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressRange
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAddressRange(){}
	public org.csapi.TpAddressPlan Plan;
	public java.lang.String AddrString;
	public java.lang.String Name;
	public java.lang.String SubAddressString;
	public TpAddressRange(org.csapi.TpAddressPlan Plan, java.lang.String AddrString, java.lang.String Name, java.lang.String SubAddressString)
	{
		this.Plan = Plan;
		this.AddrString = AddrString;
		this.Name = Name;
		this.SubAddressString = SubAddressString;
	}
}
