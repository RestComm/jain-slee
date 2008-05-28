package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMAgentTypeManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPAMAgentTypeManagementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.provisioning.IpPAMAgentTypeManagementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getAgentAttributeDefinition", new java.lang.Integer(0));
		m_opsHash.put ( "deleteAgentAttribute", new java.lang.Integer(1));
		m_opsHash.put ( "addAgentTypeAttributes", new java.lang.Integer(2));
		m_opsHash.put ( "listAgentTypeAttributes", new java.lang.Integer(3));
		m_opsHash.put ( "listAgentTypes", new java.lang.Integer(4));
		m_opsHash.put ( "createAgentType", new java.lang.Integer(5));
		m_opsHash.put ( "deleteAgentType", new java.lang.Integer(6));
		m_opsHash.put ( "createAgentAttribute", new java.lang.Integer(7));
		m_opsHash.put ( "listAllAgentAttributes", new java.lang.Integer(8));
		m_opsHash.put ( "removeAgentTypeAttributes", new java.lang.Integer(9));
	}
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMAgentTypeManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.provisioning.IpPAMAgentTypeManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMAgentTypeManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMAgentTypeManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMAgentTypeManagementHelper.narrow(_this_object(orb));
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
			case 0: // getAgentAttributeDefinition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMAttributeDefHelper.write(_out,getAgentAttributeDefinition(_arg0,_arg1));
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex2);
			}
				break;
			}
			case 1: // deleteAgentAttribute
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deleteAgentAttribute(_arg0,_arg1);
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex2);
			}
				break;
			}
			case 2: // addAgentTypeAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				addAgentTypeAttributes(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_ATTRIBUTE_EXISTSHelper.write(_out, _ex1);
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex4);
			}
				break;
			}
			case 3: // listAgentTypeAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,listAgentTypeAttributes(_arg0,_arg1));
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
				break;
			}
			case 4: // listAgentTypes
			{
			try
			{
				byte[] _arg0=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,listAgentTypes(_arg0));
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
			case 5: // createAgentType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				createAgentType(_arg0,_arg1,_arg2);
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_TYPE_EXISTS _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_TYPE_EXISTSHelper.write(_out, _ex3);
			}
				break;
			}
			case 6: // deleteAgentType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deleteAgentType(_arg0,_arg1);
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
				break;
			}
			case 7: // createAgentAttribute
			{
			try
			{
				org.csapi.pam.TpPAMAttributeDef _arg0=org.csapi.pam.TpPAMAttributeDefHelper.read(_input);
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				createAgentAttribute(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_ATTRIBUTE_EXISTSHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 8: // listAllAgentAttributes
			{
			try
			{
				byte[] _arg0=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,listAllAgentAttributes(_arg0));
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
			case 9: // removeAgentTypeAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				removeAgentTypeAttributes(_arg0,_arg1,_arg2);
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
