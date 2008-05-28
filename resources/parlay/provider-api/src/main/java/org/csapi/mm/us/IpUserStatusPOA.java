package org.csapi.mm.us;

/**
 *	Generated from IDL interface "IpUserStatus"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpUserStatusPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.mm.us.IpUserStatusOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(0));
		m_opsHash.put ( "triggeredStatusReportingStartReq", new java.lang.Integer(1));
		m_opsHash.put ( "triggeredStatusReportingStop", new java.lang.Integer(2));
		m_opsHash.put ( "setCallback", new java.lang.Integer(3));
		m_opsHash.put ( "statusReportReq", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/csapi/mm/us/IpUserStatus:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.mm.us.IpUserStatus _this()
	{
		return org.csapi.mm.us.IpUserStatusHelper.narrow(_this_object());
	}
	public org.csapi.mm.us.IpUserStatus _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.us.IpUserStatusHelper.narrow(_this_object(orb));
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
			case 1: // triggeredStatusReportingStartReq
			{
			try
			{
				org.csapi.mm.us.IpAppUserStatus _arg0=org.csapi.mm.us.IpAppUserStatusHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(triggeredStatusReportingStartReq(_arg0,_arg1));
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
			catch(org.csapi.P_APPLICATION_NOT_ACTIVATED _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_UNKNOWN_SUBSCRIBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, _ex4);
			}
				break;
			}
			case 2: // triggeredStatusReportingStop
			{
			try
			{
				org.csapi.mm.TpMobilityStopAssignmentData _arg0=org.csapi.mm.TpMobilityStopAssignmentDataHelper.read(_input);
				_out = handler.createReply();
				triggeredStatusReportingStop(_arg0);
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
			case 3: // setCallback
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
			case 4: // statusReportReq
			{
			try
			{
				org.csapi.mm.us.IpAppUserStatus _arg0=org.csapi.mm.us.IpAppUserStatusHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(statusReportReq(_arg0,_arg1));
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
			catch(org.csapi.P_APPLICATION_NOT_ACTIVATED _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_UNKNOWN_SUBSCRIBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, _ex4);
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
