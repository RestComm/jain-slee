package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpClientAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpClientAccessPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_access.trust_and_security.IpClientAccessOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "terminateAccess", new java.lang.Integer(0));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_access/trust_and_security/IpClientAccess:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_access.trust_and_security.IpClientAccess _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpClientAccessHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpClientAccess _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpClientAccessHelper.narrow(_this_object(orb));
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
			case 0: // terminateAccess
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				terminateAccess(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.fw.P_INVALID_SIGNING_ALGORITHM _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SIGNING_ALGORITHMHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_INVALID_SIGNATURE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SIGNATUREHelper.write(_out, _ex2);
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
