package org.csapi.policy;

/**
 *	Generated from IDL definition of struct "TpPolicyEvent"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyEvent
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPolicyEvent(){}
	public int EventID;
	public java.lang.String TimeGenerated;
	public org.csapi.TpAttribute[] Attributes;
	public java.lang.String EventDefinitionName;
	public java.lang.String EventDomainName;
	public TpPolicyEvent(int EventID, java.lang.String TimeGenerated, org.csapi.TpAttribute[] Attributes, java.lang.String EventDefinitionName, java.lang.String EventDomainName)
	{
		this.EventID = EventID;
		this.TimeGenerated = TimeGenerated;
		this.Attributes = Attributes;
		this.EventDefinitionName = EventDefinitionName;
		this.EventDomainName = EventDomainName;
	}
}
