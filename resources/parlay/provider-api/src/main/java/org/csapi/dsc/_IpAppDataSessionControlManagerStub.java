package org.csapi.dsc;


/**
 *	Generated from IDL interface "IpAppDataSessionControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppDataSessionControlManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.dsc.IpAppDataSessionControlManager
{
	private String[] ids = {"IDL:org/csapi/dsc/IpAppDataSessionControlManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.dsc.IpAppDataSessionControlManagerOperations.class;
	public void dataSessionNotificationInterrupted()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "dataSessionNotificationInterrupted", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "dataSessionNotificationInterrupted", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionControlManagerOperations _localServant = (IpAppDataSessionControlManagerOperations)_so.servant;
			try
			{
			_localServant.dataSessionNotificationInterrupted();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.dsc.IpAppDataSession reportNotification(org.csapi.dsc.TpDataSessionIdentifier dataSessionReference, org.csapi.dsc.TpDataSessionEventInfo eventInfo, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportNotification", true);
				org.csapi.dsc.TpDataSessionIdentifierHelper.write(_os,dataSessionReference);
				org.csapi.dsc.TpDataSessionEventInfoHelper.write(_os,eventInfo);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				org.csapi.dsc.IpAppDataSession _result = org.csapi.dsc.IpAppDataSessionHelper.read(_is);
				return _result;
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reportNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionControlManagerOperations _localServant = (IpAppDataSessionControlManagerOperations)_so.servant;
			org.csapi.dsc.IpAppDataSession _result;			try
			{
			_result = _localServant.reportNotification(dataSessionReference,eventInfo,assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void dataSessionNotificationContinued()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "dataSessionNotificationContinued", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "dataSessionNotificationContinued", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionControlManagerOperations _localServant = (IpAppDataSessionControlManagerOperations)_so.servant;
			try
			{
			_localServant.dataSessionNotificationContinued();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void dataSessionAborted(int dataSession)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "dataSessionAborted", true);
				_os.write_long(dataSession);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "dataSessionAborted", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionControlManagerOperations _localServant = (IpAppDataSessionControlManagerOperations)_so.servant;
			try
			{
			_localServant.dataSessionAborted(dataSession);
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
