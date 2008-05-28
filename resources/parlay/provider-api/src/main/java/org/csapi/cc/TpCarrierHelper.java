package org.csapi.cc;


/**
 *	Generated from IDL definition of struct "TpCarrier"
 *	@author JacORB IDL compiler 
 */

public final class TpCarrierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.TpCarrierHelper.id(),"TpCarrier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("CarrierID", org.csapi.cc.TpCarrierIDHelper.type(), null),new org.omg.CORBA.StructMember("CarrierSelectionField", org.csapi.cc.TpCarrierSelectionFieldHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCarrier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCarrier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCarrier:1.0";
	}
	public static org.csapi.cc.TpCarrier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.TpCarrier result = new org.csapi.cc.TpCarrier();
		result.CarrierID = org.csapi.TpOctetSetHelper.read(in);
		result.CarrierSelectionField=org.csapi.cc.TpCarrierSelectionFieldHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.TpCarrier s)
	{
		org.csapi.TpOctetSetHelper.write(out,s.CarrierID);
		org.csapi.cc.TpCarrierSelectionFieldHelper.write(out,s.CarrierSelectionField);
	}
}
