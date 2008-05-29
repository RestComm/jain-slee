package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpDataSessionControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpDataSessionControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpDataSessionControlManager value;
	public IpDataSessionControlManagerHolder()
	{
	}
	public IpDataSessionControlManagerHolder (final IpDataSessionControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpDataSessionControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpDataSessionControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpDataSessionControlManagerHelper.write (_out,value);
	}
}
