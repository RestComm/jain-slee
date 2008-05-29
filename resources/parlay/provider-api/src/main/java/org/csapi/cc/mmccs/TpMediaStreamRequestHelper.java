package org.csapi.cc.mmccs;


/**
 *	Generated from IDL definition of struct "TpMediaStreamRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamRequestHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mmccs.TpMediaStreamRequestHelper.id(),"TpMediaStreamRequest",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Direction", org.csapi.cc.mmccs.TpMediaStreamDirectionHelper.type(), null),new org.omg.CORBA.StructMember("DataTypeRequest", org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestHelper.type(), null),new org.omg.CORBA.StructMember("MediaMonitorMode", org.csapi.cc.TpCallMonitorModeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMediaStreamRequest s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMediaStreamRequest extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaStreamRequest:1.0";
	}
	public static org.csapi.cc.mmccs.TpMediaStreamRequest read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mmccs.TpMediaStreamRequest result = new org.csapi.cc.mmccs.TpMediaStreamRequest();
		result.Direction=org.csapi.cc.mmccs.TpMediaStreamDirectionHelper.read(in);
		result.DataTypeRequest=org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestHelper.read(in);
		result.MediaMonitorMode=org.csapi.cc.TpCallMonitorModeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mmccs.TpMediaStreamRequest s)
	{
		org.csapi.cc.mmccs.TpMediaStreamDirectionHelper.write(out,s.Direction);
		org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestHelper.write(out,s.DataTypeRequest);
		org.csapi.cc.TpCallMonitorModeHelper.write(out,s.MediaMonitorMode);
	}
}
