package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_MISSING_MANDATORY_PROPERTY"
 *	@author JacORB IDL compiler 
 */

public final class P_MISSING_MANDATORY_PROPERTYHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_MISSING_MANDATORY_PROPERTY value;

	public P_MISSING_MANDATORY_PROPERTYHolder ()
	{
	}
	public P_MISSING_MANDATORY_PROPERTYHolder(final org.csapi.fw.P_MISSING_MANDATORY_PROPERTY initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_MISSING_MANDATORY_PROPERTYHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_MISSING_MANDATORY_PROPERTYHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_MISSING_MANDATORY_PROPERTYHelper.write(_out, value);
	}
}
