package org.csapi.policy;

/**
 *	Generated from IDL definition of struct "TpPolicyActionListElement"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyActionListElement
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPolicyActionListElement(){}
	public org.csapi.policy.IpPolicyAction Action;
	public int SequenceNumber;
	public TpPolicyActionListElement(org.csapi.policy.IpPolicyAction Action, int SequenceNumber)
	{
		this.Action = Action;
		this.SequenceNumber = SequenceNumber;
	}
}
