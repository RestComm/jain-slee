package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallAdditionalTreatmentInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalTreatmentInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallAdditionalTreatmentInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallAdditionalTreatmentInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallAdditionalTreatmentInfo:1.0";
	}
	public static TpCallAdditionalTreatmentInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpCallAdditionalTreatmentInfo result = new TpCallAdditionalTreatmentInfo ();
		org.csapi.cc.TpCallTreatmentType disc = org.csapi.cc.TpCallTreatmentType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.TpCallTreatmentType._P_CALL_TREATMENT_SIAR:
			{
				org.csapi.ui.TpUIInfo _var;
				_var=org.csapi.ui.TpUIInfoHelper.read(in);
				result.InformationToSend (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpCallAdditionalTreatmentInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.TpCallTreatmentType._P_CALL_TREATMENT_SIAR:
			{
				org.csapi.ui.TpUIInfoHelper.write(out,s.InformationToSend ());
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
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[2];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallTreatmentTypeHelper.insert(label_any, org.csapi.cc.TpCallTreatmentType.P_CALL_TREATMENT_SIAR);
			members[1] = new org.omg.CORBA.UnionMember ("InformationToSend", label_any, org.csapi.ui.TpUIInfoHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpCallAdditionalTreatmentInfo",org.csapi.cc.TpCallTreatmentTypeHelper.type(), members);
		}
		return _type;
	}
}
