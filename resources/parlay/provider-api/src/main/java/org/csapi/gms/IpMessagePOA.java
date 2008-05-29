package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMessage"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpMessagePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.gms.IpMessageOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setInfoProperties", new java.lang.Integer(0));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(1));
		m_opsHash.put ( "getContent", new java.lang.Integer(2));
		m_opsHash.put ( "getInfoProperties", new java.lang.Integer(3));
		m_opsHash.put ( "getInfoAmount", new java.lang.Integer(4));
		m_opsHash.put ( "remove", new java.lang.Integer(5));
		m_opsHash.put ( "setCallback", new java.lang.Integer(6));
	}
	private String[] ids = {"IDL:org/csapi/gms/IpMessage:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.gms.IpMessage _this()
	{
		return org.csapi.gms.IpMessageHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpMessage _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpMessageHelper.narrow(_this_object(orb));
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
			case 0: // setInfoProperties
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				int _arg2=_input.read_long();
				org.csapi.gms.TpMessageInfoProperty[] _arg3=org.csapi.gms.TpMessageInfoPropertySetHelper.read(_input);
				_out = handler.createReply();
				setInfoProperties(_arg0,_arg1,_arg2,_arg3);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.gms.P_GMS_PROPERTY_NOT_SET _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_PROPERTY_NOT_SETHelper.write(_out, _ex1);
			}
			catch(org.csapi.gms.P_GMS_INVALID_MESSAGE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_MESSAGE_IDHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex3);
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
			case 2: // getContent
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				_out.write_string(getContent(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.gms.P_GMS_INVALID_MESSAGE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_MESSAGE_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 3: // getInfoProperties
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				int _arg2=_input.read_long();
				int _arg3=_input.read_long();
				_out = handler.createReply();
				org.csapi.gms.TpMessageInfoPropertySetHelper.write(_out,getInfoProperties(_arg0,_arg1,_arg2,_arg3));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.gms.P_GMS_INVALID_MESSAGE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_MESSAGE_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex2);
			}
			catch(org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVEHelper.write(_out, _ex3);
			}
				break;
			}
			case 4: // getInfoAmount
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				_out.write_long(getInfoAmount(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.gms.P_GMS_INVALID_MESSAGE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_MESSAGE_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 5: // remove
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				remove(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.gms.P_GMS_INVALID_MESSAGE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_MESSAGE_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex3);
			}
			catch(org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGEHelper.write(_out, _ex4);
			}
				break;
			}
			case 6: // setCallback
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
