package org.csapi.dsc;


/**
 *	Generated from IDL interface "IpAppDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppDataSessionStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.dsc.IpAppDataSession
{
	private String[] ids = {"IDL:org/csapi/dsc/IpAppDataSession:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.dsc.IpAppDataSessionOperations.class;
	public void dataSessionFaultDetected(int dataSessionID, org.csapi.dsc.TpDataSessionFault fault)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "dataSessionFaultDetected", true);
				_os.write_long(dataSessionID);
				org.csapi.dsc.TpDataSessionFaultHelper.write(_os,fault);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "dataSessionFaultDetected", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionOperations _localServant = (IpAppDataSessionOperations)_so.servant;
			try
			{
			_localServant.dataSessionFaultDetected(dataSessionID,fault);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseDataSessionErr(int dataSessionID, org.csapi.dsc.TpDataSessionError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseDataSessionErr", true);
				_os.write_long(dataSessionID);
				org.csapi.dsc.TpDataSessionErrorHelper.write(_os,errorIndication);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseDataSessionErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionOperations _localServant = (IpAppDataSessionOperations)_so.servant;
			try
			{
			_localServant.superviseDataSessionErr(dataSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseDataSessionRes(int dataSessionID, int report, org.csapi.dsc.TpDataSessionSuperviseVolume usedVolume, org.csapi.TpDataSessionQosClass qualityOfService)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseDataSessionRes", true);
				_os.write_long(dataSessionID);
				_os.write_long(report);
				org.csapi.dsc.TpDataSessionSuperviseVolumeHelper.write(_os,usedVolume);
				org.csapi.TpDataSessionQosClassHelper.write(_os,qualityOfService);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseDataSessionRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionOperations _localServant = (IpAppDataSessionOperations)_so.servant;
			try
			{
			_localServant.superviseDataSessionRes(dataSessionID,report,usedVolume,qualityOfService);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void connectRes(int dataSessionID, org.csapi.dsc.TpDataSessionReport eventReport, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "connectRes", true);
				_os.write_long(dataSessionID);
				org.csapi.dsc.TpDataSessionReportHelper.write(_os,eventReport);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "connectRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionOperations _localServant = (IpAppDataSessionOperations)_so.servant;
			try
			{
			_localServant.connectRes(dataSessionID,eventReport,assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void connectErr(int dataSessionID, org.csapi.dsc.TpDataSessionError errorIndication, int assignmentID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "connectErr", true);
				_os.write_long(dataSessionID);
				org.csapi.dsc.TpDataSessionErrorHelper.write(_os,errorIndication);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "connectErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppDataSessionOperations _localServant = (IpAppDataSessionOperations)_so.servant;
			try
			{
			_localServant.connectErr(dataSessionID,errorIndication,assignmentID);
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
