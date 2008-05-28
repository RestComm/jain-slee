package org.csapi.fw;

/**
 *	Generated from IDL definition of union "TpFwEventCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpFwEventCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.fw.TpFwEventName discriminator;
	private java.lang.String EventNameUndefined;
	private java.lang.String[] ServiceTypeNameList;
	private java.lang.String[] UnavailableServiceTypeNameList;

	public TpFwEventCriteria ()
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

	public java.lang.String[] ServiceTypeNameList ()
	{
		if (discriminator != org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_AVAILABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return ServiceTypeNameList;
	}

	public void ServiceTypeNameList (java.lang.String[] _x)
	{
		discriminator = org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_AVAILABLE;
		ServiceTypeNameList = _x;
	}

	public java.lang.String[] UnavailableServiceTypeNameList ()
	{
		if (discriminator != org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_UNAVAILABLE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return UnavailableServiceTypeNameList;
	}

	public void UnavailableServiceTypeNameList (java.lang.String[] _x)
	{
		discriminator = org.csapi.fw.TpFwEventName.P_EVENT_FW_SERVICE_UNAVAILABLE;
		UnavailableServiceTypeNameList = _x;
	}

}
