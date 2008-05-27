package org.csapi.fw.fw_service.service_registration;

/**
 *	Generated from IDL interface "IpFwServiceRegistration"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpFwServiceRegistrationPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_service.service_registration.IpFwServiceRegistrationOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "announceServiceAvailability", new java.lang.Integer(0));
		m_opsHash.put ( "describeService", new java.lang.Integer(1));
		m_opsHash.put ( "unannounceService", new java.lang.Integer(2));
		m_opsHash.put ( "registerService", new java.lang.Integer(3));
		m_opsHash.put ( "unregisterService", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_service/service_registration/IpFwServiceRegistration:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration _this()
	{
		return org.csapi.fw.fw_service.service_registration.IpFwServiceRegistrationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.service_registration.IpFwServiceRegistration _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.service_registration.IpFwServiceRegistrationHelper.narrow(_this_object(orb));
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
			case 0: // announceServiceAvailability
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManager _arg1=org.csapi.fw.fw_service.service_lifecycle.IpServiceInstanceLifecycleManagerHelper.read(_input);
				_out = handler.createReply();
				announceServiceAvailability(_arg0,_arg1);
			}
			catch(org.csapi.P_INVALID_INTERFACE_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_UNKNOWN_SERVICE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_ILLEGAL_SERVICE_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ILLEGAL_SERVICE_IDHelper.write(_out, _ex3);
			}
				break;
			}
			case 1: // describeService
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpServiceDescriptionHelper.write(_out,describeService(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_UNKNOWN_SERVICE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ILLEGAL_SERVICE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ILLEGAL_SERVICE_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 2: // unannounceService
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				unannounceService(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_UNKNOWN_SERVICE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ILLEGAL_SERVICE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ILLEGAL_SERVICE_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 3: // registerService
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.fw.TpServiceProperty[] _arg1=org.csapi.fw.TpServicePropertyListHelper.read(_input);
				_out = handler.createReply();
				_out.write_string(registerService(_arg0,_arg1));
			}
			catch(org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_SERVICE_TYPE_UNAVAILABLEHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_MISSING_MANDATORY_PROPERTY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_MISSING_MANDATORY_PROPERTYHelper.write(_out, _ex1);
			}
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_ILLEGAL_SERVICE_TYPE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ILLEGAL_SERVICE_TYPEHelper.write(_out, _ex3);
			}
			catch(org.csapi.fw.P_UNKNOWN_SERVICE_TYPE _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_UNKNOWN_SERVICE_TYPEHelper.write(_out, _ex4);
			}
			catch(org.csapi.fw.P_DUPLICATE_PROPERTY_NAME _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_DUPLICATE_PROPERTY_NAMEHelper.write(_out, _ex5);
			}
			catch(org.csapi.fw.P_PROPERTY_TYPE_MISMATCH _ex6)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_PROPERTY_TYPE_MISMATCHHelper.write(_out, _ex6);
			}
				break;
			}
			case 4: // unregisterService
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				unregisterService(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_UNKNOWN_SERVICE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_UNKNOWN_SERVICE_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ILLEGAL_SERVICE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ILLEGAL_SERVICE_IDHelper.write(_out, _ex2);
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
