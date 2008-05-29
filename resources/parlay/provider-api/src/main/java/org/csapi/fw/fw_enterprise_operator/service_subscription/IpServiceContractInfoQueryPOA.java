package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceContractInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpServiceContractInfoQueryPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractInfoQueryOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "listServiceContracts", new java.lang.Integer(0));
		m_opsHash.put ( "listServiceProfiles", new java.lang.Integer(1));
		m_opsHash.put ( "describeServiceContract", new java.lang.Integer(2));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpServiceContractInfoQuery:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractInfoQuery _this()
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractInfoQueryHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractInfoQuery _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractInfoQueryHelper.narrow(_this_object(orb));
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
			case 0: // listServiceContracts
			{
			try
			{
				_out = handler.createReply();
				org.csapi.fw.TpServiceContractIDListHelper.write(_out,listServiceContracts());
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
			case 1: // listServiceProfiles
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpServiceProfileIDListHelper.write(_out,listServiceProfiles(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_CONTRACT_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
				break;
			}
			case 2: // describeServiceContract
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpServiceContractDescriptionHelper.write(_out,describeServiceContract(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_CONTRACT_IDHelper.write(_out, _ex1);
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
