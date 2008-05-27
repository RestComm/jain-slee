package org.csapi.cs;

/**
 *	Generated from IDL definition of struct "TpChargingSessionID"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingSessionID
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargingSessionID(){}
	public org.csapi.cs.IpChargingSession ChargingSessionReference;
	public int ChargingSessionID;
	public int RequestNumberFirstRequest;
	public TpChargingSessionID(org.csapi.cs.IpChargingSession ChargingSessionReference, int ChargingSessionID, int RequestNumberFirstRequest)
	{
		this.ChargingSessionReference = ChargingSessionReference;
		this.ChargingSessionID = ChargingSessionID;
		this.RequestNumberFirstRequest = RequestNumberFirstRequest;
	}
}
