package org.csapi.cc.gccs;


/**
 *	Generated from IDL interface "IpAppCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppCallControlManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cc.gccs.IpAppCallControlManager
{
	private String[] ids = {"IDL:org/csapi/cc/gccs/IpAppCallControlManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cc.gccs.IpAppCallControlManagerOperations.class;
	public void callNotificationInterrupted()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callNotificationInterrupted", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callNotificationInterrupted", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallControlManagerOperations _localServant = (IpAppCallControlManagerOperations)_so.servant;
			try
			{
			_localServant.callNotificationInterrupted();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void callAborted(int callReference)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callAborted", true);
				_os.write_long(callReference);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callAborted", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallControlManagerOperations _localServant = (IpAppCallControlManagerOperations)_so.servant;
			try
			{
			_localServant.callAborted(callReference);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void callOverloadCeased(int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callOverloadCeased", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callOverloadCeased", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallControlManagerOperations _localServant = (IpAppCallControlManagerOperations)_so.servant;
			try
			{
			_localServant.callOverloadCeased(assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void callNotificationContinued()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callNotificationContinued", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callNotificationContinued", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallControlManagerOperations _localServant = (IpAppCallControlManagerOperations)_so.servant;
			try
			{
			_localServant.callNotificationContinued();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void callOverloadEncountered(int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callOverloadEncountered", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callOverloadEncountered", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallControlManagerOperations _localServant = (IpAppCallControlManagerOperations)_so.servant;
			try
			{
			_localServant.callOverloadEncountered(assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.cc.gccs.IpAppCall callEventNotify(org.csapi.cc.gccs.TpCallIdentifier callReference, org.csapi.cc.gccs.TpCallEventInfo eventInfo, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callEventNotify", true);
				org.csapi.cc.gccs.TpCallIdentifierHelper.write(_os,callReference);
				org.csapi.cc.gccs.TpCallEventInfoHelper.write(_os,eventInfo);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				org.csapi.cc.gccs.IpAppCall _result = org.csapi.cc.gccs.IpAppCallHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callEventNotify", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallControlManagerOperations _localServant = (IpAppCallControlManagerOperations)_so.servant;
			org.csapi.cc.gccs.IpAppCall _result;			try
			{
			_result = _localServant.callEventNotify(callReference,eventInfo,assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

}
