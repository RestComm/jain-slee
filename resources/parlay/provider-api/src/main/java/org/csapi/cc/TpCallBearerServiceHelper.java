package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallBearerService"
 *	@author JacORB IDL compiler 
 */

public final class TpCallBearerServiceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallBearerServiceHelper.id(),"TpCallBearerService",new String[]{"P_CALL_BEARER_SERVICE_UNKNOWN","P_CALL_BEARER_SERVICE_SPEECH","P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTED","P_CALL_BEARER_SERVICE_DIGITALRESTRICTED","P_CALL_BEARER_SERVICE_AUDIO","P_CALL_BEARER_SERVICE_DIGITALUNRESTRICTEDTONES","P_CALL_BEARER_SERVICE_VIDEO"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallBearerService s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallBearerService extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallBearerService:1.0";
	}
	public static TpCallBearerService read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallBearerService.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallBearerService s)
	{
		out.write_long(s.value());
	}
}
