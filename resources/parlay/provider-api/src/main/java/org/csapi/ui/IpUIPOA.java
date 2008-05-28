package org.csapi.ui;

/**
 *	Generated from IDL interface "IpUI"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpUIPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.ui.IpUIOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(0));
		m_opsHash.put ( "sendInfoReq", new java.lang.Integer(1));
		m_opsHash.put ( "getOriginatingAddress", new java.lang.Integer(2));
		m_opsHash.put ( "setOriginatingAddress", new java.lang.Integer(3));
		m_opsHash.put ( "sendInfoAndCollectReq", new java.lang.Integer(4));
		m_opsHash.put ( "release", new java.lang.Integer(5));
		m_opsHash.put ( "setCallback", new java.lang.Integer(6));
	}
	private String[] ids = {"IDL:org/csapi/ui/IpUI:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.ui.IpUI _this()
	{
		return org.csapi.ui.IpUIHelper.narrow(_this_object());
	}
	public org.csapi.ui.IpUI _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.ui.IpUIHelper.narrow(_this_object(orb));
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
			case 1: // sendInfoReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.ui.TpUIInfo _arg1=org.csapi.ui.TpUIInfoHelper.read(_input);
				java.lang.String _arg2=_input.read_string();
				org.csapi.ui.TpUIVariableInfo[] _arg3=org.csapi.ui.TpUIVariableInfoSetHelper.read(_input);
				int _arg4=_input.read_long();
				int _arg5=_input.read_long();
				_out = handler.createReply();
				_out.write_long(sendInfoReq(_arg0,_arg1,_arg2,_arg3,_arg4,_arg5));
			}
			catch(org.csapi.ui.P_ILLEGAL_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.ui.P_ILLEGAL_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_NETWORK_STATE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_NETWORK_STATEHelper.write(_out, _ex1);
			}
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
			}
			catch(org.csapi.ui.P_ID_NOT_FOUND _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.ui.P_ID_NOT_FOUNDHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex4);
			}
				break;
			}
			case 2: // getOriginatingAddress
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				_out.write_string(getOriginatingAddress(_arg0));
			}
			catch(org.csapi.P_INVALID_NETWORK_STATE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_NETWORK_STATEHelper.write(_out, _ex0);
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
			case 3: // setOriginatingAddress
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				setOriginatingAddress(_arg0,_arg1);
			}
			catch(org.csapi.P_INVALID_NETWORK_STATE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_NETWORK_STATEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_ADDRESS _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_ADDRESSHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex3);
			}
				break;
			}
			case 4: // sendInfoAndCollectReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.ui.TpUIInfo _arg1=org.csapi.ui.TpUIInfoHelper.read(_input);
				java.lang.String _arg2=_input.read_string();
				org.csapi.ui.TpUIVariableInfo[] _arg3=org.csapi.ui.TpUIVariableInfoSetHelper.read(_input);
				org.csapi.ui.TpUICollectCriteria _arg4=org.csapi.ui.TpUICollectCriteriaHelper.read(_input);
				int _arg5=_input.read_long();
				_out = handler.createReply();
				_out.write_long(sendInfoAndCollectReq(_arg0,_arg1,_arg2,_arg3,_arg4,_arg5));
			}
			catch(org.csapi.ui.P_ILLEGAL_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.ui.P_ILLEGAL_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_NETWORK_STATE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_NETWORK_STATEHelper.write(_out, _ex1);
			}
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
			}
			catch(org.csapi.ui.P_ILLEGAL_RANGE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.ui.P_ILLEGAL_RANGEHelper.write(_out, _ex3);
			}
			catch(org.csapi.ui.P_ID_NOT_FOUND _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.ui.P_ID_NOT_FOUNDHelper.write(_out, _ex4);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex5);
			}
			catch(org.csapi.ui.P_INVALID_COLLECTION_CRITERIA _ex6)
			{
				_out = handler.createExceptionReply();
				org.csapi.ui.P_INVALID_COLLECTION_CRITERIAHelper.write(_out, _ex6);
			}
				break;
			}
			case 5: // release
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				release(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex1);
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
