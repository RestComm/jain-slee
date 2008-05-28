package org.csapi.ui;

/**
 *	Generated from IDL definition of alias "TpUIEventCriteriaResultSet"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventCriteriaResultSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.TpUIEventCriteriaResult[] value;

	public TpUIEventCriteriaResultSetHolder ()
	{
	}
	public TpUIEventCriteriaResultSetHolder (final org.csapi.ui.TpUIEventCriteriaResult[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUIEventCriteriaResultSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUIEventCriteriaResultSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUIEventCriteriaResultSetHelper.write (out,value);
	}
}
