package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAAEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAAEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAAEventData value;

	public TpPAMAAEventDataHolder ()
	{
	}
	public TpPAMAAEventDataHolder(final org.csapi.pam.TpPAMAAEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAAEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAAEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAAEventDataHelper.write(_out, value);
	}
}
