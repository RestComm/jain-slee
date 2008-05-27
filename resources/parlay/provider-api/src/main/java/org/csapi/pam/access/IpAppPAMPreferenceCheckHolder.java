package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpAppPAMPreferenceCheck"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppPAMPreferenceCheckHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppPAMPreferenceCheck value;
	public IpAppPAMPreferenceCheckHolder()
	{
	}
	public IpAppPAMPreferenceCheckHolder (final IpAppPAMPreferenceCheck initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppPAMPreferenceCheckHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppPAMPreferenceCheckHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppPAMPreferenceCheckHelper.write (_out,value);
	}
}
