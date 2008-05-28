package org.csapi.cc.cccs;


/**
 *	Generated from IDL definition of struct "TpMultiMediaConfPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaConfPolicyHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.cccs.TpMultiMediaConfPolicyHelper.id(),"TpMultiMediaConfPolicy",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("JoinAllowed", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("MediaAllowed", org.csapi.cc.TpMediaTypeHelper.type(), null),new org.omg.CORBA.StructMember("Chaired", org.csapi.TpBooleanHelper.type(), null),new org.omg.CORBA.StructMember("VideoHandling", org.csapi.cc.cccs.TpVideoHandlingTypeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpMultiMediaConfPolicy s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpMultiMediaConfPolicy extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpMultiMediaConfPolicy:1.0";
	}
	public static org.csapi.cc.cccs.TpMultiMediaConfPolicy read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.cccs.TpMultiMediaConfPolicy result = new org.csapi.cc.cccs.TpMultiMediaConfPolicy();
		result.JoinAllowed=in.read_boolean();
		result.MediaAllowed=in.read_long();
		result.Chaired=in.read_boolean();
		result.VideoHandling=org.csapi.cc.cccs.TpVideoHandlingTypeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.cccs.TpMultiMediaConfPolicy s)
	{
		out.write_boolean(s.JoinAllowed);
		out.write_long(s.MediaAllowed);
		out.write_boolean(s.Chaired);
		org.csapi.cc.cccs.TpVideoHandlingTypeHelper.write(out,s.VideoHandling);
	}
}
