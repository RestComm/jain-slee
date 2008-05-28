package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpPerson"
 *	@author JacORB IDL compiler 
 */

public final class TpPerson
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPerson(){}
	public java.lang.String PersonName;
	public java.lang.String PostalAddress;
	public java.lang.String TelephoneNumber;
	public java.lang.String Email;
	public java.lang.String HomePage;
	public org.csapi.fw.TpProperty[] PersonProperties;
	public TpPerson(java.lang.String PersonName, java.lang.String PostalAddress, java.lang.String TelephoneNumber, java.lang.String Email, java.lang.String HomePage, org.csapi.fw.TpProperty[] PersonProperties)
	{
		this.PersonName = PersonName;
		this.PostalAddress = PostalAddress;
		this.TelephoneNumber = TelephoneNumber;
		this.Email = Email;
		this.HomePage = HomePage;
		this.PersonProperties = PersonProperties;
	}
}
