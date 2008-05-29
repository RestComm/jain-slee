package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpCallControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpCallControlManager value;
	public IpCallControlManagerHolder()
	{
	}
	public IpCallControlManagerHolder (final IpCallControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpCallControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpCallControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpCallControlManagerHelper.write (_out,value);
	}
}
