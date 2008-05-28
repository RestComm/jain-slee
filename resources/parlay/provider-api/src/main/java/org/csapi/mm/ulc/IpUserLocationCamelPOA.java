package org.csapi.mm.ulc;

/**
 *	Generated from IDL interface "IpUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpUserLocationCamelPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.mm.ulc.IpUserLocationCamelOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(0));
		m_opsHash.put ( "triggeredLocationReportingStop", new java.lang.Integer(1));
		m_opsHash.put ( "periodicLocationReportingStop", new java.lang.Integer(2));
		m_opsHash.put ( "triggeredLocationReportingStartReq", new java.lang.Integer(3));
		m_opsHash.put ( "periodicLocationReportingStartReq", new java.lang.Integer(4));
		m_opsHash.put ( "locationReportReq", new java.lang.Integer(5));
		m_opsHash.put ( "setCallback", new java.lang.Integer(6));
	}
	private String[] ids = {"IDL:org/csapi/mm/ulc/IpUserLocationCamel:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.mm.ulc.IpUserLocationCamel _this()
	{
		return org.csapi.mm.ulc.IpUserLocationCamelHelper.narrow(_this_object());
	}
	public org.csapi.mm.ulc.IpUserLocationCamel _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ulc.IpUserLocationCamelHelper.narrow(_this_object(orb));
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
			case 1: // triggeredLocationReportingStop
			{
			try
			{
				org.csapi.mm.TpMobilityStopAssignmentData _arg0=org.csapi.mm.TpMobilityStopAssignmentDataHelper.read(_input);
				_out = handler.createReply();
				triggeredLocationReportingStop(_arg0);
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
			case 2: // periodicLocationReportingStop
			{
			try
			{
				org.csapi.mm.TpMobilityStopAssignmentData _arg0=org.csapi.mm.TpMobilityStopAssignmentDataHelper.read(_input);
				_out = handler.createReply();
				periodicLocationReportingStop(_arg0);
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
			case 3: // triggeredLocationReportingStartReq
			{
			try
			{
				org.csapi.mm.ulc.IpAppUserLocationCamel _arg0=org.csapi.mm.ulc.IpAppUserLocationCamelHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				org.csapi.mm.TpLocationTriggerCamel _arg2=org.csapi.mm.TpLocationTriggerCamelHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(triggeredLocationReportingStartReq(_arg0,_arg1,_arg2));
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
			case 4: // periodicLocationReportingStartReq
			{
			try
			{
				org.csapi.mm.ulc.IpAppUserLocationCamel _arg0=org.csapi.mm.ulc.IpAppUserLocationCamelHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				_out.write_long(periodicLocationReportingStartReq(_arg0,_arg1,_arg2));
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
			catch(org.csapi.mm.P_INVALID_REPORTING_INTERVAL _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_INVALID_REPORTING_INTERVALHelper.write(_out, _ex4);
			}
			catch(org.csapi.P_UNKNOWN_SUBSCRIBER _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, _ex5);
			}
				break;
			}
			case 5: // locationReportReq
			{
			try
			{
				org.csapi.mm.ulc.IpAppUserLocationCamel _arg0=org.csapi.mm.ulc.IpAppUserLocationCamelHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(locationReportReq(_arg0,_arg1));
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
