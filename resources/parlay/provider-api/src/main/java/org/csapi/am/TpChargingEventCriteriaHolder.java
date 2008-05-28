package org.csapi.am;

/**
 *	Generated from IDL definition of struct "TpChargingEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingEventCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.am.TpChargingEventCriteria value;

	public TpChargingEventCriteriaHolder ()
	{
	}
	public TpChargingEventCriteriaHolder(final org.csapi.am.TpChargingEventCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.am.TpChargingEventCriteriaHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.am.TpChargingEventCriteriaHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.am.TpChargingEventCriteriaHelper.write(_out, value);
	}
}
