package org.csapi.policy;


/**
 *	Generated from IDL interface "IpPolicyCondition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPolicyConditionStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.policy.IpPolicyCondition
{
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyCondition:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.policy.IpPolicyConditionOperations.class;
	public org.csapi.policy.IpPolicyRepository getParentRepository() throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getParentRepository", true);
				_is = _invoke(_os);
				org.csapi.policy.IpPolicyRepository _result = org.csapi.policy.IpPolicyRepositoryHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getParentRepository", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyConditionOperations _localServant = (IpPolicyConditionOperations)_so.servant;
			org.csapi.policy.IpPolicyRepository _result;			try
			{
			_result = _localServant.getParentRepository();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.policy.IpPolicyRule getParentRule() throws org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getParentRule", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getParentRule", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPolicyConditionOperations _localServant = (IpPolicyConditionOperations)_so.servant;
			org.csapi.policy.IpPolicyRule _result;			try
			{
			_result = _localServant.getParentRule();
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
			IpPolicyConditionOperations _localServant = (IpPolicyConditionOperations)_so.servant;
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
			IpPolicyConditionOperations _localServant = (IpPolicyConditionOperations)_so.servant;
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
			IpPolicyConditionOperations _localServant = (IpPolicyConditionOperations)_so.servant;
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
			IpPolicyConditionOperations _localServant = (IpPolicyConditionOperations)_so.servant;
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

}
