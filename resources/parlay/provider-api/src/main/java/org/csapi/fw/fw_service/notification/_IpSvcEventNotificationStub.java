package org.csapi.fw.fw_service.notification;


/**
 *	Generated from IDL interface "IpSvcEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpSvcEventNotificationStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_service.notification.IpSvcEventNotification
{
	private String[] ids = {"IDL:org/csapi/fw/fw_service/notification/IpSvcEventNotification:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_service.notification.IpSvcEventNotificationOperations.class;
	public void reportNotification(org.csapi.fw.TpFwEventInfo eventInfo, int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportNotification", true);
				org.csapi.fw.TpFwEventInfoHelper.write(_os,eventInfo);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_ASSIGNMENT_ID:1.0"))
				{
					throw org.csapi.P_INVALID_ASSIGNMENT_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else 
					throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reportNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpSvcEventNotificationOperations _localServant = (IpSvcEventNotificationOperations)_so.servant;
			try
			{
			_localServant.reportNotification(eventInfo,assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void notificationTerminated() throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "notificationTerminated", true);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else 
					throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "notificationTerminated", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpSvcEventNotificationOperations _localServant = (IpSvcEventNotificationOperations)_so.servant;
			try
			{
			_localServant.notificationTerminated();
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
