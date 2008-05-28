package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpAppConfCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppConfCallControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppConfCallControlManager value;
	public IpAppConfCallControlManagerHolder()
	{
	}
	public IpAppConfCallControlManagerHolder (final IpAppConfCallControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppConfCallControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppConfCallControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppConfCallControlManagerHelper.write (_out,value);
	}
}
