package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMailboxFolder"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMailboxFolderHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMailboxFolder value;
	public IpMailboxFolderHolder()
	{
	}
	public IpMailboxFolderHolder (final IpMailboxFolder initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMailboxFolderHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMailboxFolderHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMailboxFolderHelper.write (_out,value);
	}
}
