package org.csapi.cc.gccs;


/**
 *	Generated from IDL interface "IpAppCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppCallStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cc.gccs.IpAppCall
{
	private String[] ids = {"IDL:org/csapi/cc/gccs/IpAppCall:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cc.gccs.IpAppCallOperations.class;
	public void callFaultDetected(int callSessionID, org.csapi.cc.gccs.TpCallFault fault)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callFaultDetected", true);
				_os.write_long(callSessionID);
				org.csapi.cc.gccs.TpCallFaultHelper.write(_os,fault);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callFaultDetected", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.callFaultDetected(callSessionID,fault);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseCallErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseCallErr", true);
				_os.write_long(callSessionID);
				org.csapi.cc.TpCallErrorHelper.write(_os,errorIndication);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseCallErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.superviseCallErr(callSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void routeRes(int callSessionID, org.csapi.cc.gccs.TpCallReport eventReport, int callLegSessionID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "routeRes", true);
				_os.write_long(callSessionID);
				org.csapi.cc.gccs.TpCallReportHelper.write(_os,eventReport);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "routeRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.routeRes(callSessionID,eventReport,callLegSessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void getMoreDialledDigitsErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getMoreDialledDigitsErr", true);
				_os.write_long(callSessionID);
				org.csapi.cc.TpCallErrorHelper.write(_os,errorIndication);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getMoreDialledDigitsErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.getMoreDialledDigitsErr(callSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void routeErr(int callSessionID, org.csapi.cc.TpCallError errorIndication, int callLegSessionID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "routeErr", true);
				_os.write_long(callSessionID);
				org.csapi.cc.TpCallErrorHelper.write(_os,errorIndication);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "routeErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.routeErr(callSessionID,errorIndication,callLegSessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void callEnded(int callSessionID, org.csapi.cc.gccs.TpCallEndedReport report)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callEnded", true);
				_os.write_long(callSessionID);
				org.csapi.cc.gccs.TpCallEndedReportHelper.write(_os,report);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callEnded", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.callEnded(callSessionID,report);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void getCallInfoErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getCallInfoErr", true);
				_os.write_long(callSessionID);
				org.csapi.cc.TpCallErrorHelper.write(_os,errorIndication);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getCallInfoErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.getCallInfoErr(callSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void getCallInfoRes(int callSessionID, org.csapi.cc.gccs.TpCallInfoReport callInfoReport)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getCallInfoRes", true);
				_os.write_long(callSessionID);
				org.csapi.cc.gccs.TpCallInfoReportHelper.write(_os,callInfoReport);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getCallInfoRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.getCallInfoRes(callSessionID,callInfoReport);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void getMoreDialledDigitsRes(int callSessionID, java.lang.String digits)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getMoreDialledDigitsRes", true);
				_os.write_long(callSessionID);
				_os.write_string(digits);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getMoreDialledDigitsRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.getMoreDialledDigitsRes(callSessionID,digits);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseCallRes(int callSessionID, int report, int usedTime)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseCallRes", true);
				_os.write_long(callSessionID);
				_os.write_long(report);
				_os.write_long(usedTime);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseCallRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallOperations _localServant = (IpAppCallOperations)_so.servant;
			try
			{
			_localServant.superviseCallRes(callSessionID,report,usedTime);
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
