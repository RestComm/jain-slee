package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMPresenceAvailabilityManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMPresenceAvailabilityManagerHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMPresenceAvailabilityManager value;
	public IpPAMPresenceAvailabilityManagerHolder()
	{
	}
	public IpPAMPresenceAvailabilityManagerHolder (final IpPAMPresenceAvailabilityManager initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMPresenceAvailabilityManagerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMPresenceAvailabilityManagerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMPresenceAvailabilityManagerHelper.write (_out,value);
	}
}
