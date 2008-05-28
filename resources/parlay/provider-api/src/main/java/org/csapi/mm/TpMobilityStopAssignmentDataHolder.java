package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpMobilityStopAssignmentData"
 *	@author JacORB IDL compiler 
 */

public final class TpMobilityStopAssignmentDataHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.mm.TpMobilityStopAssignmentData value;

	public TpMobilityStopAssignmentDataHolder ()
	{
	}
	public TpMobilityStopAssignmentDataHolder(final org.csapi.mm.TpMobilityStopAssignmentData initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.mm.TpMobilityStopAssignmentDataHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.mm.TpMobilityStopAssignmentDataHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.mm.TpMobilityStopAssignmentDataHelper.write(_out, value);
	}
}
