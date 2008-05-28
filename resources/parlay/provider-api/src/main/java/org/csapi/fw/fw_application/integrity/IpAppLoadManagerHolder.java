package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppLoadManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppLoadManager value;
	public IpAppLoadManagerHolder()
	{
	}
	public IpAppLoadManagerHolder (final IpAppLoadManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppLoadManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppLoadManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppLoadManagerHelper.write (_out,value);
	}
}
