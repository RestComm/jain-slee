package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_SERVICE_TYPE_UNAVAILABLE"
 *	@author JacORB IDL compiler 
 */

public final class P_SERVICE_TYPE_UNAVAILABLEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLE value;

	public P_SERVICE_TYPE_UNAVAILABLEHolder ()
	{
	}
	public P_SERVICE_TYPE_UNAVAILABLEHolder(final org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLEHelper.write(_out, value);
	}
}
