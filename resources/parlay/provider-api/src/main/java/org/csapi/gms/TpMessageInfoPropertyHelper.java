package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpMessageInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageInfoPropertyHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.gms.TpMessageInfoProperty s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.gms.TpMessageInfoProperty extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/gms/TpMessageInfoProperty:1.0";
	}
	public static TpMessageInfoProperty read (org.omg.CORBA.portable.InputStream in)
	{
		TpMessageInfoProperty result = new TpMessageInfoProperty ();
		org.csapi.gms.TpMessageInfoPropertyName disc = org.csapi.gms.TpMessageInfoPropertyName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_ID:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMessageID (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_SUBJECT:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMessageSubject (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_DATE_SENT:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMessageDateSent (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_DATE_RECEIVED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMessageDateReceived (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_DATE_CHANGED:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMessageDateChanged (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_SENT_FROM:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.MessagingMessageSentFrom (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_SENT_TO:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.MessagingMessageSentTo (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_CC_TO:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.MessagingMessageCCTo (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_BCC_TO:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.MessagingMessageBCCTo (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_SIZE:
			{
				int _var;
				_var=in.read_long();
				result.MessagingMessageSize (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_PRIORITY:
			{
				org.csapi.gms.TpMessagePriority _var;
				_var=org.csapi.gms.TpMessagePriorityHelper.read(in);
				result.MessagingMessagePriority (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_FORMAT:
			{
				org.csapi.gms.TpMessageFormat _var;
				_var=org.csapi.gms.TpMessageFormatHelper.read(in);
				result.MessagingMessageFormat (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_FOLDER:
			{
				java.lang.String _var;
				_var=in.read_string();
				result.MessagingMessageFolder (_var);
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_STATUS:
			{
				org.csapi.gms.TpMessageStatus _var;
				_var=org.csapi.gms.TpMessageStatusHelper.read(in);
				result.MessagingMessageStatus (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpMessageInfoProperty s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_ID:
			{
				out.write_string(s.MessagingMessageID ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_SUBJECT:
			{
				out.write_string(s.MessagingMessageSubject ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_DATE_SENT:
			{
				out.write_string(s.MessagingMessageDateSent ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_DATE_RECEIVED:
			{
				out.write_string(s.MessagingMessageDateReceived ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_DATE_CHANGED:
			{
				out.write_string(s.MessagingMessageDateChanged ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_SENT_FROM:
			{
				org.csapi.TpAddressHelper.write(out,s.MessagingMessageSentFrom ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_SENT_TO:
			{
				org.csapi.TpAddressHelper.write(out,s.MessagingMessageSentTo ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_CC_TO:
			{
				org.csapi.TpAddressHelper.write(out,s.MessagingMessageCCTo ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_BCC_TO:
			{
				org.csapi.TpAddressHelper.write(out,s.MessagingMessageBCCTo ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_SIZE:
			{
				out.write_long(s.MessagingMessageSize ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_PRIORITY:
			{
				org.csapi.gms.TpMessagePriorityHelper.write(out,s.MessagingMessagePriority ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_FORMAT:
			{
				org.csapi.gms.TpMessageFormatHelper.write(out,s.MessagingMessageFormat ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_FOLDER:
			{
				out.write_string(s.MessagingMessageFolder ());
				break;
			}
			case org.csapi.gms.TpMessageInfoPropertyName._P_MESSAGING_MESSAGE_STATUS:
			{
				org.csapi.gms.TpMessageStatusHelper.write(out,s.MessagingMessageStatus ());
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
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[15];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_ID);
			members[14] = new org.omg.CORBA.UnionMember ("MessagingMessageID", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SUBJECT);
			members[13] = new org.omg.CORBA.UnionMember ("MessagingMessageSubject", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_SENT);
			members[12] = new org.omg.CORBA.UnionMember ("MessagingMessageDateSent", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_RECEIVED);
			members[11] = new org.omg.CORBA.UnionMember ("MessagingMessageDateReceived", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_CHANGED);
			members[10] = new org.omg.CORBA.UnionMember ("MessagingMessageDateChanged", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SENT_FROM);
			members[9] = new org.omg.CORBA.UnionMember ("MessagingMessageSentFrom", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SENT_TO);
			members[8] = new org.omg.CORBA.UnionMember ("MessagingMessageSentTo", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_CC_TO);
			members[7] = new org.omg.CORBA.UnionMember ("MessagingMessageCCTo", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_BCC_TO);
			members[6] = new org.omg.CORBA.UnionMember ("MessagingMessageBCCTo", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SIZE);
			members[5] = new org.omg.CORBA.UnionMember ("MessagingMessageSize", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_PRIORITY);
			members[4] = new org.omg.CORBA.UnionMember ("MessagingMessagePriority", label_any, org.csapi.gms.TpMessagePriorityHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_FORMAT);
			members[3] = new org.omg.CORBA.UnionMember ("MessagingMessageFormat", label_any, org.csapi.gms.TpMessageFormatHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_FOLDER);
			members[2] = new org.omg.CORBA.UnionMember ("MessagingMessageFolder", label_any, org.omg.CORBA.ORB.init().create_string_tc(0),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.gms.TpMessageInfoPropertyNameHelper.insert(label_any, org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_STATUS);
			members[1] = new org.omg.CORBA.UnionMember ("MessagingMessageStatus", label_any, org.csapi.gms.TpMessageStatusHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpMessageInfoProperty",org.csapi.gms.TpMessageInfoPropertyNameHelper.type(), members);
		}
		return _type;
	}
}
