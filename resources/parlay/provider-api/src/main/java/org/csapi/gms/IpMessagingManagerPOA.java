package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpMessagingManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.gms.IpMessagingManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "disableMessagingNotification", new java.lang.Integer(0));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(1));
		m_opsHash.put ( "enableMessagingNotification", new java.lang.Integer(2));
		m_opsHash.put ( "openMailbox", new java.lang.Integer(3));
		m_opsHash.put ( "setCallback", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/csapi/gms/IpMessagingManager:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.gms.IpMessagingManager _this()
	{
		return org.csapi.gms.IpMessagingManagerHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpMessagingManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpMessagingManagerHelper.narrow(_this_object(orb));
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
			case 0: // disableMessagingNotification
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				disableMessagingNotification(_arg0);
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
			case 1: // setCallbackWithSessionID
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
			case 2: // enableMessagingNotification
			{
			try
			{
				org.csapi.gms.IpAppMessagingManager _arg0=org.csapi.gms.IpAppMessagingManagerHelper.read(_input);
				org.csapi.gms.TpMessagingEventCriteria _arg1=org.csapi.gms.TpMessagingEventCriteriaHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(enableMessagingNotification(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex1);
			}
				break;
			}
			case 3: // openMailbox
			{
			try
			{
				org.csapi.TpAddress _arg0=org.csapi.TpAddressHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.csapi.gms.TpMailboxIdentifierHelper.write(_out,openMailbox(_arg0,_arg1));
			}
			catch(org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATIONHelper.write(_out, _ex0);
			}
			catch(org.csapi.gms.P_GMS_INVALID_MAILBOX _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_MAILBOXHelper.write(_out, _ex1);
			}
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
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
