package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpChargingEventCriteriaResult"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventCriteriaResultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpChargingEventCriteriaResult value;

	public TpChargingEventCriteriaResultHolder ()
	{
	}
	public TpChargingEventCriteriaResultHolder(final org.csapi.am.TpChargingEventCriteriaResult initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.am.TpChargingEventCriteriaResultHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.am.TpChargingEventCriteriaResultHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.am.TpChargingEventCriteriaResultHelper.write(_out, value);
	}
}
