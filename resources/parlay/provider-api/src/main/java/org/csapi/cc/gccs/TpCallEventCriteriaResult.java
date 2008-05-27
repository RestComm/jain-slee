package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventCriteriaResult
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallEventCriteriaResult(){}
	public org.csapi.cc.gccs.TpCallEventCriteria CallEventCriteria;
	public int AssignmentID;
	public TpCallEventCriteriaResult(org.csapi.cc.gccs.TpCallEventCriteria CallEventCriteria, int AssignmentID)
	{
		this.CallEventCriteria = CallEventCriteria;
		this.AssignmentID = AssignmentID;
	}
}
