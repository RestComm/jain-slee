package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMICEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMICEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMICEventData value;

	public TpPAMICEventDataHolder ()
	{
	}
	public TpPAMICEventDataHolder(final org.csapi.pam.TpPAMICEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMICEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMICEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMICEventDataHelper.write(_out, value);
	}
}
