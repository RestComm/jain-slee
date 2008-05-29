package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpAddSagMembersConflict"
 *	@author JacORB IDL compiler 
 */

public final class TpAddSagMembersConflict
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAddSagMembersConflict(){}
	public java.lang.String ClientApplication;
	public org.csapi.fw.TpSagProfilePair ConflictGeneratingSagProfilePair;
	public org.csapi.fw.TpSagProfilePair AlreadyAssignedSagProfilePair;
	public java.lang.String Service;
	public TpAddSagMembersConflict(java.lang.String ClientApplication, org.csapi.fw.TpSagProfilePair ConflictGeneratingSagProfilePair, org.csapi.fw.TpSagProfilePair AlreadyAssignedSagProfilePair, java.lang.String Service)
	{
		this.ClientApplication = ClientApplication;
		this.ConflictGeneratingSagProfilePair = ConflictGeneratingSagProfilePair;
		this.AlreadyAssignedSagProfilePair = AlreadyAssignedSagProfilePair;
		this.Service = Service;
	}
}
