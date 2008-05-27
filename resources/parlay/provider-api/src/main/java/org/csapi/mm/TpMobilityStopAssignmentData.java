package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpMobilityStopAssignmentData"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityStopAssignmentData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMobilityStopAssignmentData(){}
	public int AssignmentId;
	public org.csapi.mm.TpMobilityStopScope StopScope;
	public org.csapi.TpAddress[] Users;
	public TpMobilityStopAssignmentData(int AssignmentId, org.csapi.mm.TpMobilityStopScope StopScope, org.csapi.TpAddress[] Users)
	{
		this.AssignmentId = AssignmentId;
		this.StopScope = StopScope;
		this.Users = Users;
	}
}
