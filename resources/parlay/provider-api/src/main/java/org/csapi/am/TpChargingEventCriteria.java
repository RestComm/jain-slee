package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpChargingEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargingEventCriteria(){}
	public org.csapi.am.TpChargingEventName[] ChargingEvents;
	public org.csapi.TpAddress[] Users;
	public TpChargingEventCriteria(org.csapi.am.TpChargingEventName[] ChargingEvents, org.csapi.TpAddress[] Users)
	{
		this.ChargingEvents = ChargingEvents;
		this.Users = Users;
	}
}
