package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of alias "TpAppCallLegRefSet"
 *	@author JacORB IDL compiler 
 */

public final class TpAppCallLegRefSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mpccs.IpAppCallLeg[] value;

	public TpAppCallLegRefSetHolder ()
	{
	}
	public TpAppCallLegRefSetHolder (final org.csapi.cc.mpccs.IpAppCallLeg[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppCallLegRefSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppCallLegRefSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppCallLegRefSetHelper.write (out,value);
	}
}
