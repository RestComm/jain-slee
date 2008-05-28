package org.csapi.fw.fw_application.integrity;


/**
 *	Generated from IDL interface "IpAppFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppFaultManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_application.integrity.IpAppFaultManager
{
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpAppFaultManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_application.integrity.IpAppFaultManagerOperations.class;
	public void svcUnavailableInd(java.lang.String serviceID, org.csapi.fw.TpSvcUnavailReason reason)
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
				org.csapi.fw.TpSvcUnavailReasonHelper.write(_os,reason);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "svcUnavailableInd", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.svcUnavailableInd(serviceID,reason);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod)
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "generateFaultStatisticsRecordReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.generateFaultStatisticsRecordReq(faultStatsReqID,timePeriod);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics, java.lang.String[] serviceIDs)
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
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "generateFaultStatisticsRecordRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.generateFaultStatisticsRecordRes(faultStatsReqID,faultStatistics,serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void appUnavailableInd(java.lang.String serviceID)
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
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
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

	public void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod)
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "genFaultStatsRecordReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.genFaultStatsRecordReq(timePeriod);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics, java.lang.String[] serviceIDs)
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
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "genFaultStatsRecordRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.genFaultStatsRecordRes(faultStatistics,serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError, java.lang.String[] serviceIDs)
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
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "genFaultStatsRecordErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.genFaultStatsRecordErr(faultStatisticsError,serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void fwUnavailableInd(org.csapi.fw.TpFwUnavailReason reason)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "fwUnavailableInd", true);
				org.csapi.fw.TpFwUnavailReasonHelper.write(_os,reason);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "fwUnavailableInd", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.fwUnavailableInd(reason);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void svcAvailStatusInd(java.lang.String serviceID, org.csapi.fw.TpSvcAvailStatusReason reason)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "svcAvailStatusInd", true);
				_os.write_string(serviceID);
				org.csapi.fw.TpSvcAvailStatusReasonHelper.write(_os,reason);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "svcAvailStatusInd", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.svcAvailStatusInd(serviceID,reason);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void activityTestErr(int activityTestID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "activityTestErr", true);
				_os.write_long(activityTestID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "activityTestErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.activityTestErr(activityTestID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void fwFaultRecoveryInd(org.csapi.fw.TpInterfaceFault fault)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "fwFaultRecoveryInd", true);
				org.csapi.fw.TpInterfaceFaultHelper.write(_os,fault);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "fwFaultRecoveryInd", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.fwFaultRecoveryInd(fault);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError[] faultStatistics, java.lang.String[] serviceIDs)
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
				org.csapi.fw.TpFaultStatsErrorListHelper.write(_os,faultStatistics);
				org.csapi.fw.TpServiceIDListHelper.write(_os,serviceIDs);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "generateFaultStatisticsRecordErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.generateFaultStatisticsRecordErr(faultStatsReqID,faultStatistics,serviceIDs);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void appActivityTestReq(int activityTestID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "appActivityTestReq", true);
				_os.write_long(activityTestID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "appActivityTestReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.appActivityTestReq(activityTestID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void fwFaultReportInd(org.csapi.fw.TpInterfaceFault fault)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "fwFaultReportInd", true);
				org.csapi.fw.TpInterfaceFaultHelper.write(_os,fault);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "fwFaultReportInd", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.fwFaultReportInd(fault);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void activityTestRes(int activityTestID, java.lang.String activityTestResult)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "activityTestRes", true);
				_os.write_long(activityTestID);
				_os.write_string(activityTestResult);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "activityTestRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppFaultManagerOperations _localServant = (IpAppFaultManagerOperations)_so.servant;
			try
			{
			_localServant.activityTestRes(activityTestID,activityTestResult);
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
