package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyConditionListType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionListTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPolicyConditionListType value;

	public TpPolicyConditionListTypeHolder ()
	{
	}
	public TpPolicyConditionListTypeHolder (final TpPolicyConditionListType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPolicyConditionListTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPolicyConditionListTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPolicyConditionListTypeHelper.write (out,value);
	}
}
