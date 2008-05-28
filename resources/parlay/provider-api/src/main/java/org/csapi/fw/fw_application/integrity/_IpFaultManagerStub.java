package org.csapi.fw.fw_application.integrity;


/**
 *	Generated from IDL interface "IpFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpFaultManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_application.integrity.IpFaultManager
{
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpFaultManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_application.integrity.IpFaultManagerOperations.class;
	public void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod, java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "genFaultStatsRecordReq", true);
				org.csapi.TpTimeIntervalHelper.write(_os,timePeriod);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "genFaultStatsRecordReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.genFaultStatsRecordReq(timePeriod,serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod, java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "generateFaultStatisticsRecordReq", true);
				_os.write_long(faultStatsReqID);
				org.csapi.TpTimeIntervalHelper.write(_os,timePeriod);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "generateFaultStatisticsRecordReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.generateFaultStatisticsRecordReq(faultStatsReqID,timePeriod,serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void appUnavailableInd(java.lang.String serviceID) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "appUnavailableInd", true);
				_os.write_string(serviceID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "appUnavailableInd", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.appUnavailableInd(serviceID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "generateFaultStatisticsRecordRes", true);
				_os.write_long(faultStatsReqID);
				org.csapi.fw.TpFaultStatsRecordHelper.write(_os,faultStatistics);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "generateFaultStatisticsRecordRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.generateFaultStatisticsRecordRes(faultStatsReqID,faultStatistics);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void appActivityTestRes(int activityTestID, java.lang.String activityTestResult) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "appActivityTestRes", true);
				_os.write_long(activityTestID);
				_os.write_string(activityTestResult);
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_ACTIVITY_TEST_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_ACTIVITY_TEST_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "appActivityTestRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.appActivityTestRes(activityTestID,activityTestResult);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void activityTestReq(int activityTestID, java.lang.String svcID) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "activityTestReq", true);
				_os.write_long(activityTestID);
				_os.write_string(svcID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "activityTestReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.activityTestReq(activityTestID,svcID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void appActivityTestErr(int activityTestID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "appActivityTestErr", true);
				_os.write_long(activityTestID);
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_ACTIVITY_TEST_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_ACTIVITY_TEST_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "appActivityTestErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.appActivityTestErr(activityTestID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "generateFaultStatisticsRecordErr", true);
				_os.write_long(faultStatsReqID);
				org.csapi.fw.TpFaultStatisticsErrorHelper.write(_os,faultStatisticsError);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "generateFaultStatisticsRecordErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.generateFaultStatisticsRecordErr(faultStatsReqID,faultStatisticsError);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void svcUnavailableInd(java.lang.String serviceID) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "svcUnavailableInd", true);
				_os.write_string(serviceID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "svcUnavailableInd", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.svcUnavailableInd(serviceID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void appAvailStatusInd(org.csapi.fw.TpAppAvailStatusReason reason) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "appAvailStatusInd", true);
				org.csapi.fw.TpAppAvailStatusReasonHelper.write(_os,reason);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "appAvailStatusInd", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.appAvailStatusInd(reason);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "genFaultStatsRecordRes", true);
				org.csapi.fw.TpFaultStatsRecordHelper.write(_os,faultStatistics);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "genFaultStatsRecordRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.genFaultStatsRecordRes(faultStatistics);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "genFaultStatsRecordErr", true);
				org.csapi.fw.TpFaultStatisticsErrorHelper.write(_os,faultStatisticsError);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "genFaultStatsRecordErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFaultManagerOperations _localServant = (IpFaultManagerOperations)_so.servant;
			try
			{
			_localServant.genFaultStatsRecordErr(faultStatisticsError);
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
