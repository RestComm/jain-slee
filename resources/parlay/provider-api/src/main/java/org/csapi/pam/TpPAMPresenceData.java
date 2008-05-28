package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMPresenceData"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMPresenceData
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpPAMPresenceData(){}
	public java.lang.String Name;
	public java.lang.String subscriberStatus;
	public java.lang.String networkStatus;
	public java.lang.String communicationMeans;
	public org.csapi.TpAddress contactAddress;
	public java.lang.String subscriberProvidedLocation;
	public java.lang.String networkProvidedLocation;
	public int Priority;
	public java.lang.String otherInfo;
	public TpPAMPresenceData(java.lang.String Name, java.lang.String subscriberStatus, java.lang.String networkStatus, java.lang.String communicationMeans, org.csapi.TpAddress contactAddress, java.lang.String subscriberProvidedLocation, java.lang.String networkProvidedLocation, int Priority, java.lang.String otherInfo)
	{
		this.Name = Name;
		this.subscriberStatus = subscriberStatus;
		this.networkStatus = networkStatus;
		this.communicationMeans = communicationMeans;
		this.contactAddress = contactAddress;
		this.subscriberProvidedLocation = subscriberProvidedLocation;
		this.networkProvidedLocation = networkProvidedLocation;
		this.Priority = Priority;
		this.otherInfo = otherInfo;
	}
}
