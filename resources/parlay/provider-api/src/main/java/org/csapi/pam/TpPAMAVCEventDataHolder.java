package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAVCEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAVCEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAVCEventData value;

	public TpPAMAVCEventDataHolder ()
	{
	}
	public TpPAMAVCEventDataHolder(final org.csapi.pam.TpPAMAVCEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAVCEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAVCEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAVCEventDataHelper.write(_out, value);
	}
}
