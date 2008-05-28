package org.csapi.cm;

/**
 *	Generated from IDL definition of exception "P_UNKNOWN_SAP"
 *	@author JacORB IDL compiler 
 */

public final class P_UNKNOWN_SAPHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.P_UNKNOWN_SAP value;

	public P_UNKNOWN_SAPHolder ()
	{
	}
	public P_UNKNOWN_SAPHolder(final org.csapi.cm.P_UNKNOWN_SAP initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.P_UNKNOWN_SAPHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.P_UNKNOWN_SAPHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.P_UNKNOWN_SAPHelper.write(_out, value);
	}
}
