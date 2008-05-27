package org.csapi;

/**
 *	Generated from IDL definition of union "TpSimpleAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpSimpleAttributeValueHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.TpSimpleAttributeValue s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.TpSimpleAttributeValue extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/TpSimpleAttributeValue:1.0";
	}
	public static TpSimpleAttributeValue read (org.omg.CORBA.portable.InputStream in)
	{
		TpSimpleAttributeValue result = new TpSimpleAttributeValue ();
		org.csapi.TpSimpleAttributeTypeInfo disc = org.csapi.TpSimpleAttributeTypeInfo.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.TpSimpleAttributeTypeInfo._P_BOOLEAN:
			{
				boolean _var;
				_var=in.read_boolean();
				result.BooleanValue (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_OCTET:
			{
				byte _var;
				_var=in.read_octet();
				result.OctetValue (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_CHAR:
			{
				char _var;
				_var=in.read_char();
				result.CharValue (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_WCHAR:
			{
				char _var;
				_var=in.read_wchar();
				result.WCharValue (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_STRING:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.StringValue (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_WSTRING:
			{
				java.lang.String _var;
				_var=in.read_wstring();
				result.WStringValue (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_INT16:
			{
				short _var;
				_var=in.read_short();
				result.Int16Value (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_UNSIGNED_INT16:
			{
				short _var;
				_var=in.read_ushort();
				result.UnsignedInt16Value (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_INT32:
			{
				int _var;
				_var=in.read_long();
				result.Int32Value (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_UNSIGNED_INT32:
			{
				int _var;
				_var=in.read_ulong();
				result.UnsignedInt32Value (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_INT64:
			{
				long _var;
				_var=in.read_longlong();
				result.Int64Value (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_UNSIGNED_INT64:
			{
				long _var;
				_var=in.read_ulonglong();
				result.UnsignedInt64Value (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_FLOAT:
			{
				float _var;
				_var=in.read_float();
				result.FloatValue (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_DOUBLE:
			{
				double _var;
				_var=in.read_double();
				result.DoubleValue (_var);
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_FIXED:
			{
				float _var;
				_var=in.read_float();
				result.FixedValue (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpSimpleAttributeValue s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.TpSimpleAttributeTypeInfo._P_BOOLEAN:
			{
				out.write_boolean(s.BooleanValue ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_OCTET:
			{
				out.write_octet(s.OctetValue ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_CHAR:
			{
				out.write_char(s.CharValue ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_WCHAR:
			{
				out.write_wchar(s.WCharValue ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_STRING:
			{
				out.write_string(s.StringValue ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_WSTRING:
			{
				out.write_wstring(s.WStringValue ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_INT16:
			{
				out.write_short(s.Int16Value ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_UNSIGNED_INT16:
			{
				out.write_ushort(s.UnsignedInt16Value ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_INT32:
			{
				out.write_long(s.Int32Value ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_UNSIGNED_INT32:
			{
				out.write_ulong(s.UnsignedInt32Value ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_INT64:
			{
				out.write_longlong(s.Int64Value ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_UNSIGNED_INT64:
			{
				out.write_ulonglong(s.UnsignedInt64Value ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_FLOAT:
			{
				out.write_float(s.FloatValue ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_DOUBLE:
			{
				out.write_double(s.DoubleValue ());
				break;
			}
			case org.csapi.TpSimpleAttributeTypeInfo._P_FIXED:
			{
				out.write_float(s.FixedValue ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[15];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_BOOLEAN);
			members[14] = new org.omg.CORBA.UnionMember ("BooleanValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(8)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_OCTET);
			members[13] = new org.omg.CORBA.UnionMember ("OctetValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(10)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_CHAR);
			members[12] = new org.omg.CORBA.UnionMember ("CharValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(9)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_WCHAR);
			members[11] = new org.omg.CORBA.UnionMember ("WCharValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(26)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_STRING);
			members[10] = new org.omg.CORBA.UnionMember ("StringValue", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_WSTRING);
			members[9] = new org.omg.CORBA.UnionMember ("WStringValue", label_any, org.omg.CORBA.ORB.init().create_wstring_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_INT16);
			members[8] = new org.omg.CORBA.UnionMember ("Int16Value", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT16);
			members[7] = new org.omg.CORBA.UnionMember ("UnsignedInt16Value", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(4)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_INT32);
			members[6] = new org.omg.CORBA.UnionMember ("Int32Value", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT32);
			members[5] = new org.omg.CORBA.UnionMember ("UnsignedInt32Value", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(5)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_INT64);
			members[4] = new org.omg.CORBA.UnionMember ("Int64Value", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(23)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_UNSIGNED_INT64);
			members[3] = new org.omg.CORBA.UnionMember ("UnsignedInt64Value", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(24)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_FLOAT);
			members[2] = new org.omg.CORBA.UnionMember ("FloatValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(6)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_DOUBLE);
			members[1] = new org.omg.CORBA.UnionMember ("DoubleValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(7)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.TpSimpleAttributeTypeInfoHelper.insert(label_any, org.csapi.TpSimpleAttributeTypeInfo.P_FIXED);
			members[0] = new org.omg.CORBA.UnionMember ("FixedValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(6)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpSimpleAttributeValue",org.csapi.TpSimpleAttributeTypeInfoHelper.type(), members);
		}
		return _type;
	}
}
