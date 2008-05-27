package org.csapi.pam.provisioning;


/**
 *	Generated from IDL interface "IpPAMAgentManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPAMAgentManagementStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.pam.provisioning.IpPAMAgentManagement
{
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMAgentManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.pam.provisioning.IpPAMAgentManagementOperations.class;
	public void setAgentAttributes(java.lang.String agentName, java.lang.String agentType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTES,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setAgentAttributes", true);
				_os.write_string(agentName);
				_os.write_string(agentType);
				org.csapi.pam.TpPAMAttributeListHelper.write(_os,attributes);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTES:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTESHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setAgentAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			try
			{
			_localServant.setAgentAttributes(agentName,agentType,attributes,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public boolean hasType(java.lang.String agentName, java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "hasType", true);
				_os.write_string(agentName);
				_os.write_string(typeName);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				boolean _result = _is.read_boolean();
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "hasType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			boolean _result;			try
			{
			_result = _localServant.hasType(agentName,typeName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void disableCapabilities(java.lang.String agentName, java.lang.String[] capabilities, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_NO_CAPABILITY,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "disableCapabilities", true);
				_os.write_string(agentName);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_NO_CAPABILITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_NO_CAPABILITYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "disableCapabilities", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			try
			{
			_localServant.disableCapabilities(agentName,capabilities,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public boolean isAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "isAgent", true);
				_os.write_string(agentName);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				boolean _result = _is.read_boolean();
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "isAgent", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			boolean _result;			try
			{
			_result = _localServant.isAgent(agentName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void disassociateTypes(java.lang.String agentName, java.lang.String[] agentTypes, byte[] authToken) throws org.csapi.pam.P_PAM_DISASSOCIATED_TYPE,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "disassociateTypes", true);
				_os.write_string(agentName);
				org.csapi.TpStringListHelper.write(_os,agentTypes);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/pam/P_PAM_DISASSOCIATED_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_DISASSOCIATED_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "disassociateTypes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			try
			{
			_localServant.disassociateTypes(agentName,agentTypes,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listTypesOfAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listTypesOfAgent", true);
				_os.write_string(agentName);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listTypesOfAgent", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listTypesOfAgent(agentName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void associateTypes(java.lang.String agentName, java.lang.String[] agentTypes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_TYPE_ASSOCIATED,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "associateTypes", true);
				_os.write_string(agentName);
				org.csapi.TpStringListHelper.write(_os,agentTypes);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_TYPE_ASSOCIATED:1.0"))
				{
					throw org.csapi.pam.P_PAM_TYPE_ASSOCIATEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "associateTypes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			try
			{
			_localServant.associateTypes(agentName,agentTypes,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.pam.TpPAMAttribute[] getAgentAttributes(java.lang.String agentName, java.lang.String agentType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAgentAttributes", true);
				_os.write_string(agentName);
				_os.write_string(agentType);
				org.csapi.TpStringListHelper.write(_os,attributeNames);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				org.csapi.pam.TpPAMAttribute[] _result = org.csapi.pam.TpPAMAttributeListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAgentAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			org.csapi.pam.TpPAMAttribute[] _result;			try
			{
			_result = _localServant.getAgentAttributes(agentName,agentType,attributeNames,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listAllCapabilities(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAllCapabilities", true);
				_os.write_string(agentName);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAllCapabilities", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAllCapabilities(agentName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void createAgent(java.lang.String agentName, java.lang.String[] agentTypes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_AGENT_EXISTS
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createAgent", true);
				_os.write_string(agentName);
				org.csapi.TpStringListHelper.write(_os,agentTypes);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_AGENT_EXISTS:1.0"))
				{
					throw org.csapi.pam.P_PAM_AGENT_EXISTSHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createAgent", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			try
			{
			_localServant.createAgent(agentName,agentTypes,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void enableCapabilities(java.lang.String agentName, java.lang.String[] capabilities, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "enableCapabilities", true);
				_os.write_string(agentName);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "enableCapabilities", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			try
			{
			_localServant.enableCapabilities(agentName,capabilities,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void deleteAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "deleteAgent", true);
				_os.write_string(agentName);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "deleteAgent", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			try
			{
			_localServant.deleteAgent(agentName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listEnabledCapabilities(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listEnabledCapabilities", true);
				_os.write_string(agentName);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listEnabledCapabilities", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listEnabledCapabilities(agentName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public boolean isCapableOf(java.lang.String agentName, java.lang.String capability, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "isCapableOf", true);
				_os.write_string(agentName);
				_os.write_string(capability);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				boolean _result = _is.read_boolean();
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_AGENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "isCapableOf", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentManagementOperations _localServant = (IpPAMAgentManagementOperations)_so.servant;
			boolean _result;			try
			{
			_result = _localServant.isCapableOf(agentName,capability,authToken);
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
