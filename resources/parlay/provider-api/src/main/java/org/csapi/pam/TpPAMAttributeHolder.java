package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAttribute"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttributeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAttribute value;

	public TpPAMAttributeHolder ()
	{
	}
	public TpPAMAttributeHolder(final org.csapi.pam.TpPAMAttribute initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAttributeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAttributeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAttributeHelper.write(_out, value);
	}
}
