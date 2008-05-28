package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserLocation"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocation
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUserLocation(){}
	public org.csapi.TpAddress UserID;
	public org.csapi.mm.TpMobilityError StatusCode;
	public org.csapi.mm.TpGeographicalPosition GeographicalPosition;
	public TpUserLocation(org.csapi.TpAddress UserID, org.csapi.mm.TpMobilityError StatusCode, org.csapi.mm.TpGeographicalPosition GeographicalPosition)
	{
		this.UserID = UserID;
		this.StatusCode = StatusCode;
		this.GeographicalPosition = GeographicalPosition;
	}
}
