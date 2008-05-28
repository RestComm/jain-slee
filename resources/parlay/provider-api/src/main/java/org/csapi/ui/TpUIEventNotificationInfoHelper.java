package org.csapi.ui;


/**
 *	Generated from IDL definition of struct "TpUIEventNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIEventNotificationInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.ui.TpUIEventNotificationInfoHelper.id(),"TpUIEventNotificationInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("OriginatingAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("DestinationAddress", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("ServiceCode", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("DataTypeIndication", org.csapi.ui.TpUIEventInfoDataTypeHelper.type(), null),new org.omg.CORBA.StructMember("UIEventData", org.csapi.TpOctetSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIEventNotificationInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIEventNotificationInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIEventNotificationInfo:1.0";
	}
	public static org.csapi.ui.TpUIEventNotificationInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.ui.TpUIEventNotificationInfo result = new org.csapi.ui.TpUIEventNotificationInfo();
		result.OriginatingAddress=org.csapi.TpAddressHelper.read(in);
		result.DestinationAddress=org.csapi.TpAddressHelper.read(in);
		result.ServiceCode=in.read_string();
		result.DataTypeIndication=org.csapi.ui.TpUIEventInfoDataTypeHelper.read(in);
		result.UIEventData = org.csapi.TpOctetSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.ui.TpUIEventNotificationInfo s)
	{
		org.csapi.TpAddressHelper.write(out,s.OriginatingAddress);
		org.csapi.TpAddressHelper.write(out,s.DestinationAddress);
		out.write_string(s.ServiceCode);
		org.csapi.ui.TpUIEventInfoDataTypeHelper.write(out,s.DataTypeIndication);
		org.csapi.TpOctetSetHelper.write(out,s.UIEventData);
	}
}
