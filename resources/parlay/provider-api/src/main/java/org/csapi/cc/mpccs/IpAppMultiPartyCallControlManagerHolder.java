package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpAppMultiPartyCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppMultiPartyCallControlManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppMultiPartyCallControlManager value;
	public IpAppMultiPartyCallControlManagerHolder()
	{
	}
	public IpAppMultiPartyCallControlManagerHolder (final IpAppMultiPartyCallControlManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppMultiPartyCallControlManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppMultiPartyCallControlManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppMultiPartyCallControlManagerHelper.write (_out,value);
	}
}
