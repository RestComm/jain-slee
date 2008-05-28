package org.csapi.cc.gccs;


/**
 *	Generated from IDL definition of struct "TpCallTreatment"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTreatmentHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.gccs.TpCallTreatmentHelper.id(),"TpCallTreatment",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CallTreatmentType", org.csapi.cc.TpCallTreatmentTypeHelper.type(), null),new org.omg.CORBA.StructMember("ReleaseCause", org.csapi.cc.gccs.TpCallReleaseCauseHelper.type(), null),new org.omg.CORBA.StructMember("AdditionalTreatmentInfo", org.csapi.cc.TpCallAdditionalTreatmentInfoHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.gccs.TpCallTreatment s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.gccs.TpCallTreatment extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/gccs/TpCallTreatment:1.0";
	}
	public static org.csapi.cc.gccs.TpCallTreatment read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.gccs.TpCallTreatment result = new org.csapi.cc.gccs.TpCallTreatment();
		result.CallTreatmentType=org.csapi.cc.TpCallTreatmentTypeHelper.read(in);
		result.ReleaseCause=org.csapi.cc.gccs.TpCallReleaseCauseHelper.read(in);
		result.AdditionalTreatmentInfo=org.csapi.cc.TpCallAdditionalTreatmentInfoHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.gccs.TpCallTreatment s)
	{
		org.csapi.cc.TpCallTreatmentTypeHelper.write(out,s.CallTreatmentType);
		org.csapi.cc.gccs.TpCallReleaseCauseHelper.write(out,s.ReleaseCause);
		org.csapi.cc.TpCallAdditionalTreatmentInfoHelper.write(out,s.AdditionalTreatmentInfo);
	}
}
