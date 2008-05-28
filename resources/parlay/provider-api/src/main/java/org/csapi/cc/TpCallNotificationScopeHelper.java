package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCallNotificationScope"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationScopeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCallNotificationScopeHelper.id(),"TpCallNotificationScope",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("DestinationAddress", org.csapi.TpAddressRangeHelper.type(), null),new org.omg.CORBA.StructMember("OriginatingAddress", org.csapi.TpAddressRangeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallNotificationScope s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallNotificationScope extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallNotificationScope:1.0";
	}
	public static org.csapi.cc.TpCallNotificationScope read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCallNotificationScope result = new org.csapi.cc.TpCallNotificationScope();
		result.DestinationAddress=org.csapi.TpAddressRangeHelper.read(in);
		result.OriginatingAddress=org.csapi.TpAddressRangeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCallNotificationScope s)
	{
		org.csapi.TpAddressRangeHelper.write(out,s.DestinationAddress);
		org.csapi.TpAddressRangeHelper.write(out,s.OriginatingAddress);
	}
}
