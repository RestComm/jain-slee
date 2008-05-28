package org.csapi.ui;

/**
 *	Generated from IDL definition of exception "P_INVALID_COLLECTION_CRITERIA"
 *	@author JacORB IDL compiler 
 */

public final class P_INVALID_COLLECTION_CRITERIAHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.ui.P_INVALID_COLLECTION_CRITERIA value;

	public P_INVALID_COLLECTION_CRITERIAHolder ()
	{
	}
	public P_INVALID_COLLECTION_CRITERIAHolder(final org.csapi.ui.P_INVALID_COLLECTION_CRITERIA initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.ui.P_INVALID_COLLECTION_CRITERIAHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.ui.P_INVALID_COLLECTION_CRITERIAHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.ui.P_INVALID_COLLECTION_CRITERIAHelper.write(_out, value);
	}
}
