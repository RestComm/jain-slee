package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_MESSAGE_NOT_REMOVED"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_MESSAGE_NOT_REMOVEDHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVED value;

	public P_GMS_MESSAGE_NOT_REMOVEDHolder ()
	{
	}
	public P_GMS_MESSAGE_NOT_REMOVEDHolder(final org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVED initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVEDHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVEDHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVEDHelper.write(_out, value);
	}
}
