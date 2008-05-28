package org.csapi.dsc;

/**
 *	Generated from IDL definition of alias "TpDataSessionEventCriteriaResultSet"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionEventCriteriaResultSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.dsc.TpDataSessionEventCriteriaResult[] value;

	public TpDataSessionEventCriteriaResultSetHolder ()
	{
	}
	public TpDataSessionEventCriteriaResultSetHolder (final org.csapi.dsc.TpDataSessionEventCriteriaResult[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpDataSessionEventCriteriaResultSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpDataSessionEventCriteriaResultSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpDataSessionEventCriteriaResultSetHelper.write (out,value);
	}
}
