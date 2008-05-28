package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of alias "TpMediaNotificationRequestedSet"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaNotificationRequestedSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.mmccs.TpMediaNotificationRequested[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.mmccs.TpMediaNotificationRequested[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.mmccs.TpMediaNotificationRequestedSetHelper.id(), "TpMediaNotificationRequestedSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.mmccs.TpMediaNotificationRequestedHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaNotificationRequestedSet:1.0";
	}
	public static org.csapi.cc.mmccs.TpMediaNotificationRequested[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.mmccs.TpMediaNotificationRequested[] _result;
		int _l_result43 = _in.read_long();
		_result = new org.csapi.cc.mmccs.TpMediaNotificationRequested[_l_result43];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cc.mmccs.TpMediaNotificationRequestedHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.mmccs.TpMediaNotificationRequested[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cc.mmccs.TpMediaNotificationRequestedHelper.write(_out,_s[i]);
		}

	}
}
