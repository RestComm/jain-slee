package org.csapi.am;


/**
 *	Generated from IDL definition of struct "TpBalance"
 *	@author JacORB IDL compiler 
 */

public final class TpBalanceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.am.TpBalanceHelper.id(),"TpBalance",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("UserID", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("StatusCode", org.csapi.am.TpBalanceQueryErrorHelper.type(), null),new org.omg.CORBA.StructMember("BalanceInfo", org.csapi.am.TpBalanceInfoHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.am.TpBalance s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.am.TpBalance extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/am/TpBalance:1.0";
	}
	public static org.csapi.am.TpBalance read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.am.TpBalance result = new org.csapi.am.TpBalance();
		result.UserID=org.csapi.TpAddressHelper.read(in);
		result.StatusCode=org.csapi.am.TpBalanceQueryErrorHelper.read(in);
		result.BalanceInfo=org.csapi.am.TpBalanceInfoHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.am.TpBalance s)
	{
		org.csapi.TpAddressHelper.write(out,s.UserID);
		org.csapi.am.TpBalanceQueryErrorHelper.write(out,s.StatusCode);
		org.csapi.am.TpBalanceInfoHelper.write(out,s.BalanceInfo);
	}
}
