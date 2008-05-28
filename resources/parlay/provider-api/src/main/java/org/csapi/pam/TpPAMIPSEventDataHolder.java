package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMIPSEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMIPSEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMIPSEventData value;

	public TpPAMIPSEventDataHolder ()
	{
	}
	public TpPAMIPSEventDataHolder(final org.csapi.pam.TpPAMIPSEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMIPSEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMIPSEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMIPSEventDataHelper.write(_out, value);
	}
}
