package org.csapi.fw.fw_service.discovery;

/**
 *	Generated from IDL interface "IpFwServiceDiscovery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpFwServiceDiscoveryPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_service.discovery.IpFwServiceDiscoveryOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "listServiceTypes", new java.lang.Integer(0));
		m_opsHash.put ( "listRegisteredServices", new java.lang.Integer(1));
		m_opsHash.put ( "discoverService", new java.lang.Integer(2));
		m_opsHash.put ( "describeServiceType", new java.lang.Integer(3));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_service/discovery/IpFwServiceDiscovery:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_service.discovery.IpFwServiceDiscovery _this()
	{
		return org.csapi.fw.fw_service.discovery.IpFwServiceDiscoveryHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.discovery.IpFwServiceDiscovery _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.discovery.IpFwServiceDiscoveryHelper.narrow(_this_object(orb));
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
			case 0: // listServiceTypes
			{
			try
			{
				_out = handler.createReply();
				org.csapi.fw.TpServiceTypeNameListHelper.write(_out,listServiceTypes());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // listRegisteredServices
			{
			try
			{
				_out = handler.createReply();
				org.csapi.fw.TpServiceListHelper.write(_out,listRegisteredServices());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // discoverService
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.fw.TpServiceProperty[] _arg1=org.csapi.fw.TpServicePropertyListHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				org.csapi.fw.TpServiceListHelper.write(_out,discoverService(_arg0,_arg1,_arg2));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_ILLEGAL_SERVICE_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ILLEGAL_SERVICE_TYPEHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_INVALID_PROPERTY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_PROPERTYHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_UNKNOWN_SERVICE_TYPE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_UNKNOWN_SERVICE_TYPEHelper.write(_out, _ex3);
			}
				break;
			}
			case 3: // describeServiceType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpServiceTypeDescriptionHelper.write(_out,describeServiceType(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_ILLEGAL_SERVICE_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ILLEGAL_SERVICE_TYPEHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_UNKNOWN_SERVICE_TYPE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_UNKNOWN_SERVICE_TYPEHelper.write(_out, _ex2);
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
