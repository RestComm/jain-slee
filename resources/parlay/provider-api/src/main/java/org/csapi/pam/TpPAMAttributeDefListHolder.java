package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMAttributeDefList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttributeDefListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAttributeDef[] value;

	public TpPAMAttributeDefListHolder ()
	{
	}
	public TpPAMAttributeDefListHolder (final org.csapi.pam.TpPAMAttributeDef[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMAttributeDefListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMAttributeDefListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMAttributeDefListHelper.write (out,value);
	}
}
