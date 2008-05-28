package org.csapi.cc.mpccs;


/**
 *	Generated from IDL definition of struct "TpAppCallLegCallBack"
 *	@author JacORB IDL compiler 
 */

public final class TpAppCallLegCallBackHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.cc.mpccs.TpAppCallLegCallBackHelper.id(),"TpAppCallLegCallBack",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("AppMultiPartyCall", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/cc/mpccs/IpAppMultiPartyCall:1.0", "IpAppMultiPartyCall"), null),new org.omg.CORBA.StructMember("AppCallLegSet", org.csapi.cc.mpccs.TpAppCallLegRefSetHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.mpccs.TpAppCallLegCallBack s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.mpccs.TpAppCallLegCallBack extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/mpccs/TpAppCallLegCallBack:1.0";
	}
	public static org.csapi.cc.mpccs.TpAppCallLegCallBack read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.cc.mpccs.TpAppCallLegCallBack result = new org.csapi.cc.mpccs.TpAppCallLegCallBack();
		result.AppMultiPartyCall=org.csapi.cc.mpccs.IpAppMultiPartyCallHelper.read(in);
		result.AppCallLegSet = org.csapi.cc.mpccs.TpAppCallLegRefSetHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.cc.mpccs.TpAppCallLegCallBack s)
	{
		org.csapi.cc.mpccs.IpAppMultiPartyCallHelper.write(out,s.AppMultiPartyCall);
		org.csapi.cc.mpccs.TpAppCallLegRefSetHelper.write(out,s.AppCallLegSet);
	}
}
