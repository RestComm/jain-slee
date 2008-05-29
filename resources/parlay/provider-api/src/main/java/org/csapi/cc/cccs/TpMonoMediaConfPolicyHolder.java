package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpMonoMediaConfPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpMonoMediaConfPolicyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.cccs.TpMonoMediaConfPolicy value;

	public TpMonoMediaConfPolicyHolder ()
	{
	}
	public TpMonoMediaConfPolicyHolder(final org.csapi.cc.cccs.TpMonoMediaConfPolicy initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.cccs.TpMonoMediaConfPolicyHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.cccs.TpMonoMediaConfPolicyHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.cccs.TpMonoMediaConfPolicyHelper.write(_out, value);
	}
}
