package org.csapi.cs;


/**
 *	Generated from IDL interface "IpAppChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppChargingSessionStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cs.IpAppChargingSession
{
	private String[] ids = {"IDL:org/csapi/cs/IpAppChargingSession:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cs.IpAppChargingSessionOperations.class;
	public void reserveAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice reservedAmount, int sessionTimeLeft, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reserveAmountRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingPriceHelper.write(_os,reservedAmount);
				_os.write_long(sessionTimeLeft);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reserveAmountRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.reserveAmountRes(sessionID,requestNumber,reservedAmount,sessionTimeLeft,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void debitUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "debitUnitErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "debitUnitErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.debitUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void creditUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] creditedVolumes, org.csapi.cs.TpVolume[] reservedUnitsLeft, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "creditUnitRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpVolumeSetHelper.write(_os,creditedVolumes);
				org.csapi.cs.TpVolumeSetHelper.write(_os,reservedUnitsLeft);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "creditUnitRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.creditUnitRes(sessionID,requestNumber,creditedVolumes,reservedUnitsLeft,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void debitAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "debitAmountErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "debitAmountErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.debitAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void reserveUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reserveUnitErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reserveUnitErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.reserveUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void sessionEnded(int sessionID, org.csapi.cs.TpSessionEndedCause report)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "sessionEnded", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpSessionEndedCauseHelper.write(_os,report);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "sessionEnded", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.sessionEnded(sessionID,report);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void extendLifeTimeErr(int sessionID, org.csapi.cs.TpChargingError error)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "extendLifeTimeErr", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "extendLifeTimeErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.extendLifeTimeErr(sessionID,error);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directDebitUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] debitedVolumes, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directDebitUnitRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpVolumeSetHelper.write(_os,debitedVolumes);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directDebitUnitRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directDebitUnitRes(sessionID,requestNumber,debitedVolumes,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void creditAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice creditedAmount, org.csapi.cs.TpChargingPrice reservedAmountLeft, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "creditAmountRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingPriceHelper.write(_os,creditedAmount);
				org.csapi.cs.TpChargingPriceHelper.write(_os,reservedAmountLeft);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "creditAmountRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.creditAmountRes(sessionID,requestNumber,creditedAmount,reservedAmountLeft,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directDebitUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directDebitUnitErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directDebitUnitErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directDebitUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void creditAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "creditAmountErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "creditAmountErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.creditAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directCreditAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directCreditAmountErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directCreditAmountErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directCreditAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directCreditAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice creditedAmount, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directCreditAmountRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingPriceHelper.write(_os,creditedAmount);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directCreditAmountRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directCreditAmountRes(sessionID,requestNumber,creditedAmount,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void debitAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice debitedAmount, org.csapi.cs.TpChargingPrice reservedAmountLeft, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "debitAmountRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingPriceHelper.write(_os,debitedAmount);
				org.csapi.cs.TpChargingPriceHelper.write(_os,reservedAmountLeft);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "debitAmountRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.debitAmountRes(sessionID,requestNumber,debitedAmount,reservedAmountLeft,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directCreditUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directCreditUnitErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directCreditUnitErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directCreditUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void reserveUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] reservedUnits, int sessionTimeLeft, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reserveUnitRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpVolumeSetHelper.write(_os,reservedUnits);
				_os.write_long(sessionTimeLeft);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reserveUnitRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.reserveUnitRes(sessionID,requestNumber,reservedUnits,sessionTimeLeft,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void extendLifeTimeRes(int sessionID, int sessionTimeLeft)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "extendLifeTimeRes", true);
				_os.write_long(sessionID);
				_os.write_long(sessionTimeLeft);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "extendLifeTimeRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.extendLifeTimeRes(sessionID,sessionTimeLeft);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void creditUnitErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "creditUnitErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "creditUnitErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.creditUnitErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void rateRes(int sessionID, org.csapi.cs.TpPriceVolume[] rates, int validityTimeLeft)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "rateRes", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpPriceVolumeSetHelper.write(_os,rates);
				_os.write_long(validityTimeLeft);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "rateRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.rateRes(sessionID,rates,validityTimeLeft);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directCreditUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] creditedVolumes, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directCreditUnitRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpVolumeSetHelper.write(_os,creditedVolumes);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directCreditUnitRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directCreditUnitRes(sessionID,requestNumber,creditedVolumes,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void rateErr(int sessionID, org.csapi.cs.TpChargingError error)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "rateErr", true);
				_os.write_long(sessionID);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "rateErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.rateErr(sessionID,error);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void reserveAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reserveAmountErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reserveAmountErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.reserveAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directDebitAmountErr(int sessionID, int requestNumber, org.csapi.cs.TpChargingError error, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directDebitAmountErr", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingErrorHelper.write(_os,error);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directDebitAmountErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directDebitAmountErr(sessionID,requestNumber,error,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void directDebitAmountRes(int sessionID, int requestNumber, org.csapi.cs.TpChargingPrice debitedAmount, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "directDebitAmountRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpChargingPriceHelper.write(_os,debitedAmount);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "directDebitAmountRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.directDebitAmountRes(sessionID,requestNumber,debitedAmount,requestNumberNextRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void debitUnitRes(int sessionID, int requestNumber, org.csapi.cs.TpVolume[] debitedVolumes, org.csapi.cs.TpVolume[] reservedUnitsLeft, int requestNumberNextRequest)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "debitUnitRes", true);
				_os.write_long(sessionID);
				_os.write_long(requestNumber);
				org.csapi.cs.TpVolumeSetHelper.write(_os,debitedVolumes);
				org.csapi.cs.TpVolumeSetHelper.write(_os,reservedUnitsLeft);
				_os.write_long(requestNumberNextRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "debitUnitRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingSessionOperations _localServant = (IpAppChargingSessionOperations)_so.servant;
			try
			{
			_localServant.debitUnitRes(sessionID,requestNumber,debitedVolumes,reservedUnitsLeft,requestNumberNextRequest);
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
