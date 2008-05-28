package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpLoadDescriptor"
 *	@author JacORB IDL compiler 
 */

public final class TpLoadDescriptorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpLoadDescriptor value;

	public TpLoadDescriptorHolder ()
	{
	}
	public TpLoadDescriptorHolder(final org.csapi.cm.TpLoadDescriptor initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpLoadDescriptorHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpLoadDescriptorHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpLoadDescriptorHelper.write(_out, value);
	}
}
