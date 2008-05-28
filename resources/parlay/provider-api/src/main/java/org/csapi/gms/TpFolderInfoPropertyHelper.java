package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpFolderInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpFolderInfoPropertyHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpFolderInfoProperty s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpFolderInfoProperty extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpFolderInfoProperty:1.0";
	}
	public static TpFolderInfoProperty read (org.omg.CORBA.portable.InputStream in)
	{
		TpFolderInfoProperty result = new TpFolderInfoProperty ();
		org.csapi.gms.TpFolderInfoPropertyName disc = org.csapi.gms.TpFolderInfoPropertyName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_ID:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingFolderID (_var);
				break;
			}
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_MESSAGE:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingFolderMessage (_var);
				break;
			}
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_SUBFOLDER:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingFolderSubfolder (_var);
				break;
			}
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_DATE_CREATED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingFolderDateCreated (_var);
				break;
			}
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_DATE_CHANGED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingFolderDateChanged (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpFolderInfoProperty s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_ID:
			{
				out.write_string(s.MessagingFolderID ());
				break;
			}
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_MESSAGE:
			{
				out.write_string(s.MessagingFolderMessage ());
				break;
			}
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_SUBFOLDER:
			{
				out.write_string(s.MessagingFolderSubfolder ());
				break;
			}
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_DATE_CREATED:
			{
				out.write_string(s.MessagingFolderDateCreated ());
				break;
			}
			case org.csapi.gms.TpFolderInfoPropertyName._P_MESSAGING_FOLDER_DATE_CHANGED:
			{
				out.write_string(s.MessagingFolderDateChanged ());
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
			org.csapi.gms.TpFolderInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_ID);
			members[5] = new org.omg.CORBA.UnionMember ("MessagingFolderID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpFolderInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_MESSAGE);
			members[4] = new org.omg.CORBA.UnionMember ("MessagingFolderMessage", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpFolderInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_SUBFOLDER);
			members[3] = new org.omg.CORBA.UnionMember ("MessagingFolderSubfolder", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpFolderInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_DATE_CREATED);
			members[2] = new org.omg.CORBA.UnionMember ("MessagingFolderDateCreated", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpFolderInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_DATE_CHANGED);
			members[1] = new org.omg.CORBA.UnionMember ("MessagingFolderDateChanged", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpFolderInfoProperty",org.csapi.gms.TpFolderInfoPropertyNameHelper.type(), members);
		}
		return _type;
	}
}
