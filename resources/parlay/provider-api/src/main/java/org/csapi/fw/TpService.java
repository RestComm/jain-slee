package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpService"
 *	@author JacORB IDL compiler 
 */

public final class TpService
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpService(){}
	public java.lang.String ServiceID;
	public org.csapi.fw.TpServiceDescription ServiceDescription;
	public TpService(java.lang.String ServiceID, org.csapi.fw.TpServiceDescription ServiceDescription)
	{
		this.ServiceID = ServiceID;
		this.ServiceDescription = ServiceDescription;
	}
}
