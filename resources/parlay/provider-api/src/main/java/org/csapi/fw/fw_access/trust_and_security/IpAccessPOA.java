package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAccessPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_access.trust_and_security.IpAccessOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "releaseInterface", new java.lang.Integer(0));
		m_opsHash.put ( "obtainInterfaceWithCallback", new java.lang.Integer(1));
		m_opsHash.put ( "terminateAccess", new java.lang.Integer(2));
		m_opsHash.put ( "endAccess", new java.lang.Integer(3));
		m_opsHash.put ( "relinquishInterface", new java.lang.Integer(4));
		m_opsHash.put ( "listInterfaces", new java.lang.Integer(5));
		m_opsHash.put ( "obtainInterface", new java.lang.Integer(6));
		m_opsHash.put ( "selectSigningAlgorithm", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_access/trust_and_security/IpAccess:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_access.trust_and_security.IpAccess _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAccessHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpAccess _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAccessHelper.narrow(_this_object(orb));
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
			case 0: // releaseInterface
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				releaseInterface(_arg0);
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
			catch(org.csapi.P_INVALID_INTERFACE_NAME _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_NAMEHelper.write(_out, _ex2);
			}
				break;
			}
			case 1: // obtainInterfaceWithCallback
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.IpInterface _arg1=org.csapi.IpInterfaceHelper.read(_input);
				_out = handler.createReply();
				org.csapi.IpInterfaceHelper.write(_out,obtainInterfaceWithCallback(_arg0,_arg1));
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
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_INVALID_INTERFACE_NAME _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_NAMEHelper.write(_out, _ex3);
			}
				break;
			}
			case 2: // terminateAccess
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				terminateAccess(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_SIGNATURE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SIGNATUREHelper.write(_out, _ex1);
			}
				break;
			}
			case 3: // endAccess
			{
			try
			{
				org.csapi.fw.TpProperty[] _arg0=org.csapi.fw.TpPropertyListHelper.read(_input);
				_out = handler.createReply();
				endAccess(_arg0);
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
			catch(org.csapi.fw.P_INVALID_PROPERTY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_PROPERTYHelper.write(_out, _ex2);
			}
				break;
			}
			case 4: // relinquishInterface
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				relinquishInterface(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_SIGNATURE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SIGNATUREHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_INTERFACE_NAME _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_NAMEHelper.write(_out, _ex2);
			}
				break;
			}
			case 5: // listInterfaces
			{
			try
			{
				_out = handler.createReply();
				org.csapi.fw.TpInterfaceNameListHelper.write(_out,listInterfaces());
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
			case 6: // obtainInterface
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.IpInterfaceHelper.write(_out,obtainInterface(_arg0));
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
			catch(org.csapi.P_INVALID_INTERFACE_NAME _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_NAMEHelper.write(_out, _ex2);
			}
				break;
			}
			case 7: // selectSigningAlgorithm
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				_out.write_string(selectSigningAlgorithm(_arg0));
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
			catch(org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHM _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHMHelper.write(_out, _ex2);
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
