package org.csapi.fw.fw_service.notification;


/**
 *	Generated from IDL interface "IpFwEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpFwEventNotificationStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_service.notification.IpFwEventNotification
{
	private String[] ids = {"IDL:org/csapi/fw/fw_service/notification/IpFwEventNotification:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_service.notification.IpFwEventNotificationOperations.class;
	public int createNotification(org.csapi.fw.TpFwEventCriteria eventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createNotification", true);
				org.csapi.fw.TpFwEventCriteriaHelper.write(_os,eventCriteria);
				_is = _invoke(_os);
				int _result = _is.read_long();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_EVENT_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_EVENT_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_CRITERIA:1.0"))
				{
					throw org.csapi.P_INVALID_CRITERIAHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFwEventNotificationOperations _localServant = (IpFwEventNotificationOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.createNotification(eventCriteria);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "destroyNotification", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "destroyNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFwEventNotificationOperations _localServant = (IpFwEventNotificationOperations)_so.servant;
			try
			{
			_localServant.destroyNotification(assignmentID);
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
