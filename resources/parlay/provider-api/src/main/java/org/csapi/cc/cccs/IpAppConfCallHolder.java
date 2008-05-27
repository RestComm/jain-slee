package org.csapi.cc.cccs;

/**
 *	Generated from IDL interface "IpAppConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppConfCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppConfCall value;
	public IpAppConfCallHolder()
	{
	}
	public IpAppConfCallHolder (final IpAppConfCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppConfCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppConfCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppConfCallHelper.write (_out,value);
	}
}
