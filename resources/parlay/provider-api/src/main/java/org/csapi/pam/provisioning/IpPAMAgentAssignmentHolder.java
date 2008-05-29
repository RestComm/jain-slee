package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMAgentAssignment"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPAMAgentAssignmentHolder	implements org.omg.CORBA.portable.Streamable{
	 public IpPAMAgentAssignment value;
	public IpPAMAgentAssignmentHolder()
	{
	}
	public IpPAMAgentAssignmentHolder (final IpPAMAgentAssignment initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return IpPAMAgentAssignmentHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = IpPAMAgentAssignmentHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		IpPAMAgentAssignmentHelper.write (_out,value);
	}
}
