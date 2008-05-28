package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMIdentityTypeManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPAMIdentityTypeManagementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.provisioning.IpPAMIdentityTypeManagementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "deleteIdentityAttribute", new java.lang.Integer(0));
		m_opsHash.put ( "getIdentityAttributeDefinition", new java.lang.Integer(1));
		m_opsHash.put ( "removeIdentityTypeAttributes", new java.lang.Integer(2));
		m_opsHash.put ( "addIdentityTypeAttributes", new java.lang.Integer(3));
		m_opsHash.put ( "createIdentityType", new java.lang.Integer(4));
		m_opsHash.put ( "createIdentityAttribute", new java.lang.Integer(5));
		m_opsHash.put ( "listIdentityTypes", new java.lang.Integer(6));
		m_opsHash.put ( "deleteIdentityType", new java.lang.Integer(7));
		m_opsHash.put ( "listIdentityTypeAttributes", new java.lang.Integer(8));
		m_opsHash.put ( "listAllIdentityAttributes", new java.lang.Integer(9));
	}
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMIdentityTypeManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.provisioning.IpPAMIdentityTypeManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMIdentityTypeManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMIdentityTypeManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMIdentityTypeManagementHelper.narrow(_this_object(orb));
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
			case 0: // deleteIdentityAttribute
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deleteIdentityAttribute(_arg0,_arg1);
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
			case 1: // getIdentityAttributeDefinition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMAttributeDefHelper.write(_out,getIdentityAttributeDefinition(_arg0,_arg1));
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
			case 2: // removeIdentityTypeAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				removeIdentityTypeAttributes(_arg0,_arg1,_arg2);
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
			case 3: // addIdentityTypeAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				addIdentityTypeAttributes(_arg0,_arg1,_arg2);
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
			case 4: // createIdentityType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				createIdentityType(_arg0,_arg1,_arg2);
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
			case 5: // createIdentityAttribute
			{
			try
			{
				org.csapi.pam.TpPAMAttributeDef _arg0=org.csapi.pam.TpPAMAttributeDefHelper.read(_input);
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				createIdentityAttribute(_arg0,_arg1);
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
			case 6: // listIdentityTypes
			{
			try
			{
				byte[] _arg0=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,listIdentityTypes(_arg0));
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
			case 7: // deleteIdentityType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deleteIdentityType(_arg0,_arg1);
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
			case 8: // listIdentityTypeAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,listIdentityTypeAttributes(_arg0,_arg1));
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
			case 9: // listAllIdentityAttributes
			{
			try
			{
				byte[] _arg0=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,listAllIdentityAttributes(_arg0));
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
