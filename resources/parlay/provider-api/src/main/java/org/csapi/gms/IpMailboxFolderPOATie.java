package org.csapi.gms;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpMailboxFolder"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpMailboxFolderPOATie
	extends IpMailboxFolderPOA
{
	private IpMailboxFolderOperations _delegate;

	private POA _poa;
	public IpMailboxFolderPOATie(IpMailboxFolderOperations delegate)
	{
		_delegate = delegate;
	}
	public IpMailboxFolderPOATie(IpMailboxFolderOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.gms.IpMailboxFolder _this()
	{
		return org.csapi.gms.IpMailboxFolderHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpMailboxFolder _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpMailboxFolderHelper.narrow(_this_object(orb));
	}
	public IpMailboxFolderOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpMailboxFolderOperations delegate)
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
	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public void close(int mailboxSessionID, int folderSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.close(mailboxSessionID,folderSessionID);
	}

	public void putMessage(int folderSessionID, java.lang.String message, org.csapi.gms.TpMessageInfoProperty[] messageInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.putMessage(folderSessionID,message,messageInfoProperties);
	}

	public org.csapi.gms.IpMessage getMessage(int folderSessionID, java.lang.String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getMessage(folderSessionID,messageID);
	}

	public void remove(int mailboxSessionID, java.lang.String folderID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_FOLDER_IS_OPEN,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE
	{
_delegate.remove(mailboxSessionID,folderID);
	}

	public void setInfoProperties(int folderSessionID, int firstProperty, org.csapi.gms.TpFolderInfoProperty[] folderInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setInfoProperties(folderSessionID,firstProperty,folderInfoProperties);
	}

	public org.csapi.gms.TpFolderInfoProperty[] getInfoProperties(int folderSessionID, int firstProperty, int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE
	{
		return _delegate.getInfoProperties(folderSessionID,firstProperty,numberOfProperties);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public int getInfoAmount(int folderSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getInfoAmount(folderSessionID);
	}

}
