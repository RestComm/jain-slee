package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMIdentityManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPAMIdentityManagementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.provisioning.IpPAMIdentityManagementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "addToGroup", new java.lang.Integer(0));
		m_opsHash.put ( "createIdentity", new java.lang.Integer(1));
		m_opsHash.put ( "listAliases", new java.lang.Integer(2));
		m_opsHash.put ( "setIdentityAttributes", new java.lang.Integer(3));
		m_opsHash.put ( "listMembers", new java.lang.Integer(4));
		m_opsHash.put ( "addAlias", new java.lang.Integer(5));
		m_opsHash.put ( "listGroupMembership", new java.lang.Integer(6));
		m_opsHash.put ( "removeAliases", new java.lang.Integer(7));
		m_opsHash.put ( "hasType", new java.lang.Integer(8));
		m_opsHash.put ( "createGroupIdentity", new java.lang.Integer(9));
		m_opsHash.put ( "getIdentityAttributes", new java.lang.Integer(10));
		m_opsHash.put ( "lookupByAlias", new java.lang.Integer(11));
		m_opsHash.put ( "disassociateTypes", new java.lang.Integer(12));
		m_opsHash.put ( "listTypesOfIdentity", new java.lang.Integer(13));
		m_opsHash.put ( "isIdentity", new java.lang.Integer(14));
		m_opsHash.put ( "deleteGroupIdentity", new java.lang.Integer(15));
		m_opsHash.put ( "deleteIdentity", new java.lang.Integer(16));
		m_opsHash.put ( "removeFromGroup", new java.lang.Integer(17));
		m_opsHash.put ( "isGroupIdentity", new java.lang.Integer(18));
		m_opsHash.put ( "associateTypes", new java.lang.Integer(19));
	}
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMIdentityManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.provisioning.IpPAMIdentityManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMIdentityManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMIdentityManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMIdentityManagementHelper.narrow(_this_object(orb));
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
			case 0: // addToGroup
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				addToGroup(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_GROUP _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_GROUPHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_IS_CYCLIC _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_IS_CYCLICHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_MEMBER _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_MEMBERHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex4);
			}
			catch(org.csapi.pam.P_PAM_MEMBER_EXISTS _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_MEMBER_EXISTSHelper.write(_out, _ex5);
			}
				break;
			}
			case 1: // createIdentity
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				createIdentity(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.pam.P_PAM_IDENTITY_EXISTS _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_IDENTITY_EXISTSHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_TYPE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
				break;
			}
			case 2: // listAliases
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMFQNameListHelper.write(_out,listAliases(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 3: // setIdentityAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				org.csapi.pam.TpPAMAttribute[] _arg2=org.csapi.pam.TpPAMAttributeListHelper.read(_input);
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				setIdentityAttributes(_arg0,_arg1,_arg2,_arg3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTES _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTESHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_TYPE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex4);
			}
				break;
			}
			case 4: // listMembers
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMFQNameListHelper.write(_out,listMembers(_arg0,_arg1));
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_GROUP _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_GROUPHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 5: // addAlias
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				addAlias(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_ALIAS_EXISTS _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_ALIAS_EXISTSHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex4);
			}
				break;
			}
			case 6: // listGroupMembership
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMFQNameListHelper.write(_out,listGroupMembership(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 7: // removeAliases
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				removeAliases(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_UNASSIGNED_ALIAS _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNASSIGNED_ALIASHelper.write(_out, _ex3);
			}
				break;
			}
			case 8: // hasType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_boolean(hasType(_arg0,_arg1,_arg2));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 9: // createGroupIdentity
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				createGroupIdentity(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.pam.P_PAM_IDENTITY_EXISTS _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_IDENTITY_EXISTSHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_TYPE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
				break;
			}
			case 10: // getIdentityAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				java.lang.String[] _arg2=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMAttributeListHelper.write(_out,getIdentityAttributes(_arg0,_arg1,_arg2,_arg3));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex4);
			}
				break;
			}
			case 11: // lookupByAlias
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_string(lookupByAlias(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_ALIAS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ALIASHelper.write(_out, _ex2);
			}
				break;
			}
			case 12: // disassociateTypes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				disassociateTypes(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.pam.P_PAM_DISASSOCIATED_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_DISASSOCIATED_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
				break;
			}
			case 13: // listTypesOfIdentity
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMFQNameListHelper.write(_out,listTypesOfIdentity(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 14: // isIdentity
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_boolean(isIdentity(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex1);
			}
				break;
			}
			case 15: // deleteGroupIdentity
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deleteGroupIdentity(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 16: // deleteIdentity
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deleteIdentity(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 17: // removeFromGroup
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				removeFromGroup(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_GROUP _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_GROUPHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_MEMBER _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_MEMBERHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_NOT_MEMBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_NOT_MEMBERHelper.write(_out, _ex4);
			}
				break;
			}
			case 18: // isGroupIdentity
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_boolean(isGroupIdentity(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex1);
			}
				break;
			}
			case 19: // associateTypes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				associateTypes(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_TYPE_ASSOCIATED _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_TYPE_ASSOCIATEDHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_TYPE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex4);
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
