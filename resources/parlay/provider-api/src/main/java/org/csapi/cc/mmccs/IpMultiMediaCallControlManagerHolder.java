package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMultiMediaCallControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMultiMediaCallControlManager value;
	public IpMultiMediaCallControlManagerHolder()
	{
	}
	public IpMultiMediaCallControlManagerHolder (final IpMultiMediaCallControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMultiMediaCallControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMultiMediaCallControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMultiMediaCallControlManagerHelper.write (_out,value);
	}
}
