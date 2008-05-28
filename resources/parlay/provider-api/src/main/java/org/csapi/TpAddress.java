package org.csapi;

/**
 *	Generated from IDL definition of struct "TpAddress"
 *	@author JacORB IDL compiler 
 */

public final class TpAddress
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAddress(){}
	public org.csapi.TpAddressPlan Plan;
	public java.lang.String AddrString;
	public java.lang.String Name;
	public org.csapi.TpAddressPresentation Presentation;
	public org.csapi.TpAddressScreening Screening;
	public java.lang.String SubAddressString;
	public TpAddress(org.csapi.TpAddressPlan Plan, java.lang.String AddrString, java.lang.String Name, org.csapi.TpAddressPresentation Presentation, org.csapi.TpAddressScreening Screening, java.lang.String SubAddressString)
	{
		this.Plan = Plan;
		this.AddrString = AddrString;
		this.Name = Name;
		this.Presentation = Presentation;
		this.Screening = Screening;
		this.SubAddressString = SubAddressString;
	}
}
