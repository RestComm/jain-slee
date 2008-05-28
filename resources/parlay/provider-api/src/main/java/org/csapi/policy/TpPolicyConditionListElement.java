package org.csapi.policy;

/**
 *	Generated from IDL definition of struct "TpPolicyConditionListElement"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionListElement
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPolicyConditionListElement(){}
	public org.csapi.policy.IpPolicyCondition Condition;
	public int GroupNumber;
	public boolean Negated;
	public TpPolicyConditionListElement(org.csapi.policy.IpPolicyCondition Condition, int GroupNumber, boolean Negated)
	{
		this.Condition = Condition;
		this.GroupNumber = GroupNumber;
		this.Negated = Negated;
	}
}
