package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyRepository"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPolicyRepositoryPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.policy.IpPolicyRepositoryOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getConditionCount", new java.lang.Integer(0));
		m_opsHash.put ( "setAttributes", new java.lang.Integer(1));
		m_opsHash.put ( "createRepository", new java.lang.Integer(2));
		m_opsHash.put ( "createCondition", new java.lang.Integer(3));
		m_opsHash.put ( "createAction", new java.lang.Integer(4));
		m_opsHash.put ( "getAttributes", new java.lang.Integer(5));
		m_opsHash.put ( "getRepositoryIterator", new java.lang.Integer(6));
		m_opsHash.put ( "getCondition", new java.lang.Integer(7));
		m_opsHash.put ( "setAttribute", new java.lang.Integer(8));
		m_opsHash.put ( "removeAction", new java.lang.Integer(9));
		m_opsHash.put ( "getAction", new java.lang.Integer(10));
		m_opsHash.put ( "removeRepository", new java.lang.Integer(11));
		m_opsHash.put ( "getConditionIterator", new java.lang.Integer(12));
		m_opsHash.put ( "getRepositoryCount", new java.lang.Integer(13));
		m_opsHash.put ( "getParentRepository", new java.lang.Integer(14));
		m_opsHash.put ( "getActionCount", new java.lang.Integer(15));
		m_opsHash.put ( "getAttribute", new java.lang.Integer(16));
		m_opsHash.put ( "removeCondition", new java.lang.Integer(17));
		m_opsHash.put ( "getActionIterator", new java.lang.Integer(18));
		m_opsHash.put ( "getRepository", new java.lang.Integer(19));
	}
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyRepository:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public org.csapi.policy.IpPolicyRepository _this()
	{
		return org.csapi.policy.IpPolicyRepositoryHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyRepository _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyRepositoryHelper.narrow(_this_object(orb));
	}
	public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler)
		throws org.omg.CORBA.SystemException
	{
		org.omg.CORBA.portable.OutputStream _out = null;
		// do something
		// quick lookup of operation
		java.lang.Integer opsIndex = (java.lang.Integer)m_opsHash.get ( method );
		if ( null == opsIndex )
			throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
		switch ( opsIndex.intValue() )
		{
			case 0: // getConditionCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getConditionCount());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
				break;
			}
			case 1: // setAttributes
			{
			try
			{
				org.csapi.TpAttribute[] _arg0=org.csapi.TpAttributeSetHelper.read(_input);
				_out = handler.createReply();
				setAttributes(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex2);
			}
				break;
			}
			case 2: // createRepository
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyRepositoryHelper.write(_out,createRepository(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex3);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex4);
			}
				break;
			}
			case 3: // createCondition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.policy.TpPolicyConditionType _arg1=org.csapi.policy.TpPolicyConditionTypeHelper.read(_input);
				org.csapi.TpAttribute[] _arg2=org.csapi.TpAttributeSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.policy.IpPolicyConditionHelper.write(_out,createCondition(_arg0,_arg1,_arg2));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex3);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex4);
			}
				break;
			}
			case 4: // createAction
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.policy.TpPolicyActionType _arg1=org.csapi.policy.TpPolicyActionTypeHelper.read(_input);
				org.csapi.TpAttribute[] _arg2=org.csapi.TpAttributeSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.policy.IpPolicyActionHelper.write(_out,createAction(_arg0,_arg1,_arg2));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex3);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex4);
			}
				break;
			}
			case 5: // getAttributes
			{
			try
			{
				java.lang.String[] _arg0=org.csapi.TpStringListHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpAttributeSetHelper.write(_out,getAttributes(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // getRepositoryIterator
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyIteratorHelper.write(_out,getRepositoryIterator());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
				break;
			}
			case 7: // getCondition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyConditionHelper.write(_out,getCondition(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex3);
			}
				break;
			}
			case 8: // setAttribute
			{
			try
			{
				org.csapi.TpAttribute _arg0=org.csapi.TpAttributeHelper.read(_input);
				_out = handler.createReply();
				setAttribute(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex2);
			}
				break;
			}
			case 9: // removeAction
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeAction(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex3);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex4);
			}
				break;
			}
			case 10: // getAction
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyActionHelper.write(_out,getAction(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex3);
			}
				break;
			}
			case 11: // removeRepository
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeRepository(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex3);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex4);
			}
				break;
			}
			case 12: // getConditionIterator
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyIteratorHelper.write(_out,getConditionIterator());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
				break;
			}
			case 13: // getRepositoryCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getRepositoryCount());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
				break;
			}
			case 14: // getParentRepository
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyRepositoryHelper.write(_out,getParentRepository());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // getActionCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getActionCount());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
				break;
			}
			case 16: // getAttribute
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.TpAttributeHelper.write(_out,getAttribute(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex2);
			}
				break;
			}
			case 17: // removeCondition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeCondition(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex3);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex4);
			}
				break;
			}
			case 18: // getActionIterator
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyIteratorHelper.write(_out,getActionIterator());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
				break;
			}
			case 19: // getRepository
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyRepositoryHelper.write(_out,getRepository(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_NAME_SPACE_ERROR _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NAME_SPACE_ERRORHelper.write(_out, _ex2);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex3);
			}
				break;
			}
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
