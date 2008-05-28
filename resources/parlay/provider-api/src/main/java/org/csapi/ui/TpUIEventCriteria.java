package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUIEventCriteria(){}
	public org.csapi.TpAddressRange OriginatingAddress;
	public org.csapi.TpAddressRange DestinationAddress;
	public java.lang.String ServiceCode;
	public TpUIEventCriteria(org.csapi.TpAddressRange OriginatingAddress, org.csapi.TpAddressRange DestinationAddress, java.lang.String ServiceCode)
	{
		this.OriginatingAddress = OriginatingAddress;
		this.DestinationAddress = DestinationAddress;
		this.ServiceCode = ServiceCode;
	}
}
