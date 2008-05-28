package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpNotificationRequested"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationRequestedHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpNotificationRequestedHelper.id(),"TpNotificationRequested",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AppCallNotificationRequest", org.csapi.cc.TpCallNotificationRequestHelper.type(), null),new org.omg.CORBA.StructMember("AssignmentID", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpNotificationRequested s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpNotificationRequested extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpNotificationRequested:1.0";
	}
	public static org.csapi.cc.TpNotificationRequested read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpNotificationRequested result = new org.csapi.cc.TpNotificationRequested();
		result.AppCallNotificationRequest=org.csapi.cc.TpCallNotificationRequestHelper.read(in);
		result.AssignmentID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpNotificationRequested s)
	{
		org.csapi.cc.TpCallNotificationRequestHelper.write(out,s.AppCallNotificationRequest);
		out.write_long(s.AssignmentID);
	}
}
