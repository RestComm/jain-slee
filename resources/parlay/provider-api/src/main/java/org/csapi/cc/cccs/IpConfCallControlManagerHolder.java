package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpConfCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpConfCallControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpConfCallControlManager value;
	public IpConfCallControlManagerHolder()
	{
	}
	public IpConfCallControlManagerHolder (final IpConfCallControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpConfCallControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpConfCallControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpConfCallControlManagerHelper.write (_out,value);
	}
}
