package org.csapi.mm.ul;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpTriggeredUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpTriggeredUserLocationPOATie
	extends IpTriggeredUserLocationPOA
{
	private IpTriggeredUserLocationOperations _delegate;

	private POA _poa;
	public IpTriggeredUserLocationPOATie(IpTriggeredUserLocationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpTriggeredUserLocationPOATie(IpTriggeredUserLocationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.mm.ul.IpTriggeredUserLocation _this()
	{
		return org.csapi.mm.ul.IpTriggeredUserLocationHelper.narrow(_this_object());
	}
	public org.csapi.mm.ul.IpTriggeredUserLocation _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ul.IpTriggeredUserLocationHelper.narrow(_this_object(orb));
	}
	public IpTriggeredUserLocationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpTriggeredUserLocationOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public int triggeredLocationReportingStartReq(org.csapi.mm.ul.IpAppTriggeredUserLocation appLocation, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationRequest request, org.csapi.mm.TpLocationTrigger[] triggers) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED,org.csapi.P_UNKNOWN_SUBSCRIBER
	{
		return _delegate.triggeredLocationReportingStartReq(appLocation,users,request,triggers);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public int periodicLocationReportingStartReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationRequest request, int reportingInterval) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_INVALID_REPORTING_INTERVAL,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED
	{
		return _delegate.periodicLocationReportingStartReq(appLocation,users,request,reportingInterval);
	}

	public void periodicLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.periodicLocationReportingStop(stopRequest);
	}

	public void triggeredLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.triggeredLocationReportingStop(stopRequest);
	}

	public int locationReportReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED
	{
		return _delegate.locationReportReq(appLocation,users);
	}

	public int extendedLocationReportReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationRequest request) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED
	{
		return _delegate.extendedLocationReportReq(appLocation,users,request);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
