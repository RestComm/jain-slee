package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpSagProfilePair"
 *	@author JacORB IDL compiler 
 */

public final class TpSagProfilePairHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpSagProfilePair value;

	public TpSagProfilePairHolder ()
	{
	}
	public TpSagProfilePairHolder(final org.csapi.fw.TpSagProfilePair initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpSagProfilePairHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpSagProfilePairHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpSagProfilePairHelper.write(_out, value);
	}
}
