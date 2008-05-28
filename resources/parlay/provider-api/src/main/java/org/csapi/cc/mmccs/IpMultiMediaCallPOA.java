package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpMultiMediaCallPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.mmccs.IpMultiMediaCallOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallback", new java.lang.Integer(0));
		m_opsHash.put ( "getCallLegs", new java.lang.Integer(1));
		m_opsHash.put ( "createAndRouteCallLegReq", new java.lang.Integer(2));
		m_opsHash.put ( "getInfoReq", new java.lang.Integer(3));
		m_opsHash.put ( "superviseVolumeReq", new java.lang.Integer(4));
		m_opsHash.put ( "createCallLeg", new java.lang.Integer(5));
		m_opsHash.put ( "deassignCall", new java.lang.Integer(6));
		m_opsHash.put ( "release", new java.lang.Integer(7));
		m_opsHash.put ( "setChargePlan", new java.lang.Integer(8));
		m_opsHash.put ( "setAdviceOfCharge", new java.lang.Integer(9));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(10));
		m_opsHash.put ( "superviseReq", new java.lang.Integer(11));
	}
	private String[] ids = {"IDL:org/csapi/cc/mmccs/IpMultiMediaCall:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/cc/mpccs/IpMultiPartyCall:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.cc.mmccs.IpMultiMediaCall _this()
	{
		return org.csapi.cc.mmccs.IpMultiMediaCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpMultiMediaCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpMultiMediaCallHelper.narrow(_this_object(orb));
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
			case 1: // getCallLegs
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				org.csapi.cc.mpccs.TpCallLegIdentifierSetHelper.write(_out,getCallLegs(_arg0));
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
			case 2: // createAndRouteCallLegReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallEventRequest[] _arg1=org.csapi.cc.TpCallEventRequestSetHelper.read(_input);
				org.csapi.TpAddress _arg2=org.csapi.TpAddressHelper.read(_input);
				org.csapi.TpAddress _arg3=org.csapi.TpAddressHelper.read(_input);
				org.csapi.cc.TpCallAppInfo[] _arg4=org.csapi.cc.TpCallAppInfoSetHelper.read(_input);
				org.csapi.cc.mpccs.IpAppCallLeg _arg5=org.csapi.cc.mpccs.IpAppCallLegHelper.read(_input);
				_out = handler.createReply();
				org.csapi.cc.mpccs.TpCallLegIdentifierHelper.write(_out,createAndRouteCallLegReq(_arg0,_arg1,_arg2,_arg3,_arg4,_arg5));
			}
			catch(org.csapi.P_INVALID_INTERFACE_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_EVENT_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_EVENT_TYPEHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_NETWORK_STATE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_NETWORK_STATEHelper.write(_out, _ex2);
			}
			catch(org.csapi.TpCommonExceptions _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_INVALID_ADDRESS _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_ADDRESSHelper.write(_out, _ex4);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex5);
			}
			catch(org.csapi.P_UNSUPPORTED_ADDRESS_PLAN _ex6)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNSUPPORTED_ADDRESS_PLANHelper.write(_out, _ex6);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex7)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex7);
			}
				break;
			}
			case 3: // getInfoReq
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				_out = handler.createReply();
				getInfoReq(_arg0,_arg1);
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
			case 4: // superviseVolumeReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.mmccs.TpCallSuperviseVolume _arg1=org.csapi.cc.mmccs.TpCallSuperviseVolumeHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				superviseVolumeReq(_arg0,_arg1,_arg2);
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
			case 5: // createCallLeg
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.mpccs.IpAppCallLeg _arg1=org.csapi.cc.mpccs.IpAppCallLegHelper.read(_input);
				_out = handler.createReply();
				org.csapi.cc.mpccs.TpCallLegIdentifierHelper.write(_out,createCallLeg(_arg0,_arg1));
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
			case 6: // deassignCall
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
			case 7: // release
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpReleaseCause _arg1=org.csapi.cc.TpReleaseCauseHelper.read(_input);
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
			case 8: // setChargePlan
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallChargePlan _arg1=org.csapi.cc.TpCallChargePlanHelper.read(_input);
				_out = handler.createReply();
				setChargePlan(_arg0,_arg1);
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
			case 9: // setAdviceOfCharge
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.TpAoCInfo _arg1=org.csapi.TpAoCInfoHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				setAdviceOfCharge(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.P_INVALID_AMOUNT _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_AMOUNTHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_CURRENCY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CURRENCYHelper.write(_out, _ex1);
			}
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex3);
			}
				break;
			}
			case 10: // setCallbackWithSessionID
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
			case 11: // superviseReq
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				superviseReq(_arg0,_arg1,_arg2);
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
