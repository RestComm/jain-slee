package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpInitial"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpInitialPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_access.trust_and_security.IpInitialOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "initiateAuthenticationWithVersion", new java.lang.Integer(0));
		m_opsHash.put ( "initiateAuthentication", new java.lang.Integer(1));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_access/trust_and_security/IpInitial:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_access.trust_and_security.IpInitial _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpInitialHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpInitial _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpInitialHelper.narrow(_this_object(orb));
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
			case 0: // initiateAuthenticationWithVersion
			{
			try
			{
				org.csapi.fw.TpAuthDomain _arg0=org.csapi.fw.TpAuthDomainHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				java.lang.String _arg2=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpAuthDomainHelper.write(_out,initiateAuthenticationWithVersion(_arg0,_arg1,_arg2));
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
			catch(org.csapi.P_INVALID_VERSION _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_VERSIONHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_INVALID_AUTH_TYPE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_AUTH_TYPEHelper.write(_out, _ex3);
			}
			catch(org.csapi.fw.P_INVALID_DOMAIN_ID _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_DOMAIN_IDHelper.write(_out, _ex4);
			}
				break;
			}
			case 1: // initiateAuthentication
			{
			try
			{
				org.csapi.fw.TpAuthDomain _arg0=org.csapi.fw.TpAuthDomainHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpAuthDomainHelper.write(_out,initiateAuthentication(_arg0,_arg1));
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
			catch(org.csapi.fw.P_INVALID_AUTH_TYPE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_AUTH_TYPEHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_INVALID_DOMAIN_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_DOMAIN_IDHelper.write(_out, _ex3);
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
