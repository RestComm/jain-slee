package org.csapi.cc.mmccs;


/**
 *	Generated from IDL definition of struct "TpAppMultiMediaCallLegCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppMultiMediaCallLegCallBackHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBackHelper.id(),"TpAppMultiMediaCallLegCallBack",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AppMultiMediaCall", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mmccs/IpAppMultiMediaCall:1.0", "IpAppMultiMediaCall"), null),new org.omg.CORBA.StructMember("AppCallLegSet", org.csapi.cc.mmccs.TpAppMultiMediaCallLegRefSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mmccs/TpAppMultiMediaCallLegCallBack:1.0";
	}
	public static org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack result = new org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack();
		result.AppMultiMediaCall=org.csapi.cc.mmccs.IpAppMultiMediaCallHelper.read(in);
		result.AppCallLegSet = org.csapi.cc.mmccs.TpAppMultiMediaCallLegRefSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mmccs.TpAppMultiMediaCallLegCallBack s)
	{
		org.csapi.cc.mmccs.IpAppMultiMediaCallHelper.write(out,s.AppMultiMediaCall);
		org.csapi.cc.mmccs.TpAppMultiMediaCallLegRefSetHelper.write(out,s.AppCallLegSet);
	}
}
