package org.csapi.fw.fw_service.service_lifecycle;


/**
 *	Generated from IDL interface "IpServiceInstanceLifecycleManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpServiceInstanceLifecycleManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager
{
	private String[] ids = {"IDL:org/csapi/fw/fw_service/service_lifecycle/IpServiceInstanceLifecycleManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManagerOperations.class;
	public org.csapi.IpService createServiceManager(java.lang.String application, org.csapi.fw.TpServiceProperty[] serviceProperties, java.lang.String serviceInstanceID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_PROPERTY
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createServiceManager", true);
				_os.write_string(application);
				org.csapi.fw.TpServicePropertyListHelper.write(_os,serviceProperties);
				_os.write_string(serviceInstanceID);
				_is = _invoke(_os);
				org.csapi.IpService _result = org.csapi.IpServiceHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_PROPERTY:1.0"))
				{
					throw org.csapi.fw.P_INVALID_PROPERTYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createServiceManager", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceInstanceLifecycleManagerOperations _localServant = (IpServiceInstanceLifecycleManagerOperations)_so.servant;
			org.csapi.IpService _result;			try
			{
			_result = _localServant.createServiceManager(application,serviceProperties,serviceInstanceID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void destroyServiceManager(java.lang.String serviceInstance) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "destroyServiceManager", true);
				_os.write_string(serviceInstance);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "destroyServiceManager", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceInstanceLifecycleManagerOperations _localServant = (IpServiceInstanceLifecycleManagerOperations)_so.servant;
			try
			{
			_localServant.destroyServiceManager(serviceInstance);
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
