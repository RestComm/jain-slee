package org.csapi.gms;

/**
 *	Generated from IDL definition of alias "TpFolderInfoPropertySet"
 *	@author JacORB IDL compiler 
 */

public final class TpFolderInfoPropertySetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.gms.TpFolderInfoProperty[] value;

	public TpFolderInfoPropertySetHolder ()
	{
	}
	public TpFolderInfoPropertySetHolder (final org.csapi.gms.TpFolderInfoProperty[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpFolderInfoPropertySetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpFolderInfoPropertySetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpFolderInfoPropertySetHelper.write (out,value);
	}
}
