package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpAppMultiMediaCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppMultiMediaCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppMultiMediaCall value;
	public IpAppMultiMediaCallHolder()
	{
	}
	public IpAppMultiMediaCallHolder (final IpAppMultiMediaCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppMultiMediaCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppMultiMediaCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppMultiMediaCallHelper.write (_out,value);
	}
}
