package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceProfileDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceProfileDescription
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpServiceProfileDescription(){}
	public java.lang.String ServiceContractID;
	public java.lang.String ServiceStartDate;
	public java.lang.String ServiceEndDate;
	public java.lang.String ServiceTypeName;
	public org.csapi.fw.TpServiceProperty[] ServiceSubscriptionProperties;
	public java.lang.String ServiceID;
	public boolean InUse;
	public TpServiceProfileDescription(java.lang.String ServiceContractID, java.lang.String ServiceStartDate, java.lang.String ServiceEndDate, java.lang.String ServiceTypeName, org.csapi.fw.TpServiceProperty[] ServiceSubscriptionProperties, java.lang.String ServiceID, boolean InUse)
	{
		this.ServiceContractID = ServiceContractID;
		this.ServiceStartDate = ServiceStartDate;
		this.ServiceEndDate = ServiceEndDate;
		this.ServiceTypeName = ServiceTypeName;
		this.ServiceSubscriptionProperties = ServiceSubscriptionProperties;
		this.ServiceID = ServiceID;
		this.InUse = InUse;
	}
}
