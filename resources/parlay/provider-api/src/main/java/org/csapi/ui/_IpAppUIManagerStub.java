package org.csapi.ui;


/**
 *	Generated from IDL interface "IpAppUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppUIManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.ui.IpAppUIManager
{
	private String[] ids = {"IDL:org/csapi/ui/IpAppUIManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.ui.IpAppUIManagerOperations.class;
	public void userInteractionNotificationContinued()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "userInteractionNotificationContinued", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "userInteractionNotificationContinued", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIManagerOperations _localServant = (IpAppUIManagerOperations)_so.servant;
			try
			{
			_localServant.userInteractionNotificationContinued();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.ui.IpAppUI reportNotification(org.csapi.ui.TpUIIdentifier userInteraction, org.csapi.ui.TpUIEventInfo eventInfo, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportNotification", true);
				org.csapi.ui.TpUIIdentifierHelper.write(_os,userInteraction);
				org.csapi.ui.TpUIEventInfoHelper.write(_os,eventInfo);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				org.csapi.ui.IpAppUI _result = org.csapi.ui.IpAppUIHelper.read(_is);
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
			IpAppUIManagerOperations _localServant = (IpAppUIManagerOperations)_so.servant;
			org.csapi.ui.IpAppUI _result;			try
			{
			_result = _localServant.reportNotification(userInteraction,eventInfo,assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void userInteractionNotificationInterrupted()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "userInteractionNotificationInterrupted", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "userInteractionNotificationInterrupted", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIManagerOperations _localServant = (IpAppUIManagerOperations)_so.servant;
			try
			{
			_localServant.userInteractionNotificationInterrupted();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.ui.IpAppUI reportEventNotification(org.csapi.ui.TpUIIdentifier userInteraction, org.csapi.ui.TpUIEventNotificationInfo eventNotificationInfo, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportEventNotification", true);
				org.csapi.ui.TpUIIdentifierHelper.write(_os,userInteraction);
				org.csapi.ui.TpUIEventNotificationInfoHelper.write(_os,eventNotificationInfo);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				org.csapi.ui.IpAppUI _result = org.csapi.ui.IpAppUIHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reportEventNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIManagerOperations _localServant = (IpAppUIManagerOperations)_so.servant;
			org.csapi.ui.IpAppUI _result;			try
			{
			_result = _localServant.reportEventNotification(userInteraction,eventNotificationInfo,assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void userInteractionAborted(org.csapi.ui.TpUIIdentifier userInteraction)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "userInteractionAborted", true);
				org.csapi.ui.TpUIIdentifierHelper.write(_os,userInteraction);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "userInteractionAborted", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIManagerOperations _localServant = (IpAppUIManagerOperations)_so.servant;
			try
			{
			_localServant.userInteractionAborted(userInteraction);
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
