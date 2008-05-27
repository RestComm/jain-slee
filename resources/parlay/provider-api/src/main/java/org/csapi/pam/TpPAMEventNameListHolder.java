package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMEventNameList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventNameListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMEventName[] value;

	public TpPAMEventNameListHolder ()
	{
	}
	public TpPAMEventNameListHolder (final org.csapi.pam.TpPAMEventName[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMEventNameListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMEventNameListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMEventNameListHelper.write (out,value);
	}
}
