package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of alias "TpAppCallLegRefSet"
 *	@author JacORB IDL compiler 
 */

public final class TpAppCallLegRefSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.mpccs.IpAppCallLeg[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.mpccs.IpAppCallLeg[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.mpccs.TpAppCallLegRefSetHelper.id(), "TpAppCallLegRefSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mpccs/IpAppCallLeg:1.0", "IpAppCallLeg")));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/TpAppCallLegRefSet:1.0";
	}
	public static org.csapi.cc.mpccs.IpAppCallLeg[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.mpccs.IpAppCallLeg[] _result;
		int _l_result55 = _in.read_long();
		_result = new org.csapi.cc.mpccs.IpAppCallLeg[_l_result55];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cc.mpccs.IpAppCallLegHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.mpccs.IpAppCallLeg[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cc.mpccs.IpAppCallLegHelper.write(_out,_s[i]);
		}

	}
}
