package org.csapi.ui;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpUI"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpUIPOATie
	extends IpUIPOA
{
	private IpUIOperations _delegate;

	private POA _poa;
	public IpUIPOATie(IpUIOperations delegate)
	{
		_delegate = delegate;
	}
	public IpUIPOATie(IpUIOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.ui.IpUI _this()
	{
		return org.csapi.ui.IpUIHelper.narrow(_this_object());
	}
	public org.csapi.ui.IpUI _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.ui.IpUIHelper.narrow(_this_object(orb));
	}
	public IpUIOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpUIOperations delegate)
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

	public int sendInfoReq(int userInteractionSessionID, org.csapi.ui.TpUIInfo info, java.lang.String language, org.csapi.ui.TpUIVariableInfo[] variableInfo, int repeatIndicator, int responseRequested) throws org.csapi.ui.P_ILLEGAL_ID,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.ui.P_ID_NOT_FOUND,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.sendInfoReq(userInteractionSessionID,info,language,variableInfo,repeatIndicator,responseRequested);
	}

	public java.lang.String getOriginatingAddress(int userInteractionSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getOriginatingAddress(userInteractionSessionID);
	}

	public void setOriginatingAddress(int userInteractionSessionID, java.lang.String origin) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setOriginatingAddress(userInteractionSessionID,origin);
	}

	public int sendInfoAndCollectReq(int userInteractionSessionID, org.csapi.ui.TpUIInfo info, java.lang.String language, org.csapi.ui.TpUIVariableInfo[] variableInfo, org.csapi.ui.TpUICollectCriteria criteria, int responseRequested) throws org.csapi.ui.P_ILLEGAL_ID,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.ui.P_ILLEGAL_RANGE,org.csapi.ui.P_ID_NOT_FOUND,org.csapi.P_INVALID_SESSION_ID,org.csapi.ui.P_INVALID_COLLECTION_CRITERIA
	{
		return _delegate.sendInfoAndCollectReq(userInteractionSessionID,info,language,variableInfo,criteria,responseRequested);
	}

	public void release(int userInteractionSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.release(userInteractionSessionID);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
