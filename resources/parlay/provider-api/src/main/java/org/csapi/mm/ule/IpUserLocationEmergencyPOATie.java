package org.csapi.mm.ule;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpUserLocationEmergencyPOATie
	extends IpUserLocationEmergencyPOA
{
	private IpUserLocationEmergencyOperations _delegate;

	private POA _poa;
	public IpUserLocationEmergencyPOATie(IpUserLocationEmergencyOperations delegate)
	{
		_delegate = delegate;
	}
	public IpUserLocationEmergencyPOATie(IpUserLocationEmergencyOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.mm.ule.IpUserLocationEmergency _this()
	{
		return org.csapi.mm.ule.IpUserLocationEmergencyHelper.narrow(_this_object());
	}
	public org.csapi.mm.ule.IpUserLocationEmergency _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ule.IpUserLocationEmergencyHelper.narrow(_this_object(orb));
	}
	public IpUserLocationEmergencyOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpUserLocationEmergencyOperations delegate)
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

	public int emergencyLocationReportReq(org.csapi.mm.ule.IpAppUserLocationEmergency appEmergencyLocation, org.csapi.mm.TpUserLocationEmergencyRequest request) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER
	{
		return _delegate.emergencyLocationReportReq(appEmergencyLocation,request);
	}

	public void unSubscribeEmergencyLocationReports(int assignmentId) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.unSubscribeEmergencyLocationReports(assignmentId);
	}

	public int subscribeEmergencyLocationReports(org.csapi.mm.ule.IpAppUserLocationEmergency appEmergencyLocation) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
		return _delegate.subscribeEmergencyLocationReports(appEmergencyLocation);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
