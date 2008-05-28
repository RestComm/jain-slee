package org.csapi.pam;

/**
 *	Generated from IDL definition of union "TpPAMEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.pam.TpPAMEventName discriminator;
	private org.csapi.pam.TpPAMIPSEventData IdentityPresenceSet;
	private org.csapi.pam.TpPAMAVCEventData AvailabilityChanged;
	private org.csapi.pam.TpPAMWCEventData WatchersChanged;
	private org.csapi.pam.TpPAMICEventData IdentityCreated;
	private org.csapi.pam.TpPAMIDEventData IdentityDeleted;
	private org.csapi.pam.TpPAMGMCEventData GroupMembershipChanged;
	private org.csapi.pam.TpPAMACEventData AgentCreated;
	private org.csapi.pam.TpPAMADEventData AgentDeleted;
	private org.csapi.pam.TpPAMAAEventData AgentAssigned;
	private org.csapi.pam.TpPAMAUEventData AgentUnassigned;
	private org.csapi.pam.TpPAMCCEventData CapabilityChanged;
	private org.csapi.pam.TpPAMACPSEventData AgentCapabilityPresenceSet;
	private org.csapi.pam.TpPAMAPSEventData AgentPresenceSet;

	public TpPAMEventInfo ()
	{
	}

	public org.csapi.pam.TpPAMEventName discriminator ()
	{
		return discriminator;
	}

	public org.csapi.pam.TpPAMIPSEventData IdentityPresenceSet ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_PRESENCE_SET)
			throw new org.omg.CORBA.BAD_OPERATION();
		return IdentityPresenceSet;
	}

	public void IdentityPresenceSet (org.csapi.pam.TpPAMIPSEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_PRESENCE_SET;
		IdentityPresenceSet = _x;
	}

	public org.csapi.pam.TpPAMAVCEventData AvailabilityChanged ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AVAILABILITY_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AvailabilityChanged;
	}

	public void AvailabilityChanged (org.csapi.pam.TpPAMAVCEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AVAILABILITY_CHANGED;
		AvailabilityChanged = _x;
	}

	public org.csapi.pam.TpPAMWCEventData WatchersChanged ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_WATCHERS_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return WatchersChanged;
	}

	public void WatchersChanged (org.csapi.pam.TpPAMWCEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_WATCHERS_CHANGED;
		WatchersChanged = _x;
	}

	public org.csapi.pam.TpPAMICEventData IdentityCreated ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_CREATED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return IdentityCreated;
	}

	public void IdentityCreated (org.csapi.pam.TpPAMICEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_CREATED;
		IdentityCreated = _x;
	}

	public org.csapi.pam.TpPAMIDEventData IdentityDeleted ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_DELETED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return IdentityDeleted;
	}

	public void IdentityDeleted (org.csapi.pam.TpPAMIDEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_IDENTITY_DELETED;
		IdentityDeleted = _x;
	}

	public org.csapi.pam.TpPAMGMCEventData GroupMembershipChanged ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_GROUP_MEMBERSHIP_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return GroupMembershipChanged;
	}

	public void GroupMembershipChanged (org.csapi.pam.TpPAMGMCEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_GROUP_MEMBERSHIP_CHANGED;
		GroupMembershipChanged = _x;
	}

	public org.csapi.pam.TpPAMACEventData AgentCreated ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CREATED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentCreated;
	}

	public void AgentCreated (org.csapi.pam.TpPAMACEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CREATED;
		AgentCreated = _x;
	}

	public org.csapi.pam.TpPAMADEventData AgentDeleted ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_DELETED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentDeleted;
	}

	public void AgentDeleted (org.csapi.pam.TpPAMADEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_DELETED;
		AgentDeleted = _x;
	}

	public org.csapi.pam.TpPAMAAEventData AgentAssigned ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_ASSIGNED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentAssigned;
	}

	public void AgentAssigned (org.csapi.pam.TpPAMAAEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_ASSIGNED;
		AgentAssigned = _x;
	}

	public org.csapi.pam.TpPAMAUEventData AgentUnassigned ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_UNASSIGNED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentUnassigned;
	}

	public void AgentUnassigned (org.csapi.pam.TpPAMAUEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_UNASSIGNED;
		AgentUnassigned = _x;
	}

	public org.csapi.pam.TpPAMCCEventData CapabilityChanged ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_CAPABILITY_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return CapabilityChanged;
	}

	public void CapabilityChanged (org.csapi.pam.TpPAMCCEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_CAPABILITY_CHANGED;
		CapabilityChanged = _x;
	}

	public org.csapi.pam.TpPAMACPSEventData AgentCapabilityPresenceSet ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CAPABILITY_PRESENCE_SET)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentCapabilityPresenceSet;
	}

	public void AgentCapabilityPresenceSet (org.csapi.pam.TpPAMACPSEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_CAPABILITY_PRESENCE_SET;
		AgentCapabilityPresenceSet = _x;
	}

	public org.csapi.pam.TpPAMAPSEventData AgentPresenceSet ()
	{
		if (discriminator != org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_PRESENCE_SET)
			throw new org.omg.CORBA.BAD_OPERATION();
		return AgentPresenceSet;
	}

	public void AgentPresenceSet (org.csapi.pam.TpPAMAPSEventData _x)
	{
		discriminator = org.csapi.pam.TpPAMEventName.PAM_CE_AGENT_PRESENCE_SET;
		AgentPresenceSet = _x;
	}

}
