package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpLoadManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_application.integrity.IpLoadManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "queryLoadStatsReq", new java.lang.Integer(0));
		m_opsHash.put ( "queryAppLoadErr", new java.lang.Integer(1));
		m_opsHash.put ( "queryAppLoadStatsErr", new java.lang.Integer(2));
		m_opsHash.put ( "queryAppLoadRes", new java.lang.Integer(3));
		m_opsHash.put ( "resumeNotification", new java.lang.Integer(4));
		m_opsHash.put ( "reportLoad", new java.lang.Integer(5));
		m_opsHash.put ( "suspendNotification", new java.lang.Integer(6));
		m_opsHash.put ( "createLoadLevelNotification", new java.lang.Integer(7));
		m_opsHash.put ( "queryLoadReq", new java.lang.Integer(8));
		m_opsHash.put ( "destroyLoadLevelNotification", new java.lang.Integer(9));
		m_opsHash.put ( "queryAppLoadStatsRes", new java.lang.Integer(10));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpLoadManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_application.integrity.IpLoadManager _this()
	{
		return org.csapi.fw.fw_application.integrity.IpLoadManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpLoadManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpLoadManagerHelper.narrow(_this_object(orb));
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
			case 0: // queryLoadStatsReq
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String[] _arg1=org.csapi.fw.TpServiceIDListHelper.read(_input);
				org.csapi.TpTimeInterval _arg2=org.csapi.TpTimeIntervalHelper.read(_input);
				_out = handler.createReply();
				queryLoadStatsReq(_arg0,_arg1,_arg2);
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
			catch(org.csapi.fw.P_SERVICE_NOT_ENABLED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_UNAUTHORISED_PARAMETER_VALUE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.write(_out, _ex3);
			}
				break;
			}
			case 1: // queryAppLoadErr
			{
			try
			{
				org.csapi.fw.TpLoadStatisticError _arg0=org.csapi.fw.TpLoadStatisticErrorHelper.read(_input);
				_out = handler.createReply();
				queryAppLoadErr(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // queryAppLoadStatsErr
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.fw.TpLoadStatisticError _arg1=org.csapi.fw.TpLoadStatisticErrorHelper.read(_input);
				_out = handler.createReply();
				queryAppLoadStatsErr(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // queryAppLoadRes
			{
			try
			{
				org.csapi.fw.TpLoadStatistic[] _arg0=org.csapi.fw.TpLoadStatisticListHelper.read(_input);
				_out = handler.createReply();
				queryAppLoadRes(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // resumeNotification
			{
			try
			{
				java.lang.String[] _arg0=org.csapi.fw.TpServiceIDListHelper.read(_input);
				_out = handler.createReply();
				resumeNotification(_arg0);
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
			catch(org.csapi.fw.P_SERVICE_NOT_ENABLED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_UNAUTHORISED_PARAMETER_VALUE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.write(_out, _ex3);
			}
				break;
			}
			case 5: // reportLoad
			{
			try
			{
				org.csapi.fw.TpLoadLevel _arg0=org.csapi.fw.TpLoadLevelHelper.read(_input);
				_out = handler.createReply();
				reportLoad(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // suspendNotification
			{
			try
			{
				java.lang.String[] _arg0=org.csapi.fw.TpServiceIDListHelper.read(_input);
				_out = handler.createReply();
				suspendNotification(_arg0);
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
			catch(org.csapi.fw.P_SERVICE_NOT_ENABLED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_UNAUTHORISED_PARAMETER_VALUE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.write(_out, _ex3);
			}
				break;
			}
			case 7: // createLoadLevelNotification
			{
			try
			{
				java.lang.String[] _arg0=org.csapi.fw.TpServiceIDListHelper.read(_input);
				_out = handler.createReply();
				createLoadLevelNotification(_arg0);
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
			case 8: // queryLoadReq
			{
			try
			{
				java.lang.String[] _arg0=org.csapi.fw.TpServiceIDListHelper.read(_input);
				org.csapi.TpTimeInterval _arg1=org.csapi.TpTimeIntervalHelper.read(_input);
				_out = handler.createReply();
				queryLoadReq(_arg0,_arg1);
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
			catch(org.csapi.fw.P_SERVICE_NOT_ENABLED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_SERVICE_NOT_ENABLEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_UNAUTHORISED_PARAMETER_VALUE _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_UNAUTHORISED_PARAMETER_VALUEHelper.write(_out, _ex3);
			}
				break;
			}
			case 9: // destroyLoadLevelNotification
			{
			try
			{
				java.lang.String[] _arg0=org.csapi.fw.TpServiceIDListHelper.read(_input);
				_out = handler.createReply();
				destroyLoadLevelNotification(_arg0);
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
			case 10: // queryAppLoadStatsRes
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.fw.TpLoadStatistic[] _arg1=org.csapi.fw.TpLoadStatisticListHelper.read(_input);
				_out = handler.createReply();
				queryAppLoadStatsRes(_arg0,_arg1);
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
