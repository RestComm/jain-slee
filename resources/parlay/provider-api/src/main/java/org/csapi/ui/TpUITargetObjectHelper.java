package org.csapi.ui;

/**
 *	Generated from IDL definition of union "TpUITargetObject"
 *	@author JacORB IDL compiler 
 */

public final class TpUITargetObjectHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUITargetObject s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUITargetObject extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUITargetObject:1.0";
	}
	public static TpUITargetObject read (org.omg.CORBA.portable.InputStream in)
	{
		TpUITargetObject result = new TpUITargetObject ();
		org.csapi.ui.TpUITargetObjectType disc = org.csapi.ui.TpUITargetObjectType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.ui.TpUITargetObjectType._P_UI_TARGET_OBJECT_CALL:
			{
				org.csapi.cc.gccs.TpCallIdentifier _var;
				_var=org.csapi.cc.gccs.TpCallIdentifierHelper.read(in);
				result.Call (_var);
				break;
			}
			case org.csapi.ui.TpUITargetObjectType._P_UI_TARGET_OBJECT_MULTI_PARTY_CALL:
			{
				org.csapi.cc.mpccs.TpMultiPartyCallIdentifier _var;
				_var=org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.read(in);
				result.MultiPartyCall (_var);
				break;
			}
			case org.csapi.ui.TpUITargetObjectType._P_UI_TARGET_OBJECT_CALL_LEG:
			{
				org.csapi.cc.mpccs.TpCallLegIdentifier _var;
				_var=org.csapi.cc.mpccs.TpCallLegIdentifierHelper.read(in);
				result.CallLeg (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpUITargetObject s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.ui.TpUITargetObjectType._P_UI_TARGET_OBJECT_CALL:
			{
				org.csapi.cc.gccs.TpCallIdentifierHelper.write(out,s.Call ());
				break;
			}
			case org.csapi.ui.TpUITargetObjectType._P_UI_TARGET_OBJECT_MULTI_PARTY_CALL:
			{
				org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.write(out,s.MultiPartyCall ());
				break;
			}
			case org.csapi.ui.TpUITargetObjectType._P_UI_TARGET_OBJECT_CALL_LEG:
			{
				org.csapi.cc.mpccs.TpCallLegIdentifierHelper.write(out,s.CallLeg ());
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
			org.csapi.ui.TpUITargetObjectTypeHelper.insert(label_any, org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL);
			members[2] = new org.omg.CORBA.UnionMember ("Call", label_any, org.csapi.cc.gccs.TpCallIdentifierHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUITargetObjectTypeHelper.insert(label_any, org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_MULTI_PARTY_CALL);
			members[1] = new org.omg.CORBA.UnionMember ("MultiPartyCall", label_any, org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUITargetObjectTypeHelper.insert(label_any, org.csapi.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL_LEG);
			members[0] = new org.omg.CORBA.UnionMember ("CallLeg", label_any, org.csapi.cc.mpccs.TpCallLegIdentifierHelper.type(),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpUITargetObject",org.csapi.ui.TpUITargetObjectTypeHelper.type(), members);
		}
		return _type;
	}
}
