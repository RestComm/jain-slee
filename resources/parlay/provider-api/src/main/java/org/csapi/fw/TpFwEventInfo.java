package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpFwEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpFwEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.fw.TpFwEventName discriminator;
	private java.lang.String EventNameUndefined;
	private java.lang.String[] ServiceIDList;
	private java.lang.String[] UnavailableServiceIDList;

	public TpFwEventInfo ()
	{
	}

	public org.csapi.fw.TpFwEventName discriminator ()
	{
		return discriminator;
	}

	public java.lang.String EventNameUndefined ()
	{
		if (discriminator != org.csapi.fw.TpFwEventName.P_EVENT_FW_NAME_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return EventNameUndefined;
	}

	public void EventNameUndefined (java.lang.String _x)
	{
		discriminator = org.csapi.fw.TpFwEventName.P_EVENT_FW_NAME_UNDEFINED;
		EventNameUndefined = _x;
	}

	public java.lang.String[] ServiceIDList ()
	{
		if (discriminator != org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_AVAILABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ServiceIDList;
	}

	public void ServiceIDList (java.lang.String[] _x)
	{
		discriminator = org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_AVAILABLE;
		ServiceIDList = _x;
	}

	public java.lang.String[] UnavailableServiceIDList ()
	{
		if (discriminator != org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_UNAVAILABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return UnavailableServiceIDList;
	}

	public void UnavailableServiceIDList (java.lang.String[] _x)
	{
		discriminator = org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_UNAVAILABLE;
		UnavailableServiceIDList = _x;
	}

}
