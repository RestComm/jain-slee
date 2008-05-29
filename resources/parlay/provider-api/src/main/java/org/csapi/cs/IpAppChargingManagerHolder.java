package org.csapi.cs;

/**
 *	Generated from IDL interface "IpAppChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppChargingManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppChargingManager value;
	public IpAppChargingManagerHolder()
	{
	}
	public IpAppChargingManagerHolder (final IpAppChargingManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppChargingManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppChargingManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppChargingManagerHelper.write (_out,value);
	}
}
