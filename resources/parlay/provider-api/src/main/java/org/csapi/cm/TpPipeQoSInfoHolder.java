package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpPipeQoSInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPipeQoSInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpPipeQoSInfo value;

	public TpPipeQoSInfoHolder ()
	{
	}
	public TpPipeQoSInfoHolder(final org.csapi.cm.TpPipeQoSInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpPipeQoSInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpPipeQoSInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpPipeQoSInfoHelper.write(_out, value);
	}
}
