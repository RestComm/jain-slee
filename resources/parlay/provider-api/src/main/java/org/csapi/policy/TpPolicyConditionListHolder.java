package org.csapi.policy;

/**
 *	Generated from IDL definition of alias "TpPolicyConditionList"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.policy.TpPolicyConditionListElement[] value;

	public TpPolicyConditionListHolder ()
	{
	}
	public TpPolicyConditionListHolder (final org.csapi.policy.TpPolicyConditionListElement[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPolicyConditionListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPolicyConditionListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPolicyConditionListHelper.write (out,value);
	}
}
