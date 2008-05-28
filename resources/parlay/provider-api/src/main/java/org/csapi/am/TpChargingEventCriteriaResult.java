package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpChargingEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventCriteriaResult
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpChargingEventCriteriaResult(){}
	public org.csapi.am.TpChargingEventCriteria ChargingEventCriteria;
	public int AssignmentID;
	public TpChargingEventCriteriaResult(org.csapi.am.TpChargingEventCriteria ChargingEventCriteria, int AssignmentID)
	{
		this.ChargingEventCriteria = ChargingEventCriteria;
		this.AssignmentID = AssignmentID;
	}
}
