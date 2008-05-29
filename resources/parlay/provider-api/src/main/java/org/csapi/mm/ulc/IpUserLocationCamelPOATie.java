package org.csapi.mm.ulc;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpUserLocationCamelPOATie
	extends IpUserLocationCamelPOA
{
	private IpUserLocationCamelOperations _delegate;

	private POA _poa;
	public IpUserLocationCamelPOATie(IpUserLocationCamelOperations delegate)
	{
		_delegate = delegate;
	}
	public IpUserLocationCamelPOATie(IpUserLocationCamelOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.mm.ulc.IpUserLocationCamel _this()
	{
		return org.csapi.mm.ulc.IpUserLocationCamelHelper.narrow(_this_object());
	}
	public org.csapi.mm.ulc.IpUserLocationCamel _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ulc.IpUserLocationCamelHelper.narrow(_this_object(orb));
	}
	public IpUserLocationCamelOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpUserLocationCamelOperations delegate)
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
	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void triggeredLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.triggeredLocationReportingStop(stopRequest);
	}

	public void periodicLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.periodicLocationReportingStop(stopRequest);
	}

	public int triggeredLocationReportingStartReq(org.csapi.mm.ulc.IpAppUserLocationCamel appLocationCamel, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationTriggerCamel trigger) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER
	{
		return _delegate.triggeredLocationReportingStartReq(appLocationCamel,users,trigger);
	}

	public int periodicLocationReportingStartReq(org.csapi.mm.ulc.IpAppUserLocationCamel appLocationCamel, org.csapi.TpAddress[] users, int reportingInterval) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_INVALID_REPORTING_INTERVAL,org.csapi.P_UNKNOWN_SUBSCRIBER
	{
		return _delegate.periodicLocationReportingStartReq(appLocationCamel,users,reportingInterval);
	}

	public int locationReportReq(org.csapi.mm.ulc.IpAppUserLocationCamel appLocationCamel, org.csapi.TpAddress[] users) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER
	{
		return _delegate.locationReportReq(appLocationCamel,users);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
