package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyConditionType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyConditionTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPolicyConditionType value;

	public TpPolicyConditionTypeHolder ()
	{
	}
	public TpPolicyConditionTypeHolder (final TpPolicyConditionType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPolicyConditionTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPolicyConditionTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPolicyConditionTypeHelper.write (out,value);
	}
}
