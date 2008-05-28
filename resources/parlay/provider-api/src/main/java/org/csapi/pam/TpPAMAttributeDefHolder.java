package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAttributeDef"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttributeDefHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAttributeDef value;

	public TpPAMAttributeDefHolder ()
	{
	}
	public TpPAMAttributeDefHolder(final org.csapi.pam.TpPAMAttributeDef initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAttributeDefHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAttributeDefHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAttributeDefHelper.write(_out, value);
	}
}
