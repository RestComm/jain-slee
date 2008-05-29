package org.csapi.gms;


/**
 *	Generated from IDL definition of struct "TpGMSNewMessageArrivedInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpGMSNewMessageArrivedInfoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.gms.TpGMSNewMessageArrivedInfoHelper.id(),"TpGMSNewMessageArrivedInfo",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MailboxID", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("FolderID", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("MessageID", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("NumberOfProperties", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpGMSNewMessageArrivedInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpGMSNewMessageArrivedInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpGMSNewMessageArrivedInfo:1.0";
	}
	public static org.csapi.gms.TpGMSNewMessageArrivedInfo read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.gms.TpGMSNewMessageArrivedInfo result = new org.csapi.gms.TpGMSNewMessageArrivedInfo();
		result.MailboxID=org.csapi.TpAddressHelper.read(in);
		result.FolderID=in.read_string();
		result.MessageID=in.read_string();
		result.NumberOfProperties=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.gms.TpGMSNewMessageArrivedInfo s)
	{
		org.csapi.TpAddressHelper.write(out,s.MailboxID);
		out.write_string(s.FolderID);
		out.write_string(s.MessageID);
		out.write_long(s.NumberOfProperties);
	}
}
