package org.csapi.pam;

/**
 *	Generated from IDL definition of alias "TpPAMAttributeList"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttributeListHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAttribute[] value;

	public TpPAMAttributeListHolder ()
	{
	}
	public TpPAMAttributeListHolder (final org.csapi.pam.TpPAMAttribute[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMAttributeListHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMAttributeListHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMAttributeListHelper.write (out,value);
	}
}
