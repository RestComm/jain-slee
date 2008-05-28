package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallTreatmentType"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTreatmentTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallTreatmentType value;

	public TpCallTreatmentTypeHolder ()
	{
	}
	public TpCallTreatmentTypeHolder (final TpCallTreatmentType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallTreatmentTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallTreatmentTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallTreatmentTypeHelper.write (out,value);
	}
}
