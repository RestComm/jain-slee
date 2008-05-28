package org.csapi.pam;

/**
 *	Generated from IDL definition of union "TpPAMNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMNotificationInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.pam.TpPAMEventName discriminator;
	private org.csapi.pam.TpPAMIPSNotificationData IdentityPresenceSetNotify;
	private org.csapi.pam.TpPAMAVCNotificationData AvailabilityChangedNotify;
	private org.csapi.pam.TpPAMWCNotificationData WatchersChangedNotify;
	private org.csapi.pam.TpPAMICNotificationData IdentityCreatedNotify;
	private org.csapi.pam.TpPAMIDNotificationData IdentityDeletedNotify;
	private org.csapi.pam.TpPAMGMCNotificationData GroupMembershipChangedNotify;
	private org.csapi.pam.TpPAMACNotificationData AgentCreatedNotify;
	private org.csapi.pam.TpPAMADNotificationData AgentDeletedNotify;
	private org.csapi.pam.TpPAMAANotificationData AgentAssignedNotify;
	private org.csapi.pam.TpPAMAUNotificationData AgentUnassignedNotify;
	private org.csapi.pam.TpPAMCCNotificationData CapabilityChangedNotify;
	private org.csapi.pam.TpPAMACPSNotificationData AgentCapabilityPresenceSetNotify;
	private org.csapi.pam.TpPAMAPSNotificationData AgentPresenceSetNotify;

	public TpPAMNotificationInfo ()
	{
	}

	public org.csapi.pam.TpPAMEventName discriminator ()
	{
		return discriminator;
	}

	public org.csapi.pam.TpPAMIPSNotificationData IdentityPresenceSetNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_PRESENCE_SET)
			throw new org.omg.CORBA.BAD_OPERATION();
		return IdentityPresenceSetNotify;
	}

	public void IdentityPresenceSetNotify (org.csapi.pam.TpPAMIPSNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_PRESENCE_SET;
		IdentityPresenceSetNotify = _x;
	}

	public org.csapi.pam.TpPAMAVCNotificationData AvailabilityChangedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AVAILABILITY_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AvailabilityChangedNotify;
	}

	public void AvailabilityChangedNotify (org.csapi.pam.TpPAMAVCNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AVAILABILITY_CHANGED;
		AvailabilityChangedNotify = _x;
	}

	public org.csapi.pam.TpPAMWCNotificationData WatchersChangedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_WATCHERS_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return WatchersChangedNotify;
	}

	public void WatchersChangedNotify (org.csapi.pam.TpPAMWCNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_WATCHERS_CHANGED;
		WatchersChangedNotify = _x;
	}

	public org.csapi.pam.TpPAMICNotificationData IdentityCreatedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_CREATED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return IdentityCreatedNotify;
	}

	public void IdentityCreatedNotify (org.csapi.pam.TpPAMICNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_CREATED;
		IdentityCreatedNotify = _x;
	}

	public org.csapi.pam.TpPAMIDNotificationData IdentityDeletedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_DELETED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return IdentityDeletedNotify;
	}

	public void IdentityDeletedNotify (org.csapi.pam.TpPAMIDNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_DELETED;
		IdentityDeletedNotify = _x;
	}

	public org.csapi.pam.TpPAMGMCNotificationData GroupMembershipChangedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_GROUP_MEMBERSHIP_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return GroupMembershipChangedNotify;
	}

	public void GroupMembershipChangedNotify (org.csapi.pam.TpPAMGMCNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_GROUP_MEMBERSHIP_CHANGED;
		GroupMembershipChangedNotify = _x;
	}

	public org.csapi.pam.TpPAMACNotificationData AgentCreatedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CREATED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentCreatedNotify;
	}

	public void AgentCreatedNotify (org.csapi.pam.TpPAMACNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CREATED;
		AgentCreatedNotify = _x;
	}

	public org.csapi.pam.TpPAMADNotificationData AgentDeletedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_DELETED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentDeletedNotify;
	}

	public void AgentDeletedNotify (org.csapi.pam.TpPAMADNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_DELETED;
		AgentDeletedNotify = _x;
	}

	public org.csapi.pam.TpPAMAANotificationData AgentAssignedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_ASSIGNED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentAssignedNotify;
	}

	public void AgentAssignedNotify (org.csapi.pam.TpPAMAANotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_ASSIGNED;
		AgentAssignedNotify = _x;
	}

	public org.csapi.pam.TpPAMAUNotificationData AgentUnassignedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_UNASSIGNED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentUnassignedNotify;
	}

	public void AgentUnassignedNotify (org.csapi.pam.TpPAMAUNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_UNASSIGNED;
		AgentUnassignedNotify = _x;
	}

	public org.csapi.pam.TpPAMCCNotificationData CapabilityChangedNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_CAPABILITY_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CapabilityChangedNotify;
	}

	public void CapabilityChangedNotify (org.csapi.pam.TpPAMCCNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_CAPABILITY_CHANGED;
		CapabilityChangedNotify = _x;
	}

	public org.csapi.pam.TpPAMACPSNotificationData AgentCapabilityPresenceSetNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CAPABILITY_PRESENCE_SET)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentCapabilityPresenceSetNotify;
	}

	public void AgentCapabilityPresenceSetNotify (org.csapi.pam.TpPAMACPSNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CAPABILITY_PRESENCE_SET;
		AgentCapabilityPresenceSetNotify = _x;
	}

	public org.csapi.pam.TpPAMAPSNotificationData AgentPresenceSetNotify ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_PRESENCE_SET)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentPresenceSetNotify;
	}

	public void AgentPresenceSetNotify (org.csapi.pam.TpPAMAPSNotificationData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_PRESENCE_SET;
		AgentPresenceSetNotify = _x;
	}

}
