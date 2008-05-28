package org.csapi.cc.cccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpConfCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpConfCallControlManagerPOATie
	extends IpConfCallControlManagerPOA
{
	private IpConfCallControlManagerOperations _delegate;

	private POA _poa;
	public IpConfCallControlManagerPOATie(IpConfCallControlManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpConfCallControlManagerPOATie(IpConfCallControlManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.cccs.IpConfCallControlManager _this()
	{
		return org.csapi.cc.cccs.IpConfCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.cccs.IpConfCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.cccs.IpConfCallControlManagerHelper.narrow(_this_object(orb));
	}
	public IpConfCallControlManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpConfCallControlManagerOperations delegate)
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
	public void disableNotifications() throws org.csapi.TpCommonExceptions
	{
_delegate.disableNotifications();
	}

	public int createNotification(org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager appCallControlManager, org.csapi.cc.TpCallNotificationRequest notificationRequest) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createNotification(appCallControlManager,notificationRequest);
	}

	public org.csapi.cc.cccs.TpResourceReservation reserveResources(org.csapi.cc.cccs.IpAppConfCallControlManager appInterface, java.lang.String startTime, int numberOfParticipants, int duration, org.csapi.cc.cccs.TpConfPolicy conferencePolicy) throws org.csapi.TpCommonExceptions
	{
		return _delegate.reserveResources(appInterface,startTime,numberOfParticipants,duration,conferencePolicy);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public int setCallLoadControl(int duration, org.csapi.cc.TpCallLoadControlMechanism mechanism, org.csapi.cc.TpCallTreatment treatment, org.csapi.TpAddressRange addressRange) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN
	{
		return _delegate.setCallLoadControl(duration,mechanism,treatment,addressRange);
	}

	public int createMediaNotification(org.csapi.cc.mmccs.IpAppMultiMediaCallControlManager appInterface, org.csapi.cc.mmccs.TpNotificationMediaRequest notificationMediaRequest) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		return _delegate.createMediaNotification(appInterface,notificationMediaRequest);
	}

	public int enableNotifications(org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager appCallControlManager) throws org.csapi.TpCommonExceptions
	{
		return _delegate.enableNotifications(appCallControlManager);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void destroyMediaNotification(int assignmentID) throws org.csapi.TpCommonExceptions
	{
_delegate.destroyMediaNotification(assignmentID);
	}

	public org.csapi.cc.cccs.TpConfCallIdentifier createConference(org.csapi.cc.cccs.IpAppConfCall appConferenceCall, int numberOfSubConferences, org.csapi.cc.cccs.TpConfPolicy conferencePolicy, int numberOfParticipants, int duration) throws org.csapi.TpCommonExceptions
	{
		return _delegate.createConference(appConferenceCall,numberOfSubConferences,conferencePolicy,numberOfParticipants,duration);
	}

	public org.csapi.cc.cccs.TpConfSearchResult checkResources(org.csapi.cc.cccs.TpConfSearchCriteria searchCriteria) throws org.csapi.TpCommonExceptions
	{
		return _delegate.checkResources(searchCriteria);
	}

	public void freeResources(org.csapi.cc.cccs.TpResourceReservation resourceReservation) throws org.csapi.TpCommonExceptions
	{
_delegate.freeResources(resourceReservation);
	}

	public void changeNotification(int assignmentID, org.csapi.cc.TpCallNotificationRequest notificationRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
_delegate.changeNotification(assignmentID,notificationRequest);
	}

	public org.csapi.cc.TpNotificationRequested[] getNotification() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getNotification();
	}

	public org.csapi.cc.TpNotificationRequestedSetEntry getNextNotification(boolean reset) throws org.csapi.TpCommonExceptions
	{
		return _delegate.getNextNotification(reset);
	}

	public org.csapi.cc.mmccs.TpMediaNotificationRequested[] getMediaNotification() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getMediaNotification();
	}

	public void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
_delegate.destroyNotification(assignmentID);
	}

	public org.csapi.cc.mpccs.TpMultiPartyCallIdentifier createCall(org.csapi.cc.mpccs.IpAppMultiPartyCall appCall) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
		return _delegate.createCall(appCall);
	}

	public void changeMediaNotification(int assignmentID, org.csapi.cc.mmccs.TpNotificationMediaRequest notificationMediaRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
_delegate.changeMediaNotification(assignmentID,notificationMediaRequest);
	}

}
