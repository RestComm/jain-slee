package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpCallAdditionalEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallAdditionalEventInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpCallAdditionalEventInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpCallAdditionalEventInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpCallAdditionalEventInfo:1.0";
	}
	public static TpCallAdditionalEventInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpCallAdditionalEventInfo result = new TpCallAdditionalEventInfo ();
		org.csapi.cc.TpCallEventType disc = org.csapi.cc.TpCallEventType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ADDRESS_COLLECTED:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.CollectedAddress (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ADDRESS_ANALYSED:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.CalledAddress (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ORIGINATING_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCode _var;
				_var=org.csapi.cc.TpCallServiceCodeHelper.read(in);
				result.OriginatingServiceCode (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ORIGINATING_RELEASE:
			{
				org.csapi.cc.TpReleaseCause _var;
				_var=org.csapi.cc.TpReleaseCauseHelper.read(in);
				result.OriginatingReleaseCause (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_TERMINATING_RELEASE:
			{
				org.csapi.cc.TpReleaseCause _var;
				_var=org.csapi.cc.TpReleaseCauseHelper.read(in);
				result.TerminatingReleaseCause (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_REDIRECTED:
			{
				org.csapi.TpAddress _var;
				_var=org.csapi.TpAddressHelper.read(in);
				result.ForwardAddress (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_TERMINATING_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCode _var;
				_var=org.csapi.cc.TpCallServiceCodeHelper.read(in);
				result.TerminatingServiceCode (_var);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpCallAdditionalEventInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ADDRESS_COLLECTED:
			{
				org.csapi.TpAddressHelper.write(out,s.CollectedAddress ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ADDRESS_ANALYSED:
			{
				org.csapi.TpAddressHelper.write(out,s.CalledAddress ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ORIGINATING_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCodeHelper.write(out,s.OriginatingServiceCode ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ORIGINATING_RELEASE:
			{
				org.csapi.cc.TpReleaseCauseHelper.write(out,s.OriginatingReleaseCause ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_TERMINATING_RELEASE:
			{
				org.csapi.cc.TpReleaseCauseHelper.write(out,s.TerminatingReleaseCause ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_REDIRECTED:
			{
				org.csapi.TpAddressHelper.write(out,s.ForwardAddress ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_TERMINATING_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCodeHelper.write(out,s.TerminatingServiceCode ());
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
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[8];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_COLLECTED);
			members[7] = new org.omg.CORBA.UnionMember ("CollectedAddress", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_ANALYSED);
			members[6] = new org.omg.CORBA.UnionMember ("CalledAddress", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_SERVICE_CODE);
			members[5] = new org.omg.CORBA.UnionMember ("OriginatingServiceCode", label_any, org.csapi.cc.TpCallServiceCodeHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_RELEASE);
			members[4] = new org.omg.CORBA.UnionMember ("OriginatingReleaseCause", label_any, org.csapi.cc.TpReleaseCauseHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_RELEASE);
			members[3] = new org.omg.CORBA.UnionMember ("TerminatingReleaseCause", label_any, org.csapi.cc.TpReleaseCauseHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_REDIRECTED);
			members[2] = new org.omg.CORBA.UnionMember ("ForwardAddress", label_any, org.csapi.TpAddressHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_SERVICE_CODE);
			members[1] = new org.omg.CORBA.UnionMember ("TerminatingServiceCode", label_any, org.csapi.cc.TpCallServiceCodeHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpCallAdditionalEventInfo",org.csapi.cc.TpCallEventTypeHelper.type(), members);
		}
		return _type;
	}
}
