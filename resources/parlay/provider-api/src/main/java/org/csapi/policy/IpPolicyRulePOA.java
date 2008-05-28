package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyRule"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPolicyRulePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.policy.IpPolicyRuleOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getParentDomain", new java.lang.Integer(0));
		m_opsHash.put ( "setActionList", new java.lang.Integer(1));
		m_opsHash.put ( "getValidityPeriodCondition", new java.lang.Integer(2));
		m_opsHash.put ( "getParentGroup", new java.lang.Integer(3));
		m_opsHash.put ( "setAttributes", new java.lang.Integer(4));
		m_opsHash.put ( "getConditionCount", new java.lang.Integer(5));
		m_opsHash.put ( "createCondition", new java.lang.Integer(6));
		m_opsHash.put ( "setConditionList", new java.lang.Integer(7));
		m_opsHash.put ( "createAction", new java.lang.Integer(8));
		m_opsHash.put ( "getAttributes", new java.lang.Integer(9));
		m_opsHash.put ( "getCondition", new java.lang.Integer(10));
		m_opsHash.put ( "setAttribute", new java.lang.Integer(11));
		m_opsHash.put ( "removeAction", new java.lang.Integer(12));
		m_opsHash.put ( "unsetValidityPeriodCondition", new java.lang.Integer(13));
		m_opsHash.put ( "getActionList", new java.lang.Integer(14));
		m_opsHash.put ( "getAction", new java.lang.Integer(15));
		m_opsHash.put ( "getConditionIterator", new java.lang.Integer(16));
		m_opsHash.put ( "getConditionList", new java.lang.Integer(17));
		m_opsHash.put ( "setValidityPeriodCondition", new java.lang.Integer(18));
		m_opsHash.put ( "setValidityPeriodConditionByName", new java.lang.Integer(19));
		m_opsHash.put ( "getActionCount", new java.lang.Integer(20));
		m_opsHash.put ( "getAttribute", new java.lang.Integer(21));
		m_opsHash.put ( "removeCondition", new java.lang.Integer(22));
		m_opsHash.put ( "getActionIterator", new java.lang.Integer(23));
	}
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyRule:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public org.csapi.policy.IpPolicyRule _this()
	{
		return org.csapi.policy.IpPolicyRuleHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyRule _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyRuleHelper.narrow(_this_object(orb));
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
			case 0: // getParentDomain
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
			case 1: // setActionList
			{
			try
			{
				org.csapi.policy.TpPolicyActionListElement[] _arg0=org.csapi.policy.TpPolicyActionListHelper.read(_input);
				_out = handler.createReply();
				setActionList(_arg0);
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
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex3);
			}
				break;
			}
			case 2: // getValidityPeriodCondition
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyTimePeriodConditionHelper.write(_out,getValidityPeriodCondition());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getParentGroup
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyGroupHelper.write(_out,getParentGroup());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // setAttributes
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
			case 5: // getConditionCount
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
			case 6: // createCondition
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
			case 7: // setConditionList
			{
			try
			{
				org.csapi.policy.TpPolicyConditionListElement[] _arg0=org.csapi.policy.TpPolicyConditionListHelper.read(_input);
				_out = handler.createReply();
				setConditionList(_arg0);
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
			catch(org.csapi.policy.P_SYNTAX_ERROR _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_SYNTAX_ERRORHelper.write(_out, _ex3);
			}
				break;
			}
			case 8: // createAction
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
			case 10: // getCondition
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
			case 11: // setAttribute
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
			case 12: // removeAction
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
			case 13: // unsetValidityPeriodCondition
			{
			try
			{
				_out = handler.createReply();
				unsetValidityPeriodCondition();
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
			case 14: // getActionList
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.TpPolicyActionListHelper.write(_out,getActionList());
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
			case 15: // getAction
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
			case 16: // getConditionIterator
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
			case 17: // getConditionList
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.TpPolicyConditionListHelper.write(_out,getConditionList());
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
			case 18: // setValidityPeriodCondition
			{
			try
			{
				org.csapi.policy.IpPolicyTimePeriodCondition _arg0=org.csapi.policy.IpPolicyTimePeriodConditionHelper.read(_input);
				_out = handler.createReply();
				setValidityPeriodCondition(_arg0);
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
			case 19: // setValidityPeriodConditionByName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setValidityPeriodConditionByName(_arg0);
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
			case 20: // getActionCount
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
			case 21: // getAttribute
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
			case 22: // removeCondition
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
			case 23: // getActionIterator
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
