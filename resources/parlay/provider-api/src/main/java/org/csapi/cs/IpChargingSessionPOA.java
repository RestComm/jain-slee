package org.csapi.cs;

/**
 *	Generated from IDL interface "IpChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpChargingSessionPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cs.IpChargingSessionOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "directCreditUnitReq", new java.lang.Integer(0));
		m_opsHash.put ( "directDebitUnitReq", new java.lang.Integer(1));
		m_opsHash.put ( "setCallback", new java.lang.Integer(2));
		m_opsHash.put ( "getUnitLeft", new java.lang.Integer(3));
		m_opsHash.put ( "extendLifeTimeReq", new java.lang.Integer(4));
		m_opsHash.put ( "creditAmountReq", new java.lang.Integer(5));
		m_opsHash.put ( "debitAmountReq", new java.lang.Integer(6));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(7));
		m_opsHash.put ( "directCreditAmountReq", new java.lang.Integer(8));
		m_opsHash.put ( "rateReq", new java.lang.Integer(9));
		m_opsHash.put ( "directDebitAmountReq", new java.lang.Integer(10));
		m_opsHash.put ( "release", new java.lang.Integer(11));
		m_opsHash.put ( "creditUnitReq", new java.lang.Integer(12));
		m_opsHash.put ( "reserveUnitReq", new java.lang.Integer(13));
		m_opsHash.put ( "debitUnitReq", new java.lang.Integer(14));
		m_opsHash.put ( "getLifeTimeLeft", new java.lang.Integer(15));
		m_opsHash.put ( "getAmountLeft", new java.lang.Integer(16));
		m_opsHash.put ( "reserveAmountReq", new java.lang.Integer(17));
	}
	private String[] ids = {"IDL:org/csapi/cs/IpChargingSession:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.cs.IpChargingSession _this()
	{
		return org.csapi.cs.IpChargingSessionHelper.narrow(_this_object());
	}
	public org.csapi.cs.IpChargingSession _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cs.IpChargingSessionHelper.narrow(_this_object(orb));
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
			case 0: // directCreditUnitReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpChargingParameter[] _arg2=org.csapi.cs.TpChargingParameterSetHelper.read(_input);
				org.csapi.cs.TpVolume[] _arg3=org.csapi.cs.TpVolumeSetHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				directCreditUnitReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex2);
			}
			catch(org.csapi.cs.P_INVALID_VOLUME _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_VOLUMEHelper.write(_out, _ex3);
			}
				break;
			}
			case 1: // directDebitUnitReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpChargingParameter[] _arg2=org.csapi.cs.TpChargingParameterSetHelper.read(_input);
				org.csapi.cs.TpVolume[] _arg3=org.csapi.cs.TpVolumeSetHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				directDebitUnitReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex2);
			}
			catch(org.csapi.cs.P_INVALID_VOLUME _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_VOLUMEHelper.write(_out, _ex3);
			}
				break;
			}
			case 2: // setCallback
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
			case 3: // getUnitLeft
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				org.csapi.cs.TpVolumeSetHelper.write(_out,getUnitLeft(_arg0));
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
			case 4: // extendLifeTimeReq
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				extendLifeTimeReq(_arg0);
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
			case 5: // creditAmountReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpChargingPrice _arg2=org.csapi.cs.TpChargingPriceHelper.read(_input);
				boolean _arg3=_input.read_boolean();
				int _arg4=_input.read_long();
				_out = handler.createReply();
				creditAmountReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex4);
			}
				break;
			}
			case 6: // debitAmountReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpChargingPrice _arg2=org.csapi.cs.TpChargingPriceHelper.read(_input);
				boolean _arg3=_input.read_boolean();
				int _arg4=_input.read_long();
				_out = handler.createReply();
				debitAmountReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex4);
			}
				break;
			}
			case 7: // setCallbackWithSessionID
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
			case 8: // directCreditAmountReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpChargingParameter[] _arg2=org.csapi.cs.TpChargingParameterSetHelper.read(_input);
				org.csapi.cs.TpChargingPrice _arg3=org.csapi.cs.TpChargingPriceHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				directCreditAmountReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex4);
			}
				break;
			}
			case 9: // rateReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpChargingParameter[] _arg1=org.csapi.cs.TpChargingParameterSetHelper.read(_input);
				_out = handler.createReply();
				rateReq(_arg0,_arg1);
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
			case 10: // directDebitAmountReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpChargingParameter[] _arg2=org.csapi.cs.TpChargingParameterSetHelper.read(_input);
				org.csapi.cs.TpChargingPrice _arg3=org.csapi.cs.TpChargingPriceHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				directDebitAmountReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex4);
			}
				break;
			}
			case 11: // release
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				_out = handler.createReply();
				release(_arg0,_arg1);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex2);
			}
				break;
			}
			case 12: // creditUnitReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpVolume[] _arg2=org.csapi.cs.TpVolumeSetHelper.read(_input);
				boolean _arg3=_input.read_boolean();
				int _arg4=_input.read_long();
				_out = handler.createReply();
				creditUnitReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex2);
			}
			catch(org.csapi.cs.P_INVALID_VOLUME _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_VOLUMEHelper.write(_out, _ex3);
			}
				break;
			}
			case 13: // reserveUnitReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpChargingParameter[] _arg2=org.csapi.cs.TpChargingParameterSetHelper.read(_input);
				org.csapi.cs.TpVolume[] _arg3=org.csapi.cs.TpVolumeSetHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				reserveUnitReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex2);
			}
			catch(org.csapi.cs.P_INVALID_VOLUME _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_VOLUMEHelper.write(_out, _ex3);
			}
				break;
			}
			case 14: // debitUnitReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpVolume[] _arg2=org.csapi.cs.TpVolumeSetHelper.read(_input);
				boolean _arg3=_input.read_boolean();
				int _arg4=_input.read_long();
				_out = handler.createReply();
				debitUnitReq(_arg0,_arg1,_arg2,_arg3,_arg4);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex2);
			}
			catch(org.csapi.cs.P_INVALID_VOLUME _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_VOLUMEHelper.write(_out, _ex3);
			}
				break;
			}
			case 15: // getLifeTimeLeft
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				_out.write_long(getLifeTimeLeft(_arg0));
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
			case 16: // getAmountLeft
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				org.csapi.cs.TpChargingPriceHelper.write(_out,getAmountLeft(_arg0));
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
			case 17: // reserveAmountReq
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpApplicationDescription _arg1=org.csapi.cs.TpApplicationDescriptionHelper.read(_input);
				org.csapi.cs.TpChargingParameter[] _arg2=org.csapi.cs.TpChargingParameterSetHelper.read(_input);
				org.csapi.cs.TpChargingPrice _arg3=org.csapi.cs.TpChargingPriceHelper.read(_input);
				org.csapi.cs.TpChargingPrice _arg4=org.csapi.cs.TpChargingPriceHelper.read(_input);
				int _arg5=_input.read_long();
				_out = handler.createReply();
				reserveAmountReq(_arg0,_arg1,_arg2,_arg3,_arg4,_arg5);
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
			catch(org.csapi.cs.P_INVALID_REQUEST_NUMBER _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.cs.P_INVALID_REQUEST_NUMBERHelper.write(_out, _ex4);
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
