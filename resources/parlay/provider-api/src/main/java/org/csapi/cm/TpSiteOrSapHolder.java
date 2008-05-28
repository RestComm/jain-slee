package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpSiteOrSap"
 *	@author JacORB IDL compiler 
 */

public final class TpSiteOrSapHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpSiteOrSap value;

	public TpSiteOrSapHolder ()
	{
	}
	public TpSiteOrSapHolder (final TpSiteOrSap initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpSiteOrSapHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpSiteOrSapHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpSiteOrSapHelper.write (out,value);
	}
}
