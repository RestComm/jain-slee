package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventCriteriaResult
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionEventCriteriaResult(){}
	public org.csapi.dsc.TpDataSessionEventCriteria EventCriteria;
	public int AssignmentID;
	public TpDataSessionEventCriteriaResult(org.csapi.dsc.TpDataSessionEventCriteria EventCriteria, int AssignmentID)
	{
		this.EventCriteria = EventCriteria;
		this.AssignmentID = AssignmentID;
	}
}
