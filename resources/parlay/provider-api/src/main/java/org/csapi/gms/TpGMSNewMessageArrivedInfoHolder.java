package org.csapi.gms;

/**
 *	Generated from IDL definition of struct "TpGMSNewMessageArrivedInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpGMSNewMessageArrivedInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.TpGMSNewMessageArrivedInfo value;

	public TpGMSNewMessageArrivedInfoHolder ()
	{
	}
	public TpGMSNewMessageArrivedInfoHolder(final org.csapi.gms.TpGMSNewMessageArrivedInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.TpGMSNewMessageArrivedInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.TpGMSNewMessageArrivedInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.TpGMSNewMessageArrivedInfoHelper.write(_out, value);
	}
}
