package org.csapi.fw.fw_application.discovery;


/**
 *	Generated from IDL interface "IpServiceDiscovery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpServiceDiscoveryStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_application.discovery.IpServiceDiscovery
{
	private String[] ids = {"IDL:org/csapi/fw/fw_application/discovery/IpServiceDiscovery:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_application.discovery.IpServiceDiscoveryOperations.class;
	public org.csapi.fw.TpService[] listSubscribedServices() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listSubscribedServices", true);
				_is = _invoke(_os);
				org.csapi.fw.TpService[] _result = org.csapi.fw.TpServiceListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listSubscribedServices", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceDiscoveryOperations _localServant = (IpServiceDiscoveryOperations)_so.servant;
			org.csapi.fw.TpService[] _result;			try
			{
			_result = _localServant.listSubscribedServices();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listServiceTypes() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listServiceTypes", true);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.fw.TpServiceTypeNameListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listServiceTypes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceDiscoveryOperations _localServant = (IpServiceDiscoveryOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listServiceTypes();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.fw.TpService[] discoverService(java.lang.String serviceTypeName, org.csapi.fw.TpServiceProperty[] desiredPropertyList, int max) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "discoverService", true);
				_os.write_string(serviceTypeName);
				org.csapi.fw.TpServicePropertyListHelper.write(_os,desiredPropertyList);
				_os.write_long(max);
				_is = _invoke(_os);
				org.csapi.fw.TpService[] _result = org.csapi.fw.TpServiceListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ILLEGAL_SERVICE_TYPE:1.0"))
				{
					throw org.csapi.fw.P_ILLEGAL_SERVICE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_PROPERTY:1.0"))
				{
					throw org.csapi.fw.P_INVALID_PROPERTYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_UNKNOWN_SERVICE_TYPE:1.0"))
				{
					throw org.csapi.fw.P_UNKNOWN_SERVICE_TYPEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "discoverService", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceDiscoveryOperations _localServant = (IpServiceDiscoveryOperations)_so.servant;
			org.csapi.fw.TpService[] _result;			try
			{
			_result = _localServant.discoverService(serviceTypeName,desiredPropertyList,max);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.fw.TpServiceTypeDescription describeServiceType(java.lang.String name) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ILLEGAL_SERVICE_TYPE,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_UNKNOWN_SERVICE_TYPE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "describeServiceType", true);
				_os.write_string(name);
				_is = _invoke(_os);
				org.csapi.fw.TpServiceTypeDescription _result = org.csapi.fw.TpServiceTypeDescriptionHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ILLEGAL_SERVICE_TYPE:1.0"))
				{
					throw org.csapi.fw.P_ILLEGAL_SERVICE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_UNKNOWN_SERVICE_TYPE:1.0"))
				{
					throw org.csapi.fw.P_UNKNOWN_SERVICE_TYPEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "describeServiceType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceDiscoveryOperations _localServant = (IpServiceDiscoveryOperations)_so.servant;
			org.csapi.fw.TpServiceTypeDescription _result;			try
			{
			_result = _localServant.describeServiceType(name);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

}
