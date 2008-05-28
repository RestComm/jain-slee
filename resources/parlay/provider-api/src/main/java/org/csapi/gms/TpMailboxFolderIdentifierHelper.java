package org.csapi.gms;


/**
 *	Generated from IDL definition of struct "TpMailboxFolderIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxFolderIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.gms.TpMailboxFolderIdentifierHelper.id(),"TpMailboxFolderIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("MailboxFolder", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/gms/IpMailboxFolder:1.0", "IpMailboxFolder"), null),new org.omg.CORBA.StructMember("SessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMailboxFolderIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMailboxFolderIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMailboxFolderIdentifier:1.0";
	}
	public static org.csapi.gms.TpMailboxFolderIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.gms.TpMailboxFolderIdentifier result = new org.csapi.gms.TpMailboxFolderIdentifier();
		result.MailboxFolder=org.csapi.gms.IpMailboxFolderHelper.read(in);
		result.SessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.gms.TpMailboxFolderIdentifier s)
	{
		org.csapi.gms.IpMailboxFolderHelper.write(out,s.MailboxFolder);
		out.write_long(s.SessionID);
	}
}
