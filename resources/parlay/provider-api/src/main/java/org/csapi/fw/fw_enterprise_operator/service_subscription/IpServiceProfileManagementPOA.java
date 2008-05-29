package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceProfileManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpServiceProfileManagementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "assign", new java.lang.Integer(0));
		m_opsHash.put ( "requestConflictInfo", new java.lang.Integer(1));
		m_opsHash.put ( "deleteServiceProfile", new java.lang.Integer(2));
		m_opsHash.put ( "modifyServiceProfile", new java.lang.Integer(3));
		m_opsHash.put ( "deassign", new java.lang.Integer(4));
		m_opsHash.put ( "createServiceProfile", new java.lang.Integer(5));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpServiceProfileManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagement _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileManagementHelper.narrow(_this_object(orb));
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
			case 0: // assign
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				assign(_arg0,_arg1);
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
			catch(org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENTHelper.write(_out, _ex3);
			}
			catch(org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_PROFILE_IDHelper.write(_out, _ex4);
			}
				break;
			}
			case 1: // requestConflictInfo
			{
			try
			{
				_out = handler.createReply();
				org.csapi.fw.TpAssignSagToServiceProfileConflictListHelper.write(_out,requestConflictInfo());
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
			case 2: // deleteServiceProfile
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				deleteServiceProfile(_arg0);
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
			catch(org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_PROFILE_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 3: // modifyServiceProfile
			{
			try
			{
				org.csapi.fw.TpServiceProfile _arg0=org.csapi.fw.TpServiceProfileHelper.read(_input);
				_out = handler.createReply();
				modifyServiceProfile(_arg0);
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
			catch(org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_PROFILE_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 4: // deassign
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				deassign(_arg0,_arg1);
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
			catch(org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_PROFILE_IDHelper.write(_out, _ex3);
			}
				break;
			}
			case 5: // createServiceProfile
			{
			try
			{
				org.csapi.fw.TpServiceProfileDescription _arg0=org.csapi.fw.TpServiceProfileDescriptionHelper.read(_input);
				_out = handler.createReply();
				_out.write_string(createServiceProfile(_arg0));
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
