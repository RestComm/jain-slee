package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpAppMultiPartyCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppMultiPartyCallHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppMultiPartyCall value;
	public IpAppMultiPartyCallHolder()
	{
	}
	public IpAppMultiPartyCallHolder (final IpAppMultiPartyCall initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppMultiPartyCallHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppMultiPartyCallHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppMultiPartyCallHelper.write (_out,value);
	}
}
