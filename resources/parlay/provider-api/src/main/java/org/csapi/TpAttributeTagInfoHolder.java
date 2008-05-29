package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAttributeTagInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeTagInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpAttributeTagInfo value;

	public TpAttributeTagInfoHolder ()
	{
	}
	public TpAttributeTagInfoHolder (final TpAttributeTagInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAttributeTagInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAttributeTagInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAttributeTagInfoHelper.write (out,value);
	}
}
