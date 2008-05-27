package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of alias "TpCallEventCriteriaResultSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallEventCriteriaResultSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallEventCriteriaResult[] value;

	public TpCallEventCriteriaResultSetHolder ()
	{
	}
	public TpCallEventCriteriaResultSetHolder (final org.csapi.cc.gccs.TpCallEventCriteriaResult[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallEventCriteriaResultSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallEventCriteriaResultSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallEventCriteriaResultSetHelper.write (out,value);
	}
}
