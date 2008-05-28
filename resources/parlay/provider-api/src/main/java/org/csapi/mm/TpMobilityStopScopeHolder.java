package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpMobilityStopScope"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityStopScopeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TpMobilityStopScope value;

	public TpMobilityStopScopeHolder ()
	{
	}
	public TpMobilityStopScopeHolder (final TpMobilityStopScope initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpMobilityStopScopeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpMobilityStopScopeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpMobilityStopScopeHelper.write (out,value);
	}
}
