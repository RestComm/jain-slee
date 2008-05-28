package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpMailboxInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxInfoPropertyHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMailboxInfoProperty s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMailboxInfoProperty extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMailboxInfoProperty:1.0";
	}
	public static TpMailboxInfoProperty read (org.omg.CORBA.portable.InputStream in)
	{
		TpMailboxInfoProperty result = new TpMailboxInfoProperty ();
		org.csapi.gms.TpMailboxInfoPropertyName disc = org.csapi.gms.TpMailboxInfoPropertyName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_ID:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.MessagingMailboxID (_var);
				break;
			}
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_OWNER:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMailboxOwner (_var);
				break;
			}
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_FOLDER:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMailboxFolder (_var);
				break;
			}
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_DATE_CREATED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMailboxDateCreated (_var);
				break;
			}
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_DATE_CHANGED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMailboxDateChanged (_var);
				break;
			}
			default:
			{
				short _var;
				_var=in.read_short();
				result.Dummy (_var);
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpMailboxInfoProperty s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_ID:
			{
				org.csapi.TpAddressHelper.write(out,s.MessagingMailboxID ());
				break;
			}
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_OWNER:
			{
				out.write_string(s.MessagingMailboxOwner ());
				break;
			}
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_FOLDER:
			{
				out.write_string(s.MessagingMailboxFolder ());
				break;
			}
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_DATE_CREATED:
			{
				out.write_string(s.MessagingMailboxDateCreated ());
				break;
			}
			case org.csapi.gms.TpMailboxInfoPropertyName._P_MESSAGING_MAILBOX_DATE_CHANGED:
			{
				out.write_string(s.MessagingMailboxDateChanged ());
				break;
			}
			default:
			{
				out.write_short(s.Dummy ());
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[6];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMailboxInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_ID);
			members[5] = new org.omg.CORBA.UnionMember ("MessagingMailboxID", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMailboxInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_OWNER);
			members[4] = new org.omg.CORBA.UnionMember ("MessagingMailboxOwner", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMailboxInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_FOLDER);
			members[3] = new org.omg.CORBA.UnionMember ("MessagingMailboxFolder", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMailboxInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_DATE_CREATED);
			members[2] = new org.omg.CORBA.UnionMember ("MessagingMailboxDateCreated", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMailboxInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_DATE_CHANGED);
			members[1] = new org.omg.CORBA.UnionMember ("MessagingMailboxDateChanged", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpMailboxInfoProperty",org.csapi.gms.TpMailboxInfoPropertyNameHelper.type(), members);
		}
		return _type;
	}
}
