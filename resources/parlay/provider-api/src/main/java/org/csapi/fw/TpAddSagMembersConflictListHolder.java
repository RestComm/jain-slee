package org.csapi.fw;

/**
 *	Generated from IDL definition of alias "TpAddSagMembersConflictList"
 *	@author JacORB IDL compiler 
 */

public final class TpAddSagMembersConflictListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpAddSagMembersConflict[] value;

	public TpAddSagMembersConflictListHolder ()
	{
	}
	public TpAddSagMembersConflictListHolder (final org.csapi.fw.TpAddSagMembersConflict[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAddSagMembersConflictListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAddSagMembersConflictListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAddSagMembersConflictListHelper.write (out,value);
	}
}
