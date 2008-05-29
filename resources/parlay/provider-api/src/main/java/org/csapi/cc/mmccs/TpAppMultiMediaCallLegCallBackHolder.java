package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpAppMultiMediaCallLegCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallLegCallBackHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack value;

	public TpAppMultiMediaCallLegCallBackHolder ()
	{
	}
	public TpAppMultiMediaCallLegCallBackHolder(final org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBackHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBackHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBackHelper.write(_out, value);
	}
}
