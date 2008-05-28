package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserLocationEmergencyRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationEmergencyRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUserLocationEmergencyRequest(){}
	public boolean UserAddressPresent;
	public org.csapi.TpAddress UserAddress;
	public boolean NaEsrdPresent;
	public java.lang.String NaEsrd;
	public boolean NaEsrkPresent;
	public java.lang.String NaEsrk;
	public boolean ImeiPresent;
	public java.lang.String Imei;
	public org.csapi.mm.TpLocationRequest LocationReq;
	public TpUserLocationEmergencyRequest(boolean UserAddressPresent, org.csapi.TpAddress UserAddress, boolean NaEsrdPresent, java.lang.String NaEsrd, boolean NaEsrkPresent, java.lang.String NaEsrk, boolean ImeiPresent, java.lang.String Imei, org.csapi.mm.TpLocationRequest LocationReq)
	{
		this.UserAddressPresent = UserAddressPresent;
		this.UserAddress = UserAddress;
		this.NaEsrdPresent = NaEsrdPresent;
		this.NaEsrd = NaEsrd;
		this.NaEsrkPresent = NaEsrkPresent;
		this.NaEsrk = NaEsrk;
		this.ImeiPresent = ImeiPresent;
		this.Imei = Imei;
		this.LocationReq = LocationReq;
	}
}
