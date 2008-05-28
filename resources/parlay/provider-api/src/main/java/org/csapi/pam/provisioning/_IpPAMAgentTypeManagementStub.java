package org.csapi.pam.provisioning;


/**
 *	Generated from IDL interface "IpPAMAgentTypeManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPAMAgentTypeManagementStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.pam.provisioning.IpPAMAgentTypeManagement
{
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMAgentTypeManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.pam.provisioning.IpPAMAgentTypeManagementOperations.class;
	public org.csapi.pam.TpPAMAttributeDef getAgentAttributeDefinition(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAgentAttributeDefinition", true);
				_os.write_string(attributeName);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				org.csapi.pam.TpPAMAttributeDef _result = org.csapi.pam.TpPAMAttributeDefHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAgentAttributeDefinition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			org.csapi.pam.TpPAMAttributeDef _result;			try
			{
			_result = _localServant.getAgentAttributeDefinition(attributeName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void deleteAgentAttribute(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "deleteAgentAttribute", true);
				_os.write_string(attributeName);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "deleteAgentAttribute", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			try
			{
			_localServant.deleteAgentAttribute(attributeName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void addAgentTypeAttributes(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "addAgentTypeAttributes", true);
				_os.write_string(typeName);
				org.csapi.TpStringListHelper.write(_os,attributeNames);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_ATTRIBUTE_EXISTS:1.0"))
				{
					throw org.csapi.pam.P_PAM_ATTRIBUTE_EXISTSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "addAgentTypeAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			try
			{
			_localServant.addAgentTypeAttributes(typeName,attributeNames,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listAgentTypeAttributes(java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAgentTypeAttributes", true);
				_os.write_string(typeName);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.TpStringListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAgentTypeAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAgentTypeAttributes(typeName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listAgentTypes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAgentTypes", true);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.TpStringListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAgentTypes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAgentTypes(authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void createAgentType(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_TYPE_EXISTS
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createAgentType", true);
				_os.write_string(typeName);
				org.csapi.TpStringListHelper.write(_os,attributeNames);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_TYPE_EXISTS:1.0"))
				{
					throw org.csapi.pam.P_PAM_TYPE_EXISTSHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createAgentType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			try
			{
			_localServant.createAgentType(typeName,attributeNames,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void deleteAgentType(java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "deleteAgentType", true);
				_os.write_string(typeName);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "deleteAgentType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			try
			{
			_localServant.deleteAgentType(typeName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void createAgentAttribute(org.csapi.pam.TpPAMAttributeDef pAttribute, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createAgentAttribute", true);
				org.csapi.pam.TpPAMAttributeDefHelper.write(_os,pAttribute);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_ATTRIBUTE_EXISTS:1.0"))
				{
					throw org.csapi.pam.P_PAM_ATTRIBUTE_EXISTSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createAgentAttribute", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			try
			{
			_localServant.createAgentAttribute(pAttribute,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listAllAgentAttributes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAllAgentAttributes", true);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.TpStringListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAllAgentAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAllAgentAttributes(authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void removeAgentTypeAttributes(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeAgentTypeAttributes", true);
				_os.write_string(typeName);
				org.csapi.TpStringListHelper.write(_os,attributeNames);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeAgentTypeAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentTypeManagementOperations _localServant = (IpPAMAgentTypeManagementOperations)_so.servant;
			try
			{
			_localServant.removeAgentTypeAttributes(typeName,attributeNames,authToken);
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
