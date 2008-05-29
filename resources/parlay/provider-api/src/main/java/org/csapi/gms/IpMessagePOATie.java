package org.csapi.gms;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpMessage"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpMessagePOATie
	extends IpMessagePOA
{
	private IpMessageOperations _delegate;

	private POA _poa;
	public IpMessagePOATie(IpMessageOperations delegate)
	{
		_delegate = delegate;
	}
	public IpMessagePOATie(IpMessageOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.gms.IpMessage _this()
	{
		return org.csapi.gms.IpMessageHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpMessage _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpMessageHelper.narrow(_this_object(orb));
	}
	public IpMessageOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpMessageOperations delegate)
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
	public void setInfoProperties(int folderSessionID, java.lang.String messageID, int firstProperty, org.csapi.gms.TpMessageInfoProperty[] messageInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setInfoProperties(folderSessionID,messageID,firstProperty,messageInfoProperties);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public java.lang.String getContent(int folderSessionID, java.lang.String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getContent(folderSessionID,messageID);
	}

	public org.csapi.gms.TpMessageInfoProperty[] getInfoProperties(int folderSessionID, java.lang.String messageID, int firstProperty, int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE
	{
		return _delegate.getInfoProperties(folderSessionID,messageID,firstProperty,numberOfProperties);
	}

	public int getInfoAmount(int folderSessionID, java.lang.String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getInfoAmount(folderSessionID,messageID);
	}

	public void remove(int folderSessionID, java.lang.String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVED,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE
	{
_delegate.remove(folderSessionID,messageID);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
