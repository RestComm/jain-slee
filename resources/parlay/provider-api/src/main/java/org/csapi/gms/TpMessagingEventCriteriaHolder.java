package org.csapi.gms;
/**
 *	Generated from IDL definition of union "TpMessagingEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingEventCriteriaHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMessagingEventCriteria value;

	public TpMessagingEventCriteriaHolder ()
	{
	}
	public TpMessagingEventCriteriaHolder (final TpMessagingEventCriteria initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMessagingEventCriteriaHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMessagingEventCriteriaHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMessagingEventCriteriaHelper.write (out, value);
	}
}
