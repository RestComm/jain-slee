package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpCallPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.gccs.IpCallOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallback", new java.lang.Integer(0));
		m_opsHash.put ( "getMoreDialledDigitsReq", new java.lang.Integer(1));
		m_opsHash.put ( "release", new java.lang.Integer(2));
		m_opsHash.put ( "setCallChargePlan", new java.lang.Integer(3));
		m_opsHash.put ( "superviseCallReq", new java.lang.Integer(4));
		m_opsHash.put ( "getCallInfoReq", new java.lang.Integer(5));
		m_opsHash.put ( "continueProcessing", new java.lang.Integer(6));
		m_opsHash.put ( "deassignCall", new java.lang.Integer(7));
		m_opsHash.put ( "routeReq", new java.lang.Integer(8));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(9));
		m_opsHash.put ( "setAdviceOfCharge", new java.lang.Integer(10));
	}
	private String[] ids = {"IDL:org/csapi/cc/gccs/IpCall:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.cc.gccs.IpCall _this()
	{
		return org.csapi.cc.gccs.IpCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.gccs.IpCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.gccs.IpCallHelper.narrow(_this_object(orb));
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
			case 0: // setCallback
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
			case 1: // getMoreDialledDigitsReq
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				_out = handler.createReply();
				getMoreDialledDigitsReq(_arg0,_arg1);
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
			case 2: // release
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.gccs.TpCallReleaseCause _arg1=org.csapi.cc.gccs.TpCallReleaseCauseHelper.read(_input);
				_out = handler.createReply();
				release(_arg0,_arg1);
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
			case 3: // setCallChargePlan
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallChargePlan _arg1=org.csapi.cc.TpCallChargePlanHelper.read(_input);
				_out = handler.createReply();
				setCallChargePlan(_arg0,_arg1);
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
			case 4: // superviseCallReq
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				superviseCallReq(_arg0,_arg1,_arg2);
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
			case 5: // getCallInfoReq
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				_out = handler.createReply();
				getCallInfoReq(_arg0,_arg1);
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
			case 6: // continueProcessing
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				continueProcessing(_arg0);
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
			case 7: // deassignCall
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				deassignCall(_arg0);
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
			case 8: // routeReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.gccs.TpCallReportRequest[] _arg1=org.csapi.cc.gccs.TpCallReportRequestSetHelper.read(_input);
				org.csapi.TpAddress _arg2=org.csapi.TpAddressHelper.read(_input);
				org.csapi.TpAddress _arg3=org.csapi.TpAddressHelper.read(_input);
				org.csapi.TpAddress _arg4=org.csapi.TpAddressHelper.read(_input);
				org.csapi.TpAddress _arg5=org.csapi.TpAddressHelper.read(_input);
				org.csapi.cc.gccs.TpCallAppInfo[] _arg6=org.csapi.cc.gccs.TpCallAppInfoSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(routeReq(_arg0,_arg1,_arg2,_arg3,_arg4,_arg5,_arg6));
			}
			catch(org.csapi.P_INVALID_EVENT_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_EVENT_TYPEHelper.write(_out, _ex0);
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
			catch(org.csapi.P_INVALID_ADDRESS _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_ADDRESSHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex4);
			}
			catch(org.csapi.P_UNSUPPORTED_ADDRESS_PLAN _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNSUPPORTED_ADDRESS_PLANHelper.write(_out, _ex5);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex6)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex6);
			}
				break;
			}
			case 9: // setCallbackWithSessionID
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
			case 10: // setAdviceOfCharge
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.TpAoCInfo _arg1=org.csapi.TpAoCInfoHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				setAdviceOfCharge(_arg0,_arg1,_arg2);
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
