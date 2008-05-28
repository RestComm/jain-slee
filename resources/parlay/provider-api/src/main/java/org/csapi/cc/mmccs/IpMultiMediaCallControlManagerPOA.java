package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpMultiMediaCallControlManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.mmccs.IpMultiMediaCallControlManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallback", new java.lang.Integer(0));
		m_opsHash.put ( "destroyMediaNotification", new java.lang.Integer(1));
		m_opsHash.put ( "createMediaNotification", new java.lang.Integer(2));
		m_opsHash.put ( "getNotification", new java.lang.Integer(3));
		m_opsHash.put ( "disableNotifications", new java.lang.Integer(4));
		m_opsHash.put ( "createCall", new java.lang.Integer(5));
		m_opsHash.put ( "setCallLoadControl", new java.lang.Integer(6));
		m_opsHash.put ( "createNotification", new java.lang.Integer(7));
		m_opsHash.put ( "destroyNotification", new java.lang.Integer(8));
		m_opsHash.put ( "enableNotifications", new java.lang.Integer(9));
		m_opsHash.put ( "changeMediaNotification", new java.lang.Integer(10));
		m_opsHash.put ( "changeNotification", new java.lang.Integer(11));
		m_opsHash.put ( "getMediaNotification", new java.lang.Integer(12));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(13));
		m_opsHash.put ( "getNextNotification", new java.lang.Integer(14));
	}
	private String[] ids = {"IDL:org/csapi/cc/mmccs/IpMultiMediaCallControlManager:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0","IDL:org/csapi/cc/mpccs/IpMultiPartyCallControlManager:1.0"};
	public org.csapi.cc.mmccs.IpMultiMediaCallControlManager _this()
	{
		return org.csapi.cc.mmccs.IpMultiMediaCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpMultiMediaCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpMultiMediaCallControlManagerHelper.narrow(_this_object(orb));
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
			case 1: // destroyMediaNotification
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				destroyMediaNotification(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // createMediaNotification
			{
			try
			{
				org.csapi.cc.mmccs.IpAppMultiMediaCallControlManager _arg0=org.csapi.cc.mmccs.IpAppMultiMediaCallControlManagerHelper.read(_input);
				org.csapi.cc.mmccs.TpNotificationMediaRequest _arg1=org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(createMediaNotification(_arg0,_arg1));
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
			case 3: // getNotification
			{
			try
			{
				_out = handler.createReply();
				org.csapi.cc.TpNotificationRequestedSetHelper.write(_out,getNotification());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // disableNotifications
			{
			try
			{
				_out = handler.createReply();
				disableNotifications();
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // createCall
			{
			try
			{
				org.csapi.cc.mpccs.IpAppMultiPartyCall _arg0=org.csapi.cc.mpccs.IpAppMultiPartyCallHelper.read(_input);
				_out = handler.createReply();
				org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.write(_out,createCall(_arg0));
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
			case 6: // setCallLoadControl
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallLoadControlMechanism _arg1=org.csapi.cc.TpCallLoadControlMechanismHelper.read(_input);
				org.csapi.cc.TpCallTreatment _arg2=org.csapi.cc.TpCallTreatmentHelper.read(_input);
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
			case 7: // createNotification
			{
			try
			{
				org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager _arg0=org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerHelper.read(_input);
				org.csapi.cc.TpCallNotificationRequest _arg1=org.csapi.cc.TpCallNotificationRequestHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(createNotification(_arg0,_arg1));
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
			case 8: // destroyNotification
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				destroyNotification(_arg0);
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
			case 9: // enableNotifications
			{
			try
			{
				org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager _arg0=org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(enableNotifications(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // changeMediaNotification
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.mmccs.TpNotificationMediaRequest _arg1=org.csapi.cc.mmccs.TpNotificationMediaRequestHelper.read(_input);
				_out = handler.createReply();
				changeMediaNotification(_arg0,_arg1);
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
			case 11: // changeNotification
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallNotificationRequest _arg1=org.csapi.cc.TpCallNotificationRequestHelper.read(_input);
				_out = handler.createReply();
				changeNotification(_arg0,_arg1);
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
			case 12: // getMediaNotification
			{
			try
			{
				_out = handler.createReply();
				org.csapi.cc.mmccs.TpMediaNotificationRequestedSetHelper.write(_out,getMediaNotification());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // setCallbackWithSessionID
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
			case 14: // getNextNotification
			{
			try
			{
				boolean _arg0=_input.read_boolean();
				_out = handler.createReply();
				org.csapi.cc.TpNotificationRequestedSetEntryHelper.write(_out,getNextNotification(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
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
