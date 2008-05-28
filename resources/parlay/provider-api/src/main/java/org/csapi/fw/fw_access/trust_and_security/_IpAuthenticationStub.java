package org.csapi.fw.fw_access.trust_and_security;


/**
 *	Generated from IDL interface "IpAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAuthenticationStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_access.trust_and_security.IpAuthentication
{
	private String[] ids = {"IDL:org/csapi/fw/fw_access/trust_and_security/IpAuthentication:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_access.trust_and_security.IpAuthenticationOperations.class;
	public org.csapi.IpInterface requestAccess(java.lang.String accessType, org.csapi.IpInterface clientAccessInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACCESS_TYPE,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "requestAccess", true);
				_os.write_string(accessType);
				org.csapi.IpInterfaceHelper.write(_os,clientAccessInterface);
				_is = _invoke(_os);
				org.csapi.IpInterface _result = org.csapi.IpInterfaceHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_ACCESS_TYPE:1.0"))
				{
					throw org.csapi.fw.P_INVALID_ACCESS_TYPEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "requestAccess", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAuthenticationOperations _localServant = (IpAuthenticationOperations)_so.servant;
			org.csapi.IpInterface _result;			try
			{
			_result = _localServant.requestAccess(accessType,clientAccessInterface);
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
