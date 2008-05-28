package org.csapi.fw.fw_access.trust_and_security;


/**
 *	Generated from IDL interface "IpAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAccessStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_access.trust_and_security.IpAccess
{
	private String[] ids = {"IDL:org/csapi/fw/fw_access/trust_and_security/IpAccess:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_access.trust_and_security.IpAccessOperations.class;
	public void releaseInterface(java.lang.String interfaceName) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "releaseInterface", true);
				_os.write_string(interfaceName);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_NAME:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_NAMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "releaseInterface", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAccessOperations _localServant = (IpAccessOperations)_so.servant;
			try
			{
			_localServant.releaseInterface(interfaceName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.IpInterface obtainInterfaceWithCallback(java.lang.String interfaceName, org.csapi.IpInterface clientInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "obtainInterfaceWithCallback", true);
				_os.write_string(interfaceName);
				org.csapi.IpInterfaceHelper.write(_os,clientInterface);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_NAME:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_NAMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "obtainInterfaceWithCallback", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAccessOperations _localServant = (IpAccessOperations)_so.servant;
			org.csapi.IpInterface _result;			try
			{
			_result = _localServant.obtainInterfaceWithCallback(interfaceName,clientInterface);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void terminateAccess(java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "terminateAccess", true);
				_os.write_string(terminationText);
				org.csapi.TpOctetSetHelper.write(_os,digitalSignature);
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SIGNATURE:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SIGNATUREHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "terminateAccess", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAccessOperations _localServant = (IpAccessOperations)_so.servant;
			try
			{
			_localServant.terminateAccess(terminationText,digitalSignature);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void endAccess(org.csapi.fw.TpProperty[] endAccessProperties) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "endAccess", true);
				org.csapi.fw.TpPropertyListHelper.write(_os,endAccessProperties);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "endAccess", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAccessOperations _localServant = (IpAccessOperations)_so.servant;
			try
			{
			_localServant.endAccess(endAccessProperties);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void relinquishInterface(java.lang.String interfaceName, java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE,org.csapi.P_INVALID_INTERFACE_NAME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "relinquishInterface", true);
				_os.write_string(interfaceName);
				_os.write_string(terminationText);
				org.csapi.TpOctetSetHelper.write(_os,digitalSignature);
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SIGNATURE:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SIGNATUREHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_NAME:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_NAMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "relinquishInterface", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAccessOperations _localServant = (IpAccessOperations)_so.servant;
			try
			{
			_localServant.relinquishInterface(interfaceName,terminationText,digitalSignature);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listInterfaces() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listInterfaces", true);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.fw.TpInterfaceNameListHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listInterfaces", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAccessOperations _localServant = (IpAccessOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listInterfaces();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.IpInterface obtainInterface(java.lang.String interfaceName) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "obtainInterface", true);
				_os.write_string(interfaceName);
				_is = _invoke(_os);
				org.csapi.IpInterface _result = org.csapi.IpInterfaceHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_NAME:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_NAMEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "obtainInterface", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAccessOperations _localServant = (IpAccessOperations)_so.servant;
			org.csapi.IpInterface _result;			try
			{
			_result = _localServant.obtainInterface(interfaceName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String selectSigningAlgorithm(java.lang.String signingAlgorithmCaps) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHM
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "selectSigningAlgorithm", true);
				_os.write_string(signingAlgorithmCaps);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
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
				else if( _id.equals("IDL:org/csapi/fw/P_NO_ACCEPTABLE_SIGNING_ALGORITHM:1.0"))
				{
					throw org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHMHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "selectSigningAlgorithm", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAccessOperations _localServant = (IpAccessOperations)_so.servant;
			java.lang.String _result;			try
			{
			_result = _localServant.selectSigningAlgorithm(signingAlgorithmCaps);
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
