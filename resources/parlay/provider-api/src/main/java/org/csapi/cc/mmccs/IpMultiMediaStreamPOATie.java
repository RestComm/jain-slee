package org.csapi.cc.mmccs;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpMultiMediaStream"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpMultiMediaStreamPOATie
	extends IpMultiMediaStreamPOA
{
	private IpMultiMediaStreamOperations _delegate;

	private POA _poa;
	public IpMultiMediaStreamPOATie(IpMultiMediaStreamOperations delegate)
	{
		_delegate = delegate;
	}
	public IpMultiMediaStreamPOATie(IpMultiMediaStreamOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cc.mmccs.IpMultiMediaStream _this()
	{
		return org.csapi.cc.mmccs.IpMultiMediaStreamHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpMultiMediaStream _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpMultiMediaStreamHelper.narrow(_this_object(orb));
	}
	public IpMultiMediaStreamOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpMultiMediaStreamOperations delegate)
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

	public void subtract(int mediaStreamSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.subtract(mediaStreamSessionID);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
