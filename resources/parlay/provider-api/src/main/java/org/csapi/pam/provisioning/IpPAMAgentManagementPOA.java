package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMAgentManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPAMAgentManagementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.provisioning.IpPAMAgentManagementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setAgentAttributes", new java.lang.Integer(0));
		m_opsHash.put ( "hasType", new java.lang.Integer(1));
		m_opsHash.put ( "disableCapabilities", new java.lang.Integer(2));
		m_opsHash.put ( "isAgent", new java.lang.Integer(3));
		m_opsHash.put ( "disassociateTypes", new java.lang.Integer(4));
		m_opsHash.put ( "listTypesOfAgent", new java.lang.Integer(5));
		m_opsHash.put ( "associateTypes", new java.lang.Integer(6));
		m_opsHash.put ( "getAgentAttributes", new java.lang.Integer(7));
		m_opsHash.put ( "listAllCapabilities", new java.lang.Integer(8));
		m_opsHash.put ( "createAgent", new java.lang.Integer(9));
		m_opsHash.put ( "enableCapabilities", new java.lang.Integer(10));
		m_opsHash.put ( "deleteAgent", new java.lang.Integer(11));
		m_opsHash.put ( "listEnabledCapabilities", new java.lang.Integer(12));
		m_opsHash.put ( "isCapableOf", new java.lang.Integer(13));
	}
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMAgentManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.provisioning.IpPAMAgentManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMAgentManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMAgentManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMAgentManagementHelper.narrow(_this_object(orb));
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
			case 0: // setAgentAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				org.csapi.pam.TpPAMAttribute[] _arg2=org.csapi.pam.TpPAMAttributeListHelper.read(_input);
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				setAgentAttributes(_arg0,_arg1,_arg2,_arg3);
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
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex4);
			}
				break;
			}
			case 1: // hasType
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
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex2);
			}
				break;
			}
			case 2: // disableCapabilities
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.pam.TpPAMCapabilityListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				disableCapabilities(_arg0,_arg1,_arg2);
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
			catch(org.csapi.pam.P_PAM_NO_CAPABILITY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_NO_CAPABILITYHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex3);
			}
				break;
			}
			case 3: // isAgent
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_boolean(isAgent(_arg0,_arg1));
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
			case 4: // disassociateTypes
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
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex3);
			}
				break;
			}
			case 5: // listTypesOfAgent
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,listTypesOfAgent(_arg0,_arg1));
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex2);
			}
				break;
			}
			case 6: // associateTypes
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
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex4);
			}
				break;
			}
			case 7: // getAgentAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				java.lang.String[] _arg2=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMAttributeListHelper.write(_out,getAgentAttributes(_arg0,_arg1,_arg2,_arg3));
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
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex4);
			}
				break;
			}
			case 8: // listAllCapabilities
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMCapabilityListHelper.write(_out,listAllCapabilities(_arg0,_arg1));
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex2);
			}
				break;
			}
			case 9: // createAgent
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				createAgent(_arg0,_arg1,_arg2);
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
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_AGENT_EXISTS _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_AGENT_EXISTSHelper.write(_out, _ex3);
			}
				break;
			}
			case 10: // enableCapabilities
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.pam.TpPAMCapabilityListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				enableCapabilities(_arg0,_arg1,_arg2);
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex2);
			}
				break;
			}
			case 11: // deleteAgent
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deleteAgent(_arg0,_arg1);
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex2);
			}
				break;
			}
			case 12: // listEnabledCapabilities
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMCapabilityListHelper.write(_out,listEnabledCapabilities(_arg0,_arg1));
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex2);
			}
				break;
			}
			case 13: // isCapableOf
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_boolean(isCapableOf(_arg0,_arg1,_arg2));
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex2);
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
