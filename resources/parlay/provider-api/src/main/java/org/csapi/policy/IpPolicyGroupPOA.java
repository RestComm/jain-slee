package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyGroup"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPolicyGroupPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.policy.IpPolicyGroupOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getAttributes", new java.lang.Integer(0));
		m_opsHash.put ( "getParentDomain", new java.lang.Integer(1));
		m_opsHash.put ( "setAttributes", new java.lang.Integer(2));
		m_opsHash.put ( "removeRule", new java.lang.Integer(3));
		m_opsHash.put ( "getGroupCount", new java.lang.Integer(4));
		m_opsHash.put ( "createGroup", new java.lang.Integer(5));
		m_opsHash.put ( "getGroup", new java.lang.Integer(6));
		m_opsHash.put ( "removeGroup", new java.lang.Integer(7));
		m_opsHash.put ( "getParentGroup", new java.lang.Integer(8));
		m_opsHash.put ( "createRule", new java.lang.Integer(9));
		m_opsHash.put ( "setAttribute", new java.lang.Integer(10));
		m_opsHash.put ( "getRuleIterator", new java.lang.Integer(11));
		m_opsHash.put ( "getRuleCount", new java.lang.Integer(12));
		m_opsHash.put ( "getRule", new java.lang.Integer(13));
		m_opsHash.put ( "getGroupIterator", new java.lang.Integer(14));
		m_opsHash.put ( "getAttribute", new java.lang.Integer(15));
	}
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyGroup:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public org.csapi.policy.IpPolicyGroup _this()
	{
		return org.csapi.policy.IpPolicyGroupHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyGroup _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyGroupHelper.narrow(_this_object(orb));
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
			case 0: // getAttributes
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
			case 2: // setAttributes
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
			case 3: // removeRule
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
			case 4: // getGroupCount
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
			case 5: // createGroup
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
			case 6: // getGroup
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
			case 7: // removeGroup
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
			case 8: // getParentGroup
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
			case 9: // createRule
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
			case 10: // setAttribute
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
			case 11: // getRuleIterator
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
			case 12: // getRuleCount
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
			case 13: // getRule
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
			case 14: // getGroupIterator
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
			case 15: // getAttribute
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
