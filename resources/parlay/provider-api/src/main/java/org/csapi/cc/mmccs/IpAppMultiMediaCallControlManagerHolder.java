package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpAppMultiMediaCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppMultiMediaCallControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppMultiMediaCallControlManager value;
	public IpAppMultiMediaCallControlManagerHolder()
	{
	}
	public IpAppMultiMediaCallControlManagerHolder (final IpAppMultiMediaCallControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppMultiMediaCallControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppMultiMediaCallControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppMultiMediaCallControlManagerHelper.write (_out,value);
	}
}
