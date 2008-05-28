package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of union "TpMediaStreamDataTypeRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDataTypeRequestHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaStreamDataTypeRequest:1.0";
	}
	public static TpMediaStreamDataTypeRequest read (org.omg.CORBA.portable.InputStream in)
	{
		TpMediaStreamDataTypeRequest result = new TpMediaStreamDataTypeRequest ();
		org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType disc = org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType._P_AUDIO_CAPABILITIES:
			{
				int _var;
				_var=in.read_long();
				result.Audio (_var);
				break;
			}
			case org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType._P_VIDEO_CAPABILITIES:
			{
				int _var;
				_var=in.read_long();
				result.Video (_var);
				break;
			}
			case org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType._P_DATA_CAPABILITIES:
			{
				int _var;
				_var=in.read_long();
				result.Data (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpMediaStreamDataTypeRequest s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType._P_AUDIO_CAPABILITIES:
			{
				out.write_long(s.Audio ());
				break;
			}
			case org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType._P_VIDEO_CAPABILITIES:
			{
				out.write_long(s.Video ());
				break;
			}
			case org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType._P_DATA_CAPABILITIES:
			{
				out.write_long(s.Data ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[3];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestTypeHelper.insert(label_any, org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_AUDIO_CAPABILITIES);
			members[2] = new org.omg.CORBA.UnionMember ("Audio", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestTypeHelper.insert(label_any, org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_VIDEO_CAPABILITIES);
			members[1] = new org.omg.CORBA.UnionMember ("Video", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestTypeHelper.insert(label_any, org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestType.P_DATA_CAPABILITIES);
			members[0] = new org.omg.CORBA.UnionMember ("Data", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpMediaStreamDataTypeRequest",org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestTypeHelper.type(), members);
		}
		return _type;
	}
}
