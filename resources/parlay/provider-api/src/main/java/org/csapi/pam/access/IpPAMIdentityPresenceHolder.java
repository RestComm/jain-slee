package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMIdentityPresence"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMIdentityPresenceHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMIdentityPresence value;
	public IpPAMIdentityPresenceHolder()
	{
	}
	public IpPAMIdentityPresenceHolder (final IpPAMIdentityPresence initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMIdentityPresenceHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMIdentityPresenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMIdentityPresenceHelper.write (_out,value);
	}
}
