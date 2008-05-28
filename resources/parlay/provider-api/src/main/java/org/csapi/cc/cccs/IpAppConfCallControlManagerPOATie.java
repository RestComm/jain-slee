package org.csapi.cc.cccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppConfCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppConfCallControlManagerPOATie
	extends IpAppConfCallControlManagerPOA
{
	private IpAppConfCallControlManagerOperations _delegate;

	private POA _poa;
	public IpAppConfCallControlManagerPOATie(IpAppConfCallControlManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppConfCallControlManagerPOATie(IpAppConfCallControlManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.cccs.IpAppConfCallControlManager _this()
	{
		return org.csapi.cc.cccs.IpAppConfCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.cccs.IpAppConfCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.cccs.IpAppConfCallControlManagerHelper.narrow(_this_object(orb));
	}
	public IpAppConfCallControlManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppConfCallControlManagerOperations delegate)
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
	public void callAborted(int callReference)
	{
_delegate.callAborted(callReference);
	}

	public void callOverloadCeased(int assignmentID)
	{
_delegate.callOverloadCeased(assignmentID);
	}

	public org.csapi.cc.mmccs.TpAppMultiMediaCallBack reportMediaNotification(org.csapi.cc.mmccs.TpMultiMediaCallIdentifier callReference, org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier[] callLegReferenceSet, org.csapi.cc.mmccs.TpMediaStream[] mediaStreams, org.csapi.cc.mmccs.TpMediaStreamEventType type, int assignmentID)
	{
		return _delegate.reportMediaNotification(callReference,callLegReferenceSet,mediaStreams,type,assignmentID);
	}

	public void managerResumed()
	{
_delegate.managerResumed();
	}

	public void managerInterrupted()
	{
_delegate.managerInterrupted();
	}

	public void callOverloadEncountered(int assignmentID)
	{
_delegate.callOverloadEncountered(assignmentID);
	}

	public org.csapi.cc.cccs.IpAppConfCall conferenceCreated(org.csapi.cc.cccs.TpConfCallIdentifier conferenceCall)
	{
		return _delegate.conferenceCreated(conferenceCall);
	}

	public org.csapi.cc.mpccs.TpAppMultiPartyCallBack reportNotification(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier callReference, org.csapi.cc.mpccs.TpCallLegIdentifier[] callLegReferenceSet, org.csapi.cc.TpCallNotificationInfo notificationInfo, int assignmentID)
	{
		return _delegate.reportNotification(callReference,callLegReferenceSet,notificationInfo,assignmentID);
	}

}
