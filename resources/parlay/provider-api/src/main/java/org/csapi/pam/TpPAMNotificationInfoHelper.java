package org.csapi.pam;

/**
 *	Generated from IDL definition of union "TpPAMNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMNotificationInfoHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.pam.TpPAMNotificationInfo s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.csapi.pam.TpPAMNotificationInfo extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/csapi/pam/TpPAMNotificationInfo:1.0";
	}
	public static TpPAMNotificationInfo read (org.omg.CORBA.portable.InputStream in)
	{
		TpPAMNotificationInfo result = new TpPAMNotificationInfo ();
		org.csapi.pam.TpPAMEventName disc = org.csapi.pam.TpPAMEventName.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMIPSNotificationData _var;
				_var=org.csapi.pam.TpPAMIPSNotificationDataHelper.read(in);
				result.IdentityPresenceSetNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AVAILABILITY_CHANGED:
			{
				org.csapi.pam.TpPAMAVCNotificationData _var;
				_var=org.csapi.pam.TpPAMAVCNotificationDataHelper.read(in);
				result.AvailabilityChangedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_WATCHERS_CHANGED:
			{
				org.csapi.pam.TpPAMWCNotificationData _var;
				_var=org.csapi.pam.TpPAMWCNotificationDataHelper.read(in);
				result.WatchersChangedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_CREATED:
			{
				org.csapi.pam.TpPAMICNotificationData _var;
				_var=org.csapi.pam.TpPAMICNotificationDataHelper.read(in);
				result.IdentityCreatedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_DELETED:
			{
				org.csapi.pam.TpPAMIDNotificationData _var;
				_var=org.csapi.pam.TpPAMIDNotificationDataHelper.read(in);
				result.IdentityDeletedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_GROUP_MEMBERSHIP_CHANGED:
			{
				org.csapi.pam.TpPAMGMCNotificationData _var;
				_var=org.csapi.pam.TpPAMGMCNotificationDataHelper.read(in);
				result.GroupMembershipChangedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_CREATED:
			{
				org.csapi.pam.TpPAMACNotificationData _var;
				_var=org.csapi.pam.TpPAMACNotificationDataHelper.read(in);
				result.AgentCreatedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_DELETED:
			{
				org.csapi.pam.TpPAMADNotificationData _var;
				_var=org.csapi.pam.TpPAMADNotificationDataHelper.read(in);
				result.AgentDeletedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_ASSIGNED:
			{
				org.csapi.pam.TpPAMAANotificationData _var;
				_var=org.csapi.pam.TpPAMAANotificationDataHelper.read(in);
				result.AgentAssignedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_UNASSIGNED:
			{
				org.csapi.pam.TpPAMAUNotificationData _var;
				_var=org.csapi.pam.TpPAMAUNotificationDataHelper.read(in);
				result.AgentUnassignedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_CAPABILITY_CHANGED:
			{
				org.csapi.pam.TpPAMCCNotificationData _var;
				_var=org.csapi.pam.TpPAMCCNotificationDataHelper.read(in);
				result.CapabilityChangedNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_CAPABILITY_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMACPSNotificationData _var;
				_var=org.csapi.pam.TpPAMACPSNotificationDataHelper.read(in);
				result.AgentCapabilityPresenceSetNotify (_var);
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMAPSNotificationData _var;
				_var=org.csapi.pam.TpPAMAPSNotificationDataHelper.read(in);
				result.AgentPresenceSetNotify (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TpPAMNotificationInfo s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMIPSNotificationDataHelper.write(out,s.IdentityPresenceSetNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AVAILABILITY_CHANGED:
			{
				org.csapi.pam.TpPAMAVCNotificationDataHelper.write(out,s.AvailabilityChangedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_WATCHERS_CHANGED:
			{
				org.csapi.pam.TpPAMWCNotificationDataHelper.write(out,s.WatchersChangedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_CREATED:
			{
				org.csapi.pam.TpPAMICNotificationDataHelper.write(out,s.IdentityCreatedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_IDENTITY_DELETED:
			{
				org.csapi.pam.TpPAMIDNotificationDataHelper.write(out,s.IdentityDeletedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_GROUP_MEMBERSHIP_CHANGED:
			{
				org.csapi.pam.TpPAMGMCNotificationDataHelper.write(out,s.GroupMembershipChangedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_CREATED:
			{
				org.csapi.pam.TpPAMACNotificationDataHelper.write(out,s.AgentCreatedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_DELETED:
			{
				org.csapi.pam.TpPAMADNotificationDataHelper.write(out,s.AgentDeletedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_ASSIGNED:
			{
				org.csapi.pam.TpPAMAANotificationDataHelper.write(out,s.AgentAssignedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_UNASSIGNED:
			{
				org.csapi.pam.TpPAMAUNotificationDataHelper.write(out,s.AgentUnassignedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_CAPABILITY_CHANGED:
			{
				org.csapi.pam.TpPAMCCNotificationDataHelper.write(out,s.CapabilityChangedNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_CAPABILITY_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMACPSNotificationDataHelper.write(out,s.AgentCapabilityPresenceSetNotify ());
				break;
			}
			case org.csapi.pam.TpPAMEventName._PAM_CE_AGENT_PRESENCE_SET:
			{
				org.csapi.pam.TpPAMAPSNotificationDataHelper.write(out,s.AgentPresenceSetNotify ());
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
			members[12] = new org.omg.CORBA.UnionMember ("IdentityPresenceSetNotify", label_any, org.csapi.pam.TpPAMIPSNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AVAILABILITY_CHANGED);
			members[11] = new org.omg.CORBA.UnionMember ("AvailabilityChangedNotify", label_any, org.csapi.pam.TpPAMAVCNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_WATCHERS_CHANGED);
			members[10] = new org.omg.CORBA.UnionMember ("WatchersChangedNotify", label_any, org.csapi.pam.TpPAMWCNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_CREATED);
			members[9] = new org.omg.CORBA.UnionMember ("IdentityCreatedNotify", label_any, org.csapi.pam.TpPAMICNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_DELETED);
			members[8] = new org.omg.CORBA.UnionMember ("IdentityDeletedNotify", label_any, org.csapi.pam.TpPAMIDNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_GROUP_MEMBERSHIP_CHANGED);
			members[7] = new org.omg.CORBA.UnionMember ("GroupMembershipChangedNotify", label_any, org.csapi.pam.TpPAMGMCNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CREATED);
			members[6] = new org.omg.CORBA.UnionMember ("AgentCreatedNotify", label_any, org.csapi.pam.TpPAMACNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_DELETED);
			members[5] = new org.omg.CORBA.UnionMember ("AgentDeletedNotify", label_any, org.csapi.pam.TpPAMADNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_ASSIGNED);
			members[4] = new org.omg.CORBA.UnionMember ("AgentAssignedNotify", label_any, org.csapi.pam.TpPAMAANotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_UNASSIGNED);
			members[3] = new org.omg.CORBA.UnionMember ("AgentUnassignedNotify", label_any, org.csapi.pam.TpPAMAUNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_CAPABILITY_CHANGED);
			members[2] = new org.omg.CORBA.UnionMember ("CapabilityChangedNotify", label_any, org.csapi.pam.TpPAMCCNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CAPABILITY_PRESENCE_SET);
			members[1] = new org.omg.CORBA.UnionMember ("AgentCapabilityPresenceSetNotify", label_any, org.csapi.pam.TpPAMACPSNotificationDataHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.csapi.pam.TpPAMEventNameHelper.insert(label_any, org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_PRESENCE_SET);
			members[0] = new org.omg.CORBA.UnionMember ("AgentPresenceSetNotify", label_any, org.csapi.pam.TpPAMAPSNotificationDataHelper.type(),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TpPAMNotificationInfo",org.csapi.pam.TpPAMEventNameHelper.type(), members);
		}
		return _type;
	}
}
