package org.csapi.fw;

/**
 *	Generated from IDL definition of exception "P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT value;

	public P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHolder ()
	{
	}
	public P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHolder(final org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHelper.write(_out, value);
	}
}
