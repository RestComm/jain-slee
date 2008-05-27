package org.csapi.ui;

/**
 *	Generated from IDL definition of union "TpUIVariableInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpUIVariableInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIVariableInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIVariableInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIVariableInfo:1.0";
	}
	public static TpUIVariableInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpUIVariableInfo result = new TpUIVariableInfo ();
		org.csapi.ui.TpUIVariablePartType disc = org.csapi.ui.TpUIVariablePartType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_INT:
			{
				int _var;
				_var=in.read_long();
				result.VariablePartInteger (_var);
				break;
			}
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_ADDRESS:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.VariablePartAddress (_var);
				break;
			}
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_TIME:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.VariablePartTime (_var);
				break;
			}
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_DATE:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.VariablePartDate (_var);
				break;
			}
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_PRICE:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.VariablePartPrice (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpUIVariableInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_INT:
			{
				out.write_long(s.VariablePartInteger ());
				break;
			}
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_ADDRESS:
			{
				out.write_string(s.VariablePartAddress ());
				break;
			}
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_TIME:
			{
				out.write_string(s.VariablePartTime ());
				break;
			}
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_DATE:
			{
				out.write_string(s.VariablePartDate ());
				break;
			}
			case org.csapi.ui.TpUIVariablePartType._P_UI_VARIABLE_PART_PRICE:
			{
				out.write_string(s.VariablePartPrice ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[5];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIVariablePartTypeHelper.insert(label_any, org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_INT);
			members[4] = new org.omg.CORBA.UnionMember ("VariablePartInteger", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIVariablePartTypeHelper.insert(label_any, org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_ADDRESS);
			members[3] = new org.omg.CORBA.UnionMember ("VariablePartAddress", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIVariablePartTypeHelper.insert(label_any, org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_TIME);
			members[2] = new org.omg.CORBA.UnionMember ("VariablePartTime", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIVariablePartTypeHelper.insert(label_any, org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_DATE);
			members[1] = new org.omg.CORBA.UnionMember ("VariablePartDate", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.ui.TpUIVariablePartTypeHelper.insert(label_any, org.csapi.ui.TpUIVariablePartType.P_UI_VARIABLE_PART_PRICE);
			members[0] = new org.omg.CORBA.UnionMember ("VariablePartPrice", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpUIVariableInfo",org.csapi.ui.TpUIVariablePartTypeHelper.type(), members);
		}
		return _type;
	}
}
