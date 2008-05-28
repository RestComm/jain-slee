package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMContextList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMContext[] value;

	public TpPAMContextListHolder ()
	{
	}
	public TpPAMContextListHolder (final org.csapi.pam.TpPAMContext[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMContextListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMContextListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMContextListHelper.write (out,value);
	}
}
