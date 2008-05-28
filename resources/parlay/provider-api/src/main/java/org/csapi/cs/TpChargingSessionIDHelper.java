package org.csapi.cs;


/**
 *	Generated from IDL definition of struct "TpChargingSessionID"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingSessionIDHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cs.TpChargingSessionIDHelper.id(),"TpChargingSessionID",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("ChargingSessionReference", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cs/IpChargingSession:1.0", "IpChargingSession"), null),new org.omg.CORBA.StructMember("ChargingSessionID", org.csapi.TpSessionIDHelper.type(), null),new org.omg.CORBA.StructMember("RequestNumberFirstRequest", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpChargingSessionID s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpChargingSessionID extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpChargingSessionID:1.0";
	}
	public static org.csapi.cs.TpChargingSessionID read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cs.TpChargingSessionID result = new org.csapi.cs.TpChargingSessionID();
		result.ChargingSessionReference=org.csapi.cs.IpChargingSessionHelper.read(in);
		result.ChargingSessionID=in.read_long();
		result.RequestNumberFirstRequest=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cs.TpChargingSessionID s)
	{
		org.csapi.cs.IpChargingSessionHelper.write(out,s.ChargingSessionReference);
		out.write_long(s.ChargingSessionID);
		out.write_long(s.RequestNumberFirstRequest);
	}
}
