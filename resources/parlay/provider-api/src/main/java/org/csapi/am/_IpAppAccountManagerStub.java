package org.csapi.am;


/**
 *	Generated from IDL interface "IpAppAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppAccountManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.am.IpAppAccountManager
{
	private String[] ids = {"IDL:org/csapi/am/IpAppAccountManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.am.IpAppAccountManagerOperations.class;
	public void retrieveTransactionHistoryErr(int retrievalID, org.csapi.am.TpTransactionHistoryStatus transactionHistoryError)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "retrieveTransactionHistoryErr", true);
				_os.write_long(retrievalID);
				org.csapi.am.TpTransactionHistoryStatusHelper.write(_os,transactionHistoryError);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "retrieveTransactionHistoryErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppAccountManagerOperations _localServant = (IpAppAccountManagerOperations)_so.servant;
			try
			{
			_localServant.retrieveTransactionHistoryErr(retrievalID,transactionHistoryError);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void queryBalanceRes(int queryId, org.csapi.am.TpBalance[] balances)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "queryBalanceRes", true);
				_os.write_long(queryId);
				org.csapi.am.TpBalanceSetHelper.write(_os,balances);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "queryBalanceRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppAccountManagerOperations _localServant = (IpAppAccountManagerOperations)_so.servant;
			try
			{
			_localServant.queryBalanceRes(queryId,balances);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void retrieveTransactionHistoryRes(int retrievalID, org.csapi.am.TpTransactionHistory[] transactionHistory)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "retrieveTransactionHistoryRes", true);
				_os.write_long(retrievalID);
				org.csapi.am.TpTransactionHistorySetHelper.write(_os,transactionHistory);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "retrieveTransactionHistoryRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppAccountManagerOperations _localServant = (IpAppAccountManagerOperations)_so.servant;
			try
			{
			_localServant.retrieveTransactionHistoryRes(retrievalID,transactionHistory);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void queryBalanceErr(int queryId, org.csapi.am.TpBalanceQueryError cause)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "queryBalanceErr", true);
				_os.write_long(queryId);
				org.csapi.am.TpBalanceQueryErrorHelper.write(_os,cause);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "queryBalanceErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppAccountManagerOperations _localServant = (IpAppAccountManagerOperations)_so.servant;
			try
			{
			_localServant.queryBalanceErr(queryId,cause);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void reportNotification(org.csapi.am.TpChargingEventInfo chargingEventInfo, int assignmentId)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportNotification", true);
				org.csapi.am.TpChargingEventInfoHelper.write(_os,chargingEventInfo);
				_os.write_long(assignmentId);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reportNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppAccountManagerOperations _localServant = (IpAppAccountManagerOperations)_so.servant;
			try
			{
			_localServant.reportNotification(chargingEventInfo,assignmentId);
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
