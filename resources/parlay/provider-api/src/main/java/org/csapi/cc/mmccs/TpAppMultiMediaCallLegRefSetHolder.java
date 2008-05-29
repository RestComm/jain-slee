package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of alias "TpAppMultiMediaCallLegRefSet"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallLegRefSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cc.mmccs.IpAppMultiMediaCallLeg[] value;

	public TpAppMultiMediaCallLegRefSetHolder ()
	{
	}
	public TpAppMultiMediaCallLegRefSetHolder (final org.csapi.cc.mmccs.IpAppMultiMediaCallLeg[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TpAppMultiMediaCallLegRefSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TpAppMultiMediaCallLegRefSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TpAppMultiMediaCallLegRefSetHelper.write (out,value);
	}
}
