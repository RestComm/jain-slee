package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallLegConnectionProperties"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegConnectionPropertiesHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallLegConnectionPropertiesHelper.id(),"TpCallLegConnectionProperties",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AttachMechanism", org.csapi.cc.TpCallLegAttachMechanismHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallLegConnectionProperties s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallLegConnectionProperties extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallLegConnectionProperties:1.0";
	}
	public static org.csapi.cc.TpCallLegConnectionProperties read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallLegConnectionProperties result = new org.csapi.cc.TpCallLegConnectionProperties();
		result.AttachMechanism=org.csapi.cc.TpCallLegAttachMechanismHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallLegConnectionProperties s)
	{
		org.csapi.cc.TpCallLegAttachMechanismHelper.write(out,s.AttachMechanism);
	}
}
