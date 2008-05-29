package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceProfileInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpServiceProfileInfoQueryHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpServiceProfileInfoQuery value;
	public IpServiceProfileInfoQueryHolder()
	{
	}
	public IpServiceProfileInfoQueryHolder (final IpServiceProfileInfoQuery initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpServiceProfileInfoQueryHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpServiceProfileInfoQueryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpServiceProfileInfoQueryHelper.write (_out,value);
	}
}
