package org.csapi.cc;

/**
 *	Generated from IDL definition of alias "TpReleaseCauseSet"
 *	@author JacORB IDL compiler 
 */

public final class TpReleaseCauseSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpReleaseCause[] value;

	public TpReleaseCauseSetHolder ()
	{
	}
	public TpReleaseCauseSetHolder (final org.csapi.cc.TpReleaseCause[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpReleaseCauseSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpReleaseCauseSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpReleaseCauseSetHelper.write (out,value);
	}
}
