package org.csapi;

/**
 *	Generated from IDL definition of exception "P_INFORMATION_NOT_AVAILABLE"
 *	@author JacORB IDL compiler 
 */

public final class P_INFORMATION_NOT_AVAILABLEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.P_INFORMATION_NOT_AVAILABLE value;

	public P_INFORMATION_NOT_AVAILABLEHolder ()
	{
	}
	public P_INFORMATION_NOT_AVAILABLEHolder(final org.csapi.P_INFORMATION_NOT_AVAILABLE initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.P_INFORMATION_NOT_AVAILABLEHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.P_INFORMATION_NOT_AVAILABLEHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.P_INFORMATION_NOT_AVAILABLEHelper.write(_out, value);
	}
}
