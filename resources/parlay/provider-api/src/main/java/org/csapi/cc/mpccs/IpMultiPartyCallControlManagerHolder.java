package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpMultiPartyCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMultiPartyCallControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMultiPartyCallControlManager value;
	public IpMultiPartyCallControlManagerHolder()
	{
	}
	public IpMultiPartyCallControlManagerHolder (final IpMultiPartyCallControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMultiPartyCallControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMultiPartyCallControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMultiPartyCallControlManagerHelper.write (_out,value);
	}
}
