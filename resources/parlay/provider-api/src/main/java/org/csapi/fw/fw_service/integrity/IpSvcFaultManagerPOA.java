package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpSvcFaultManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_service.integrity.IpSvcFaultManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "genFaultStatsRecordReq", new java.lang.Integer(0));
		m_opsHash.put ( "generateFaultStatisticsRecordReq", new java.lang.Integer(1));
		m_opsHash.put ( "genFaultStatsRecordErr", new java.lang.Integer(2));
		m_opsHash.put ( "generateFaultStatisticsRecordRes", new java.lang.Integer(3));
		m_opsHash.put ( "fwUnavailableInd", new java.lang.Integer(4));
		m_opsHash.put ( "svcUnavailableInd", new java.lang.Integer(5));
		m_opsHash.put ( "activityTestErr", new java.lang.Integer(6));
		m_opsHash.put ( "svcActivityTestReq", new java.lang.Integer(7));
		m_opsHash.put ( "fwFaultRecoveryInd", new java.lang.Integer(8));
		m_opsHash.put ( "genFaultStatsRecordRes", new java.lang.Integer(9));
		m_opsHash.put ( "appAvailStatusInd", new java.lang.Integer(10));
		m_opsHash.put ( "generateFaultStatisticsRecordErr", new java.lang.Integer(11));
		m_opsHash.put ( "generateFaultStatsRecordReq", new java.lang.Integer(12));
		m_opsHash.put ( "fwFaultReportInd", new java.lang.Integer(13));
		m_opsHash.put ( "appUnavailableInd", new java.lang.Integer(14));
		m_opsHash.put ( "activityTestRes", new java.lang.Integer(15));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_service/integrity/IpSvcFaultManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_service.integrity.IpSvcFaultManager _this()
	{
		return org.csapi.fw.fw_service.integrity.IpSvcFaultManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpSvcFaultManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpSvcFaultManagerHelper.narrow(_this_object(orb));
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
			case 0: // genFaultStatsRecordReq
			{
			try
			{
				org.csapi.TpTimeInterval _arg0=org.csapi.TpTimeIntervalHelper.read(_input);
				java.lang.String[] _arg1=org.csapi.fw.TpServiceIDListHelper.read(_input);
				_out = handler.createReply();
				genFaultStatsRecordReq(_arg0,_arg1);
			}
			catch(org.csapi.fw.P_INVALID_SERVICE_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_UNAUTHORISED_PARAMETER_VALUE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.write(_out, _ex2);
			}
				break;
			}
			case 1: // generateFaultStatisticsRecordReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.TpTimeInterval _arg1=org.csapi.TpTimeIntervalHelper.read(_input);
				_out = handler.createReply();
				generateFaultStatisticsRecordReq(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // genFaultStatsRecordErr
			{
			try
			{
				org.csapi.fw.TpFaultStatisticsError _arg0=org.csapi.fw.TpFaultStatisticsErrorHelper.read(_input);
				org.csapi.fw.TpSubjectType _arg1=org.csapi.fw.TpSubjectTypeHelper.read(_input);
				_out = handler.createReply();
				genFaultStatsRecordErr(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // generateFaultStatisticsRecordRes
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.fw.TpFaultStatsRecord _arg1=org.csapi.fw.TpFaultStatsRecordHelper.read(_input);
				org.csapi.fw.TpSubjectType _arg2=org.csapi.fw.TpSubjectTypeHelper.read(_input);
				_out = handler.createReply();
				generateFaultStatisticsRecordRes(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // fwUnavailableInd
			{
			try
			{
				org.csapi.fw.TpFwUnavailReason _arg0=org.csapi.fw.TpFwUnavailReasonHelper.read(_input);
				_out = handler.createReply();
				fwUnavailableInd(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // svcUnavailableInd
			{
			try
			{
				_out = handler.createReply();
				svcUnavailableInd();
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // activityTestErr
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				activityTestErr(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_ACTIVITY_TEST_IDHelper.write(_out, _ex1);
			}
				break;
			}
			case 7: // svcActivityTestReq
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				svcActivityTestReq(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // fwFaultRecoveryInd
			{
			try
			{
				org.csapi.fw.TpInterfaceFault _arg0=org.csapi.fw.TpInterfaceFaultHelper.read(_input);
				_out = handler.createReply();
				fwFaultRecoveryInd(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // genFaultStatsRecordRes
			{
			try
			{
				org.csapi.fw.TpFaultStatsRecord _arg0=org.csapi.fw.TpFaultStatsRecordHelper.read(_input);
				org.csapi.fw.TpSubjectType _arg1=org.csapi.fw.TpSubjectTypeHelper.read(_input);
				_out = handler.createReply();
				genFaultStatsRecordRes(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // appAvailStatusInd
			{
			try
			{
				org.csapi.fw.TpAppAvailStatusReason _arg0=org.csapi.fw.TpAppAvailStatusReasonHelper.read(_input);
				_out = handler.createReply();
				appAvailStatusInd(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // generateFaultStatisticsRecordErr
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.fw.TpFaultStatisticsError _arg1=org.csapi.fw.TpFaultStatisticsErrorHelper.read(_input);
				org.csapi.fw.TpSubjectType _arg2=org.csapi.fw.TpSubjectTypeHelper.read(_input);
				_out = handler.createReply();
				generateFaultStatisticsRecordErr(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 12: // generateFaultStatsRecordReq
			{
			try
			{
				org.csapi.TpTimeInterval _arg0=org.csapi.TpTimeIntervalHelper.read(_input);
				_out = handler.createReply();
				generateFaultStatsRecordReq(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // fwFaultReportInd
			{
			try
			{
				org.csapi.fw.TpInterfaceFault _arg0=org.csapi.fw.TpInterfaceFaultHelper.read(_input);
				_out = handler.createReply();
				fwFaultReportInd(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // appUnavailableInd
			{
			try
			{
				_out = handler.createReply();
				appUnavailableInd();
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // activityTestRes
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				activityTestRes(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_ACTIVITY_TEST_IDHelper.write(_out, _ex1);
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
