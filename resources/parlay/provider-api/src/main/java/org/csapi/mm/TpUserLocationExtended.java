package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserLocationExtended"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationExtended
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUserLocationExtended(){}
	public org.csapi.TpAddress UserID;
	public org.csapi.mm.TpMobilityError StatusCode;
	public org.csapi.mm.TpUlExtendedData[] Locations;
	public TpUserLocationExtended(org.csapi.TpAddress UserID, org.csapi.mm.TpMobilityError StatusCode, org.csapi.mm.TpUlExtendedData[] Locations)
	{
		this.UserID = UserID;
		this.StatusCode = StatusCode;
		this.Locations = Locations;
	}
}
