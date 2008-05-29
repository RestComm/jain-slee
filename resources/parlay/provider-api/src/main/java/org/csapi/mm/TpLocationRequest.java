package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpLocationRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpLocationRequest(){}
	public float RequestedAccuracy;
	public org.csapi.mm.TpLocationResponseTime RequestedResponseTime;
	public boolean AltitudeRequested;
	public org.csapi.mm.TpLocationType Type;
	public org.csapi.mm.TpLocationPriority Priority;
	public java.lang.String RequestedLocationMethod;
	public TpLocationRequest(float RequestedAccuracy, org.csapi.mm.TpLocationResponseTime RequestedResponseTime, boolean AltitudeRequested, org.csapi.mm.TpLocationType Type, org.csapi.mm.TpLocationPriority Priority, java.lang.String RequestedLocationMethod)
	{
		this.RequestedAccuracy = RequestedAccuracy;
		this.RequestedResponseTime = RequestedResponseTime;
		this.AltitudeRequested = AltitudeRequested;
		this.Type = Type;
		this.Priority = Priority;
		this.RequestedLocationMethod = RequestedLocationMethod;
	}
}
