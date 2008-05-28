package org.csapi.policy;
/**
 *	Generated from IDL definition of enum "TpPolicyActionType"
 *	@author JacORB IDL compiler 
 */

public final class TpPolicyActionTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPolicyActionType value;

	public TpPolicyActionTypeHolder ()
	{
	}
	public TpPolicyActionTypeHolder (final TpPolicyActionType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPolicyActionTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPolicyActionTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPolicyActionTypeHelper.write (out,value);
	}
}
