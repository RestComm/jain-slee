package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of alias "TpMediaStreamDataType"
 *	@author JacORB IDL compiler 
 */

public final class TpMediaStreamDataTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.csapi.cc.mmccs.TpMediaStreamDataTypeHelper.id(), "TpMediaStreamDataType",org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestHelper.type());
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpMediaStreamDataType:1.0";
	}
	public static org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest _result;
		_result=org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestHelper.read(_in);
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.csapi.cc.mmccs.TpMediaStreamDataTypeRequest _s)
	{
		org.csapi.cc.mmccs.TpMediaStreamDataTypeRequestHelper.write(_out,_s);
	}
}
