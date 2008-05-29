package org.csapi.pam;

/**
 *	Generated from IDL definition of union "TpPAMEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMEventInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMEventInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMEventInfo:1.0";
	}
	public static TpPAMEventInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpPAMEventInfo result = new TpPAMEventInfo ();
		org.csapi.pam.TpPAMEventName disc = org.csapi.pam.TpPAMEventName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMIPSEventData _var;
				_var=org.csapi.pam.TpPAMIPSEventDataHelper.read(in);
				result.IdentityPresenceSet (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AVAILABILITY_CHANGED:
			{
				org.csapi.pam.TpPAMAVCEventData _var;
				_var=org.csapi.pam.TpPAMAVCEventDataHelper.read(in);
				result.AvailabilityChanged (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_WATCHERS_CHANGED:
			{
				org.csapi.pam.TpPAMWCEventData _var;
				_var=org.csapi.pam.TpPAMWCEventDataHelper.read(in);
				result.WatchersChanged (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_CREATED:
			{
				org.csapi.pam.TpPAMICEventData _var;
				_var=org.csapi.pam.TpPAMICEventDataHelper.read(in);
				result.IdentityCreated (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_DELETED:
			{
				org.csapi.pam.TpPAMIDEventData _var;
				_var=org.csapi.pam.TpPAMIDEventDataHelper.read(in);
				result.IdentityDeleted (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_GROUP_MEMBERSHIP_CHANGED:
			{
				org.csapi.pam.TpPAMGMCEventData _var;
				_var=org.csapi.pam.TpPAMGMCEventDataHelper.read(in);
				result.GroupMembershipChanged (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_CREATED:
			{
				org.csapi.pam.TpPAMACEventData _var;
				_var=org.csapi.pam.TpPAMACEventDataHelper.read(in);
				result.AgentCreated (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_DELETED:
			{
				org.csapi.pam.TpPAMADEventData _var;
				_var=org.csapi.pam.TpPAMADEventDataHelper.read(in);
				result.AgentDeleted (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_ASSIGNED:
			{
				org.csapi.pam.TpPAMAAEventData _var;
				_var=org.csapi.pam.TpPAMAAEventDataHelper.read(in);
				result.AgentAssigned (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_UNASSIGNED:
			{
				org.csapi.pam.TpPAMAUEventData _var;
				_var=org.csapi.pam.TpPAMAUEventDataHelper.read(in);
				result.AgentUnassigned (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_CAPABILITY_CHANGED:
			{
				org.csapi.pam.TpPAMCCEventData _var;
				_var=org.csapi.pam.TpPAMCCEventDataHelper.read(in);
				result.CapabilityChanged (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_CAPABILITY_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMACPSEventData _var;
				_var=org.csapi.pam.TpPAMACPSEventDataHelper.read(in);
				result.AgentCapabilityPresenceSet (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMAPSEventData _var;
				_var=org.csapi.pam.TpPAMAPSEventDataHelper.read(in);
				result.AgentPresenceSet (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpPAMEventInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMIPSEventDataHelper.write(out,s.IdentityPresenceSet ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AVAILABILITY_CHANGED:
			{
				org.csapi.pam.TpPAMAVCEventDataHelper.write(out,s.AvailabilityChanged ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_WATCHERS_CHANGED:
			{
				org.csapi.pam.TpPAMWCEventDataHelper.write(out,s.WatchersChanged ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_CREATED:
			{
				org.csapi.pam.TpPAMICEventDataHelper.write(out,s.IdentityCreated ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_DELETED:
			{
				org.csapi.pam.TpPAMIDEventDataHelper.write(out,s.IdentityDeleted ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_GROUP_MEMBERSHIP_CHANGED:
			{
				org.csapi.pam.TpPAMGMCEventDataHelper.write(out,s.GroupMembershipChanged ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_CREATED:
			{
				org.csapi.pam.TpPAMACEventDataHelper.write(out,s.AgentCreated ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_DELETED:
			{
				org.csapi.pam.TpPAMADEventDataHelper.write(out,s.AgentDeleted ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_ASSIGNED:
			{
				org.csapi.pam.TpPAMAAEventDataHelper.write(out,s.AgentAssigned ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_UNASSIGNED:
			{
				org.csapi.pam.TpPAMAUEventDataHelper.write(out,s.AgentUnassigned ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_CAPABILITY_CHANGED:
			{
				org.csapi.pam.TpPAMCCEventDataHelper.write(out,s.CapabilityChanged ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_CAPABILITY_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMACPSEventDataHelper.write(out,s.AgentCapabilityPresenceSet ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMAPSEventDataHelper.write(out,s.AgentPresenceSet ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[13];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_PRESENCE_SET);
			members[12] = new org.omg.CORBA.UnionMember ("IdentityPresenceSet", label_any, org.csapi.pam.TpPAMIPSEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AVAILABILITY_CHANGED);
			members[11] = new org.omg.CORBA.UnionMember ("AvailabilityChanged", label_any, org.csapi.pam.TpPAMAVCEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_WATCHERS_CHANGED);
			members[10] = new org.omg.CORBA.UnionMember ("WatchersChanged", label_any, org.csapi.pam.TpPAMWCEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_CREATED);
			members[9] = new org.omg.CORBA.UnionMember ("IdentityCreated", label_any, org.csapi.pam.TpPAMICEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_DELETED);
			members[8] = new org.omg.CORBA.UnionMember ("IdentityDeleted", label_any, org.csapi.pam.TpPAMIDEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_GROUP_MEMBERSHIP_CHANGED);
			members[7] = new org.omg.CORBA.UnionMember ("GroupMembershipChanged", label_any, org.csapi.pam.TpPAMGMCEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CREATED);
			members[6] = new org.omg.CORBA.UnionMember ("AgentCreated", label_any, org.csapi.pam.TpPAMACEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_DELETED);
			members[5] = new org.omg.CORBA.UnionMember ("AgentDeleted", label_any, org.csapi.pam.TpPAMADEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_ASSIGNED);
			members[4] = new org.omg.CORBA.UnionMember ("AgentAssigned", label_any, org.csapi.pam.TpPAMAAEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_UNASSIGNED);
			members[3] = new org.omg.CORBA.UnionMember ("AgentUnassigned", label_any, org.csapi.pam.TpPAMAUEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_CAPABILITY_CHANGED);
			members[2] = new org.omg.CORBA.UnionMember ("CapabilityChanged", label_any, org.csapi.pam.TpPAMCCEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CAPABILITY_PRESENCE_SET);
			members[1] = new org.omg.CORBA.UnionMember ("AgentCapabilityPresenceSet", label_any, org.csapi.pam.TpPAMACPSEventDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_PRESENCE_SET);
			members[0] = new org.omg.CORBA.UnionMember ("AgentPresenceSet", label_any, org.csapi.pam.TpPAMAPSEventDataHelper.type(),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpPAMEventInfo",org.csapi.pam.TpPAMEventNameHelper.type(), members);
		}
		return _type;
	}
}
