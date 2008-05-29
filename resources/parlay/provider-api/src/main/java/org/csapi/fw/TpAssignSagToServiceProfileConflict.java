package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpAssignSagToServiceProfileConflict"
 *	@author JacORB IDL compiler 
 */

public final class TpAssignSagToServiceProfileConflict
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpAssignSagToServiceProfileConflict(){}
	public java.lang.String ClientApplication;
	public org.csapi.fw.TpSagProfilePair AlreadyAssignedSagProfilePair;
	public java.lang.String Service;
	public TpAssignSagToServiceProfileConflict(java.lang.String ClientApplication, org.csapi.fw.TpSagProfilePair AlreadyAssignedSagProfilePair, java.lang.String Service)
	{
		this.ClientApplication = ClientApplication;
		this.AlreadyAssignedSagProfilePair = AlreadyAssignedSagProfilePair;
		this.Service = Service;
	}
}
