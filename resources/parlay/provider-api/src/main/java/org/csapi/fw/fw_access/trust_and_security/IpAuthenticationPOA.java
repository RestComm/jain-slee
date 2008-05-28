package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAuthenticationPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_access.trust_and_security.IpAuthenticationOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "requestAccess", new java.lang.Integer(0));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_access/trust_and_security/IpAuthentication:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_access.trust_and_security.IpAuthentication _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAuthenticationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpAuthentication _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAuthenticationHelper.narrow(_this_object(orb));
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
			case 0: // requestAccess
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.IpInterface _arg1=org.csapi.IpInterfaceHelper.read(_input);
				_out = handler.createReply();
				org.csapi.IpInterfaceHelper.write(_out,requestAccess(_arg0,_arg1));
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
			catch(org.csapi.fw.P_INVALID_ACCESS_TYPE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_ACCESS_TYPEHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex3);
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
