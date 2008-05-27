package org.csapi;

/**
 *	Generated from IDL definition of struct "TpStructuredAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpStructuredAttributeValueHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpStructuredAttributeValue value;

	public TpStructuredAttributeValueHolder ()
	{
	}
	public TpStructuredAttributeValueHolder(final org.csapi.TpStructuredAttributeValue initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.TpStructuredAttributeValueHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.TpStructuredAttributeValueHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.TpStructuredAttributeValueHelper.write(_out, value);
	}
}
