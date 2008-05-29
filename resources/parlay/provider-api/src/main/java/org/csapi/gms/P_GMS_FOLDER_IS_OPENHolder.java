package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_FOLDER_IS_OPEN"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_FOLDER_IS_OPENHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_FOLDER_IS_OPEN value;

	public P_GMS_FOLDER_IS_OPENHolder ()
	{
	}
	public P_GMS_FOLDER_IS_OPENHolder(final org.csapi.gms.P_GMS_FOLDER_IS_OPEN initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_FOLDER_IS_OPENHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_FOLDER_IS_OPENHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_FOLDER_IS_OPENHelper.write(_out, value);
	}
}
