package org.csapi.cc.mmccs;


/**
 *	Generated from IDL definition of struct "TpNotificationMediaRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationMediaRequestHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.id(),"TpNotificationMediaRequest",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MediaNotificationScope", org.csapi.cc.TpCallNotificationScopeHelper.type(), null),new org.omg.CORBA.StructMember("MediaStreamsRequested", org.csapi.cc.mmccs.TpMediaStreamRequestSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpNotificationMediaRequest s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpNotificationMediaRequest extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpNotificationMediaRequest:1.0";
	}
	public static org.csapi.cc.mmccs.TpNotificationMediaRequest read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mmccs.TpNotificationMediaRequest result = new org.csapi.cc.mmccs.TpNotificationMediaRequest();
		result.MediaNotificationScope=org.csapi.cc.TpCallNotificationScopeHelper.read(in);
		result.MediaStreamsRequested = org.csapi.cc.mmccs.TpMediaStreamRequestSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mmccs.TpNotificationMediaRequest s)
	{
		org.csapi.cc.TpCallNotificationScopeHelper.write(out,s.MediaNotificationScope);
		org.csapi.cc.mmccs.TpMediaStreamRequestSetHelper.write(out,s.MediaStreamsRequested);
	}
}
