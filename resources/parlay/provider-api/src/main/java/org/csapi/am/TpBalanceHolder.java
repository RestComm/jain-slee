package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpBalance"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpBalance value;

	public TpBalanceHolder ()
	{
	}
	public TpBalanceHolder(final org.csapi.am.TpBalance initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.am.TpBalanceHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.am.TpBalanceHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.am.TpBalanceHelper.write(_out, value);
	}
}
