package org.csapi.policy;

/**
 *	Generated from IDL definition of struct "TpPolicyConditionListElement"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionListElementHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.policy.TpPolicyConditionListElement value;

	public TpPolicyConditionListElementHolder ()
	{
	}
	public TpPolicyConditionListElementHolder(final org.csapi.policy.TpPolicyConditionListElement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.policy.TpPolicyConditionListElementHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.policy.TpPolicyConditionListElementHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.policy.TpPolicyConditionListElementHelper.write(_out, value);
	}
}
