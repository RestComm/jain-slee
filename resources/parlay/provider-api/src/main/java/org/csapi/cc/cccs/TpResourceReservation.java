package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpResourceReservation"
 *	@author JacORB IDL compiler 
 */

public final class TpResourceReservation
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpResourceReservation(){}
	public org.csapi.TpAddress ResourceID;
	public int ReservationID;
	public TpResourceReservation(org.csapi.TpAddress ResourceID, int ReservationID)
	{
		this.ResourceID = ResourceID;
		this.ReservationID = ReservationID;
	}
}
