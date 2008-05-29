package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaStream"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMultiMediaStreamHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMultiMediaStream value;
	public IpMultiMediaStreamHolder()
	{
	}
	public IpMultiMediaStreamHolder (final IpMultiMediaStream initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMultiMediaStreamHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMultiMediaStreamHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMultiMediaStreamHelper.write (_out,value);
	}
}
