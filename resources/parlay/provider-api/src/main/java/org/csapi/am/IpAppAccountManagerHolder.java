package org.csapi.am;

/**
 *	Generated from IDL interface "IpAppAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppAccountManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppAccountManager value;
	public IpAppAccountManagerHolder()
	{
	}
	public IpAppAccountManagerHolder (final IpAppAccountManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppAccountManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppAccountManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppAccountManagerHelper.write (_out,value);
	}
}
