package org.csapi.cc.cccs;


/**
 *	Generated from IDL definition of struct "TpJoinEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpJoinEventInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.cccs.TpJoinEventInfoHelper.id(),"TpJoinEventInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DestinationAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("OriginatingAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("CallAppInfo", org.csapi.cc.TpCallAppInfoSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpJoinEventInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpJoinEventInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpJoinEventInfo:1.0";
	}
	public static org.csapi.cc.cccs.TpJoinEventInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.cccs.TpJoinEventInfo result = new org.csapi.cc.cccs.TpJoinEventInfo();
		result.DestinationAddress=org.csapi.TpAddressHelper.read(in);
		result.OriginatingAddress=org.csapi.TpAddressHelper.read(in);
		result.CallAppInfo = org.csapi.cc.TpCallAppInfoSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.cccs.TpJoinEventInfo s)
	{
		org.csapi.TpAddressHelper.write(out,s.DestinationAddress);
		org.csapi.TpAddressHelper.write(out,s.OriginatingAddress);
		org.csapi.cc.TpCallAppInfoSetHelper.write(out,s.CallAppInfo);
	}
}
