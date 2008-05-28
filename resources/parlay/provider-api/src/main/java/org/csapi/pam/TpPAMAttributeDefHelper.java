package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMAttributeDef"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMAttributeDefHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMAttributeDefHelper.id(),"TpPAMAttributeDef",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Name", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("Type", org.csapi.TpStringHelper.type(), null),new org.omg.CORBA.StructMember("IsStatic", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("IsRevertOnExpiration", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("DefaultValues", org.csapi.TpAnyHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMAttributeDef s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMAttributeDef extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMAttributeDef:1.0";
	}
	public static org.csapi.pam.TpPAMAttributeDef read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMAttributeDef result = new org.csapi.pam.TpPAMAttributeDef();
		result.Name=in.read_string();
		result.Type=in.read_string();
		result.IsStatic=in.read_boolean();
		result.IsRevertOnExpiration=in.read_boolean();
		result.DefaultValues=in.read_any();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMAttributeDef s)
	{
		out.write_string(s.Name);
		out.write_string(s.Type);
		out.write_boolean(s.IsStatic);
		out.write_boolean(s.IsRevertOnExpiration);
		out.write_any(s.DefaultValues);
	}
}
