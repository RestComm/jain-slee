package org.csapi.cc.mpccs;


/**
 *	Generated from IDL interface "IpAppCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppCallLegStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cc.mpccs.IpAppCallLeg
{
	private String[] ids = {"IDL:org/csapi/cc/mpccs/IpAppCallLeg:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cc.mpccs.IpAppCallLegOperations.class;
	public void attachMediaRes(int callLegSessionID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "attachMediaRes", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "attachMediaRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.attachMediaRes(callLegSessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void routeErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "routeErr", true);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "routeErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.routeErr(callLegSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseErr", true);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.superviseErr(callLegSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseRes(int callLegSessionID, int report, int usedTime)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseRes", true);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.superviseRes(callLegSessionID,report,usedTime);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void eventReportRes(int callLegSessionID, org.csapi.cc.TpCallEventInfo eventInfo)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "eventReportRes", true);
				_os.write_long(callLegSessionID);
				org.csapi.cc.TpCallEventInfoHelper.write(_os,eventInfo);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "eventReportRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.eventReportRes(callLegSessionID,eventInfo);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void attachMediaErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "attachMediaErr", true);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "attachMediaErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.attachMediaErr(callLegSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void eventReportErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "eventReportErr", true);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "eventReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.eventReportErr(callLegSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void detachMediaRes(int callLegSessionID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "detachMediaRes", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "detachMediaRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.detachMediaRes(callLegSessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void detachMediaErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "detachMediaErr", true);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "detachMediaErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.detachMediaErr(callLegSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void callLegEnded(int callLegSessionID, org.csapi.cc.TpReleaseCause cause)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "callLegEnded", true);
				_os.write_long(callLegSessionID);
				org.csapi.cc.TpReleaseCauseHelper.write(_os,cause);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "callLegEnded", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.callLegEnded(callLegSessionID,cause);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void getInfoErr(int callLegSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInfoErr", true);
				_os.write_long(callLegSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInfoErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.getInfoErr(callLegSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void getInfoRes(int callLegSessionID, org.csapi.cc.TpCallLegInfoReport callLegInfoReport)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInfoRes", true);
				_os.write_long(callLegSessionID);
				org.csapi.cc.TpCallLegInfoReportHelper.write(_os,callLegInfoReport);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInfoRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppCallLegOperations _localServant = (IpAppCallLegOperations)_so.servant;
			try
			{
			_localServant.getInfoRes(callLegSessionID,callLegInfoReport);
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
