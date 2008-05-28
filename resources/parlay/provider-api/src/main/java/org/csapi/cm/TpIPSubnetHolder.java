package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpIPSubnet"
 *	@author JacORB IDL compiler 
 */

public final class TpIPSubnetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpIPSubnet value;

	public TpIPSubnetHolder ()
	{
	}
	public TpIPSubnetHolder(final org.csapi.cm.TpIPSubnet initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpIPSubnetHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpIPSubnetHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpIPSubnetHelper.write(_out, value);
	}
}
