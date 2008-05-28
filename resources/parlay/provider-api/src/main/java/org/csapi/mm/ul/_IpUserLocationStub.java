package org.csapi.mm.ul;


/**
 *	Generated from IDL interface "IpUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpUserLocationStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.mm.ul.IpUserLocation
{
	private String[] ids = {"IDL:org/csapi/mm/ul/IpUserLocation:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.mm.ul.IpUserLocationOperations.class;
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
			IpUserLocationOperations _localServant = (IpUserLocationOperations)_so.servant;
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

	public void periodicLocationReportingStop(org.csapi.mm.TpMobilityStopAssignmentData stopRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "periodicLocationReportingStop", true);
				org.csapi.mm.TpMobilityStopAssignmentDataHelper.write(_os,stopRequest);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "periodicLocationReportingStop", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationOperations _localServant = (IpUserLocationOperations)_so.servant;
			try
			{
			_localServant.periodicLocationReportingStop(stopRequest);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int periodicLocationReportingStartReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationRequest request, int reportingInterval) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_INVALID_REPORTING_INTERVAL,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "periodicLocationReportingStartReq", true);
				org.csapi.mm.ul.IpAppUserLocationHelper.write(_os,appLocation);
				org.csapi.TpAddressSetHelper.write(_os,users);
				org.csapi.mm.TpLocationRequestHelper.write(_os,request);
				_os.write_long(reportingInterval);
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
				else if( _id.equals("IDL:org/csapi/mm/P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED:1.0"))
				{
					throw org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVEREDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_APPLICATION_NOT_ACTIVATED:1.0"))
				{
					throw org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/mm/P_INVALID_REPORTING_INTERVAL:1.0"))
				{
					throw org.csapi.mm.P_INVALID_REPORTING_INTERVALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/mm/P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED:1.0"))
				{
					throw org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "periodicLocationReportingStartReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationOperations _localServant = (IpUserLocationOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.periodicLocationReportingStartReq(appLocation,users,request,reportingInterval);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public int locationReportReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "locationReportReq", true);
				org.csapi.mm.ul.IpAppUserLocationHelper.write(_os,appLocation);
				org.csapi.TpAddressSetHelper.write(_os,users);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "locationReportReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationOperations _localServant = (IpUserLocationOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.locationReportReq(appLocation,users);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public int extendedLocationReportReq(org.csapi.mm.ul.IpAppUserLocation appLocation, org.csapi.TpAddress[] users, org.csapi.mm.TpLocationRequest request) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED,org.csapi.TpCommonExceptions,org.csapi.P_APPLICATION_NOT_ACTIVATED,org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "extendedLocationReportReq", true);
				org.csapi.mm.ul.IpAppUserLocationHelper.write(_os,appLocation);
				org.csapi.TpAddressSetHelper.write(_os,users);
				org.csapi.mm.TpLocationRequestHelper.write(_os,request);
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
				else if( _id.equals("IDL:org/csapi/mm/P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED:1.0"))
				{
					throw org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVEREDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_APPLICATION_NOT_ACTIVATED:1.0"))
				{
					throw org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/mm/P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED:1.0"))
				{
					throw org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "extendedLocationReportReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpUserLocationOperations _localServant = (IpUserLocationOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.extendedLocationReportReq(appLocation,users,request);
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
			IpUserLocationOperations _localServant = (IpUserLocationOperations)_so.servant;
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
