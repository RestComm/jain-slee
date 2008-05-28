package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpEndpoint"
 *	@author JacORB IDL compiler 
 */

public final class TpEndpointHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpEndpoint value;

	public TpEndpointHolder ()
	{
	}
	public TpEndpointHolder(final org.csapi.cm.TpEndpoint initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpEndpointHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpEndpointHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpEndpointHelper.write(_out, value);
	}
}
