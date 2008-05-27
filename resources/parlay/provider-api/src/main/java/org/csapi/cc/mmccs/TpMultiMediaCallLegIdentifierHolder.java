package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMultiMediaCallLegIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaCallLegIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier value;

	public TpMultiMediaCallLegIdentifierHolder ()
	{
	}
	public TpMultiMediaCallLegIdentifierHolder(final org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifierHelper.write(_out, value);
	}
}
