package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpAppMultiMediaCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpAppMultiMediaCallLegHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpAppMultiMediaCallLeg value;
	public IpAppMultiMediaCallLegHolder()
	{
	}
	public IpAppMultiMediaCallLegHolder (final IpAppMultiMediaCallLeg initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpAppMultiMediaCallLegHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpAppMultiMediaCallLegHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpAppMultiMediaCallLegHelper.write (_out,value);
	}
}
