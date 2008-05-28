package org.csapi;

/**
 *	Generated from IDL definition of alias "TpAttributeSet"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.TpAttribute[] value;

	public TpAttributeSetHolder ()
	{
	}
	public TpAttributeSetHolder (final org.csapi.TpAttribute[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAttributeSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAttributeSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAttributeSetHelper.write (out,value);
	}
}
