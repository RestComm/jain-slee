package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallNotificationScope"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationScope
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallNotificationScope(){}
	public org.csapi.TpAddressRange DestinationAddress;
	public org.csapi.TpAddressRange OriginatingAddress;
	public TpCallNotificationScope(org.csapi.TpAddressRange DestinationAddress, org.csapi.TpAddressRange OriginatingAddress)
	{
		this.DestinationAddress = DestinationAddress;
		this.OriginatingAddress = OriginatingAddress;
	}
}
