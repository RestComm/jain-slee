package org.csapi.cc.mpccs;

/**
 *	Generated from IDL definition of alias "TpMultiPartyCallIdentifierSet"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiPartyCallIdentifierSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.mpccs.TpMultiPartyCallIdentifier[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.mpccs.TpMultiPartyCallIdentifier[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.mpccs.TpMultiPartyCallIdentifierSetHelper.id(), "TpMultiPartyCallIdentifierSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/TpMultiPartyCallIdentifierSet:1.0";
	}
	public static org.csapi.cc.mpccs.TpMultiPartyCallIdentifier[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.mpccs.TpMultiPartyCallIdentifier[] _result;
		int _l_result57 = _in.read_long();
		_result = new org.csapi.cc.mpccs.TpMultiPartyCallIdentifier[_l_result57];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.mpccs.TpMultiPartyCallIdentifier[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.write(_out,_s[i]);
		}

	}
}
