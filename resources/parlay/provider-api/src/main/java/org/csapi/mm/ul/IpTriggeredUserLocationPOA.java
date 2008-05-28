package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpTriggeredUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpTriggeredUserLocationPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.mm.ul.IpTriggeredUserLocationOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "triggeredLocationReportingStartReq", new java.lang.Integer(0));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(1));
		m_opsHash.put ( "periodicLocationReportingStartReq", new java.lang.Integer(2));
		m_opsHash.put ( "periodicLocationReportingStop", new java.lang.Integer(3));
		m_opsHash.put ( "triggeredLocationReportingStop", new java.lang.Integer(4));
		m_opsHash.put ( "locationReportReq", new java.lang.Integer(5));
		m_opsHash.put ( "extendedLocationReportReq", new java.lang.Integer(6));
		m_opsHash.put ( "setCallback", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/csapi/mm/ul/IpTriggeredUserLocation:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0","IDL:org/csapi/mm/ul/IpUserLocation:1.0"};
	public org.csapi.mm.ul.IpTriggeredUserLocation _this()
	{
		return org.csapi.mm.ul.IpTriggeredUserLocationHelper.narrow(_this_object());
	}
	public org.csapi.mm.ul.IpTriggeredUserLocation _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ul.IpTriggeredUserLocationHelper.narrow(_this_object(orb));
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
			case 0: // triggeredLocationReportingStartReq
			{
			try
			{
				org.csapi.mm.ul.IpAppTriggeredUserLocation _arg0=org.csapi.mm.ul.IpAppTriggeredUserLocationHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				org.csapi.mm.TpLocationRequest _arg2=org.csapi.mm.TpLocationRequestHelper.read(_input);
				org.csapi.mm.TpLocationTrigger[] _arg3=org.csapi.mm.TpLocationTriggerSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(triggeredLocationReportingStartReq(_arg0,_arg1,_arg2,_arg3));
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
			catch(org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVEREDHelper.write(_out, _ex2);
			}
			catch(org.csapi.TpCommonExceptions _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_APPLICATION_NOT_ACTIVATED _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.write(_out, _ex4);
			}
			catch(org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBED _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_TRIGGER_CONDITIONS_NOT_SUBSCRIBEDHelper.write(_out, _ex5);
			}
			catch(org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED _ex6)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHelper.write(_out, _ex6);
			}
			catch(org.csapi.P_UNKNOWN_SUBSCRIBER _ex7)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNKNOWN_SUBSCRIBERHelper.write(_out, _ex7);
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
			case 2: // periodicLocationReportingStartReq
			{
			try
			{
				org.csapi.mm.ul.IpAppUserLocation _arg0=org.csapi.mm.ul.IpAppUserLocationHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				org.csapi.mm.TpLocationRequest _arg2=org.csapi.mm.TpLocationRequestHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				_out.write_long(periodicLocationReportingStartReq(_arg0,_arg1,_arg2,_arg3));
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
			catch(org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVEREDHelper.write(_out, _ex2);
			}
			catch(org.csapi.TpCommonExceptions _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_APPLICATION_NOT_ACTIVATED _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.write(_out, _ex4);
			}
			catch(org.csapi.mm.P_INVALID_REPORTING_INTERVAL _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_INVALID_REPORTING_INTERVALHelper.write(_out, _ex5);
			}
			catch(org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED _ex6)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHelper.write(_out, _ex6);
			}
				break;
			}
			case 3: // periodicLocationReportingStop
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
			case 4: // triggeredLocationReportingStop
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
			case 5: // locationReportReq
			{
			try
			{
				org.csapi.mm.ul.IpAppUserLocation _arg0=org.csapi.mm.ul.IpAppUserLocationHelper.read(_input);
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
				break;
			}
			case 6: // extendedLocationReportReq
			{
			try
			{
				org.csapi.mm.ul.IpAppUserLocation _arg0=org.csapi.mm.ul.IpAppUserLocationHelper.read(_input);
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				org.csapi.mm.TpLocationRequest _arg2=org.csapi.mm.TpLocationRequestHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(extendedLocationReportReq(_arg0,_arg1,_arg2));
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
			catch(org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVERED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_REQUESTED_RESPONSE_TIME_CANNOT_BE_DELIVEREDHelper.write(_out, _ex2);
			}
			catch(org.csapi.TpCommonExceptions _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex3);
			}
			catch(org.csapi.P_APPLICATION_NOT_ACTIVATED _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_APPLICATION_NOT_ACTIVATEDHelper.write(_out, _ex4);
			}
			catch(org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVERED _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.mm.P_REQUESTED_ACCURACY_CANNOT_BE_DELIVEREDHelper.write(_out, _ex5);
			}
				break;
			}
			case 7: // setCallback
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
