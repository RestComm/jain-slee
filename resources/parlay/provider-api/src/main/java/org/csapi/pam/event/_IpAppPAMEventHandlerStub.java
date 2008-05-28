package org.csapi.pam.event;


/**
 *	Generated from IDL interface "IpAppPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppPAMEventHandlerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.pam.event.IpAppPAMEventHandler
{
	private String[] ids = {"IDL:org/csapi/pam/event/IpAppPAMEventHandler:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.pam.event.IpAppPAMEventHandlerOperations.class;
	public void eventNotify(int eventID, org.csapi.pam.TpPAMNotificationInfo[] eventInfo)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "eventNotify", true);
				_os.write_long(eventID);
				org.csapi.pam.TpPAMNotificationInfoListHelper.write(_os,eventInfo);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "eventNotify", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppPAMEventHandlerOperations _localServant = (IpAppPAMEventHandlerOperations)_so.servant;
			try
			{
			_localServant.eventNotify(eventID,eventInfo);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void eventNotifyErr(int eventID, org.csapi.pam.TpPAMErrorInfo errorInfo)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "eventNotifyErr", true);
				_os.write_long(eventID);
				org.csapi.pam.TpPAMErrorInfoHelper.write(_os,errorInfo);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "eventNotifyErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppPAMEventHandlerOperations _localServant = (IpAppPAMEventHandlerOperations)_so.servant;
			try
			{
			_localServant.eventNotifyErr(eventID,errorInfo);
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
