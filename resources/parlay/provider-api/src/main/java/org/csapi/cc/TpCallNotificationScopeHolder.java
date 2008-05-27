package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallNotificationScope"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationScopeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallNotificationScope value;

	public TpCallNotificationScopeHolder ()
	{
	}
	public TpCallNotificationScopeHolder(final org.csapi.cc.TpCallNotificationScope initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallNotificationScopeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallNotificationScopeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallNotificationScopeHelper.write(_out, value);
	}
}
