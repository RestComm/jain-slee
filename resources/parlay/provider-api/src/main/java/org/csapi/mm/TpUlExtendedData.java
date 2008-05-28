package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUlExtendedData"
 *	@author JacORB IDL compiler 
 */

public final class TpUlExtendedData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUlExtendedData(){}
	public org.csapi.mm.TpGeographicalPosition GeographicalPosition;
	public org.csapi.mm.TpTerminalType TerminalType;
	public boolean AltitudePresent;
	public float Altitude;
	public float UncertaintyAltitude;
	public boolean TimestampPresent;
	public java.lang.String Timestamp;
	public java.lang.String UsedLocationMethod;
	public TpUlExtendedData(org.csapi.mm.TpGeographicalPosition GeographicalPosition, org.csapi.mm.TpTerminalType TerminalType, boolean AltitudePresent, float Altitude, float UncertaintyAltitude, boolean TimestampPresent, java.lang.String Timestamp, java.lang.String UsedLocationMethod)
	{
		this.GeographicalPosition = GeographicalPosition;
		this.TerminalType = TerminalType;
		this.AltitudePresent = AltitudePresent;
		this.Altitude = Altitude;
		this.UncertaintyAltitude = UncertaintyAltitude;
		this.TimestampPresent = TimestampPresent;
		this.Timestamp = Timestamp;
		this.UsedLocationMethod = UsedLocationMethod;
	}
}
