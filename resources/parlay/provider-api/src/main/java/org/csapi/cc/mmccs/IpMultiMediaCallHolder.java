package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMultiMediaCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMultiMediaCall value;
	public IpMultiMediaCallHolder()
	{
	}
	public IpMultiMediaCallHolder (final IpMultiMediaCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMultiMediaCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMultiMediaCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMultiMediaCallHelper.write (_out,value);
	}
}
