package org.csapi.termcap;

/**
 *	Generated from IDL interface "IpTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpTerminalCapabilitiesHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpTerminalCapabilities value;
	public IpTerminalCapabilitiesHolder()
	{
	}
	public IpTerminalCapabilitiesHolder (final IpTerminalCapabilities initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpTerminalCapabilitiesHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpTerminalCapabilitiesHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpTerminalCapabilitiesHelper.write (_out,value);
	}
}
