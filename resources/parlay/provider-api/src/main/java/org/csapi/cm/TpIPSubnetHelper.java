package org.csapi.cm;


/**
 *	Generated from IDL definition of struct "TpIPSubnet"
 *	@author JacORB IDL compiler 
 */

public final class TpIPSubnetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cm.TpIPSubnetHelper.id(),"TpIPSubnet",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("subnetNumber", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("subnetMask", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("addressType", org.csapi.cm.TpIPv4AddTypeHelper.type(), null),new org.omg.CORBA.StructMember("IPVersionSupport", org.csapi.cm.TpIPVersionHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpIPSubnet s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpIPSubnet extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpIPSubnet:1.0";
	}
	public static org.csapi.cm.TpIPSubnet read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cm.TpIPSubnet result = new org.csapi.cm.TpIPSubnet();
		result.subnetNumber=in.read_string();
		result.subnetMask=in.read_string();
		result.addressType=org.csapi.cm.TpIPv4AddTypeHelper.read(in);
		result.IPVersionSupport=org.csapi.cm.TpIPVersionHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cm.TpIPSubnet s)
	{
		out.write_string(s.subnetNumber);
		out.write_string(s.subnetMask);
		org.csapi.cm.TpIPv4AddTypeHelper.write(out,s.addressType);
		org.csapi.cm.TpIPVersionHelper.write(out,s.IPVersionSupport);
	}
}
