package org.csapi.pam;


/**
 *	Generated from IDL definition of struct "TpPAMADNotificationData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMADNotificationDataHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.pam.TpPAMADNotificationDataHelper.id(),"TpPAMADNotificationData",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Agents", org.csapi.pam.TpPAMFQNameListHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMADNotificationData s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMADNotificationData extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMADNotificationData:1.0";
	}
	public static org.csapi.pam.TpPAMADNotificationData read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.pam.TpPAMADNotificationData result = new org.csapi.pam.TpPAMADNotificationData();
		result.Agents = org.csapi.pam.TpPAMFQNameListHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.pam.TpPAMADNotificationData s)
	{
		org.csapi.pam.TpPAMFQNameListHelper.write(out,s.Agents);
	}
}
