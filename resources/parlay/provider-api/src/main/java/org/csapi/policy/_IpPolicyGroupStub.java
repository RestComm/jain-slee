package org.csapi.policy;


/**
 *	Generated from IDL interface "IpPolicyGroup"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPolicyGroupStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.policy.IpPolicyGroup
{
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyGroup:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.policy.IpPolicyGroupOperations.class;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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

	public org.csapi.policy.IpPolicyGroup getParentGroup() throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getParentGroup", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getParentGroup", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
			org.csapi.policy.IpPolicyGroup _result;			try
			{
			_result = _localServant.getParentGroup();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
			IpPolicyGroupOperations _localServant = (IpPolicyGroupOperations)_so.servant;
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
