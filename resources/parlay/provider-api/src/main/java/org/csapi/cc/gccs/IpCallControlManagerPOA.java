package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpCallControlManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.gccs.IpCallControlManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "disableCallNotification", new java.lang.Integer(0));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(1));
		m_opsHash.put ( "setCallLoadControl", new java.lang.Integer(2));
		m_opsHash.put ( "changeCallNotification", new java.lang.Integer(3));
		m_opsHash.put ( "createCall", new java.lang.Integer(4));
		m_opsHash.put ( "getCriteria", new java.lang.Integer(5));
		m_opsHash.put ( "setCallback", new java.lang.Integer(6));
		m_opsHash.put ( "enableCallNotification", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/csapi/cc/gccs/IpCallControlManager:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.cc.gccs.IpCallControlManager _this()
	{
		return org.csapi.cc.gccs.IpCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.gccs.IpCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.gccs.IpCallControlManagerHelper.narrow(_this_object(orb));
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
			case 0: // disableCallNotification
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				disableCallNotification(_arg0);
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
			case 2: // setCallLoadControl
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallLoadControlMechanism _arg1=org.csapi.cc.TpCallLoadControlMechanismHelper.read(_input);
				org.csapi.cc.gccs.TpCallTreatment _arg2=org.csapi.cc.gccs.TpCallTreatmentHelper.read(_input);
				org.csapi.TpAddressRange _arg3=org.csapi.TpAddressRangeHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(setCallLoadControl(_arg0,_arg1,_arg2,_arg3));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_ADDRESS _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_ADDRESSHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_UNSUPPORTED_ADDRESS_PLAN _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNSUPPORTED_ADDRESS_PLANHelper.write(_out, _ex2);
			}
				break;
			}
			case 3: // changeCallNotification
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.gccs.TpCallEventCriteria _arg1=org.csapi.cc.gccs.TpCallEventCriteriaHelper.read(_input);
				_out = handler.createReply();
				changeCallNotification(_arg0,_arg1);
			}
			catch(org.csapi.P_INVALID_ASSIGNMENT_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_ASSIGNMENT_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_EVENT_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_EVENT_TYPEHelper.write(_out, _ex1);
			}
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex3);
			}
				break;
			}
			case 4: // createCall
			{
			try
			{
				org.csapi.cc.gccs.IpAppCall _arg0=org.csapi.cc.gccs.IpAppCallHelper.read(_input);
				_out = handler.createReply();
				org.csapi.cc.gccs.TpCallIdentifierHelper.write(_out,createCall(_arg0));
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
			case 5: // getCriteria
			{
			try
			{
				_out = handler.createReply();
				org.csapi.cc.gccs.TpCallEventCriteriaResultSetHelper.write(_out,getCriteria());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
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
			case 7: // enableCallNotification
			{
			try
			{
				org.csapi.cc.gccs.IpAppCallControlManager _arg0=org.csapi.cc.gccs.IpAppCallControlManagerHelper.read(_input);
				org.csapi.cc.gccs.TpCallEventCriteria _arg1=org.csapi.cc.gccs.TpCallEventCriteriaHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(enableCallNotification(_arg0,_arg1));
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
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex3);
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
