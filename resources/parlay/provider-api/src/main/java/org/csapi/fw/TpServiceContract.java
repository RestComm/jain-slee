package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceContract"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceContract
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpServiceContract(){}
	public java.lang.String ServiceContractID;
	public org.csapi.fw.TpServiceContractDescription ServiceContractDescription;
	public TpServiceContract(java.lang.String ServiceContractID, org.csapi.fw.TpServiceContractDescription ServiceContractDescription)
	{
		this.ServiceContractID = ServiceContractID;
		this.ServiceContractDescription = ServiceContractDescription;
	}
}
