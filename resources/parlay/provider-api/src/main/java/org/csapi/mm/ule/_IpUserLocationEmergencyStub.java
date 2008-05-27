package org.csapi.mm.ule;


/**
 *	Generated from IDL interface "IpUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpUserLocationEmergencyStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.mm.ule.IpUserLocationEmergency
{
	private String[] ids = {"IDL:org/csapi/mm/ule/IpUserLocationEmergency:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.mm.ule.IpUserLocationEmergencyOperations.class;
	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setCallbackWithSessionID", true);
				org.csapi.IpInterfaceHelper.write(_os,appInterface);
				_os.write_long(sessionID);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setCallbackWithSessionID", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationEmergencyOperations _localServant = (IpUserLocationEmergencyOperations)_so.servant;
			try
			{
			_localServant.setCallbackWithSessionID(appInterface,sessionID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int emergencyLocationReportReq(org.csapi.mm.ule.IpAppUserLocationEmergency appEmergencyLocation, org.csapi.mm.TpUserLocationEmergencyRequest request) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.P_UNKNOWN_SUBSCRIBER
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "emergencyLocationReportReq", true);
				org.csapi.mm.ule.IpAppUserLocationEmergencyHelper.write(_os,appEmergencyLocation);
				org.csapi.mm.TpUserLocationEmergencyRequestHelper.write(_os,request);
				_is = _invoke(_os);
				int _result = _is.read_long();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INFORMATION_NOT_AVAILABLE:1.0"))
				{
					throw org.csapi.P_INFORMATION_NOT_AVAILABLEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_APPLICATION_NOT_ACTIVATED:1.0"))
				{
					throw org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_UNKNOWN_SUBSCRIBER:1.0"))
				{
					throw org.csapi.P_UNKNOWN_SUBSCRIBERHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "emergencyLocationReportReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationEmergencyOperations _localServant = (IpUserLocationEmergencyOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.emergencyLocationReportReq(appEmergencyLocation,request);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void unSubscribeEmergencyLocationReports(int assignmentId) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "unSubscribeEmergencyLocationReports", true);
				_os.write_long(assignmentId);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_ASSIGNMENT_ID:1.0"))
				{
					throw org.csapi.P_INVALID_ASSIGNMENT_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "unSubscribeEmergencyLocationReports", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationEmergencyOperations _localServant = (IpUserLocationEmergencyOperations)_so.servant;
			try
			{
			_localServant.unSubscribeEmergencyLocationReports(assignmentId);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int subscribeEmergencyLocationReports(org.csapi.mm.ule.IpAppUserLocationEmergency appEmergencyLocation) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "subscribeEmergencyLocationReports", true);
				org.csapi.mm.ule.IpAppUserLocationEmergencyHelper.write(_os,appEmergencyLocation);
				_is = _invoke(_os);
				int _result = _is.read_long();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "subscribeEmergencyLocationReports", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationEmergencyOperations _localServant = (IpUserLocationEmergencyOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.subscribeEmergencyLocationReports(appEmergencyLocation);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setCallback", true);
				org.csapi.IpInterfaceHelper.write(_os,appInterface);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setCallback", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationEmergencyOperations _localServant = (IpUserLocationEmergencyOperations)_so.servant;
			try
			{
			_localServant.setCallback(appInterface);
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
