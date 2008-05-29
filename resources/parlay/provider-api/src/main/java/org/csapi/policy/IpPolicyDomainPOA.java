package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyDomain"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPolicyDomainPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.policy.IpPolicyDomainOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "removeGroup", new java.lang.Integer(0));
		m_opsHash.put ( "getParentDomain", new java.lang.Integer(1));
		m_opsHash.put ( "getRule", new java.lang.Integer(2));
		m_opsHash.put ( "setAttributes", new java.lang.Integer(3));
		m_opsHash.put ( "getVariableSetCount", new java.lang.Integer(4));
		m_opsHash.put ( "removeDomain", new java.lang.Integer(5));
		m_opsHash.put ( "getDomain", new java.lang.Integer(6));
		m_opsHash.put ( "setVariable", new java.lang.Integer(7));
		m_opsHash.put ( "createVariableSet", new java.lang.Integer(8));
		m_opsHash.put ( "getAttributes", new java.lang.Integer(9));
		m_opsHash.put ( "getEventDefinitionCount", new java.lang.Integer(10));
		m_opsHash.put ( "getVariableSetIterator", new java.lang.Integer(11));
		m_opsHash.put ( "createDomain", new java.lang.Integer(12));
		m_opsHash.put ( "removeEventDefinition", new java.lang.Integer(13));
		m_opsHash.put ( "getGroupCount", new java.lang.Integer(14));
		m_opsHash.put ( "createNotification", new java.lang.Integer(15));
		m_opsHash.put ( "removeRule", new java.lang.Integer(16));
		m_opsHash.put ( "setAttribute", new java.lang.Integer(17));
		m_opsHash.put ( "removeVariableSet", new java.lang.Integer(18));
		m_opsHash.put ( "getEventDefinitionIterator", new java.lang.Integer(19));
		m_opsHash.put ( "getRuleIterator", new java.lang.Integer(20));
		m_opsHash.put ( "getRuleCount", new java.lang.Integer(21));
		m_opsHash.put ( "getDomainCount", new java.lang.Integer(22));
		m_opsHash.put ( "getVariable", new java.lang.Integer(23));
		m_opsHash.put ( "getEventDefinition", new java.lang.Integer(24));
		m_opsHash.put ( "getDomainIterator", new java.lang.Integer(25));
		m_opsHash.put ( "getGroup", new java.lang.Integer(26));
		m_opsHash.put ( "getVariableSet", new java.lang.Integer(27));
		m_opsHash.put ( "destroyNotification", new java.lang.Integer(28));
		m_opsHash.put ( "createRule", new java.lang.Integer(29));
		m_opsHash.put ( "getGroupIterator", new java.lang.Integer(30));
		m_opsHash.put ( "createGroup", new java.lang.Integer(31));
		m_opsHash.put ( "generateEvent", new java.lang.Integer(32));
		m_opsHash.put ( "createEventDefinition", new java.lang.Integer(33));
		m_opsHash.put ( "getAttribute", new java.lang.Integer(34));
	}
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyDomain:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public org.csapi.policy.IpPolicyDomain _this()
	{
		return org.csapi.policy.IpPolicyDomainHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyDomain _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyDomainHelper.narrow(_this_object(orb));
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
			case 0: // removeGroup
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeGroup(_arg0);
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
			case 1: // getParentDomain
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyDomainHelper.write(_out,getParentDomain());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getRule
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyRuleHelper.write(_out,getRule(_arg0));
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
			case 3: // setAttributes
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
			case 4: // getVariableSetCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getVariableSetCount());
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
			case 5: // removeDomain
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeDomain(_arg0);
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
			case 6: // getDomain
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyDomainHelper.write(_out,getDomain(_arg0));
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
			case 7: // setVariable
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.TpAttribute _arg1=org.csapi.TpAttributeHelper.read(_input);
				_out = handler.createReply();
				setVariable(_arg0,_arg1);
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
			case 8: // createVariableSet
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				createVariableSet(_arg0);
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
			case 9: // getAttributes
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
			case 10: // getEventDefinitionCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getEventDefinitionCount());
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
			case 11: // getVariableSetIterator
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyIteratorHelper.write(_out,getVariableSetIterator());
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
			case 12: // createDomain
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyDomainHelper.write(_out,createDomain(_arg0));
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
			case 13: // removeEventDefinition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeEventDefinition(_arg0);
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
			case 14: // getGroupCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getGroupCount());
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
			case 15: // createNotification
			{
			try
			{
				org.csapi.policy.IpAppPolicyDomain _arg0=org.csapi.policy.IpAppPolicyDomainHelper.read(_input);
				java.lang.String[] _arg1=org.csapi.TpStringSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(createNotification(_arg0,_arg1));
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
			case 16: // removeRule
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeRule(_arg0);
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
			case 17: // setAttribute
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
			case 18: // removeVariableSet
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeVariableSet(_arg0);
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
			case 19: // getEventDefinitionIterator
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyIteratorHelper.write(_out,getEventDefinitionIterator());
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
			case 20: // getRuleIterator
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyIteratorHelper.write(_out,getRuleIterator());
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
			case 21: // getRuleCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getRuleCount());
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
			case 22: // getDomainCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getDomainCount());
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
			case 23: // getVariable
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.csapi.TpAttributeHelper.write(_out,getVariable(_arg0,_arg1));
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
			case 24: // getEventDefinition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyEventDefinitionHelper.write(_out,getEventDefinition(_arg0));
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
			case 25: // getDomainIterator
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyIteratorHelper.write(_out,getDomainIterator());
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
			case 26: // getGroup
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyGroupHelper.write(_out,getGroup(_arg0));
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
			case 27: // getVariableSet
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.TpAttributeSetHelper.write(_out,getVariableSet(_arg0));
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
			case 28: // destroyNotification
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String[] _arg1=org.csapi.TpStringSetHelper.read(_input);
				_out = handler.createReply();
				destroyNotification(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex1);
			}
				break;
			}
			case 29: // createRule
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyRuleHelper.write(_out,createRule(_arg0));
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
			case 30: // getGroupIterator
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyIteratorHelper.write(_out,getGroupIterator());
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
			case 31: // createGroup
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.policy.IpPolicyGroupHelper.write(_out,createGroup(_arg0));
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
			case 32: // generateEvent
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.TpAttribute[] _arg1=org.csapi.TpAttributeSetHelper.read(_input);
				_out = handler.createReply();
				generateEvent(_arg0,_arg1);
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
			case 33: // createEventDefinition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringSetHelper.read(_input);
				java.lang.String[] _arg2=org.csapi.TpStringSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.policy.IpPolicyEventDefinitionHelper.write(_out,createEventDefinition(_arg0,_arg1,_arg2));
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
			case 34: // getAttribute
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
