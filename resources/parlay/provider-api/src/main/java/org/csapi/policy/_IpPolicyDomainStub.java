package org.csapi.policy;


/**
 *	Generated from IDL interface "IpPolicyDomain"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPolicyDomainStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.policy.IpPolicyDomain
{
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyDomain:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.policy.IpPolicyDomainOperations.class;
	public void removeGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeGroup", true);
				_os.write_string(groupName);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeGroup", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.removeGroup(groupName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.IpPolicyDomain getParentDomain() throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getParentDomain", true);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyDomain _result = org.csapi.policy.IpPolicyDomainHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getParentDomain", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyDomain _result;			try
			{
			_result = _localServant.getParentDomain();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyRule getRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRule", true);
				_os.write_string(ruleName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyRule _result = org.csapi.policy.IpPolicyRuleHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRule", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyRule _result;			try
			{
			_result = _localServant.getRule(ruleName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setAttributes(org.csapi.TpAttribute[] targetAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setAttributes", true);
				org.csapi.TpAttributeSetHelper.write(_os,targetAttributes);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.setAttributes(targetAttributes);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int getVariableSetCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getVariableSetCount", true);
				_is = _invoke(_os);
				int _result = _is.read_long();
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getVariableSetCount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.getVariableSetCount();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void removeDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeDomain", true);
				_os.write_string(domainName);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeDomain", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.removeDomain(domainName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.IpPolicyDomain getDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getDomain", true);
				_os.write_string(domainName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyDomain _result = org.csapi.policy.IpPolicyDomainHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getDomain", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyDomain _result;			try
			{
			_result = _localServant.getDomain(domainName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setVariable(java.lang.String variableSetName, org.csapi.TpAttribute variable) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setVariable", true);
				_os.write_string(variableSetName);
				org.csapi.TpAttributeHelper.write(_os,variable);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setVariable", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.setVariable(variableSetName,variable);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void createVariableSet(java.lang.String variableSetName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createVariableSet", true);
				_os.write_string(variableSetName);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createVariableSet", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.createVariableSet(variableSetName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.TpAttribute[] getAttributes(java.lang.String[] attributeNames) throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAttributes", true);
				org.csapi.TpStringListHelper.write(_os,attributeNames);
				_is = _invoke(_os);
				org.csapi.TpAttribute[] _result = org.csapi.TpAttributeSetHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAttributes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.TpAttribute[] _result;			try
			{
			_result = _localServant.getAttributes(attributeNames);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public int getEventDefinitionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getEventDefinitionCount", true);
				_is = _invoke(_os);
				int _result = _is.read_long();
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getEventDefinitionCount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.getEventDefinitionCount();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyIterator getVariableSetIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getVariableSetIterator", true);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyIterator _result = org.csapi.policy.IpPolicyIteratorHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getVariableSetIterator", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyIterator _result;			try
			{
			_result = _localServant.getVariableSetIterator();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyDomain createDomain(java.lang.String domainName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createDomain", true);
				_os.write_string(domainName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyDomain _result = org.csapi.policy.IpPolicyDomainHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createDomain", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyDomain _result;			try
			{
			_result = _localServant.createDomain(domainName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void removeEventDefinition(java.lang.String eventDefinitionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeEventDefinition", true);
				_os.write_string(eventDefinitionName);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeEventDefinition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.removeEventDefinition(eventDefinitionName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int getGroupCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getGroupCount", true);
				_is = _invoke(_os);
				int _result = _is.read_long();
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getGroupCount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.getGroupCount();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public int createNotification(org.csapi.policy.IpAppPolicyDomain appPolicyDomain, java.lang.String[] events) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createNotification", true);
				org.csapi.policy.IpAppPolicyDomainHelper.write(_os,appPolicyDomain);
				org.csapi.TpStringSetHelper.write(_os,events);
				_is = _invoke(_os);
				int _result = _is.read_long();
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.createNotification(appPolicyDomain,events);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void removeRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeRule", true);
				_os.write_string(ruleName);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeRule", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.removeRule(ruleName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void setAttribute(org.csapi.TpAttribute targetAttribute) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setAttribute", true);
				org.csapi.TpAttributeHelper.write(_os,targetAttribute);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setAttribute", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.setAttribute(targetAttribute);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void removeVariableSet(java.lang.String variableSetName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeVariableSet", true);
				_os.write_string(variableSetName);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeVariableSet", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.removeVariableSet(variableSetName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.IpPolicyIterator getEventDefinitionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getEventDefinitionIterator", true);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyIterator _result = org.csapi.policy.IpPolicyIteratorHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getEventDefinitionIterator", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyIterator _result;			try
			{
			_result = _localServant.getEventDefinitionIterator();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyIterator getRuleIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRuleIterator", true);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyIterator _result = org.csapi.policy.IpPolicyIteratorHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRuleIterator", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyIterator _result;			try
			{
			_result = _localServant.getRuleIterator();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public int getRuleCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getRuleCount", true);
				_is = _invoke(_os);
				int _result = _is.read_long();
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getRuleCount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.getRuleCount();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public int getDomainCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getDomainCount", true);
				_is = _invoke(_os);
				int _result = _is.read_long();
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getDomainCount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.getDomainCount();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.TpAttribute getVariable(java.lang.String variableSetName, java.lang.String variableName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getVariable", true);
				_os.write_string(variableSetName);
				_os.write_string(variableName);
				_is = _invoke(_os);
				org.csapi.TpAttribute _result = org.csapi.TpAttributeHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getVariable", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.TpAttribute _result;			try
			{
			_result = _localServant.getVariable(variableSetName,variableName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyEventDefinition getEventDefinition(java.lang.String eventDefinitionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getEventDefinition", true);
				_os.write_string(eventDefinitionName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyEventDefinition _result = org.csapi.policy.IpPolicyEventDefinitionHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getEventDefinition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyEventDefinition _result;			try
			{
			_result = _localServant.getEventDefinition(eventDefinitionName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyIterator getDomainIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getDomainIterator", true);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyIterator _result = org.csapi.policy.IpPolicyIteratorHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getDomainIterator", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyIterator _result;			try
			{
			_result = _localServant.getDomainIterator();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyGroup getGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getGroup", true);
				_os.write_string(groupName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyGroup _result = org.csapi.policy.IpPolicyGroupHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getGroup", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyGroup _result;			try
			{
			_result = _localServant.getGroup(groupName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.TpAttribute[] getVariableSet(java.lang.String variableSetName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getVariableSet", true);
				_os.write_string(variableSetName);
				_is = _invoke(_os);
				org.csapi.TpAttribute[] _result = org.csapi.TpAttributeSetHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getVariableSet", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.TpAttribute[] _result;			try
			{
			_result = _localServant.getVariableSet(variableSetName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void destroyNotification(int assignmentID, java.lang.String[] events) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "destroyNotification", true);
				_os.write_long(assignmentID);
				org.csapi.TpStringSetHelper.write(_os,events);
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
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "destroyNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.destroyNotification(assignmentID,events);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.IpPolicyRule createRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createRule", true);
				_os.write_string(ruleName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyRule _result = org.csapi.policy.IpPolicyRuleHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createRule", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyRule _result;			try
			{
			_result = _localServant.createRule(ruleName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyIterator getGroupIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getGroupIterator", true);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyIterator _result = org.csapi.policy.IpPolicyIteratorHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getGroupIterator", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyIterator _result;			try
			{
			_result = _localServant.getGroupIterator();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyGroup createGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createGroup", true);
				_os.write_string(groupName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyGroup _result = org.csapi.policy.IpPolicyGroupHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createGroup", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyGroup _result;			try
			{
			_result = _localServant.createGroup(groupName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void generateEvent(java.lang.String eventDefinitionName, org.csapi.TpAttribute[] attributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "generateEvent", true);
				_os.write_string(eventDefinitionName);
				org.csapi.TpAttributeSetHelper.write(_os,attributes);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "generateEvent", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.generateEvent(eventDefinitionName,attributes);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.IpPolicyEventDefinition createEventDefinition(java.lang.String eventDefinitionName, java.lang.String[] requiredAttributes, java.lang.String[] optionalAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createEventDefinition", true);
				_os.write_string(eventDefinitionName);
				org.csapi.TpStringSetHelper.write(_os,requiredAttributes);
				org.csapi.TpStringSetHelper.write(_os,optionalAttributes);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyEventDefinition _result = org.csapi.policy.IpPolicyEventDefinitionHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_ACCESS_VIOLATION:1.0"))
				{
					throw org.csapi.policy.P_ACCESS_VIOLATIONHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NO_TRANSACTION_IN_PROCESS:1.0"))
				{
					throw org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createEventDefinition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.policy.IpPolicyEventDefinition _result;			try
			{
			_result = _localServant.createEventDefinition(eventDefinitionName,requiredAttributes,optionalAttributes);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.TpAttribute getAttribute(java.lang.String attributeName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAttribute", true);
				_os.write_string(attributeName);
				_is = _invoke(_os);
				org.csapi.TpAttribute _result = org.csapi.TpAttributeHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/policy/P_NAME_SPACE_ERROR:1.0"))
				{
					throw org.csapi.policy.P_NAME_SPACE_ERRORHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/policy/P_SYNTAX_ERROR:1.0"))
				{
					throw org.csapi.policy.P_SYNTAX_ERRORHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAttribute", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyDomainOperations _localServant = (IpPolicyDomainOperations)_so.servant;
			org.csapi.TpAttribute _result;			try
			{
			_result = _localServant.getAttribute(attributeName);
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
