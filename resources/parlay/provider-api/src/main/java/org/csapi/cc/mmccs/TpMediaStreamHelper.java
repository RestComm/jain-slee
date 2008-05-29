package org.csapi.cc.mmccs;


/**
 *	Generated from IDL definition of struct "TpMediaStream"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mmccs.TpMediaStreamHelper.id(),"TpMediaStream",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Direction", org.csapi.cc.mmccs.TpMediaStreamDirectionHelper.type(), null),new org.omg.CORBA.StructMember("DataType", org.csapi.cc.mmccs.TpMediaStreamDataTypeHelper.type(), null),new org.omg.CORBA.StructMember("ChannelSessionID", org.csapi.TpSessionIDHelper.type(), null),new org.omg.CORBA.StructMember("MediaStream", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mmccs/IpMultiMediaStream:1.0", "IpMultiMediaStream"), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMediaStream s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMediaStream extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaStream:1.0";
	}
	public static org.csapi.cc.mmccs.TpMediaStream read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mmccs.TpMediaStream result = new org.csapi.cc.mmccs.TpMediaStream();
		result.Direction=org.csapi.cc.mmccs.TpMediaStreamDirectionHelper.read(in);
		result.DataType=org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestHelper.read(in);
		result.ChannelSessionID=in.read_long();
		result.MediaStream=org.csapi.cc.mmccs.IpMultiMediaStreamHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mmccs.TpMediaStream s)
	{
		org.csapi.cc.mmccs.TpMediaStreamDirectionHelper.write(out,s.Direction);
		org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestHelper.write(out,s.DataType);
		out.write_long(s.ChannelSessionID);
		org.csapi.cc.mmccs.IpMultiMediaStreamHelper.write(out,s.MediaStream);
	}
}
