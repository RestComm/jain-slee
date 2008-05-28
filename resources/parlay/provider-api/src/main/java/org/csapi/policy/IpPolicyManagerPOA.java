package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPolicyManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.policy.IpPolicyManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "abortTransaction", new java.lang.Integer(0));
		m_opsHash.put ( "removeRepository", new java.lang.Integer(1));
		m_opsHash.put ( "getDomain", new java.lang.Integer(2));
		m_opsHash.put ( "createRepository", new java.lang.Integer(3));
		m_opsHash.put ( "getDomainCount", new java.lang.Integer(4));
		m_opsHash.put ( "createDomain", new java.lang.Integer(5));
		m_opsHash.put ( "getRepository", new java.lang.Integer(6));
		m_opsHash.put ( "commitTransaction", new java.lang.Integer(7));
		m_opsHash.put ( "startTransaction", new java.lang.Integer(8));
		m_opsHash.put ( "removeDomain", new java.lang.Integer(9));
		m_opsHash.put ( "findMatchingDomains", new java.lang.Integer(10));
		m_opsHash.put ( "getRepositoryIterator", new java.lang.Integer(11));
		m_opsHash.put ( "getRepositoryCount", new java.lang.Integer(12));
		m_opsHash.put ( "getDomainIterator", new java.lang.Integer(13));
	}
	private String[] ids = {"IDL:org/csapi/policy/IpPolicyManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.policy.IpPolicyManager _this()
	{
		return org.csapi.policy.IpPolicyManagerHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyManagerHelper.narrow(_this_object(orb));
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
			case 0: // abortTransaction
			{
			try
			{
				_out = handler.createReply();
				abortTransaction();
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex1);
			}
				break;
			}
			case 1: // removeRepository
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
			case 2: // getDomain
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
			case 3: // createRepository
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
			case 4: // getDomainCount
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
			case 5: // createDomain
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
			case 6: // getRepository
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
			case 7: // commitTransaction
			{
			try
			{
				_out = handler.createReply();
				_out.write_boolean(commitTransaction());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_NO_TRANSACTION_IN_PROCESSHelper.write(_out, _ex1);
			}
				break;
			}
			case 8: // startTransaction
			{
			try
			{
				_out = handler.createReply();
				startTransaction();
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.policy.P_TRANSACTION_IN_PROCESS _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_TRANSACTION_IN_PROCESSHelper.write(_out, _ex1);
			}
			catch(org.csapi.policy.P_ACCESS_VIOLATION _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.policy.P_ACCESS_VIOLATIONHelper.write(_out, _ex2);
			}
				break;
			}
			case 9: // removeDomain
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
			case 10: // findMatchingDomains
			{
			try
			{
				org.csapi.TpAttribute[] _arg0=org.csapi.TpAttributeSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpStringSetHelper.write(_out,findMatchingDomains(_arg0));
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
			case 11: // getRepositoryIterator
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
			case 12: // getRepositoryCount
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
			case 13: // getDomainIterator
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
