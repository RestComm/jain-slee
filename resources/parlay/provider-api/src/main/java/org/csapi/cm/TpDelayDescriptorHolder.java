package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpDelayDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpDelayDescriptorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpDelayDescriptor value;

	public TpDelayDescriptorHolder ()
	{
	}
	public TpDelayDescriptorHolder(final org.csapi.cm.TpDelayDescriptor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpDelayDescriptorHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpDelayDescriptorHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpDelayDescriptorHelper.write(_out, value);
	}
}
