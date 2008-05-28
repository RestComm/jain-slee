package org.csapi.fw.fw_access.trust_and_security;


/**
 *	Generated from IDL interface "IpInitial"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpInitialStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_access.trust_and_security.IpInitial
{
	private String[] ids = {"IDL:org/csapi/fw/fw_access/trust_and_security/IpInitial:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_access.trust_and_security.IpInitialOperations.class;
	public org.csapi.fw.TpAuthDomain initiateAuthenticationWithVersion(org.csapi.fw.TpAuthDomain clientDomain, java.lang.String authType, java.lang.String frameworkVersion) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_VERSION,org.csapi.fw.P_INVALID_AUTH_TYPE,org.csapi.fw.P_INVALID_DOMAIN_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "initiateAuthenticationWithVersion", true);
				org.csapi.fw.TpAuthDomainHelper.write(_os,clientDomain);
				_os.write_string(authType);
				_os.write_string(frameworkVersion);
				_is = _invoke(_os);
				org.csapi.fw.TpAuthDomain _result = org.csapi.fw.TpAuthDomainHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/P_INVALID_VERSION:1.0"))
				{
					throw org.csapi.P_INVALID_VERSIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_AUTH_TYPE:1.0"))
				{
					throw org.csapi.fw.P_INVALID_AUTH_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_DOMAIN_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_DOMAIN_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "initiateAuthenticationWithVersion", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpInitialOperations _localServant = (IpInitialOperations)_so.servant;
			org.csapi.fw.TpAuthDomain _result;			try
			{
			_result = _localServant.initiateAuthenticationWithVersion(clientDomain,authType,frameworkVersion);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.fw.TpAuthDomain initiateAuthentication(org.csapi.fw.TpAuthDomain clientDomain, java.lang.String authType) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_AUTH_TYPE,org.csapi.fw.P_INVALID_DOMAIN_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "initiateAuthentication", true);
				org.csapi.fw.TpAuthDomainHelper.write(_os,clientDomain);
				_os.write_string(authType);
				_is = _invoke(_os);
				org.csapi.fw.TpAuthDomain _result = org.csapi.fw.TpAuthDomainHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_AUTH_TYPE:1.0"))
				{
					throw org.csapi.fw.P_INVALID_AUTH_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_DOMAIN_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_DOMAIN_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "initiateAuthentication", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpInitialOperations _localServant = (IpInitialOperations)_so.servant;
			org.csapi.fw.TpAuthDomain _result;			try
			{
			_result = _localServant.initiateAuthentication(clientDomain,authType);
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
