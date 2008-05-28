package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpUserLocationEmergency"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationEmergency
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUserLocationEmergency(){}
	public org.csapi.mm.TpMobilityError StatusCode;
	public boolean UserIdPresent;
	public org.csapi.TpAddress UserId;
	public boolean NaEsrdPresent;
	public java.lang.String NaEsrd;
	public boolean NaEsrkPresent;
	public java.lang.String NaEsrk;
	public boolean ImeiPresent;
	public java.lang.String Imei;
	public org.csapi.mm.TpUserLocationEmergencyTrigger TriggeringEvent;
	public boolean GeographicalPositionPresent;
	public org.csapi.mm.TpGeographicalPosition GeographicalPosition;
	public boolean AltitudePresent;
	public float Altitude;
	public float UncertaintyAltitude;
	public boolean TimestampPresent;
	public java.lang.String Timestamp;
	public java.lang.String UsedLocationMethod;
	public TpUserLocationEmergency(org.csapi.mm.TpMobilityError StatusCode, boolean UserIdPresent, org.csapi.TpAddress UserId, boolean NaEsrdPresent, java.lang.String NaEsrd, boolean NaEsrkPresent, java.lang.String NaEsrk, boolean ImeiPresent, java.lang.String Imei, org.csapi.mm.TpUserLocationEmergencyTrigger TriggeringEvent, boolean GeographicalPositionPresent, org.csapi.mm.TpGeographicalPosition GeographicalPosition, boolean AltitudePresent, float Altitude, float UncertaintyAltitude, boolean TimestampPresent, java.lang.String Timestamp, java.lang.String UsedLocationMethod)
	{
		this.StatusCode = StatusCode;
		this.UserIdPresent = UserIdPresent;
		this.UserId = UserId;
		this.NaEsrdPresent = NaEsrdPresent;
		this.NaEsrd = NaEsrd;
		this.NaEsrkPresent = NaEsrkPresent;
		this.NaEsrk = NaEsrk;
		this.ImeiPresent = ImeiPresent;
		this.Imei = Imei;
		this.TriggeringEvent = TriggeringEvent;
		this.GeographicalPositionPresent = GeographicalPositionPresent;
		this.GeographicalPosition = GeographicalPosition;
		this.AltitudePresent = AltitudePresent;
		this.Altitude = Altitude;
		this.UncertaintyAltitude = UncertaintyAltitude;
		this.TimestampPresent = TimestampPresent;
		this.Timestamp = Timestamp;
		this.UsedLocationMethod = UsedLocationMethod;
	}
}
