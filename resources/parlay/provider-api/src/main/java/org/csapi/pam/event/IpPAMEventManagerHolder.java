package org.csapi.pam.event;

/**
 *	Generated from IDL interface "IpPAMEventManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMEventManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMEventManager value;
	public IpPAMEventManagerHolder()
	{
	}
	public IpPAMEventManagerHolder (final IpPAMEventManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMEventManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMEventManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMEventManagerHelper.write (_out,value);
	}
}
