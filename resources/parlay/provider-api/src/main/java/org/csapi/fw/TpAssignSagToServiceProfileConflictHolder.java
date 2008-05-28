package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpAssignSagToServiceProfileConflict"
 *	@author JacORB IDL compiler 
 */

public final class TpAssignSagToServiceProfileConflictHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpAssignSagToServiceProfileConflict value;

	public TpAssignSagToServiceProfileConflictHolder ()
	{
	}
	public TpAssignSagToServiceProfileConflictHolder(final org.csapi.fw.TpAssignSagToServiceProfileConflict initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpAssignSagToServiceProfileConflictHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpAssignSagToServiceProfileConflictHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpAssignSagToServiceProfileConflictHelper.write(_out, value);
	}
}
