package org.csapi.cs;

/**
 *	Generated from IDL interface "IpAppChargingSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppChargingSessionPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cs.IpAppChargingSessionOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "reserveAmountRes", new java.lang.Integer(0));
		m_opsHash.put ( "debitUnitErr", new java.lang.Integer(1));
		m_opsHash.put ( "creditUnitRes", new java.lang.Integer(2));
		m_opsHash.put ( "debitAmountErr", new java.lang.Integer(3));
		m_opsHash.put ( "reserveUnitErr", new java.lang.Integer(4));
		m_opsHash.put ( "sessionEnded", new java.lang.Integer(5));
		m_opsHash.put ( "extendLifeTimeErr", new java.lang.Integer(6));
		m_opsHash.put ( "directDebitUnitRes", new java.lang.Integer(7));
		m_opsHash.put ( "creditAmountRes", new java.lang.Integer(8));
		m_opsHash.put ( "directDebitUnitErr", new java.lang.Integer(9));
		m_opsHash.put ( "creditAmountErr", new java.lang.Integer(10));
		m_opsHash.put ( "directCreditAmountErr", new java.lang.Integer(11));
		m_opsHash.put ( "directCreditAmountRes", new java.lang.Integer(12));
		m_opsHash.put ( "debitAmountRes", new java.lang.Integer(13));
		m_opsHash.put ( "directCreditUnitErr", new java.lang.Integer(14));
		m_opsHash.put ( "reserveUnitRes", new java.lang.Integer(15));
		m_opsHash.put ( "extendLifeTimeRes", new java.lang.Integer(16));
		m_opsHash.put ( "creditUnitErr", new java.lang.Integer(17));
		m_opsHash.put ( "rateRes", new java.lang.Integer(18));
		m_opsHash.put ( "directCreditUnitRes", new java.lang.Integer(19));
		m_opsHash.put ( "rateErr", new java.lang.Integer(20));
		m_opsHash.put ( "reserveAmountErr", new java.lang.Integer(21));
		m_opsHash.put ( "directDebitAmountErr", new java.lang.Integer(22));
		m_opsHash.put ( "directDebitAmountRes", new java.lang.Integer(23));
		m_opsHash.put ( "debitUnitRes", new java.lang.Integer(24));
	}
	private String[] ids = {"IDL:org/csapi/cs/IpAppChargingSession:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.cs.IpAppChargingSession _this()
	{
		return org.csapi.cs.IpAppChargingSessionHelper.narrow(_this_object());
	}
	public org.csapi.cs.IpAppChargingSession _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cs.IpAppChargingSessionHelper.narrow(_this_object(orb));
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
			case 0: // reserveAmountRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingPrice _arg2=org.csapi.cs.TpChargingPriceHelper.read(_input);
				int _arg3=_input.read_long();
				int _arg4=_input.read_long();
				_out = handler.createReply();
				reserveAmountRes(_arg0,_arg1,_arg2,_arg3,_arg4);
				break;
			}
			case 1: // debitUnitErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				debitUnitErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 2: // creditUnitRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpVolume[] _arg2=org.csapi.cs.TpVolumeSetHelper.read(_input);
				org.csapi.cs.TpVolume[] _arg3=org.csapi.cs.TpVolumeSetHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				creditUnitRes(_arg0,_arg1,_arg2,_arg3,_arg4);
				break;
			}
			case 3: // debitAmountErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				debitAmountErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 4: // reserveUnitErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				reserveUnitErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 5: // sessionEnded
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpSessionEndedCause _arg1=org.csapi.cs.TpSessionEndedCauseHelper.read(_input);
				_out = handler.createReply();
				sessionEnded(_arg0,_arg1);
				break;
			}
			case 6: // extendLifeTimeErr
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpChargingError _arg1=org.csapi.cs.TpChargingErrorHelper.read(_input);
				_out = handler.createReply();
				extendLifeTimeErr(_arg0,_arg1);
				break;
			}
			case 7: // directDebitUnitRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpVolume[] _arg2=org.csapi.cs.TpVolumeSetHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				directDebitUnitRes(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 8: // creditAmountRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingPrice _arg2=org.csapi.cs.TpChargingPriceHelper.read(_input);
				org.csapi.cs.TpChargingPrice _arg3=org.csapi.cs.TpChargingPriceHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				creditAmountRes(_arg0,_arg1,_arg2,_arg3,_arg4);
				break;
			}
			case 9: // directDebitUnitErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				directDebitUnitErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 10: // creditAmountErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				creditAmountErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 11: // directCreditAmountErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				directCreditAmountErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 12: // directCreditAmountRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingPrice _arg2=org.csapi.cs.TpChargingPriceHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				directCreditAmountRes(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 13: // debitAmountRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingPrice _arg2=org.csapi.cs.TpChargingPriceHelper.read(_input);
				org.csapi.cs.TpChargingPrice _arg3=org.csapi.cs.TpChargingPriceHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				debitAmountRes(_arg0,_arg1,_arg2,_arg3,_arg4);
				break;
			}
			case 14: // directCreditUnitErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				directCreditUnitErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 15: // reserveUnitRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpVolume[] _arg2=org.csapi.cs.TpVolumeSetHelper.read(_input);
				int _arg3=_input.read_long();
				int _arg4=_input.read_long();
				_out = handler.createReply();
				reserveUnitRes(_arg0,_arg1,_arg2,_arg3,_arg4);
				break;
			}
			case 16: // extendLifeTimeRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				_out = handler.createReply();
				extendLifeTimeRes(_arg0,_arg1);
				break;
			}
			case 17: // creditUnitErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				creditUnitErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 18: // rateRes
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpPriceVolume[] _arg1=org.csapi.cs.TpPriceVolumeSetHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				rateRes(_arg0,_arg1,_arg2);
				break;
			}
			case 19: // directCreditUnitRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpVolume[] _arg2=org.csapi.cs.TpVolumeSetHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				directCreditUnitRes(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 20: // rateErr
			{
				int _arg0=_input.read_long();
				org.csapi.cs.TpChargingError _arg1=org.csapi.cs.TpChargingErrorHelper.read(_input);
				_out = handler.createReply();
				rateErr(_arg0,_arg1);
				break;
			}
			case 21: // reserveAmountErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				reserveAmountErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 22: // directDebitAmountErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingError _arg2=org.csapi.cs.TpChargingErrorHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				directDebitAmountErr(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 23: // directDebitAmountRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpChargingPrice _arg2=org.csapi.cs.TpChargingPriceHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				directDebitAmountRes(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 24: // debitUnitRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cs.TpVolume[] _arg2=org.csapi.cs.TpVolumeSetHelper.read(_input);
				org.csapi.cs.TpVolume[] _arg3=org.csapi.cs.TpVolumeSetHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				debitUnitRes(_arg0,_arg1,_arg2,_arg3,_arg4);
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
