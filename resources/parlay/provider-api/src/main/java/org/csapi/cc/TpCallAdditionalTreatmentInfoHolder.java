package org.csapi.cc;
/**
 *	Generated from IDL definition of union "TpCallAdditionalTreatmentInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalTreatmentInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallAdditionalTreatmentInfo value;

	public TpCallAdditionalTreatmentInfoHolder ()
	{
	}
	public TpCallAdditionalTreatmentInfoHolder (final TpCallAdditionalTreatmentInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallAdditionalTreatmentInfoHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallAdditionalTreatmentInfoHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallAdditionalTreatmentInfoHelper.write (out, value);
	}
}
