package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpMultiMediaConfPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaConfPolicyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.cccs.TpMultiMediaConfPolicy value;

	public TpMultiMediaConfPolicyHolder ()
	{
	}
	public TpMultiMediaConfPolicyHolder(final org.csapi.cc.cccs.TpMultiMediaConfPolicy initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.cccs.TpMultiMediaConfPolicyHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.cccs.TpMultiMediaConfPolicyHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.cccs.TpMultiMediaConfPolicyHelper.write(_out, value);
	}
}
