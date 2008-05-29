package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMailbox"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMailboxHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMailbox value;
	public IpMailboxHolder()
	{
	}
	public IpMailboxHolder (final IpMailbox initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMailboxHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMailboxHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMailboxHelper.write (_out,value);
	}
}
