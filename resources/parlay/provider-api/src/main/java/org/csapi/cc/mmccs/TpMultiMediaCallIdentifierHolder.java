package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpMultiMediaCallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaCallIdentifierHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpMultiMediaCallIdentifier value;

	public TpMultiMediaCallIdentifierHolder ()
	{
	}
	public TpMultiMediaCallIdentifierHolder(final org.csapi.cc.mmccs.TpMultiMediaCallIdentifier initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mmccs.TpMultiMediaCallIdentifierHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mmccs.TpMultiMediaCallIdentifierHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mmccs.TpMultiMediaCallIdentifierHelper.write(_out, value);
	}
}
