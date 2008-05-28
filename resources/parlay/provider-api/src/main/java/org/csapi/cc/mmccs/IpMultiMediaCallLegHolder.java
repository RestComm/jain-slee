package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpMultiMediaCallLegHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpMultiMediaCallLeg value;
	public IpMultiMediaCallLegHolder()
	{
	}
	public IpMultiMediaCallLegHolder (final IpMultiMediaCallLeg initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpMultiMediaCallLegHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpMultiMediaCallLegHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpMultiMediaCallLegHelper.write (_out,value);
	}
}
