package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_PROPERTY_TYPE_MISMATCH"
 *	@author JacORB IDL compiler 
 */

public final class P_PROPERTY_TYPE_MISMATCHHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_PROPERTY_TYPE_MISMATCH value;

	public P_PROPERTY_TYPE_MISMATCHHolder ()
	{
	}
	public P_PROPERTY_TYPE_MISMATCHHolder(final org.csapi.fw.P_PROPERTY_TYPE_MISMATCH initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_PROPERTY_TYPE_MISMATCHHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_PROPERTY_TYPE_MISMATCHHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_PROPERTY_TYPE_MISMATCHHelper.write(_out, value);
	}
}
