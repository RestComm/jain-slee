package org.csapi.ui;


/**
 *	Generated from IDL definition of struct "TpUICallIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpUICallIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.ui.TpUICallIdentifierHelper.id(),"TpUICallIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UICallRef", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/ui/IpUICall:1.0", "IpUICall"), null),new org.omg.CORBA.StructMember("UserInteractionSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUICallIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUICallIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUICallIdentifier:1.0";
	}
	public static org.csapi.ui.TpUICallIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.ui.TpUICallIdentifier result = new org.csapi.ui.TpUICallIdentifier();
		result.UICallRef=org.csapi.ui.IpUICallHelper.read(in);
		result.UserInteractionSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.ui.TpUICallIdentifier s)
	{
		org.csapi.ui.IpUICallHelper.write(out,s.UICallRef);
		out.write_long(s.UserInteractionSessionID);
	}
}
