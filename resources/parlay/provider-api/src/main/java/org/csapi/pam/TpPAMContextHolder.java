package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMContext"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMContext value;

	public TpPAMContextHolder ()
	{
	}
	public TpPAMContextHolder(final org.csapi.pam.TpPAMContext initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMContextHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMContextHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMContextHelper.write(_out, value);
	}
}
