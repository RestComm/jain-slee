package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpUserLocationEmergencyTrigger"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationEmergencyTriggerHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpUserLocationEmergencyTrigger value;

	public TpUserLocationEmergencyTriggerHolder ()
	{
	}
	public TpUserLocationEmergencyTriggerHolder (final TpUserLocationEmergencyTrigger initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpUserLocationEmergencyTriggerHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpUserLocationEmergencyTriggerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpUserLocationEmergencyTriggerHelper.write (out,value);
	}
}
