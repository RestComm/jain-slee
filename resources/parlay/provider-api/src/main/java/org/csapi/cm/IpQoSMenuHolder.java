package org.csapi.cm;

/**
 *	Generated from IDL interface "IpQoSMenu"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpQoSMenuHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpQoSMenu value;
	public IpQoSMenuHolder()
	{
	}
	public IpQoSMenuHolder (final IpQoSMenu initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpQoSMenuHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpQoSMenuHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpQoSMenuHelper.write (_out,value);
	}
}
