package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpAddSagMembersConflict"
 *	@author JacORB IDL compiler 
 */

public final class TpAddSagMembersConflictHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpAddSagMembersConflict value;

	public TpAddSagMembersConflictHolder ()
	{
	}
	public TpAddSagMembersConflictHolder(final org.csapi.fw.TpAddSagMembersConflict initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpAddSagMembersConflictHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpAddSagMembersConflictHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpAddSagMembersConflictHelper.write(_out, value);
	}
}
