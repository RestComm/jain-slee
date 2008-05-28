package org.csapi.cs;

/**
 *	Generated from IDL definition of alias "TpPriceVolumeSet"
 *	@author JacORB IDL compiler 
 */

public final class TpPriceVolumeSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cs.TpPriceVolume[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cs.TpPriceVolume[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cs.TpPriceVolumeSetHelper.id(), "TpPriceVolumeSet",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cs.TpPriceVolumeHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cs/TpPriceVolumeSet:1.0";
	}
	public static org.csapi.cs.TpPriceVolume[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cs.TpPriceVolume[] _result;
		int _l_result7 = _in.read_long();
		_result = new org.csapi.cs.TpPriceVolume[_l_result7];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.csapi.cs.TpPriceVolumeHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cs.TpPriceVolume[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.csapi.cs.TpPriceVolumeHelper.write(_out,_s[i]);
		}

	}
}
