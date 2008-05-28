package org.csapi.cm;

/**
 *	Generated from IDL interface "IpQoSTemplate"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpQoSTemplateHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpQoSTemplate value;
	public IpQoSTemplateHolder()
	{
	}
	public IpQoSTemplateHolder (final IpQoSTemplate initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpQoSTemplateHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpQoSTemplateHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpQoSTemplateHelper.write (_out,value);
	}
}
