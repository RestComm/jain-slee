package org.csapi.gms;


/**
 *	Generated from IDL definition of struct "TpGMSNewMessageArrivedCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpGMSNewMessageArrivedCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.gms.TpGMSNewMessageArrivedCriteriaHelper.id(),"TpGMSNewMessageArrivedCriteria",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MailboxID", org.csapi.TpAddressHelper.type(), null),new org.omg.CORBA.StructMember("AuthenticationInfo", org.csapi.TpStringHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpGMSNewMessageArrivedCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpGMSNewMessageArrivedCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpGMSNewMessageArrivedCriteria:1.0";
	}
	public static org.csapi.gms.TpGMSNewMessageArrivedCriteria read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.gms.TpGMSNewMessageArrivedCriteria result = new org.csapi.gms.TpGMSNewMessageArrivedCriteria();
		result.MailboxID=org.csapi.TpAddressHelper.read(in);
		result.AuthenticationInfo=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.gms.TpGMSNewMessageArrivedCriteria s)
	{
		org.csapi.TpAddressHelper.write(out,s.MailboxID);
		out.write_string(s.AuthenticationInfo);
	}
}
