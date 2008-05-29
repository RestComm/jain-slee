package org.csapi.gms;

/**
 *	Generated from IDL definition of struct "TpGMSNewMessageArrivedCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpGMSNewMessageArrivedCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.TpGMSNewMessageArrivedCriteria value;

	public TpGMSNewMessageArrivedCriteriaHolder ()
	{
	}
	public TpGMSNewMessageArrivedCriteriaHolder(final org.csapi.gms.TpGMSNewMessageArrivedCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.TpGMSNewMessageArrivedCriteriaHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.TpGMSNewMessageArrivedCriteriaHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.TpGMSNewMessageArrivedCriteriaHelper.write(_out, value);
	}
}
