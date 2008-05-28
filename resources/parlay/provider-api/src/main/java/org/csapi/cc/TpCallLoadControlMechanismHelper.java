package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallLoadControlMechanism"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLoadControlMechanismHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallLoadControlMechanism s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallLoadControlMechanism extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallLoadControlMechanism:1.0";
	}
	public static TpCallLoadControlMechanism read (org.omg.CORBA.portable.InputStream in)
	{
		TpCallLoadControlMechanism result = new TpCallLoadControlMechanism ();
		org.csapi.cc.TpCallLoadControlMechanismType disc = org.csapi.cc.TpCallLoadControlMechanismType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.TpCallLoadControlMechanismType._P_CALL_LOAD_CONTROL_PER_INTERVAL:
			{
				int _var;
				_var=in.read_long();
				result.CallLoadControlPerInterval (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpCallLoadControlMechanism s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.TpCallLoadControlMechanismType._P_CALL_LOAD_CONTROL_PER_INTERVAL:
			{
				out.write_long(s.CallLoadControlPerInterval ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[1];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallLoadControlMechanismTypeHelper.insert(label_any, org.csapi.cc.TpCallLoadControlMechanismType.P_CALL_LOAD_CONTROL_PER_INTERVAL);
			members[0] = new org.omg.CORBA.UnionMember ("CallLoadControlPerInterval", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpCallLoadControlMechanism",org.csapi.cc.TpCallLoadControlMechanismTypeHelper.type(), members);
		}
		return _type;
	}
}
