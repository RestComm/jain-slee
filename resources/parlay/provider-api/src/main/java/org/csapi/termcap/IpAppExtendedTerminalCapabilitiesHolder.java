package org.csapi.termcap;

/**
 *	Generated from IDL interface "IpAppExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppExtendedTerminalCapabilitiesHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppExtendedTerminalCapabilities value;
	public IpAppExtendedTerminalCapabilitiesHolder()
	{
	}
	public IpAppExtendedTerminalCapabilitiesHolder (final IpAppExtendedTerminalCapabilities initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppExtendedTerminalCapabilitiesHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppExtendedTerminalCapabilitiesHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppExtendedTerminalCapabilitiesHelper.write (_out,value);
	}
}
