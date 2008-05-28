package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCallTeleService"
 *	@author JacORB IDL compiler 
 */

public final class TpCallTeleServiceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCallTeleServiceHelper.id(),"TpCallTeleService",new String[]{"P_CALL_TELE_SERVICE_UNKNOWN","P_CALL_TELE_SERVICE_TELEPHONY","P_CALL_TELE_SERVICE_FAX_2_3","P_CALL_TELE_SERVICE_FAX_4_I","P_CALL_TELE_SERVICE_FAX_4_II_III","P_CALL_TELE_SERVICE_VIDEOTEX_SYN","P_CALL_TELE_SERVICE_VIDEOTEX_INT","P_CALL_TELE_SERVICE_TELEX","P_CALL_TELE_SERVICE_MHS","P_CALL_TELE_SERVICE_OSI","P_CALL_TELE_SERVICE_FTAM","P_CALL_TELE_SERVICE_VIDEO","P_CALL_TELE_SERVICE_VIDEO_CONF","P_CALL_TELE_SERVICE_AUDIOGRAPH_CONF","P_CALL_TELE_SERVICE_MULTIMEDIA","P_CALL_TELE_SERVICE_CS_INI_H221","P_CALL_TELE_SERVICE_CS_SUB_H221","P_CALL_TELE_SERVICE_CS_INI_CALL","P_CALL_TELE_SERVICE_DATATRAFFIC","P_CALL_TELE_SERVICE_EMERGENCY_CALLS","P_CALL_TELE_SERVICE_SMS_MT_PP","P_CALL_TELE_SERVICE_SMS_MO_PP","P_CALL_TELE_SERVICE_CELL_BROADCAST","P_CALL_TELE_SERVICE_ALT_SPEECH_FAX_3","P_CALL_TELE_SERVICE_AUTOMATIC_FAX_3","P_CALL_TELE_SERVICE_VOICE_GROUP_CALL","P_CALL_TELE_SERVICE_VOICE_BROADCAST"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallTeleService s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallTeleService extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallTeleService:1.0";
	}
	public static TpCallTeleService read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCallTeleService.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCallTeleService s)
	{
		out.write_long(s.value());
	}
}
