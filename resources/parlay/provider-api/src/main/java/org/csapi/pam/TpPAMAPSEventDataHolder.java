package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMAPSEventData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAPSEventDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMAPSEventData value;

	public TpPAMAPSEventDataHolder ()
	{
	}
	public TpPAMAPSEventDataHolder(final org.csapi.pam.TpPAMAPSEventData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMAPSEventDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMAPSEventDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMAPSEventDataHelper.write(_out, value);
	}
}
