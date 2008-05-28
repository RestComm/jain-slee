package org.csapi.pam;
/**
 *	Generated from IDL definition of union "TpPAMContextData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMContextDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpPAMContextData value;

	public TpPAMContextDataHolder ()
	{
	}
	public TpPAMContextDataHolder (final TpPAMContextData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpPAMContextDataHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpPAMContextDataHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpPAMContextDataHelper.write (out, value);
	}
}
