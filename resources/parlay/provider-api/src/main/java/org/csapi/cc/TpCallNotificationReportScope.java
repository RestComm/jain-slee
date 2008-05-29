package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallNotificationReportScope"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationReportScope
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallNotificationReportScope(){}
	public org.csapi.TpAddress DestinationAddress;
	public org.csapi.TpAddress OriginatingAddress;
	public TpCallNotificationReportScope(org.csapi.TpAddress DestinationAddress, org.csapi.TpAddress OriginatingAddress)
	{
		this.DestinationAddress = DestinationAddress;
		this.OriginatingAddress = OriginatingAddress;
	}
}
