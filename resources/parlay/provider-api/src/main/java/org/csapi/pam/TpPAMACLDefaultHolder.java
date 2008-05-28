package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMACLDefault"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMACLDefaultHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMACLDefault value;

	public TpPAMACLDefaultHolder ()
	{
	}
	public TpPAMACLDefaultHolder (final TpPAMACLDefault initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMACLDefaultHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMACLDefaultHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMACLDefaultHelper.write (out,value);
	}
}
