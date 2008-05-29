package org.csapi.fw.fw_service.service_lifecycle;

/**
 *	Generated from IDL interface "IpServiceInstanceLifecycleManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpServiceInstanceLifecycleManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "createServiceManager", new java.lang.Integer(0));
		m_opsHash.put ( "destroyServiceManager", new java.lang.Integer(1));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_service/service_lifecycle/IpServiceInstanceLifecycleManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager _this()
	{
		return org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManagerHelper.narrow(_this_object(orb));
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
			case 0: // createServiceManager
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.fw.TpServiceProperty[] _arg1=org.csapi.fw.TpServicePropertyListHelper.read(_input);
				java.lang.String _arg2=_input.read_string();
				_out = handler.createReply();
				org.csapi.IpServiceHelper.write(_out,createServiceManager(_arg0,_arg1,_arg2));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_PROPERTY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_PROPERTYHelper.write(_out, _ex1);
			}
				break;
			}
			case 1: // destroyServiceManager
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				destroyServiceManager(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
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
