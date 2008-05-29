package org.csapi.ui;


/**
 *	Generated from IDL interface "IpAppUI"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppUIStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.ui.IpAppUI
{
	private String[] ids = {"IDL:org/csapi/ui/IpAppUI:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.ui.IpAppUIOperations.class;
	public void userInteractionFaultDetected(int userInteractionSessionID, org.csapi.ui.TpUIFault fault)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "userInteractionFaultDetected", true);
				_os.write_long(userInteractionSessionID);
				org.csapi.ui.TpUIFaultHelper.write(_os,fault);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "userInteractionFaultDetected", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIOperations _localServant = (IpAppUIOperations)_so.servant;
			try
			{
			_localServant.userInteractionFaultDetected(userInteractionSessionID,fault);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void sendInfoAndCollectRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response, java.lang.String collectedInfo)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "sendInfoAndCollectRes", true);
				_os.write_long(userInteractionSessionID);
				_os.write_long(assignmentID);
				org.csapi.ui.TpUIReportHelper.write(_os,response);
				_os.write_string(collectedInfo);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "sendInfoAndCollectRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIOperations _localServant = (IpAppUIOperations)_so.servant;
			try
			{
			_localServant.sendInfoAndCollectRes(userInteractionSessionID,assignmentID,response,collectedInfo);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void sendInfoErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "sendInfoErr", true);
				_os.write_long(userInteractionSessionID);
				_os.write_long(assignmentID);
				org.csapi.ui.TpUIErrorHelper.write(_os,error);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "sendInfoErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIOperations _localServant = (IpAppUIOperations)_so.servant;
			try
			{
			_localServant.sendInfoErr(userInteractionSessionID,assignmentID,error);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void sendInfoAndCollectErr(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIError error)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "sendInfoAndCollectErr", true);
				_os.write_long(userInteractionSessionID);
				_os.write_long(assignmentID);
				org.csapi.ui.TpUIErrorHelper.write(_os,error);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "sendInfoAndCollectErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIOperations _localServant = (IpAppUIOperations)_so.servant;
			try
			{
			_localServant.sendInfoAndCollectErr(userInteractionSessionID,assignmentID,error);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void sendInfoRes(int userInteractionSessionID, int assignmentID, org.csapi.ui.TpUIReport response)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "sendInfoRes", true);
				_os.write_long(userInteractionSessionID);
				_os.write_long(assignmentID);
				org.csapi.ui.TpUIReportHelper.write(_os,response);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "sendInfoRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUIOperations _localServant = (IpAppUIOperations)_so.servant;
			try
			{
			_localServant.sendInfoRes(userInteractionSessionID,assignmentID,response);
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
