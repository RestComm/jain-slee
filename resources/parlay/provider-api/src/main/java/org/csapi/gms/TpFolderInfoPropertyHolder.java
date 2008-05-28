package org.csapi.gms;
/**
 *	Generated from IDL definition of union "TpFolderInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpFolderInfoPropertyHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpFolderInfoProperty value;

	public TpFolderInfoPropertyHolder ()
	{
	}
	public TpFolderInfoPropertyHolder (final TpFolderInfoProperty initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFolderInfoPropertyHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFolderInfoPropertyHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFolderInfoPropertyHelper.write (out, value);
	}
}
