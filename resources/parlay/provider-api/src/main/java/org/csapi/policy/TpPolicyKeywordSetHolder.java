package org.csapi.policy;

/**
 *	Generated from IDL definition of alias "TpPolicyKeywordSet"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyKeywordSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public TpPolicyKeywordSetHolder ()
	{
	}
	public TpPolicyKeywordSetHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPolicyKeywordSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPolicyKeywordSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPolicyKeywordSetHelper.write (out,value);
	}
}
