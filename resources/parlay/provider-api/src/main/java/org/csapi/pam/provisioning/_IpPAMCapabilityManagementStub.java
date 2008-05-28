package org.csapi.pam.provisioning;


/**
 *	Generated from IDL interface "IpPAMCapabilityManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPAMCapabilityManagementStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.pam.provisioning.IpPAMCapabilityManagement
{
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMCapabilityManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.pam.provisioning.IpPAMCapabilityManagementOperations.class;
	public java.lang.String[] listAllCapabilityAttributes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAllCapabilityAttributes", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAllCapabilityAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAllCapabilityAttributes(authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void addCapabilityAttributes(java.lang.String capabilityName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "addCapabilityAttributes", true);
				_os.write_string(capabilityName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "addCapabilityAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			try
			{
			_localServant.addCapabilityAttributes(capabilityName,attributeNames,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void unassignCapabilitiesFromType(java.lang.String agentType, java.lang.String[] capabilities, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "unassignCapabilitiesFromType", true);
				_os.write_string(agentType);
				org.csapi.pam.TpPAMCapabilityListHelper.write(_os,capabilities);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_CAPABILITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_CAPABILITYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "unassignCapabilitiesFromType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			try
			{
			_localServant.unassignCapabilitiesFromType(agentType,capabilities,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void deleteCapabilityAttribute(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "deleteCapabilityAttribute", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "deleteCapabilityAttribute", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			try
			{
			_localServant.deleteCapabilityAttribute(attributeName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void createCapabilityAttribute(org.csapi.pam.TpPAMAttributeDef pAttribute, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createCapabilityAttribute", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createCapabilityAttribute", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			try
			{
			_localServant.createCapabilityAttribute(pAttribute,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void deleteCapability(java.lang.String capabilityName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "deleteCapability", true);
				_os.write_string(capabilityName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "deleteCapability", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			try
			{
			_localServant.deleteCapability(capabilityName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listCapabilities(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listCapabilities", true);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.pam.TpPAMCapabilityListHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listCapabilities", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listCapabilities(authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void assignCapabilitiesToType(java.lang.String agentType, java.lang.String[] capabilities, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "assignCapabilitiesToType", true);
				_os.write_string(agentType);
				org.csapi.pam.TpPAMCapabilityListHelper.write(_os,capabilities);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_CAPABILITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_CAPABILITYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "assignCapabilitiesToType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			try
			{
			_localServant.assignCapabilitiesToType(agentType,capabilities,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.pam.TpPAMAttributeDef getCapabilityAttributeDefinition(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getCapabilityAttributeDefinition", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getCapabilityAttributeDefinition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			org.csapi.pam.TpPAMAttributeDef _result;			try
			{
			_result = _localServant.getCapabilityAttributeDefinition(attributeName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listCapabilityAttributes(java.lang.String capabilityName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listCapabilityAttributes", true);
				_os.write_string(capabilityName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listCapabilityAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listCapabilityAttributes(capabilityName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void removeCapabilityAttributes(java.lang.String capabilityName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeCapabilityAttributes", true);
				_os.write_string(capabilityName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeCapabilityAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			try
			{
			_localServant.removeCapabilityAttributes(capabilityName,attributeNames,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listCapabilitiesOfType(java.lang.String agentType, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listCapabilitiesOfType", true);
				_os.write_string(agentType);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.pam.TpPAMCapabilityListHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listCapabilitiesOfType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listCapabilitiesOfType(agentType,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void createCapability(java.lang.String capabilityName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_TYPE_EXISTS
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createCapability", true);
				_os.write_string(capabilityName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createCapability", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMCapabilityManagementOperations _localServant = (IpPAMCapabilityManagementOperations)_so.servant;
			try
			{
			_localServant.createCapability(capabilityName,attributeNames,authToken);
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
