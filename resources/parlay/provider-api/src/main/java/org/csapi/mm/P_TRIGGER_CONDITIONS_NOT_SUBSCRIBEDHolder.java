package org.csapi.mm;

/**
 *	Generated from IDL definition of exception "P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED"
 *	@author JacORB IDL compiler 
 */

public final class P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED value;

	public P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHolder ()
	{
	}
	public P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHolder(final org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHelper.write(_out, value);
	}
}
