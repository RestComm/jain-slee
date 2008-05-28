package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMessagingManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMessagingManager value;
	public IpMessagingManagerHolder()
	{
	}
	public IpMessagingManagerHolder (final IpMessagingManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMessagingManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMessagingManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMessagingManagerHelper.write (_out,value);
	}
}
