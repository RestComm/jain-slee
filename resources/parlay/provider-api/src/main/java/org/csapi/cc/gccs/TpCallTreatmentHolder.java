package org.csapi.cc.gccs;

/**
 *	Generated from IDL definition of struct "TpCallTreatment"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTreatmentHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.gccs.TpCallTreatment value;

	public TpCallTreatmentHolder ()
	{
	}
	public TpCallTreatmentHolder(final org.csapi.cc.gccs.TpCallTreatment initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cc.gccs.TpCallTreatmentHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cc.gccs.TpCallTreatmentHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cc.gccs.TpCallTreatmentHelper.write(_out, value);
	}
}
