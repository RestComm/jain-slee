package org.csapi.fw.fw_service.service_registration;

/**
 *	Generated from IDL interface "IpFwServiceRegistration"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpFwServiceRegistrationHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpFwServiceRegistration value;
	public IpFwServiceRegistrationHolder()
	{
	}
	public IpFwServiceRegistrationHolder (final IpFwServiceRegistration initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpFwServiceRegistrationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpFwServiceRegistrationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpFwServiceRegistrationHelper.write (_out,value);
	}
}
