package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpAssignSagToServiceProfileConflictList"
 *	@author JacORB IDL compiler 
 */

public final class TpAssignSagToServiceProfileConflictListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpAssignSagToServiceProfileConflict[] value;

	public TpAssignSagToServiceProfileConflictListHolder ()
	{
	}
	public TpAssignSagToServiceProfileConflictListHolder (final org.csapi.fw.TpAssignSagToServiceProfileConflict[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAssignSagToServiceProfileConflictListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAssignSagToServiceProfileConflictListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAssignSagToServiceProfileConflictListHelper.write (out,value);
	}
}
