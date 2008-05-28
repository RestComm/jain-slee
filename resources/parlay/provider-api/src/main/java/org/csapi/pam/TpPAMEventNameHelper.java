package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventNameHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.pam.TpPAMEventNameHelper.id(),"TpPAMEventName",new String[]{"PAM_CE_IDENTITY_PRESENCE_SET","PAM_CE_AVAILABILITY_CHANGED","PAM_CE_WATCHERS_CHANGED","PAM_CE_IDENTITY_CREATED","PAM_CE_IDENTITY_DELETED","PAM_CE_GROUP_MEMBERSHIP_CHANGED","PAM_CE_AGENT_CREATED","PAM_CE_AGENT_DELETED","PAM_CE_AGENT_ASSIGNED","PAM_CE_AGENT_UNASSIGNED","PAM_CE_CAPABILITY_CHANGED","PAM_CE_AGENT_CAPABILITY_PRESENCE_SET","PAM_CE_AGENT_PRESENCE_SET"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMEventName s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMEventName extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMEventName:1.0";
	}
	public static TpPAMEventName read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPAMEventName.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPAMEventName s)
	{
		out.write_long(s.value());
	}
}
