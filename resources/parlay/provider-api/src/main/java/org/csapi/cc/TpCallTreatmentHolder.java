package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallTreatment"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTreatmentHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.TpCallTreatment value;

	public TpCallTreatmentHolder ()
	{
	}
	public TpCallTreatmentHolder(final org.csapi.cc.TpCallTreatment initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.TpCallTreatmentHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.TpCallTreatmentHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.TpCallTreatmentHelper.write(_out, value);
	}
}
