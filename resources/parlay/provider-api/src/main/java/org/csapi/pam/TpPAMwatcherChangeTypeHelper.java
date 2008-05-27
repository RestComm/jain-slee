package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMwatcherChangeType"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMwatcherChangeTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.pam.TpPAMwatcherChangeTypeHelper.id(),"TpPAMwatcherChangeType",new String[]{"PAM_WATCHERS_PERIODIC","PAM_WATCHERS_ADDED","PAM_WATCHERS_DELETED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMwatcherChangeType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMwatcherChangeType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMwatcherChangeType:1.0";
	}
	public static TpPAMwatcherChangeType read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpPAMwatcherChangeType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpPAMwatcherChangeType s)
	{
		out.write_long(s.value());
	}
}
