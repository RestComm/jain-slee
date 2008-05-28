package org.csapi.cs;

/**
 *	Generated from IDL interface "IpChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpChargingSessionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpChargingSession value;
	public IpChargingSessionHolder()
	{
	}
	public IpChargingSessionHolder (final IpChargingSession initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpChargingSessionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpChargingSessionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpChargingSessionHelper.write (_out,value);
	}
}
