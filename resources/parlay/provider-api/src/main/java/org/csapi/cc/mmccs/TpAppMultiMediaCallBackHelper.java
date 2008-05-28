package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of union "TpAppMultiMediaCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallBackHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpAppMultiMediaCallBack s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpAppMultiMediaCallBack extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpAppMultiMediaCallBack:1.0";
	}
	public static TpAppMultiMediaCallBack read (org.omg.CORBA.portable.InputStream in)
	{
		TpAppMultiMediaCallBack result = new TpAppMultiMediaCallBack ();
		org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType disc = org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType._P_APP_MULTIMEDIA_CALL_CALLBACK:
			{
				org.csapi.cc.mmccs.IpAppMultiMediaCall _var;
				_var=org.csapi.cc.mmccs.IpAppMultiMediaCallHelper.read(in);
				result.AppMultiMediaCall (_var);
				break;
			}
			case org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType._P_APP_CALL_LEG_CALLBACK:
			{
				org.csapi.cc.mmccs.IpAppMultiMediaCallLeg _var;
				_var=org.csapi.cc.mmccs.IpAppMultiMediaCallLegHelper.read(in);
				result.AppMultiMediaCallLeg (_var);
				break;
			}
			case org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType._P_APP_CALL_AND_CALL_LEG_CALLBACK:
			{
				org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack _var;
				_var=org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBackHelper.read(in);
				result.AppMultiMediaCallAndCallLeg (_var);
				break;
			}
			default:
			{
				short _var;
				_var=in.read_short();
				result.Dummy (_var);
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpAppMultiMediaCallBack s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType._P_APP_MULTIMEDIA_CALL_CALLBACK:
			{
				org.csapi.cc.mmccs.IpAppMultiMediaCallHelper.write(out,s.AppMultiMediaCall ());
				break;
			}
			case org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType._P_APP_CALL_LEG_CALLBACK:
			{
				org.csapi.cc.mmccs.IpAppMultiMediaCallLegHelper.write(out,s.AppMultiMediaCallLeg ());
				break;
			}
			case org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType._P_APP_CALL_AND_CALL_LEG_CALLBACK:
			{
				org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBackHelper.write(out,s.AppMultiMediaCallAndCallLeg ());
				break;
			}
			default:
			{
				out.write_short(s.Dummy ());
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[4];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefTypeHelper.insert(label_any, org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_MULTIMEDIA_CALL_CALLBACK);
			members[3] = new org.omg.CORBA.UnionMember ("AppMultiMediaCall", label_any, org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mmccs/IpAppMultiMediaCall:1.0", "IpAppMultiMediaCall"),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefTypeHelper.insert(label_any, org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALL_LEG_CALLBACK);
			members[2] = new org.omg.CORBA.UnionMember ("AppMultiMediaCallLeg", label_any, org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mmccs/IpAppMultiMediaCallLeg:1.0", "IpAppMultiMediaCallLeg"),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefTypeHelper.insert(label_any, org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefType.P_APP_CALL_AND_CALL_LEG_CALLBACK);
			members[1] = new org.omg.CORBA.UnionMember ("AppMultiMediaCallAndCallLeg", label_any, org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBackHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpAppMultiMediaCallBack",org.csapi.cc.mmccs.TpAppMultiMediaCallBackRefTypeHelper.type(), members);
		}
		return _type;
	}
}
