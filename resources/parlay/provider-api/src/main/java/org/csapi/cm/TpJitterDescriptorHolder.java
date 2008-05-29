package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpJitterDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpJitterDescriptorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpJitterDescriptor value;

	public TpJitterDescriptorHolder ()
	{
	}
	public TpJitterDescriptorHolder(final org.csapi.cm.TpJitterDescriptor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpJitterDescriptorHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpJitterDescriptorHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpJitterDescriptorHelper.write(_out, value);
	}
}
