package org.csapi.cs;

/**
 *	Generated from IDL interface "IpChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpChargingManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpChargingManager value;
	public IpChargingManagerHolder()
	{
	}
	public IpChargingManagerHolder (final IpChargingManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpChargingManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpChargingManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpChargingManagerHelper.write (_out,value);
	}
}
