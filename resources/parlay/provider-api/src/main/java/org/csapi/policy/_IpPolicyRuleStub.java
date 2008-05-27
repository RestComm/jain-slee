package org.csapi.policy;


/**
 *	Generated from IDL interface "IpPolicyRule"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPolicyRuleStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.policy.IpPolicyRule
{
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyRule:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.policy.IpPolicyRuleOperations.class;
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
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
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

	public void setActionList(org.csapi.policy.TpPolicyActionListElement[] actionList) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setActionList", true);
				org.csapi.policy.TpPolicyActionListHelper.write(_os,actionList);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setActionList", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			try
			{
			_localServant.setActionList(actionList);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.IpPolicyTimePeriodCondition getValidityPeriodCondition() throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getValidityPeriodCondition", true);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyTimePeriodCondition _result = org.csapi.policy.IpPolicyTimePeriodConditionHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getValidityPeriodCondition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.IpPolicyTimePeriodCondition _result;			try
			{
			_result = _localServant.getValidityPeriodCondition();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
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
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
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
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
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

	public int getConditionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getConditionCount", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getConditionCount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.getConditionCount();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyCondition createCondition(java.lang.String conditionName, org.csapi.policy.TpPolicyConditionType conditionType, org.csapi.TpAttribute[] conditionAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createCondition", true);
				_os.write_string(conditionName);
				org.csapi.policy.TpPolicyConditionTypeHelper.write(_os,conditionType);
				org.csapi.TpAttributeSetHelper.write(_os,conditionAttributes);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyCondition _result = org.csapi.policy.IpPolicyConditionHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createCondition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.IpPolicyCondition _result;			try
			{
			_result = _localServant.createCondition(conditionName,conditionType,conditionAttributes);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setConditionList(org.csapi.policy.TpPolicyConditionListElement[] conditionList) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setConditionList", true);
				org.csapi.policy.TpPolicyConditionListHelper.write(_os,conditionList);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setConditionList", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			try
			{
			_localServant.setConditionList(conditionList);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.IpPolicyAction createAction(java.lang.String actionName, org.csapi.policy.TpPolicyActionType actionType, org.csapi.TpAttribute[] actionAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createAction", true);
				_os.write_string(actionName);
				org.csapi.policy.TpPolicyActionTypeHelper.write(_os,actionType);
				org.csapi.TpAttributeSetHelper.write(_os,actionAttributes);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyAction _result = org.csapi.policy.IpPolicyActionHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createAction", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.IpPolicyAction _result;			try
			{
			_result = _localServant.createAction(actionName,actionType,actionAttributes);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
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
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
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

	public org.csapi.policy.IpPolicyCondition getCondition(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getCondition", true);
				_os.write_string(conditionName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyCondition _result = org.csapi.policy.IpPolicyConditionHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getCondition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.IpPolicyCondition _result;			try
			{
			_result = _localServant.getCondition(conditionName);
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
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
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

	public void removeAction(java.lang.String actionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeAction", true);
				_os.write_string(actionName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeAction", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			try
			{
			_localServant.removeAction(actionName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void unsetValidityPeriodCondition() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "unsetValidityPeriodCondition", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "unsetValidityPeriodCondition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			try
			{
			_localServant.unsetValidityPeriodCondition();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.TpPolicyActionListElement[] getActionList() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getActionList", true);
				_is = _invoke(_os);
				org.csapi.policy.TpPolicyActionListElement[] _result = org.csapi.policy.TpPolicyActionListHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getActionList", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.TpPolicyActionListElement[] _result;			try
			{
			_result = _localServant.getActionList();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyAction getAction(java.lang.String actionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getAction", true);
				_os.write_string(actionName);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyAction _result = org.csapi.policy.IpPolicyActionHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getAction", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.IpPolicyAction _result;			try
			{
			_result = _localServant.getAction(actionName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyIterator getConditionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getConditionIterator", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getConditionIterator", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.IpPolicyIterator _result;			try
			{
			_result = _localServant.getConditionIterator();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.TpPolicyConditionListElement[] getConditionList() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getConditionList", true);
				_is = _invoke(_os);
				org.csapi.policy.TpPolicyConditionListElement[] _result = org.csapi.policy.TpPolicyConditionListHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getConditionList", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.TpPolicyConditionListElement[] _result;			try
			{
			_result = _localServant.getConditionList();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setValidityPeriodCondition(org.csapi.policy.IpPolicyTimePeriodCondition conditionReference) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setValidityPeriodCondition", true);
				org.csapi.policy.IpPolicyTimePeriodConditionHelper.write(_os,conditionReference);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setValidityPeriodCondition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			try
			{
			_localServant.setValidityPeriodCondition(conditionReference);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void setValidityPeriodConditionByName(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setValidityPeriodConditionByName", true);
				_os.write_string(conditionName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setValidityPeriodConditionByName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			try
			{
			_localServant.setValidityPeriodConditionByName(conditionName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int getActionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getActionCount", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getActionCount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.getActionCount();
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
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
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

	public void removeCondition(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "removeCondition", true);
				_os.write_string(conditionName);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "removeCondition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			try
			{
			_localServant.removeCondition(conditionName);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.policy.IpPolicyIterator getActionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getActionIterator", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getActionIterator", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyRuleOperations _localServant = (IpPolicyRuleOperations)_so.servant;
			org.csapi.policy.IpPolicyIterator _result;			try
			{
			_result = _localServant.getActionIterator();
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
