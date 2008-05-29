package org.csapi.policy;

/**
 *	Generated from IDL definition of struct "TpPolicyEvent"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyEventHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.policy.TpPolicyEvent value;

	public TpPolicyEventHolder ()
	{
	}
	public TpPolicyEventHolder(final org.csapi.policy.TpPolicyEvent initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.policy.TpPolicyEventHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.policy.TpPolicyEventHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.policy.TpPolicyEventHelper.write(_out, value);
	}
}
