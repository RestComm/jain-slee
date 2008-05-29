package org.csapi.gms;


/**
 *	Generated from IDL interface "IpAppMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppMessagingManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.gms.IpAppMessagingManager
{
	private String[] ids = {"IDL:org/csapi/gms/IpAppMessagingManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.gms.IpAppMessagingManagerOperations.class;
	public void mailboxTerminated(org.csapi.gms.IpMailbox mailbox, int mailboxSessionID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "mailboxTerminated", true);
				org.csapi.gms.IpMailboxHelper.write(_os,mailbox);
				_os.write_long(mailboxSessionID);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "mailboxTerminated", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppMessagingManagerOperations _localServant = (IpAppMessagingManagerOperations)_so.servant;
			try
			{
			_localServant.mailboxTerminated(mailbox,mailboxSessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void messagingEventNotify(org.csapi.gms.IpMessagingManager messagingManager, org.csapi.gms.TpMessagingEventInfo eventInfo, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "messagingEventNotify", true);
				org.csapi.gms.IpMessagingManagerHelper.write(_os,messagingManager);
				org.csapi.gms.TpMessagingEventInfoHelper.write(_os,eventInfo);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "messagingEventNotify", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppMessagingManagerOperations _localServant = (IpAppMessagingManagerOperations)_so.servant;
			try
			{
			_localServant.messagingEventNotify(messagingManager,eventInfo,assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void messagingNotificationTerminated()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "messagingNotificationTerminated", true);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "messagingNotificationTerminated", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppMessagingManagerOperations _localServant = (IpAppMessagingManagerOperations)_so.servant;
			try
			{
			_localServant.messagingNotificationTerminated();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void mailboxFaultDetected(org.csapi.gms.IpMailbox mailbox, int mailboxSessionID, org.csapi.gms.TpMessagingFault fault)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "mailboxFaultDetected", true);
				org.csapi.gms.IpMailboxHelper.write(_os,mailbox);
				_os.write_long(mailboxSessionID);
				org.csapi.gms.TpMessagingFaultHelper.write(_os,fault);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "mailboxFaultDetected", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppMessagingManagerOperations _localServant = (IpAppMessagingManagerOperations)_so.servant;
			try
			{
			_localServant.mailboxFaultDetected(mailbox,mailboxSessionID,fault);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

}
