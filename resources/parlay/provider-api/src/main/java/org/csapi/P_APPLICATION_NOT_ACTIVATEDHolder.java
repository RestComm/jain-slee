package org.csapi;

/**
 *	Generated from IDL definition of exception "P_APPLICATION_NOT_ACTIVATED"
 *	@author JacORB IDL compiler 
 */

public final class P_APPLICATION_NOT_ACTIVATEDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_APPLICATION_NOT_ACTIVATED value;

	public P_APPLICATION_NOT_ACTIVATEDHolder ()
	{
	}
	public P_APPLICATION_NOT_ACTIVATEDHolder(final org.csapi.P_APPLICATION_NOT_ACTIVATED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.write(_out, value);
	}
}
