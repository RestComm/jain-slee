package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpFolderInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpFolderInfoPropertyNameHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpFolderInfoPropertyName value;

	public TpFolderInfoPropertyNameHolder ()
	{
	}
	public TpFolderInfoPropertyNameHolder (final TpFolderInfoPropertyName initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFolderInfoPropertyNameHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFolderInfoPropertyNameHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFolderInfoPropertyNameHelper.write (out,value);
	}
}
