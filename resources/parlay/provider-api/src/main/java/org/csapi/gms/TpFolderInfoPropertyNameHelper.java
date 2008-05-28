package org.csapi.gms;
/**
 *	Generated from IDL definition of enum "TpFolderInfoPropertyName"
 *	@author JacORB IDL compiler 
 */

public final class TpFolderInfoPropertyNameHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.csapi.gms.TpFolderInfoPropertyNameHelper.id(),"TpFolderInfoPropertyName",new String[]{"P_MESSAGING_FOLDER_UNDEFINED","P_MESSAGING_FOLDER_ID","P_MESSAGING_FOLDER_MESSAGE","P_MESSAGING_FOLDER_SUBFOLDER","P_MESSAGING_FOLDER_DATE_CREATED","P_MESSAGING_FOLDER_DATE_CHANGED"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpFolderInfoPropertyName s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpFolderInfoPropertyName extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpFolderInfoPropertyName:1.0";
	}
	public static TpFolderInfoPropertyName read (final org.omg.CORBA.portable.InputStream in)
	{
		return TpFolderInfoPropertyName.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final TpFolderInfoPropertyName s)
	{
		out.write_long(s.value());
	}
}
