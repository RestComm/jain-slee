package org.csapi.am;

/**
 *	Generated from IDL definition of alias "TpChargingEventCriteriaResultSet"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventCriteriaResultSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpChargingEventCriteriaResult[] value;

	public TpChargingEventCriteriaResultSetHolder ()
	{
	}
	public TpChargingEventCriteriaResultSetHolder (final org.csapi.am.TpChargingEventCriteriaResult[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpChargingEventCriteriaResultSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpChargingEventCriteriaResultSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpChargingEventCriteriaResultSetHelper.write (out,value);
	}
}
