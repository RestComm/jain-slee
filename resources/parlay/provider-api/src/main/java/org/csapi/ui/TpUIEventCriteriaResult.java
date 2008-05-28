package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventCriteriaResult
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUIEventCriteriaResult(){}
	public org.csapi.ui.TpUIEventCriteria EventCriteria;
	public int AssignmentID;
	public TpUIEventCriteriaResult(org.csapi.ui.TpUIEventCriteria EventCriteria, int AssignmentID)
	{
		this.EventCriteria = EventCriteria;
		this.AssignmentID = AssignmentID;
	}
}
