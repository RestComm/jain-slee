package org.csapi.am;

/**
 *	Generated from IDL interface "IpAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAccountManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAccountManager value;
	public IpAccountManagerHolder()
	{
	}
	public IpAccountManagerHolder (final IpAccountManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAccountManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAccountManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAccountManagerHelper.write (_out,value);
	}
}
