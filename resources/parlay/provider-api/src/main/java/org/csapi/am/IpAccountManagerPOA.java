package org.csapi.am;

/**
 *	Generated from IDL interface "IpAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAccountManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.am.IpAccountManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallback", new java.lang.Integer(0));
		m_opsHash.put ( "changeNotification", new java.lang.Integer(1));
		m_opsHash.put ( "getNotification", new java.lang.Integer(2));
		m_opsHash.put ( "createNotification", new java.lang.Integer(3));
		m_opsHash.put ( "disableNotifications", new java.lang.Integer(4));
		m_opsHash.put ( "destroyNotification", new java.lang.Integer(5));
		m_opsHash.put ( "queryBalanceReq", new java.lang.Integer(6));
		m_opsHash.put ( "enableNotifications", new java.lang.Integer(7));
		m_opsHash.put ( "retrieveTransactionHistoryReq", new java.lang.Integer(8));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(9));
	}
	private String[] ids = {"IDL:org/csapi/am/IpAccountManager:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.am.IpAccountManager _this()
	{
		return org.csapi.am.IpAccountManagerHelper.narrow(_this_object());
	}
	public org.csapi.am.IpAccountManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.am.IpAccountManagerHelper.narrow(_this_object(orb));
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
			case 1: // changeNotification
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.am.TpChargingEventCriteria _arg1=org.csapi.am.TpChargingEventCriteriaHelper.read(_input);
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
			catch(org.csapi.P_INVALID_ADDRESS _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_ADDRESSHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_UNKNOWN_SUBSCRIBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, _ex4);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex5);
			}
				break;
			}
			case 2: // getNotification
			{
			try
			{
				_out = handler.createReply();
				org.csapi.am.TpChargingEventCriteriaResultSetHelper.write(_out,getNotification());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // createNotification
			{
			try
			{
				org.csapi.am.IpAppAccountManager _arg0=org.csapi.am.IpAppAccountManagerHelper.read(_input);
				org.csapi.am.TpChargingEventCriteria _arg1=org.csapi.am.TpChargingEventCriteriaHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(createNotification(_arg0,_arg1));
			}
			catch(org.csapi.P_INVALID_EVENT_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_EVENT_TYPEHelper.write(_out, _ex0);
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
			catch(org.csapi.P_UNKNOWN_SUBSCRIBER _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex4);
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
			case 5: // destroyNotification
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
			case 6: // queryBalanceReq
			{
			try
			{
				org.csapi.TpAddress[] _arg0=org.csapi.TpAddressSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(queryBalanceReq(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.am.P_UNAUTHORIZED_APPLICATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.am.P_UNAUTHORIZED_APPLICATIONHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_UNKNOWN_SUBSCRIBER _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, _ex2);
			}
				break;
			}
			case 7: // enableNotifications
			{
			try
			{
				org.csapi.am.IpAppAccountManager _arg0=org.csapi.am.IpAppAccountManagerHelper.read(_input);
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
			case 8: // retrieveTransactionHistoryReq
			{
			try
			{
				org.csapi.TpAddress _arg0=org.csapi.TpAddressHelper.read(_input);
				org.csapi.TpTimeInterval _arg1=org.csapi.TpTimeIntervalHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(retrieveTransactionHistoryReq(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_TIME_AND_DATE_FORMAT _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_TIME_AND_DATE_FORMATHelper.write(_out, _ex1);
			}
			catch(org.csapi.am.P_UNAUTHORIZED_APPLICATION _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.am.P_UNAUTHORIZED_APPLICATIONHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_UNKNOWN_SUBSCRIBER _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, _ex3);
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
