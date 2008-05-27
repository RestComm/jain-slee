package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMAgentPresence"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMAgentPresenceHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMAgentPresence value;
	public IpPAMAgentPresenceHolder()
	{
	}
	public IpPAMAgentPresenceHolder (final IpPAMAgentPresence initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMAgentPresenceHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMAgentPresenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMAgentPresenceHelper.write (_out,value);
	}
}
