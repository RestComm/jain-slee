package org.csapi.termcap;

/**
 *	Generated from IDL interface "IpExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpExtendedTerminalCapabilitiesPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.termcap.IpExtendedTerminalCapabilitiesOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(0));
		m_opsHash.put ( "triggeredTerminalCapabilityStartReq", new java.lang.Integer(1));
		m_opsHash.put ( "getTerminalCapabilities", new java.lang.Integer(2));
		m_opsHash.put ( "triggeredTerminalCapabilityStop", new java.lang.Integer(3));
		m_opsHash.put ( "setCallback", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/csapi/termcap/IpExtendedTerminalCapabilities:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/termcap/IpTerminalCapabilities:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.termcap.IpExtendedTerminalCapabilities _this()
	{
		return org.csapi.termcap.IpExtendedTerminalCapabilitiesHelper.narrow(_this_object());
	}
	public org.csapi.termcap.IpExtendedTerminalCapabilities _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.termcap.IpExtendedTerminalCapabilitiesHelper.narrow(_this_object(orb));
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
			case 0: // setCallbackWithSessionID
			{
			try
			{
				org.csapi.IpInterface _arg0=org.csapi.IpInterfaceHelper.read(_input);
				int _arg1=_input.read_long();
				_out = handler.createReply();
				setCallbackWithSessionID(_arg0,_arg1);
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
			catch(org.csapi.P_INVALID_SESSION_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 1: // triggeredTerminalCapabilityStartReq
			{
			try
			{
				org.csapi.termcap.IpAppExtendedTerminalCapabilities _arg0=org.csapi.termcap.IpAppExtendedTerminalCapabilitiesHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				org.csapi.termcap.TpTerminalCapabilityScope _arg2=org.csapi.termcap.TpTerminalCapabilityScopeHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				_out.write_long(triggeredTerminalCapabilityStartReq(_arg0,_arg1,_arg2,_arg3));
			}
			catch(org.csapi.P_INVALID_INTERFACE_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INFORMATION_NOT_AVAILABLE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INFORMATION_NOT_AVAILABLEHelper.write(_out, _ex1);
			}
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
			}
			catch(org.csapi.termcap.P_INVALID_TERMINAL_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.termcap.P_INVALID_TERMINAL_IDHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex4);
			}
				break;
			}
			case 2: // getTerminalCapabilities
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.termcap.TpTerminalCapabilitiesHelper.write(_out,getTerminalCapabilities(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.termcap.P_INVALID_TERMINAL_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.termcap.P_INVALID_TERMINAL_IDHelper.write(_out, _ex1);
			}
				break;
			}
			case 3: // triggeredTerminalCapabilityStop
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				triggeredTerminalCapabilityStop(_arg0);
			}
			catch(org.csapi.P_INVALID_ASSIGNMENT_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_ASSIGNMENT_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
				break;
			}
			case 4: // setCallback
			{
			try
			{
				org.csapi.IpInterface _arg0=org.csapi.IpInterfaceHelper.read(_input);
				_out = handler.createReply();
				setCallback(_arg0);
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
