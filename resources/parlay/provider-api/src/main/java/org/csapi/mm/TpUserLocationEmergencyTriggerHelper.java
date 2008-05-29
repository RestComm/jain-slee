package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpUserLocationEmergencyTrigger"
 *	@author JacORB IDL compiler 
 */

public final class TpUserLocationEmergencyTriggerHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.mm.TpUserLocationEmergencyTriggerHelper.id(),"TpUserLocationEmergencyTrigger",new String[]{"P_ULE_CALL_ORIGINATION","P_ULE_CALL_RELEASE","P_ULE_LOCATION_REQUEST"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.mm.TpUserLocationEmergencyTrigger s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.mm.TpUserLocationEmergencyTrigger extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/mm/TpUserLocationEmergencyTrigger:1.0";
	}
	public static TpUserLocationEmergencyTrigger read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpUserLocationEmergencyTrigger.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpUserLocationEmergencyTrigger s)
	{
		out.write_long(s.value());
	}
}
