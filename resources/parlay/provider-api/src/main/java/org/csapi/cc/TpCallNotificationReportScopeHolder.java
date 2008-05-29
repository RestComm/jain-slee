package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallNotificationReportScope"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationReportScopeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallNotificationReportScope value;

	public TpCallNotificationReportScopeHolder ()
	{
	}
	public TpCallNotificationReportScopeHolder(final org.csapi.cc.TpCallNotificationReportScope initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallNotificationReportScopeHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallNotificationReportScopeHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallNotificationReportScopeHelper.write(_out, value);
	}
}
