package org.csapi.termcap;

/**
 *	Generated from IDL interface "IpExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpExtendedTerminalCapabilitiesHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpExtendedTerminalCapabilities value;
	public IpExtendedTerminalCapabilitiesHolder()
	{
	}
	public IpExtendedTerminalCapabilitiesHolder (final IpExtendedTerminalCapabilities initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpExtendedTerminalCapabilitiesHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpExtendedTerminalCapabilitiesHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpExtendedTerminalCapabilitiesHelper.write (_out,value);
	}
}
