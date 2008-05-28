package org.csapi.cs;


/**
 *	Generated from IDL interface "IpChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpChargingManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cs.IpChargingManager
{
	private String[] ids = {"IDL:org/csapi/cs/IpChargingManager:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cs.IpChargingManagerOperations.class;
	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setCallbackWithSessionID", true);
				org.csapi.IpInterfaceHelper.write(_os,appInterface);
				_os.write_long(sessionID);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setCallbackWithSessionID", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingManagerOperations _localServant = (IpChargingManagerOperations)_so.servant;
			try
			{
			_localServant.setCallbackWithSessionID(appInterface,sessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.cs.TpChargingSessionID createSplitChargingSession(org.csapi.cs.IpAppChargingSession appChargingSession, java.lang.String sessionDescription, org.csapi.cs.TpMerchantAccountID merchantAccount, org.csapi.TpAddress[] users, org.csapi.cs.TpCorrelationID correlationID) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_ACCOUNT,org.csapi.cs.P_INVALID_USER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createSplitChargingSession", true);
				org.csapi.cs.IpAppChargingSessionHelper.write(_os,appChargingSession);
				_os.write_string(sessionDescription);
				org.csapi.cs.TpMerchantAccountIDHelper.write(_os,merchantAccount);
				org.csapi.TpAddressSetHelper.write(_os,users);
				org.csapi.cs.TpCorrelationIDHelper.write(_os,correlationID);
				_is = _invoke(_os);
				org.csapi.cs.TpChargingSessionID _result = org.csapi.cs.TpChargingSessionIDHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_ACCOUNT:1.0"))
				{
					throw org.csapi.cs.P_INVALID_ACCOUNTHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_USER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_USERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createSplitChargingSession", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingManagerOperations _localServant = (IpChargingManagerOperations)_so.servant;
			org.csapi.cs.TpChargingSessionID _result;			try
			{
			_result = _localServant.createSplitChargingSession(appChargingSession,sessionDescription,merchantAccount,users,correlationID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.cs.TpChargingSessionID createChargingSession(org.csapi.cs.IpAppChargingSession appChargingSession, java.lang.String sessionDescription, org.csapi.cs.TpMerchantAccountID merchantAccount, org.csapi.TpAddress user, org.csapi.cs.TpCorrelationID correlationID) throws org.csapi.TpCommonExceptions,org.csapi.cs.P_INVALID_ACCOUNT,org.csapi.cs.P_INVALID_USER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createChargingSession", true);
				org.csapi.cs.IpAppChargingSessionHelper.write(_os,appChargingSession);
				_os.write_string(sessionDescription);
				org.csapi.cs.TpMerchantAccountIDHelper.write(_os,merchantAccount);
				org.csapi.TpAddressHelper.write(_os,user);
				org.csapi.cs.TpCorrelationIDHelper.write(_os,correlationID);
				_is = _invoke(_os);
				org.csapi.cs.TpChargingSessionID _result = org.csapi.cs.TpChargingSessionIDHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_ACCOUNT:1.0"))
				{
					throw org.csapi.cs.P_INVALID_ACCOUNTHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_USER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_USERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createChargingSession", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingManagerOperations _localServant = (IpChargingManagerOperations)_so.servant;
			org.csapi.cs.TpChargingSessionID _result;			try
			{
			_result = _localServant.createChargingSession(appChargingSession,sessionDescription,merchantAccount,user,correlationID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setCallback", true);
				org.csapi.IpInterfaceHelper.write(_os,appInterface);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setCallback", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingManagerOperations _localServant = (IpChargingManagerOperations)_so.servant;
			try
			{
			_localServant.setCallback(appInterface);
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
