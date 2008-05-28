package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of alias "TpCallLegIdentifierSet"
 *	@author JacORB IDL compiler 
 */

public final class TpCallLegIdentifierSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.mpccs.TpCallLegIdentifier[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.mpccs.TpCallLegIdentifier[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.mpccs.TpCallLegIdentifierSetHelper.id(), "TpCallLegIdentifierSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.mpccs.TpCallLegIdentifierHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/TpCallLegIdentifierSet:1.0";
	}
	public static org.csapi.cc.mpccs.TpCallLegIdentifier[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.mpccs.TpCallLegIdentifier[] _result;
		int _l_result56 = _in.read_long();
		_result = new org.csapi.cc.mpccs.TpCallLegIdentifier[_l_result56];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cc.mpccs.TpCallLegIdentifierHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.mpccs.TpCallLegIdentifier[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cc.mpccs.TpCallLegIdentifierHelper.write(_out,_s[i]);
		}

	}
}
