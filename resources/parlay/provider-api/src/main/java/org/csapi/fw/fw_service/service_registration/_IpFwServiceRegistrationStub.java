package org.csapi.fw.fw_service.service_registration;


/**
 *	Generated from IDL interface "IpFwServiceRegistration"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpFwServiceRegistrationStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration
{
	private String[] ids = {"IDL:org/csapi/fw/fw_service/service_registration/IpFwServiceRegistration:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_service.service_registration.IpFwServiceRegistrationOperations.class;
	public void announceServiceAvailability(java.lang.String serviceID, org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager serviceInstanceLifecycleManagerRef) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "announceServiceAvailability", true);
				_os.write_string(serviceID);
				org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManagerHelper.write(_os,serviceInstanceLifecycleManagerRef);
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
				else if( _id.equals("IDL:org/csapi/fw/P_UNKNOWN_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_ILLEGAL_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_ILLEGAL_SERVICE_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "announceServiceAvailability", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFwServiceRegistrationOperations _localServant = (IpFwServiceRegistrationOperations)_so.servant;
			try
			{
			_localServant.announceServiceAvailability(serviceID,serviceInstanceLifecycleManagerRef);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.fw.TpServiceDescription describeService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "describeService", true);
				_os.write_string(serviceID);
				_is = _invoke(_os);
				org.csapi.fw.TpServiceDescription _result = org.csapi.fw.TpServiceDescriptionHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_UNKNOWN_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_ILLEGAL_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_ILLEGAL_SERVICE_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "describeService", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFwServiceRegistrationOperations _localServant = (IpFwServiceRegistrationOperations)_so.servant;
			org.csapi.fw.TpServiceDescription _result;			try
			{
			_result = _localServant.describeService(serviceID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void unannounceService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "unannounceService", true);
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
				else if( _id.equals("IDL:org/csapi/fw/P_UNKNOWN_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_ILLEGAL_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_ILLEGAL_SERVICE_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "unannounceService", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFwServiceRegistrationOperations _localServant = (IpFwServiceRegistrationOperations)_so.servant;
			try
			{
			_localServant.unannounceService(serviceID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String registerService(java.lang.String serviceTypeName, org.csapi.fw.TpServiceProperty[] servicePropertyList) throws org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLE,org.csapi.fw.P_MISSING_MANDATORY_PROPERTY,org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE,org.csapi.fw.P_DUPLICATE_PROPERTY_NAME,org.csapi.fw.P_PROPERTY_TYPE_MISMATCH
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "registerService", true);
				_os.write_string(serviceTypeName);
				org.csapi.fw.TpServicePropertyListHelper.write(_os,servicePropertyList);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_SERVICE_TYPE_UNAVAILABLE:1.0"))
				{
					throw org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_MISSING_MANDATORY_PROPERTY:1.0"))
				{
					throw org.csapi.fw.P_MISSING_MANDATORY_PROPERTYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_ILLEGAL_SERVICE_TYPE:1.0"))
				{
					throw org.csapi.fw.P_ILLEGAL_SERVICE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_UNKNOWN_SERVICE_TYPE:1.0"))
				{
					throw org.csapi.fw.P_UNKNOWN_SERVICE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_DUPLICATE_PROPERTY_NAME:1.0"))
				{
					throw org.csapi.fw.P_DUPLICATE_PROPERTY_NAMEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_PROPERTY_TYPE_MISMATCH:1.0"))
				{
					throw org.csapi.fw.P_PROPERTY_TYPE_MISMATCHHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "registerService", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFwServiceRegistrationOperations _localServant = (IpFwServiceRegistrationOperations)_so.servant;
			java.lang.String _result;			try
			{
			_result = _localServant.registerService(serviceTypeName,servicePropertyList);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void unregisterService(java.lang.String serviceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_UNKNOWN_SERVICE_ID,org.csapi.fw.P_ILLEGAL_SERVICE_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "unregisterService", true);
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
				else if( _id.equals("IDL:org/csapi/fw/P_UNKNOWN_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_ILLEGAL_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_ILLEGAL_SERVICE_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "unregisterService", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpFwServiceRegistrationOperations _localServant = (IpFwServiceRegistrationOperations)_so.servant;
			try
			{
			_localServant.unregisterService(serviceID);
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
