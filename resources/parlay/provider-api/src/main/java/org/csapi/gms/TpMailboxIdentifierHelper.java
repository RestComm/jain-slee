package org.csapi.gms;


/**
 *	Generated from IDL definition of struct "TpMailboxIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxIdentifierHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.csapi.gms.TpMailboxIdentifierHelper.id(),"TpMailboxIdentifier",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("Mailbox", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/gms/IpMailbox:1.0", "IpMailbox"), null),new org.omg.CORBA.StructMember("SessionID", org.csapi.TpSessionIDHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMailboxIdentifier s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMailboxIdentifier extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMailboxIdentifier:1.0";
	}
	public static org.csapi.gms.TpMailboxIdentifier read (final org.omg.CORBA.portable.InputStream in)
	{
		org.csapi.gms.TpMailboxIdentifier result = new org.csapi.gms.TpMailboxIdentifier();
		result.Mailbox=org.csapi.gms.IpMailboxHelper.read(in);
		result.SessionID=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.csapi.gms.TpMailboxIdentifier s)
	{
		org.csapi.gms.IpMailboxHelper.write(out,s.Mailbox);
		out.write_long(s.SessionID);
	}
}
