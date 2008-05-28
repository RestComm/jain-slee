package org.csapi.cc;
/**
 *	Generated from IDL definition of enum "TpCarrierSelectionField"
 *	@author JacORB IDL compiler 
 */

public final class TpCarrierSelectionFieldHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.cc.TpCarrierSelectionFieldHelper.id(),"TpCarrierSelectionField",new String[]{"P_CIC_UNDEFINED","P_CIC_NO_INPUT","P_CIC_INPUT","P_CIC_UNDETERMINED","P_CIC_NOT_PRESCRIBED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCarrierSelectionField s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCarrierSelectionField extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCarrierSelectionField:1.0";
	}
	public static TpCarrierSelectionField read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpCarrierSelectionField.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpCarrierSelectionField s)
	{
		out.write_long(s.value());
	}
}
