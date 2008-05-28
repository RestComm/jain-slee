package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMErrorInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMErrorInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMErrorInfo value;

	public TpPAMErrorInfoHolder ()
	{
	}
	public TpPAMErrorInfoHolder(final org.csapi.pam.TpPAMErrorInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMErrorInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMErrorInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMErrorInfoHelper.write(_out, value);
	}
}
