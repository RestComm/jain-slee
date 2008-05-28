package org.csapi.cs;


/**
 *	Generated from IDL interface "IpChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpChargingSessionStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cs.IpChargingSession
{
	private String[] ids = {"IDL:org/csapi/cs/IpChargingSession:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cs.IpChargingSessionOperations.class;
	public void directCreditUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directCreditUnitReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpChargingParameterSetHelper.write(_os,chargingParameters);
				org.csapi.cs.TpVolumeSetHelper.write(_os,volumes);
				_os.write_long(requestNumber);
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
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_VOLUME:1.0"))
				{
					throw org.csapi.cs.P_INVALID_VOLUMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directCreditUnitReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directCreditUnitReq(sessionID,applicationDescription,chargingParameters,volumes,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directDebitUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directDebitUnitReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpChargingParameterSetHelper.write(_os,chargingParameters);
				org.csapi.cs.TpVolumeSetHelper.write(_os,volumes);
				_os.write_long(requestNumber);
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
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_VOLUME:1.0"))
				{
					throw org.csapi.cs.P_INVALID_VOLUMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directDebitUnitReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directDebitUnitReq(sessionID,applicationDescription,chargingParameters,volumes,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
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
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
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

	public org.csapi.cs.TpVolume[] getUnitLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getUnitLeft", true);
				_os.write_long(sessionID);
				_is = _invoke(_os);
				org.csapi.cs.TpVolume[] _result = org.csapi.cs.TpVolumeSetHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getUnitLeft", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			org.csapi.cs.TpVolume[] _result;			try
			{
			_result = _localServant.getUnitLeft(sessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void extendLifeTimeReq(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "extendLifeTimeReq", true);
				_os.write_long(sessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "extendLifeTimeReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.extendLifeTimeReq(sessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void creditAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingPrice amount, boolean closeReservation, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "creditAmountReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpChargingPriceHelper.write(_os,amount);
				_os.write_boolean(closeReservation);
				_os.write_long(requestNumber);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_AMOUNT:1.0"))
				{
					throw org.csapi.P_INVALID_AMOUNTHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_CURRENCY:1.0"))
				{
					throw org.csapi.P_INVALID_CURRENCYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "creditAmountReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.creditAmountReq(sessionID,applicationDescription,amount,closeReservation,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void debitAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingPrice amount, boolean closeReservation, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "debitAmountReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpChargingPriceHelper.write(_os,amount);
				_os.write_boolean(closeReservation);
				_os.write_long(requestNumber);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_AMOUNT:1.0"))
				{
					throw org.csapi.P_INVALID_AMOUNTHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_CURRENCY:1.0"))
				{
					throw org.csapi.P_INVALID_CURRENCYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "debitAmountReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.debitAmountReq(sessionID,applicationDescription,amount,closeReservation,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

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
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
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

	public void directCreditAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice amount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directCreditAmountReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpChargingParameterSetHelper.write(_os,chargingParameters);
				org.csapi.cs.TpChargingPriceHelper.write(_os,amount);
				_os.write_long(requestNumber);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_AMOUNT:1.0"))
				{
					throw org.csapi.P_INVALID_AMOUNTHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_CURRENCY:1.0"))
				{
					throw org.csapi.P_INVALID_CURRENCYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directCreditAmountReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directCreditAmountReq(sessionID,applicationDescription,chargingParameters,amount,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void rateReq(int sessionID, org.csapi.cs.TpChargingParameter[] chargingParameters) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "rateReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpChargingParameterSetHelper.write(_os,chargingParameters);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "rateReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.rateReq(sessionID,chargingParameters);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directDebitAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice amount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directDebitAmountReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpChargingParameterSetHelper.write(_os,chargingParameters);
				org.csapi.cs.TpChargingPriceHelper.write(_os,amount);
				_os.write_long(requestNumber);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_AMOUNT:1.0"))
				{
					throw org.csapi.P_INVALID_AMOUNTHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_CURRENCY:1.0"))
				{
					throw org.csapi.P_INVALID_CURRENCYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directDebitAmountReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directDebitAmountReq(sessionID,applicationDescription,chargingParameters,amount,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void release(int sessionID, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "release", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
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
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "release", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.release(sessionID,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void creditUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpVolume[] volumes, boolean closeReservation, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "creditUnitReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpVolumeSetHelper.write(_os,volumes);
				_os.write_boolean(closeReservation);
				_os.write_long(requestNumber);
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
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_VOLUME:1.0"))
				{
					throw org.csapi.cs.P_INVALID_VOLUMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "creditUnitReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.creditUnitReq(sessionID,applicationDescription,volumes,closeReservation,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void reserveUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpVolume[] volumes, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reserveUnitReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpChargingParameterSetHelper.write(_os,chargingParameters);
				org.csapi.cs.TpVolumeSetHelper.write(_os,volumes);
				_os.write_long(requestNumber);
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
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_VOLUME:1.0"))
				{
					throw org.csapi.cs.P_INVALID_VOLUMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reserveUnitReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.reserveUnitReq(sessionID,applicationDescription,chargingParameters,volumes,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void debitUnitReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpVolume[] volumes, boolean closeReservation, int requestNumber) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER,org.csapi.cs.P_INVALID_VOLUME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "debitUnitReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpVolumeSetHelper.write(_os,volumes);
				_os.write_boolean(closeReservation);
				_os.write_long(requestNumber);
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
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_VOLUME:1.0"))
				{
					throw org.csapi.cs.P_INVALID_VOLUMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "debitUnitReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.debitUnitReq(sessionID,applicationDescription,volumes,closeReservation,requestNumber);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int getLifeTimeLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getLifeTimeLeft", true);
				_os.write_long(sessionID);
				_is = _invoke(_os);
				int _result = _is.read_long();
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getLifeTimeLeft", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.getLifeTimeLeft(sessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.cs.TpChargingPrice getAmountLeft(int sessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAmountLeft", true);
				_os.write_long(sessionID);
				_is = _invoke(_os);
				org.csapi.cs.TpChargingPrice _result = org.csapi.cs.TpChargingPriceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAmountLeft", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			org.csapi.cs.TpChargingPrice _result;			try
			{
			_result = _localServant.getAmountLeft(sessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void reserveAmountReq(int sessionID, org.csapi.cs.TpApplicationDescription applicationDescription, org.csapi.cs.TpChargingParameter[] chargingParameters, org.csapi.cs.TpChargingPrice preferredAmount, org.csapi.cs.TpChargingPrice minimumAmount, int requestNumber) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.cs.P_INVALID_REQUEST_NUMBER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reserveAmountReq", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpApplicationDescriptionHelper.write(_os,applicationDescription);
				org.csapi.cs.TpChargingParameterSetHelper.write(_os,chargingParameters);
				org.csapi.cs.TpChargingPriceHelper.write(_os,preferredAmount);
				org.csapi.cs.TpChargingPriceHelper.write(_os,minimumAmount);
				_os.write_long(requestNumber);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_AMOUNT:1.0"))
				{
					throw org.csapi.P_INVALID_AMOUNTHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_CURRENCY:1.0"))
				{
					throw org.csapi.P_INVALID_CURRENCYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cs/P_INVALID_REQUEST_NUMBER:1.0"))
				{
					throw org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reserveAmountReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpChargingSessionOperations _localServant = (IpChargingSessionOperations)_so.servant;
			try
			{
			_localServant.reserveAmountReq(sessionID,applicationDescription,chargingParameters,preferredAmount,minimumAmount,requestNumber);
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
