package org.csapi.fw.fw_application.integrity;


/**
 *	Generated from IDL interface "IpLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpLoadManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_application.integrity.IpLoadManager
{
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpLoadManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_application.integrity.IpLoadManagerOperations.class;
	public void queryLoadStatsReq(int loadStatsReqID, java.lang.String[] serviceIDs, org.csapi.TpTimeInterval timeInterval) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "queryLoadStatsReq", true);
				_os.write_long(loadStatsReqID);
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
				org.csapi.TpTimeIntervalHelper.write(_os,timeInterval);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_SERVICE_NOT_ENABLED:1.0"))
				{
					throw org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_UNAUTHORISED_PARAMETER_VALUE:1.0"))
				{
					throw org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "queryLoadStatsReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.queryLoadStatsReq(loadStatsReqID,serviceIDs,timeInterval);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void queryAppLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "queryAppLoadErr", true);
				org.csapi.fw.TpLoadStatisticErrorHelper.write(_os,loadStatisticsError);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "queryAppLoadErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.queryAppLoadErr(loadStatisticsError);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void queryAppLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "queryAppLoadStatsErr", true);
				_os.write_long(loadStatsReqID);
				org.csapi.fw.TpLoadStatisticErrorHelper.write(_os,loadStatisticsError);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "queryAppLoadStatsErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.queryAppLoadStatsErr(loadStatsReqID,loadStatisticsError);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void queryAppLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "queryAppLoadRes", true);
				org.csapi.fw.TpLoadStatisticListHelper.write(_os,loadStatistics);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "queryAppLoadRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.queryAppLoadRes(loadStatistics);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void resumeNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "resumeNotification", true);
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_SERVICE_NOT_ENABLED:1.0"))
				{
					throw org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_UNAUTHORISED_PARAMETER_VALUE:1.0"))
				{
					throw org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "resumeNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.resumeNotification(serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void reportLoad(org.csapi.fw.TpLoadLevel loadLevel) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportLoad", true);
				org.csapi.fw.TpLoadLevelHelper.write(_os,loadLevel);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reportLoad", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.reportLoad(loadLevel);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void suspendNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "suspendNotification", true);
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_SERVICE_NOT_ENABLED:1.0"))
				{
					throw org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_UNAUTHORISED_PARAMETER_VALUE:1.0"))
				{
					throw org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "suspendNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.suspendNotification(serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void createLoadLevelNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createLoadLevelNotification", true);
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_UNAUTHORISED_PARAMETER_VALUE:1.0"))
				{
					throw org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createLoadLevelNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.createLoadLevelNotification(serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void queryLoadReq(java.lang.String[] serviceIDs, org.csapi.TpTimeInterval timeInterval) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "queryLoadReq", true);
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
				org.csapi.TpTimeIntervalHelper.write(_os,timeInterval);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_SERVICE_NOT_ENABLED:1.0"))
				{
					throw org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_UNAUTHORISED_PARAMETER_VALUE:1.0"))
				{
					throw org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "queryLoadReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.queryLoadReq(serviceIDs,timeInterval);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void destroyLoadLevelNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "destroyLoadLevelNotification", true);
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_UNAUTHORISED_PARAMETER_VALUE:1.0"))
				{
					throw org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "destroyLoadLevelNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.destroyLoadLevelNotification(serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void queryAppLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "queryAppLoadStatsRes", true);
				_os.write_long(loadStatsReqID);
				org.csapi.fw.TpLoadStatisticListHelper.write(_os,loadStatistics);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "queryAppLoadStatsRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpLoadManagerOperations _localServant = (IpLoadManagerOperations)_so.servant;
			try
			{
			_localServant.queryAppLoadStatsRes(loadStatsReqID,loadStatistics);
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
