package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of struct "TpAppCallLegCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppCallLegCallBackHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mpccs.TpAppCallLegCallBack value;

	public TpAppCallLegCallBackHolder ()
	{
	}
	public TpAppCallLegCallBackHolder(final org.csapi.cc.mpccs.TpAppCallLegCallBack initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mpccs.TpAppCallLegCallBackHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mpccs.TpAppCallLegCallBackHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mpccs.TpAppCallLegCallBackHelper.write(_out, value);
	}
}
