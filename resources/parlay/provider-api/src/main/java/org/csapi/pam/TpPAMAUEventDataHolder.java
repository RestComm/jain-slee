package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAUEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAUEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAUEventData value;

	public TpPAMAUEventDataHolder ()
	{
	}
	public TpPAMAUEventDataHolder(final org.csapi.pam.TpPAMAUEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAUEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAUEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAUEventDataHelper.write(_out, value);
	}
}
