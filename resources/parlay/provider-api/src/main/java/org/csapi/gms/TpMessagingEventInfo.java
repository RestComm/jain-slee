package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpMessagingEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpMessagingEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.gms.TpMessagingEventName discriminator;
	private java.lang.String EventNameUndefined;
	private org.csapi.gms.TpGMSNewMessageArrivedInfo EventGMSNewMessageArrived;

	public TpMessagingEventInfo ()
	{
	}

	public org.csapi.gms.TpMessagingEventName discriminator ()
	{
		return discriminator;
	}

	public java.lang.String EventNameUndefined ()
	{
		if (discriminator != org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NAME_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return EventNameUndefined;
	}

	public void EventNameUndefined (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NAME_UNDEFINED;
		EventNameUndefined = _x;
	}

	public org.csapi.gms.TpGMSNewMessageArrivedInfo EventGMSNewMessageArrived ()
	{
		if (discriminator != org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NEW_MESSAGE_ARRIVED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return EventGMSNewMessageArrived;
	}

	public void EventGMSNewMessageArrived (org.csapi.gms.TpGMSNewMessageArrivedInfo _x)
	{
		discriminator = org.csapi.gms.TpMessagingEventName.P_EVENT_GMS_NEW_MESSAGE_ARRIVED;
		EventGMSNewMessageArrived = _x;
	}

}
