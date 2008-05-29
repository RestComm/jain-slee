package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpServiceContractDescription"
 *	@author JacORB IDL compiler 
 */

public final class TpServiceContractDescription
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpServiceContractDescription(){}
	public org.csapi.fw.TpPerson ServiceRequestor;
	public org.csapi.fw.TpPerson BillingContact;
	public java.lang.String ServiceStartDate;
	public java.lang.String ServiceEndDate;
	public java.lang.String ServiceTypeName;
	public java.lang.String ServiceID;
	public org.csapi.fw.TpServiceProperty[] ServiceSubscriptionProperties;
	public boolean InUse;
	public TpServiceContractDescription(org.csapi.fw.TpPerson ServiceRequestor, org.csapi.fw.TpPerson BillingContact, java.lang.String ServiceStartDate, java.lang.String ServiceEndDate, java.lang.String ServiceTypeName, java.lang.String ServiceID, org.csapi.fw.TpServiceProperty[] ServiceSubscriptionProperties, boolean InUse)
	{
		this.ServiceRequestor = ServiceRequestor;
		this.BillingContact = BillingContact;
		this.ServiceStartDate = ServiceStartDate;
		this.ServiceEndDate = ServiceEndDate;
		this.ServiceTypeName = ServiceTypeName;
		this.ServiceID = ServiceID;
		this.ServiceSubscriptionProperties = ServiceSubscriptionProperties;
		this.InUse = InUse;
	}
}
