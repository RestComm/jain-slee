package org.csapi.cc.cccs;


/**
 *	Generated from IDL definition of struct "TpMonoMediaConfPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpMonoMediaConfPolicyHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.cccs.TpMonoMediaConfPolicyHelper.id(),"TpMonoMediaConfPolicy",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("JoinAllowed", org.csapi.TpBooleanHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.cccs.TpMonoMediaConfPolicy s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.cccs.TpMonoMediaConfPolicy extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/cccs/TpMonoMediaConfPolicy:1.0";
	}
	public static org.csapi.cc.cccs.TpMonoMediaConfPolicy read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.cccs.TpMonoMediaConfPolicy result = new org.csapi.cc.cccs.TpMonoMediaConfPolicy();
		result.JoinAllowed=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.cccs.TpMonoMediaConfPolicy s)
	{
		out.write_boolean(s.JoinAllowed);
	}
}
