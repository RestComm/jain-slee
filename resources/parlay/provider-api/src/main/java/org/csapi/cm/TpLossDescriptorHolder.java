package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpLossDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpLossDescriptorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpLossDescriptor value;

	public TpLossDescriptorHolder ()
	{
	}
	public TpLossDescriptorHolder(final org.csapi.cm.TpLossDescriptor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpLossDescriptorHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpLossDescriptorHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpLossDescriptorHelper.write(_out, value);
	}
}
