package org.csapi.cc;

/**
 *	Generated from IDL definition of union "TpAdditionalCallEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpAdditionalCallEventCriteriaHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.cc.TpAdditionalCallEventCriteria s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.cc.TpAdditionalCallEventCriteria extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/cc/TpAdditionalCallEventCriteria:1.0";
	}
	public static TpAdditionalCallEventCriteria read (org.omg.CORBA.portable.InputStream in)
	{
		TpAdditionalCallEventCriteria result = new TpAdditionalCallEventCriteria ();
		org.csapi.cc.TpCallEventType disc = org.csapi.cc.TpCallEventType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ADDRESS_COLLECTED:
			{
				int _var;
				_var=in.read_long();
				result.MinAddressLength (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ORIGINATING_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCode[] _var;
				_var = org.csapi.cc.TpCallServiceCodeSetHelper.read(in);
				result.OriginatingServiceCode (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ORIGINATING_RELEASE:
			{
				org.csapi.cc.TpReleaseCause[] _var;
				_var = org.csapi.cc.TpReleaseCauseSetHelper.read(in);
				result.OriginatingReleaseCauseSet (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_TERMINATING_RELEASE:
			{
				org.csapi.cc.TpReleaseCause[] _var;
				_var = org.csapi.cc.TpReleaseCauseSetHelper.read(in);
				result.TerminatingReleaseCauseSet (_var);
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_TERMINATING_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCode[] _var;
				_var = org.csapi.cc.TpCallServiceCodeSetHelper.read(in);
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
	public static void write (org.omg.CORBA.portable.OutputStream out, TpAdditionalCallEventCriteria s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ADDRESS_COLLECTED:
			{
				out.write_long(s.MinAddressLength ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ORIGINATING_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCodeSetHelper.write(out,s.OriginatingServiceCode ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_ORIGINATING_RELEASE:
			{
				org.csapi.cc.TpReleaseCauseSetHelper.write(out,s.OriginatingReleaseCauseSet ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_TERMINATING_RELEASE:
			{
				org.csapi.cc.TpReleaseCauseSetHelper.write(out,s.TerminatingReleaseCauseSet ());
				break;
			}
			case org.csapi.cc.TpCallEventType._P_CALL_EVENT_TERMINATING_SERVICE_CODE:
			{
				org.csapi.cc.TpCallServiceCodeSetHelper.write(out,s.TerminatingServiceCode ());
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
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_ADDRESS_COLLECTED);
			members[5] = new org.omg.CORBA.UnionMember ("MinAddressLength", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_SERVICE_CODE);
			members[4] = new org.omg.CORBA.UnionMember ("OriginatingServiceCode", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.TpCallServiceCodeHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_ORIGINATING_RELEASE);
			members[3] = new org.omg.CORBA.UnionMember ("OriginatingReleaseCauseSet", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.TpReleaseCauseHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_RELEASE);
			members[2] = new org.omg.CORBA.UnionMember ("TerminatingReleaseCauseSet", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.TpReleaseCauseHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.cc.TpCallEventTypeHelper.insert(label_any, org.csapi.cc.TpCallEventType.P_CALL_EVENT_TERMINATING_SERVICE_CODE);
			members[1] = new org.omg.CORBA.UnionMember ("TerminatingServiceCode", label_any, org.omg.CORBA.ORB.init().create_sequence_tc(0, org.csapi.cc.TpCallServiceCodeHelper.type()),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			label_any.insert_octet ((byte)0);
			members[0] = new org.omg.CORBA.UnionMember ("Dummy", label_any, org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(2)),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpAdditionalCallEventCriteria",org.csapi.cc.TpCallEventTypeHelper.type(), members);
		}
		return _type;
	}
}
