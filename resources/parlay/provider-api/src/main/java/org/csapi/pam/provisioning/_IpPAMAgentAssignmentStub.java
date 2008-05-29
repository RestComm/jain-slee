package org.csapi.pam.provisioning;


/**
 *	Generated from IDL interface "IpPAMAgentAssignment"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPAMAgentAssignmentStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.pam.provisioning.IpPAMAgentAssignment
{
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMAgentAssignment:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.pam.provisioning.IpPAMAgentAssignmentOperations.class;
	public java.lang.String[] listAssignedAgentsByCapability(java.lang.String identity, java.lang.String capability, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAssignedAgentsByCapability", true);
				_os.write_string(identity);
				_os.write_string(capability);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.pam.TpPAMFQNameListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAssignedAgentsByCapability", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentAssignmentOperations _localServant = (IpPAMAgentAssignmentOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAssignedAgentsByCapability(identity,capability,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void unassignAgent(java.lang.String identity, java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENT,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "unassignAgent", true);
				_os.write_string(identity);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ASSIGNMENT:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENTHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "unassignAgent", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentAssignmentOperations _localServant = (IpPAMAgentAssignmentOperations)_so.servant;
			try
			{
			_localServant.unassignAgent(identity,agentName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listCapabilitiesOfIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listCapabilitiesOfIdentity", true);
				_os.write_string(identity);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listCapabilitiesOfIdentity", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentAssignmentOperations _localServant = (IpPAMAgentAssignmentOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listCapabilitiesOfIdentity(identity,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void assignAgent(java.lang.String identity, java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "assignAgent", true);
				_os.write_string(identity);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "assignAgent", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentAssignmentOperations _localServant = (IpPAMAgentAssignmentOperations)_so.servant;
			try
			{
			_localServant.assignAgent(identity,agentName,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public java.lang.String[] listAssignedAgents(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAssignedAgents", true);
				_os.write_string(identity);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.pam.TpPAMFQNameListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAssignedAgents", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentAssignmentOperations _localServant = (IpPAMAgentAssignmentOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAssignedAgents(identity,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public boolean isIdentityCapableOf(java.lang.String identity, java.lang.String capability, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "isIdentityCapableOf", true);
				_os.write_string(identity);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "isIdentityCapableOf", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentAssignmentOperations _localServant = (IpPAMAgentAssignmentOperations)_so.servant;
			boolean _result;			try
			{
			_result = _localServant.isIdentityCapableOf(identity,capability,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listAssociatedIdentitiesOfAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAssociatedIdentitiesOfAgent", true);
				_os.write_string(agentName);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.pam.TpPAMFQNameListHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAssociatedIdentitiesOfAgent", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMAgentAssignmentOperations _localServant = (IpPAMAgentAssignmentOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAssociatedIdentitiesOfAgent(agentName,authToken);
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
