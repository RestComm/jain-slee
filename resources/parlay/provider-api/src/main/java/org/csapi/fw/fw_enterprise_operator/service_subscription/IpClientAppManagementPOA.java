package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpClientAppManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpClientAppManagementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "modifySAG", new java.lang.Integer(0));
		m_opsHash.put ( "removeSAGMembers", new java.lang.Integer(1));
		m_opsHash.put ( "requestConflictInfo", new java.lang.Integer(2));
		m_opsHash.put ( "addSAGMembers", new java.lang.Integer(3));
		m_opsHash.put ( "createClientApp", new java.lang.Integer(4));
		m_opsHash.put ( "deleteClientApp", new java.lang.Integer(5));
		m_opsHash.put ( "createSAG", new java.lang.Integer(6));
		m_opsHash.put ( "modifyClientApp", new java.lang.Integer(7));
		m_opsHash.put ( "deleteSAG", new java.lang.Integer(8));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpClientAppManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagement _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppManagementHelper.narrow(_this_object(orb));
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
			case 0: // modifySAG
			{
			try
			{
				org.csapi.fw.TpSag _arg0=org.csapi.fw.TpSagHelper.read(_input);
				_out = handler.createReply();
				modifySAG(_arg0);
			}
			catch(org.csapi.fw.P_INVALID_SAG_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SAG_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
				break;
			}
			case 1: // removeSAGMembers
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.fw.TpClientAppIDListHelper.read(_input);
				_out = handler.createReply();
				removeSAGMembers(_arg0,_arg1);
			}
			catch(org.csapi.fw.P_INVALID_SAG_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SAG_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_INVALID_CLIENT_APP_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_CLIENT_APP_IDHelper.write(_out, _ex3);
			}
				break;
			}
			case 2: // requestConflictInfo
			{
			try
			{
				_out = handler.createReply();
				org.csapi.fw.TpAddSagMembersConflictListHelper.write(_out,requestConflictInfo());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex1);
			}
				break;
			}
			case 3: // addSAGMembers
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String[] _arg1=org.csapi.fw.TpClientAppIDListHelper.read(_input);
				_out = handler.createReply();
				addSAGMembers(_arg0,_arg1);
			}
			catch(org.csapi.fw.P_INVALID_SAG_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SAG_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_INVALID_ADDITION_TO_SAG _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_ADDITION_TO_SAGHelper.write(_out, _ex3);
			}
			catch(org.csapi.fw.P_INVALID_CLIENT_APP_ID _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_CLIENT_APP_IDHelper.write(_out, _ex4);
			}
				break;
			}
			case 4: // createClientApp
			{
			try
			{
				org.csapi.fw.TpClientAppDescription _arg0=org.csapi.fw.TpClientAppDescriptionHelper.read(_input);
				_out = handler.createReply();
				createClientApp(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_INVALID_CLIENT_APP_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_CLIENT_APP_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 5: // deleteClientApp
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				deleteClientApp(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_INVALID_CLIENT_APP_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_CLIENT_APP_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 6: // createSAG
			{
			try
			{
				org.csapi.fw.TpSag _arg0=org.csapi.fw.TpSagHelper.read(_input);
				java.lang.String[] _arg1=org.csapi.fw.TpClientAppIDListHelper.read(_input);
				_out = handler.createReply();
				createSAG(_arg0,_arg1);
			}
			catch(org.csapi.fw.P_INVALID_SAG_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SAG_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_INVALID_CLIENT_APP_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_CLIENT_APP_IDHelper.write(_out, _ex3);
			}
				break;
			}
			case 7: // modifyClientApp
			{
			try
			{
				org.csapi.fw.TpClientAppDescription _arg0=org.csapi.fw.TpClientAppDescriptionHelper.read(_input);
				_out = handler.createReply();
				modifyClientApp(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_INVALID_CLIENT_APP_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_CLIENT_APP_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 8: // deleteSAG
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				deleteSAG(_arg0);
			}
			catch(org.csapi.fw.P_INVALID_SAG_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SAG_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
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
