package org.csapi.ui;

/**
 *	Generated from IDL definition of union "TpUIInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIInfo:1.0";
	}
	public static TpUIInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpUIInfo result = new TpUIInfo ();
		org.csapi.ui.TpUIInfoType disc = org.csapi.ui.TpUIInfoType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_ID:
			{
				int _var;
				_var=in.read_long();
				result.InfoID (_var);
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_DATA:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.InfoData (_var);
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_ADDRESS:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.InfoAddress (_var);
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_BIN_DATA:
			{
				byte[] _var;
				_var = org.csapi.TpOctetSetHelper.read(in);
				result.InfoBinData (_var);
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_UUENCODED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.InfoUUEncData (_var);
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_MIME:
			{
				byte[] _var;
				_var = org.csapi.TpOctetSetHelper.read(in);
				result.InfoMimeData (_var);
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_WAVE:
			{
				byte[] _var;
				_var = org.csapi.TpOctetSetHelper.read(in);
				result.InfoWaveData (_var);
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_AU:
			{
				byte[] _var;
				_var = org.csapi.TpOctetSetHelper.read(in);
				result.InfoAuData (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpUIInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_ID:
			{
				out.write_long(s.InfoID ());
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_DATA:
			{
				out.write_string(s.InfoData ());
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_ADDRESS:
			{
				out.write_string(s.InfoAddress ());
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_BIN_DATA:
			{
				org.csapi.TpOctetSetHelper.write(out,s.InfoBinData ());
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_UUENCODED:
			{
				out.write_string(s.InfoUUEncData ());
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_MIME:
			{
				org.csapi.TpOctetSetHelper.write(out,s.InfoMimeData ());
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_WAVE:
			{
				org.csapi.TpOctetSetHelper.write(out,s.InfoWaveData ());
				break;
			}
			case org.csapi.ui.TpUIInfoType._P_UI_INFO_AU:
			{
				org.csapi.TpOctetSetHelper.write(out,s.InfoAuData ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[8];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIInfoTypeHelper.insert(label_any, org.csapi.ui.TpUIInfoType.P_UI_INFO_ID);
			members[7] = new org.omg.CORBA.UnionMember ("InfoID", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIInfoTypeHelper.insert(label_any, org.csapi.ui.TpUIInfoType.P_UI_INFO_DATA);
			members[6] = new org.omg.CORBA.UnionMember ("InfoData", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIInfoTypeHelper.insert(label_any, org.csapi.ui.TpUIInfoType.P_UI_INFO_ADDRESS);
			members[5] = new org.omg.CORBA.UnionMember ("InfoAddress", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIInfoTypeHelper.insert(label_any, org.csapi.ui.TpUIInfoType.P_UI_INFO_BIN_DATA);
			members[4] = new org.omg.CORBA.UnionMember ("InfoBinData", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.TpOctetHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIInfoTypeHelper.insert(label_any, org.csapi.ui.TpUIInfoType.P_UI_INFO_UUENCODED);
			members[3] = new org.omg.CORBA.UnionMember ("InfoUUEncData", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIInfoTypeHelper.insert(label_any, org.csapi.ui.TpUIInfoType.P_UI_INFO_MIME);
			members[2] = new org.omg.CORBA.UnionMember ("InfoMimeData", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.TpOctetHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIInfoTypeHelper.insert(label_any, org.csapi.ui.TpUIInfoType.P_UI_INFO_WAVE);
			members[1] = new org.omg.CORBA.UnionMember ("InfoWaveData", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.TpOctetHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIInfoTypeHelper.insert(label_any, org.csapi.ui.TpUIInfoType.P_UI_INFO_AU);
			members[0] = new org.omg.CORBA.UnionMember ("InfoAuData", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.TpOctetHelper.type()),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpUIInfo",org.csapi.ui.TpUIInfoTypeHelper.type(), members);
		}
		return _type;
	}
}
