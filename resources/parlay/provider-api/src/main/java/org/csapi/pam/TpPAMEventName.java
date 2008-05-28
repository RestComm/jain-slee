package org.csapi.pam;
/**
 *	Generated from IDL definition of enum "TpPAMEventName"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventName
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _PAM_CE_IDENTITY_PRESENCE_SET = 0;
	public static final TpPAMEventName PAM_CE_IDENTITY_PRESENCE_SET = new TpPAMEventName(_PAM_CE_IDENTITY_PRESENCE_SET);
	public static final int _PAM_CE_AVAILABILITY_CHANGED = 1;
	public static final TpPAMEventName PAM_CE_AVAILABILITY_CHANGED = new TpPAMEventName(_PAM_CE_AVAILABILITY_CHANGED);
	public static final int _PAM_CE_WATCHERS_CHANGED = 2;
	public static final TpPAMEventName PAM_CE_WATCHERS_CHANGED = new TpPAMEventName(_PAM_CE_WATCHERS_CHANGED);
	public static final int _PAM_CE_IDENTITY_CREATED = 3;
	public static final TpPAMEventName PAM_CE_IDENTITY_CREATED = new TpPAMEventName(_PAM_CE_IDENTITY_CREATED);
	public static final int _PAM_CE_IDENTITY_DELETED = 4;
	public static final TpPAMEventName PAM_CE_IDENTITY_DELETED = new TpPAMEventName(_PAM_CE_IDENTITY_DELETED);
	public static final int _PAM_CE_GROUP_MEMBERSHIP_CHANGED = 5;
	public static final TpPAMEventName PAM_CE_GROUP_MEMBERSHIP_CHANGED = new TpPAMEventName(_PAM_CE_GROUP_MEMBERSHIP_CHANGED);
	public static final int _PAM_CE_AGENT_CREATED = 6;
	public static final TpPAMEventName PAM_CE_AGENT_CREATED = new TpPAMEventName(_PAM_CE_AGENT_CREATED);
	public static final int _PAM_CE_AGENT_DELETED = 7;
	public static final TpPAMEventName PAM_CE_AGENT_DELETED = new TpPAMEventName(_PAM_CE_AGENT_DELETED);
	public static final int _PAM_CE_AGENT_ASSIGNED = 8;
	public static final TpPAMEventName PAM_CE_AGENT_ASSIGNED = new TpPAMEventName(_PAM_CE_AGENT_ASSIGNED);
	public static final int _PAM_CE_AGENT_UNASSIGNED = 9;
	public static final TpPAMEventName PAM_CE_AGENT_UNASSIGNED = new TpPAMEventName(_PAM_CE_AGENT_UNASSIGNED);
	public static final int _PAM_CE_CAPABILITY_CHANGED = 10;
	public static final TpPAMEventName PAM_CE_CAPABILITY_CHANGED = new TpPAMEventName(_PAM_CE_CAPABILITY_CHANGED);
	public static final int _PAM_CE_AGENT_CAPABILITY_PRESENCE_SET = 11;
	public static final TpPAMEventName PAM_CE_AGENT_CAPABILITY_PRESENCE_SET = new TpPAMEventName(_PAM_CE_AGENT_CAPABILITY_PRESENCE_SET);
	public static final int _PAM_CE_AGENT_PRESENCE_SET = 12;
	public static final TpPAMEventName PAM_CE_AGENT_PRESENCE_SET = new TpPAMEventName(_PAM_CE_AGENT_PRESENCE_SET);
	public int value()
	{
		return value;
	}
	public static TpPAMEventName from_int(int value)
	{
		switch (value) {
			case _PAM_CE_IDENTITY_PRESENCE_SET: return PAM_CE_IDENTITY_PRESENCE_SET;
			case _PAM_CE_AVAILABILITY_CHANGED: return PAM_CE_AVAILABILITY_CHANGED;
			case _PAM_CE_WATCHERS_CHANGED: return PAM_CE_WATCHERS_CHANGED;
			case _PAM_CE_IDENTITY_CREATED: return PAM_CE_IDENTITY_CREATED;
			case _PAM_CE_IDENTITY_DELETED: return PAM_CE_IDENTITY_DELETED;
			case _PAM_CE_GROUP_MEMBERSHIP_CHANGED: return PAM_CE_GROUP_MEMBERSHIP_CHANGED;
			case _PAM_CE_AGENT_CREATED: return PAM_CE_AGENT_CREATED;
			case _PAM_CE_AGENT_DELETED: return PAM_CE_AGENT_DELETED;
			case _PAM_CE_AGENT_ASSIGNED: return PAM_CE_AGENT_ASSIGNED;
			case _PAM_CE_AGENT_UNASSIGNED: return PAM_CE_AGENT_UNASSIGNED;
			case _PAM_CE_CAPABILITY_CHANGED: return PAM_CE_CAPABILITY_CHANGED;
			case _PAM_CE_AGENT_CAPABILITY_PRESENCE_SET: return PAM_CE_AGENT_CAPABILITY_PRESENCE_SET;
			case _PAM_CE_AGENT_PRESENCE_SET: return PAM_CE_AGENT_PRESENCE_SET;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpPAMEventName(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
