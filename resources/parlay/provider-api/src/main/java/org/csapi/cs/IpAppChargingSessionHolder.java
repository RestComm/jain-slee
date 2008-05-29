package org.csapi.cs;

/**
 *	Generated from IDL interface "IpAppChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppChargingSessionHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppChargingSession value;
	public IpAppChargingSessionHolder()
	{
	}
	public IpAppChargingSessionHolder (final IpAppChargingSession initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppChargingSessionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppChargingSessionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppChargingSessionHelper.write (_out,value);
	}
}
