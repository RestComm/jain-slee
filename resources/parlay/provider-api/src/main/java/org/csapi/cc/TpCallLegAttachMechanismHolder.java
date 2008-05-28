package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallLegAttachMechanism"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegAttachMechanismHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpCallLegAttachMechanism value;

	public TpCallLegAttachMechanismHolder ()
	{
	}
	public TpCallLegAttachMechanismHolder (final TpCallLegAttachMechanism initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpCallLegAttachMechanismHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpCallLegAttachMechanismHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpCallLegAttachMechanismHelper.write (out,value);
	}
}
