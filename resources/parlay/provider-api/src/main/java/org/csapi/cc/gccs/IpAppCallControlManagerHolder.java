package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpAppCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppCallControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppCallControlManager value;
	public IpAppCallControlManagerHolder()
	{
	}
	public IpAppCallControlManagerHolder (final IpAppCallControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppCallControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppCallControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppCallControlManagerHelper.write (_out,value);
	}
}
