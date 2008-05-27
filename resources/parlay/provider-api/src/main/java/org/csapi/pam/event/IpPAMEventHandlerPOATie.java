package org.csapi.pam.event;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMEventHandlerPOATie
	extends IpPAMEventHandlerPOA
{
	private IpPAMEventHandlerOperations _delegate;

	private POA _poa;
	public IpPAMEventHandlerPOATie(IpPAMEventHandlerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMEventHandlerPOATie(IpPAMEventHandlerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.event.IpPAMEventHandler _this()
	{
		return org.csapi.pam.event.IpPAMEventHandlerHelper.narrow(_this_object());
	}
	public org.csapi.pam.event.IpPAMEventHandler _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.event.IpPAMEventHandlerHelper.narrow(_this_object(orb));
	}
	public IpPAMEventHandlerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMEventHandlerOperations delegate)
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
	public void deregisterFromEvent(int eventID, byte[] authToken) throws org.csapi.pam.P_PAM_NOT_REGISTERED,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.deregisterFromEvent(eventID,authToken);
	}

	public int registerAppInterface(org.csapi.pam.event.IpAppPAMEventHandler appInterface, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.registerAppInterface(appInterface,authToken);
	}

	public boolean isRegistered(int clientID, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.isRegistered(clientID,authToken);
	}

	public int registerForEvent(int clientID, org.csapi.pam.TpPAMEventInfo[] eventList, int validFor, byte[] authToken) throws org.csapi.pam.P_PAM_NOT_REGISTERED,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.registerForEvent(clientID,eventList,validFor,authToken);
	}

	public void deregisterAppInterface(int clientID, byte[] authToken) throws org.csapi.pam.P_PAM_NOT_REGISTERED,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.deregisterAppInterface(clientID,authToken);
	}

}
