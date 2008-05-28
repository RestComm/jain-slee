package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpMessagingEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingEventCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.gms.TpMessagingEventName discriminator;
	private org.csapi.gms.TpGMSNewMessageArrivedCriteria EventGMSNewMessageArrived;
	private short Dummy;

	public TpMessagingEventCriteria ()
	{
	}

	public org.csapi.gms.TpMessagingEventName discriminator ()
	{
		return discriminator;
	}

	public org.csapi.gms.TpGMSNewMessageArrivedCriteria EventGMSNewMessageArrived ()
	{
		if (discriminator != org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NEW_MESSAGE_ARRIVED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return EventGMSNewMessageArrived;
	}

	public void EventGMSNewMessageArrived (org.csapi.gms.TpGMSNewMessageArrivedCriteria _x)
	{
		discriminator = org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NEW_MESSAGE_ARRIVED;
		EventGMSNewMessageArrived = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NAME_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NAME_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.gms.TpMessagingEventName _discriminator, short _x)
	{
		if (_discriminator != org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NAME_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
