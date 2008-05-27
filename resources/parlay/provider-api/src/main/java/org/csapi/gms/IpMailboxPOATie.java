package org.csapi.gms;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpMailbox"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpMailboxPOATie
	extends IpMailboxPOA
{
	private IpMailboxOperations _delegate;

	private POA _poa;
	public IpMailboxPOATie(IpMailboxOperations delegate)
	{
		_delegate = delegate;
	}
	public IpMailboxPOATie(IpMailboxOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.gms.IpMailbox _this()
	{
		return org.csapi.gms.IpMailboxHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpMailbox _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpMailboxHelper.narrow(_this_object(orb));
	}
	public IpMailboxOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpMailboxOperations delegate)
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

	public void lock(int mailboxSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOX
	{
_delegate.lock(mailboxSessionID);
	}

	public void createFolder(int mailboxSessionID, java.lang.String folderID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_MAILBOX_LOCKED
	{
_delegate.createFolder(mailboxSessionID,folderID);
	}

	public org.csapi.gms.TpMailboxFolderIdentifier openFolder(int mailboxSessionID, java.lang.String folderID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_FOLDER_IS_OPEN,org.csapi.gms.P_GMS_MAILBOX_LOCKED
	{
		return _delegate.openFolder(mailboxSessionID,folderID);
	}

	public void remove(org.csapi.TpAddress mailboxID, java.lang.String authenticationInfo) throws org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION,org.csapi.gms.P_GMS_INVALID_MAILBOX,org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_MAILBOX_OPEN,org.csapi.gms.P_GMS_MAILBOX_LOCKED,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE
	{
_delegate.remove(mailboxID,authenticationInfo);
	}

	public void unlock(int mailboxSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_UNLOCKING_UNLOCKED_MAILBOX,org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOX
	{
_delegate.unlock(mailboxSessionID);
	}

	public void close(int mailboxSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.close(mailboxSessionID);
	}

	public void setInfoProperties(int mailboxSessionID, int firstProperty, org.csapi.gms.TpMailboxInfoProperty[] mailboxInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_MAILBOX_LOCKED
	{
_delegate.setInfoProperties(mailboxSessionID,firstProperty,mailboxInfoProperties);
	}

	public org.csapi.gms.TpMailboxInfoProperty[] getInfoProperties(int mailboxSessionID, int firstProperty, int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE
	{
		return _delegate.getInfoProperties(mailboxSessionID,firstProperty,numberOfProperties);
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public int getInfoAmount(int mailboxSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		return _delegate.getInfoAmount(mailboxSessionID);
	}

}
