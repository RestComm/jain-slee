package org.csapi.pam.event;

/**
 *	Generated from IDL interface "IpPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPAMEventHandlerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.event.IpPAMEventHandlerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "deregisterFromEvent", new java.lang.Integer(0));
		m_opsHash.put ( "registerAppInterface", new java.lang.Integer(1));
		m_opsHash.put ( "isRegistered", new java.lang.Integer(2));
		m_opsHash.put ( "registerForEvent", new java.lang.Integer(3));
		m_opsHash.put ( "deregisterAppInterface", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/csapi/pam/event/IpPAMEventHandler:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.event.IpPAMEventHandler _this()
	{
		return org.csapi.pam.event.IpPAMEventHandlerHelper.narrow(_this_object());
	}
	public org.csapi.pam.event.IpPAMEventHandler _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.event.IpPAMEventHandlerHelper.narrow(_this_object(orb));
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
			case 0: // deregisterFromEvent
			{
			try
			{
				int _arg0=_input.read_long();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deregisterFromEvent(_arg0,_arg1);
			}
			catch(org.csapi.pam.P_PAM_NOT_REGISTERED _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_NOT_REGISTEREDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 1: // registerAppInterface
			{
			try
			{
				org.csapi.pam.event.IpAppPAMEventHandler _arg0=org.csapi.pam.event.IpAppPAMEventHandlerHelper.read(_input);
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(registerAppInterface(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex1);
			}
				break;
			}
			case 2: // isRegistered
			{
			try
			{
				int _arg0=_input.read_long();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_boolean(isRegistered(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex1);
			}
				break;
			}
			case 3: // registerForEvent
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.pam.TpPAMEventInfo[] _arg1=org.csapi.pam.TpPAMEventInfoListHelper.read(_input);
				int _arg2=_input.read_long();
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(registerForEvent(_arg0,_arg1,_arg2,_arg3));
			}
			catch(org.csapi.pam.P_PAM_NOT_REGISTERED _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_NOT_REGISTEREDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 4: // deregisterAppInterface
			{
			try
			{
				int _arg0=_input.read_long();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				deregisterAppInterface(_arg0,_arg1);
			}
			catch(org.csapi.pam.P_PAM_NOT_REGISTERED _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_NOT_REGISTEREDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
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
