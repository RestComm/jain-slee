package org.csapi.cc.cccs;


/**
 *	Generated from IDL interface "IpAppSubConfCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppSubConfCallStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cc.cccs.IpAppSubConfCall
{
	private String[] ids = {"IDL:org/csapi/cc/cccs/IpAppSubConfCall:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/cc/mpccs/IpAppMultiPartyCall:1.0","IDL:org/csapi/cc/mmccs/IpAppMultiMediaCall:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cc.cccs.IpAppSubConfCallOperations.class;
	public void createAndRouteCallLegErr(int callSessionID, org.csapi.cc.mpccs.TpCallLegIdentifier callLegReference, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createAndRouteCallLegErr", true);
				_os.write_long(callSessionID);
				org.csapi.cc.mpccs.TpCallLegIdentifierHelper.write(_os,callLegReference);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createAndRouteCallLegErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.createAndRouteCallLegErr(callSessionID,callLegReference,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.superviseErr(callSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void floorRequest(int subConferenceSessionID, int callLegSessionID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "floorRequest", true);
				_os.write_long(subConferenceSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "floorRequest", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.floorRequest(subConferenceSessionID,callLegSessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseRes(int callSessionID, int report, int usedTime)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseRes", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.superviseRes(callSessionID,report,usedTime);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void getInfoRes(int callSessionID, org.csapi.cc.TpCallInfoReport callInfoReport)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInfoRes", true);
				_os.write_long(callSessionID);
				org.csapi.cc.TpCallInfoReportHelper.write(_os,callInfoReport);
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
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.getInfoRes(callSessionID,callInfoReport);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseVolumeRes(int callSessionID, int report, org.csapi.cc.mmccs.TpCallSuperviseVolume usedVolume)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseVolumeRes", true);
				_os.write_long(callSessionID);
				_os.write_long(report);
				org.csapi.cc.mmccs.TpCallSuperviseVolumeHelper.write(_os,usedVolume);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseVolumeRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.superviseVolumeRes(callSessionID,report,usedVolume);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void callEnded(int callSessionID, org.csapi.cc.TpCallEndedReport report)
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
				org.csapi.cc.TpCallEndedReportHelper.write(_os,report);
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
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
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

	public void getInfoErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInfoErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInfoErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.getInfoErr(callSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void superviseVolumeErr(int callSessionID, org.csapi.cc.TpCallError errorIndication)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "superviseVolumeErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "superviseVolumeErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.superviseVolumeErr(callSessionID,errorIndication);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void chairSelection(int subConferenceSessionID, int callLegSessionID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "chairSelection", true);
				_os.write_long(subConferenceSessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "chairSelection", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppSubConfCallOperations _localServant = (IpAppSubConfCallOperations)_so.servant;
			try
			{
			_localServant.chairSelection(subConferenceSessionID,callLegSessionID);
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
