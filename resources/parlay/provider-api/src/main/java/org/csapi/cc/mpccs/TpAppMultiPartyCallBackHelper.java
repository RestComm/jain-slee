package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of union "TpAppMultiPartyCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiPartyCallBackHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mpccs.TpAppMultiPartyCallBack s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mpccs.TpAppMultiPartyCallBack extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/TpAppMultiPartyCallBack:1.0";
	}
	public static TpAppMultiPartyCallBack read (org.omg.CORBA.portable.InputStream in)
	{
		TpAppMultiPartyCallBack result = new TpAppMultiPartyCallBack ();
		org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType disc = org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType._P_APP_MULTIPARTY_CALL_CALLBACK:
			{
				org.csapi.cc.mpccs.IpAppMultiPartyCall _var;
				_var=org.csapi.cc.mpccs.IpAppMultiPartyCallHelper.read(in);
				result.AppMultiPartyCall (_var);
				break;
			}
			case org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType._P_APP_CALL_LEG_CALLBACK:
			{
				org.csapi.cc.mpccs.IpAppCallLeg _var;
				_var=org.csapi.cc.mpccs.IpAppCallLegHelper.read(in);
				result.AppCallLeg (_var);
				break;
			}
			case org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType._P_APP_CALL_AND_CALL_LEG_CALLBACK:
			{
				org.csapi.cc.mpccs.TpAppCallLegCallBack _var;
				_var=org.csapi.cc.mpccs.TpAppCallLegCallBackHelper.read(in);
				result.AppMultiPartyCallAndCallLeg (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpAppMultiPartyCallBack s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType._P_APP_MULTIPARTY_CALL_CALLBACK:
			{
				org.csapi.cc.mpccs.IpAppMultiPartyCallHelper.write(out,s.AppMultiPartyCall ());
				break;
			}
			case org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType._P_APP_CALL_LEG_CALLBACK:
			{
				org.csapi.cc.mpccs.IpAppCallLegHelper.write(out,s.AppCallLeg ());
				break;
			}
			case org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType._P_APP_CALL_AND_CALL_LEG_CALLBACK:
			{
				org.csapi.cc.mpccs.TpAppCallLegCallBackHelper.write(out,s.AppMultiPartyCallAndCallLeg ());
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
			org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefTypeHelper.insert(label_any, org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_MULTIPARTY_CALL_CALLBACK);
			members[3] = new org.omg.CORBA.UnionMember ("AppMultiPartyCall", label_any, org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mpccs/IpAppMultiPartyCall:1.0", "IpAppMultiPartyCall"),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefTypeHelper.insert(label_any, org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALL_LEG_CALLBACK);
			members[2] = new org.omg.CORBA.UnionMember ("AppCallLeg", label_any, org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mpccs/IpAppCallLeg:1.0", "IpAppCallLeg"),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefTypeHelper.insert(label_any, org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefType.P_APP_CALL_AND_CALL_LEG_CALLBACK);
			members[1] = new org.omg.CORBA.UnionMember ("AppMultiPartyCallAndCallLeg", label_any, org.csapi.cc.mpccs.TpAppCallLegCallBackHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpAppMultiPartyCallBack",org.csapi.cc.mpccs.TpAppMultiPartyCallBackRefTypeHelper.type(), members);
		}
		return _type;
	}
}
