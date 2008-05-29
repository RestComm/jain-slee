package org.csapi.cs;

/**
 *	Generated from IDL definition of union "TpChargingParameterValue"
 *	@author JacORB IDL compiler 
 */

public final class TpChargingParameterValueHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cs.TpChargingParameterValue s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cs.TpChargingParameterValue extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpChargingParameterValue:1.0";
	}
	public static TpChargingParameterValue read (org.omg.CORBA.portable.InputStream in)
	{
		TpChargingParameterValue result = new TpChargingParameterValue ();
		org.csapi.cs.TpChargingParameterValueType disc = org.csapi.cs.TpChargingParameterValueType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_INT32:
			{
				int _var;
				_var=in.read_long();
				result.IntValue (_var);
				break;
			}
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_FLOAT:
			{
				float _var;
				_var=in.read_float();
				result.FloatValue (_var);
				break;
			}
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_STRING:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.StringValue (_var);
				break;
			}
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_BOOLEAN:
			{
				boolean _var;
				_var=in.read_boolean();
				result.BooleanValue (_var);
				break;
			}
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_OCTETSET:
			{
				byte[] _var;
				_var = org.csapi.TpOctetSetHelper.read(in);
				result.OctetValue (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpChargingParameterValue s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_INT32:
			{
				out.write_long(s.IntValue ());
				break;
			}
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_FLOAT:
			{
				out.write_float(s.FloatValue ());
				break;
			}
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_STRING:
			{
				out.write_string(s.StringValue ());
				break;
			}
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_BOOLEAN:
			{
				out.write_boolean(s.BooleanValue ());
				break;
			}
			case org.csapi.cs.TpChargingParameterValueType._P_CHS_PARAMETER_OCTETSET:
			{
				org.csapi.TpOctetSetHelper.write(out,s.OctetValue ());
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
			org.csapi.cs.TpChargingParameterValueTypeHelper.insert(label_any, org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_INT32);
			members[4] = new org.omg.CORBA.UnionMember ("IntValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cs.TpChargingParameterValueTypeHelper.insert(label_any, org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_FLOAT);
			members[3] = new org.omg.CORBA.UnionMember ("FloatValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(6)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cs.TpChargingParameterValueTypeHelper.insert(label_any, org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_STRING);
			members[2] = new org.omg.CORBA.UnionMember ("StringValue", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cs.TpChargingParameterValueTypeHelper.insert(label_any, org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_BOOLEAN);
			members[1] = new org.omg.CORBA.UnionMember ("BooleanValue", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(8)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cs.TpChargingParameterValueTypeHelper.insert(label_any, org.csapi.cs.TpChargingParameterValueType.P_CHS_PARAMETER_OCTETSET);
			members[0] = new org.omg.CORBA.UnionMember ("OctetValue", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.TpOctetHelper.type()),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpChargingParameterValue",org.csapi.cs.TpChargingParameterValueTypeHelper.type(), members);
		}
		return _type;
	}
}
