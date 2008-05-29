package org.csapi.cc.mmccs;


/**
 *	Generated from IDL definition of struct "TpMediaNotificationRequested"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaNotificationRequestedHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mmccs.TpMediaNotificationRequestedHelper.id(),"TpMediaNotificationRequested",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AppNotificationMediaRequest", org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.type(), null),new org.omg.CORBA.StructMember("AssignmentID", org.csapi.TpInt32Helper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpMediaNotificationRequested s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpMediaNotificationRequested extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaNotificationRequested:1.0";
	}
	public static org.csapi.cc.mmccs.TpMediaNotificationRequested read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mmccs.TpMediaNotificationRequested result = new org.csapi.cc.mmccs.TpMediaNotificationRequested();
		result.AppNotificationMediaRequest=org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.read(in);
		result.AssignmentID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mmccs.TpMediaNotificationRequested s)
	{
		org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.write(out,s.AppNotificationMediaRequest);
		out.write_long(s.AssignmentID);
	}
}
