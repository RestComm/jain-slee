package org.csapi.ui;


/**
 *	Generated from IDL definition of struct "TpUIIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpUIIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.ui.TpUIIdentifierHelper.id(),"TpUIIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UIRef", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/ui/IpUI:1.0", "IpUI"), null),new org.omg.CORBA.StructMember("UserInteractionSessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.ui.TpUIIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.ui.TpUIIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/ui/TpUIIdentifier:1.0";
	}
	public static org.csapi.ui.TpUIIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.ui.TpUIIdentifier result = new org.csapi.ui.TpUIIdentifier();
		result.UIRef=org.csapi.ui.IpUIHelper.read(in);
		result.UserInteractionSessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.ui.TpUIIdentifier s)
	{
		org.csapi.ui.IpUIHelper.write(out,s.UIRef);
		out.write_long(s.UserInteractionSessionID);
	}
}
