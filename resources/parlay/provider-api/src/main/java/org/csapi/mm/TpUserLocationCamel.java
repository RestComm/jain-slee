package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserLocationCamel"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationCamel
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUserLocationCamel(){}
	public org.csapi.TpAddress UserID;
	public org.csapi.mm.TpMobilityError StatusCode;
	public boolean GeographicalPositionPresent;
	public org.csapi.mm.TpGeographicalPosition GeographicalPosition;
	public boolean TimestampPresent;
	public java.lang.String Timestamp;
	public boolean VlrNumberPresent;
	public org.csapi.TpAddress VlrNumber;
	public boolean LocationNumberPresent;
	public org.csapi.TpAddress LocationNumber;
	public boolean CellIdOrLaiPresent;
	public java.lang.String CellIdOrLai;
	public TpUserLocationCamel(org.csapi.TpAddress UserID, org.csapi.mm.TpMobilityError StatusCode, boolean GeographicalPositionPresent, org.csapi.mm.TpGeographicalPosition GeographicalPosition, boolean TimestampPresent, java.lang.String Timestamp, boolean VlrNumberPresent, org.csapi.TpAddress VlrNumber, boolean LocationNumberPresent, org.csapi.TpAddress LocationNumber, boolean CellIdOrLaiPresent, java.lang.String CellIdOrLai)
	{
		this.UserID = UserID;
		this.StatusCode = StatusCode;
		this.GeographicalPositionPresent = GeographicalPositionPresent;
		this.GeographicalPosition = GeographicalPosition;
		this.TimestampPresent = TimestampPresent;
		this.Timestamp = Timestamp;
		this.VlrNumberPresent = VlrNumberPresent;
		this.VlrNumber = VlrNumber;
		this.LocationNumberPresent = LocationNumberPresent;
		this.LocationNumber = LocationNumber;
		this.CellIdOrLaiPresent = CellIdOrLaiPresent;
		this.CellIdOrLai = CellIdOrLai;
	}
}
