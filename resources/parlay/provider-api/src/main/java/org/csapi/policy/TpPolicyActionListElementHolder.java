package org.csapi.policy;

/**
 *	Generated from IDL definition of struct "TpPolicyActionListElement"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyActionListElementHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.policy.TpPolicyActionListElement value;

	public TpPolicyActionListElementHolder ()
	{
	}
	public TpPolicyActionListElementHolder(final org.csapi.policy.TpPolicyActionListElement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.policy.TpPolicyActionListElementHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.policy.TpPolicyActionListElementHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.policy.TpPolicyActionListElementHelper.write(_out, value);
	}
}
