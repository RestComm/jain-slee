package org.csapi.gms;

/**
 *	Generated from IDL interface "IpAppMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppMessagingManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppMessagingManager value;
	public IpAppMessagingManagerHolder()
	{
	}
	public IpAppMessagingManagerHolder (final IpAppMessagingManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppMessagingManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppMessagingManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppMessagingManagerHelper.write (_out,value);
	}
}
