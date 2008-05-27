package org.csapi.policy;

/**
 *	Generated from IDL definition of alias "TpPolicyActionList"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyActionListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.policy.TpPolicyActionListElement[] value;

	public TpPolicyActionListHolder ()
	{
	}
	public TpPolicyActionListHolder (final org.csapi.policy.TpPolicyActionListElement[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPolicyActionListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPolicyActionListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPolicyActionListHelper.write (out,value);
	}
}
