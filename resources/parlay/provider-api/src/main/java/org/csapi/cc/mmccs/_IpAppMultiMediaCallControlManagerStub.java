package org.csapi.cc.mmccs;


/**
 *	Generated from IDL interface "IpAppMultiMediaCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppMultiMediaCallControlManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cc.mmccs.IpAppMultiMediaCallControlManager
{
	private String[] ids = {"IDL:org/csapi/cc/mmccs/IpAppMultiMediaCallControlManager:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/cc/mpccs/IpAppMultiPartyCallControlManager:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cc.mmccs.IpAppMultiMediaCallControlManagerOperations.class;
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
			IpAppMultiMediaCallControlManagerOperations _localServant = (IpAppMultiMediaCallControlManagerOperations)_so.servant;
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
			IpAppMultiMediaCallControlManagerOperations _localServant = (IpAppMultiMediaCallControlManagerOperations)_so.servant;
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

	public void managerInterrupted()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "managerInterrupted", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "managerInterrupted", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppMultiMediaCallControlManagerOperations _localServant = (IpAppMultiMediaCallControlManagerOperations)_so.servant;
			try
			{
			_localServant.managerInterrupted();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void managerResumed()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "managerResumed", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "managerResumed", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppMultiMediaCallControlManagerOperations _localServant = (IpAppMultiMediaCallControlManagerOperations)_so.servant;
			try
			{
			_localServant.managerResumed();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.cc.mmccs.TpAppMultiMediaCallBack reportMediaNotification(org.csapi.cc.mmccs.TpMultiMediaCallIdentifier callReference, org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier[] callLegReferenceSet, org.csapi.cc.mmccs.TpMediaStream[] mediaStreams, org.csapi.cc.mmccs.TpMediaStreamEventType type, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportMediaNotification", true);
				org.csapi.cc.mmccs.TpMultiMediaCallIdentifierHelper.write(_os,callReference);
				org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifierSetHelper.write(_os,callLegReferenceSet);
				org.csapi.cc.mmccs.TpMediaStreamSetHelper.write(_os,mediaStreams);
				org.csapi.cc.mmccs.TpMediaStreamEventTypeHelper.write(_os,type);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				org.csapi.cc.mmccs.TpAppMultiMediaCallBack _result = org.csapi.cc.mmccs.TpAppMultiMediaCallBackHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reportMediaNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppMultiMediaCallControlManagerOperations _localServant = (IpAppMultiMediaCallControlManagerOperations)_so.servant;
			org.csapi.cc.mmccs.TpAppMultiMediaCallBack _result;			try
			{
			_result = _localServant.reportMediaNotification(callReference,callLegReferenceSet,mediaStreams,type,assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
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
			IpAppMultiMediaCallControlManagerOperations _localServant = (IpAppMultiMediaCallControlManagerOperations)_so.servant;
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

	public org.csapi.cc.mpccs.TpAppMultiPartyCallBack reportNotification(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier callReference, org.csapi.cc.mpccs.TpCallLegIdentifier[] callLegReferenceSet, org.csapi.cc.TpCallNotificationInfo notificationInfo, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportNotification", true);
				org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.write(_os,callReference);
				org.csapi.cc.mpccs.TpCallLegIdentifierSetHelper.write(_os,callLegReferenceSet);
				org.csapi.cc.TpCallNotificationInfoHelper.write(_os,notificationInfo);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				org.csapi.cc.mpccs.TpAppMultiPartyCallBack _result = org.csapi.cc.mpccs.TpAppMultiPartyCallBackHelper.read(_is);
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
			IpAppMultiMediaCallControlManagerOperations _localServant = (IpAppMultiMediaCallControlManagerOperations)_so.servant;
			org.csapi.cc.mpccs.TpAppMultiPartyCallBack _result;			try
			{
			_result = _localServant.reportNotification(callReference,callLegReferenceSet,notificationInfo,assignmentID);
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
