package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyAction"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPolicyActionPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.policy.IpPolicyActionOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getParentRepository", new java.lang.Integer(0));
		m_opsHash.put ( "getParentRule", new java.lang.Integer(1));
		m_opsHash.put ( "setAttributes", new java.lang.Integer(2));
		m_opsHash.put ( "getAttributes", new java.lang.Integer(3));
		m_opsHash.put ( "getAttribute", new java.lang.Integer(4));
		m_opsHash.put ( "setAttribute", new java.lang.Integer(5));
	}
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyAction:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/policy/IpPolicy:1.0"};
	public org.csapi.policy.IpPolicyAction _this()
	{
		return org.csapi.policy.IpPolicyActionHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyAction _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyActionHelper.narrow(_this_object(orb));
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
			case 0: // getParentRepository
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
			case 1: // getParentRule
			{
			try
			{
				_out = handler.createReply();
				org.csapi.policy.IpPolicyRuleHelper.write(_out,getParentRule());
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
			case 3: // getAttributes
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
			case 4: // getAttribute
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
			case 5: // setAttribute
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
