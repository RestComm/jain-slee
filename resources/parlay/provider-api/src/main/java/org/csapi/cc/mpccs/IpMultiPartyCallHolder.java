package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpMultiPartyCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMultiPartyCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMultiPartyCall value;
	public IpMultiPartyCallHolder()
	{
	}
	public IpMultiPartyCallHolder (final IpMultiPartyCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMultiPartyCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMultiPartyCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMultiPartyCallHelper.write (_out,value);
	}
}
