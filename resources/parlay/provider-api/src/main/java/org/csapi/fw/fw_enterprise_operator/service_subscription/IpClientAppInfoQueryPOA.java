package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpClientAppInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpClientAppInfoQueryPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQueryOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "describeSAG", new java.lang.Integer(0));
		m_opsHash.put ( "describeClientApp", new java.lang.Integer(1));
		m_opsHash.put ( "listSAGMembers", new java.lang.Integer(2));
		m_opsHash.put ( "listSAGs", new java.lang.Integer(3));
		m_opsHash.put ( "listClientAppMembership", new java.lang.Integer(4));
		m_opsHash.put ( "listClientApps", new java.lang.Integer(5));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpClientAppInfoQuery:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQuery _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQueryHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQuery _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpClientAppInfoQueryHelper.narrow(_this_object(orb));
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
			case 0: // describeSAG
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				_out.write_string(describeSAG(_arg0));
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
			case 1: // describeClientApp
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpClientAppDescriptionHelper.write(_out,describeClientApp(_arg0));
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
			case 2: // listSAGMembers
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpClientAppIDListHelper.write(_out,listSAGMembers(_arg0));
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
			case 3: // listSAGs
			{
			try
			{
				_out = handler.createReply();
				org.csapi.fw.TpSagIDListHelper.write(_out,listSAGs());
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
			case 4: // listClientAppMembership
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpSagIDListHelper.write(_out,listClientAppMembership(_arg0));
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
			case 5: // listClientApps
			{
			try
			{
				_out = handler.createReply();
				org.csapi.fw.TpClientAppIDListHelper.write(_out,listClientApps());
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
