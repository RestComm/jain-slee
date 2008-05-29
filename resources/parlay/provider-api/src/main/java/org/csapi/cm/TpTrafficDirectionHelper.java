package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpTrafficDirection"
 *	@author JacORB IDL compiler 
 */

public final class TpTrafficDirectionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cm.TpTrafficDirectionHelper.id(),"TpTrafficDirection",new String[]{"UNIDIRECTIONAL","BIDIRECTIONAL"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cm.TpTrafficDirection s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cm.TpTrafficDirection extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cm/TpTrafficDirection:1.0";
	}
	public static TpTrafficDirection read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpTrafficDirection.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpTrafficDirection s)
	{
		out.write_long(s.value());
	}
}
