package org.csapi.am;

/**
 *	Generated from IDL interface "IpAppAccountManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppAccountManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.am.IpAppAccountManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "retrieveTransactionHistoryErr", new java.lang.Integer(0));
		m_opsHash.put ( "queryBalanceRes", new java.lang.Integer(1));
		m_opsHash.put ( "retrieveTransactionHistoryRes", new java.lang.Integer(2));
		m_opsHash.put ( "queryBalanceErr", new java.lang.Integer(3));
		m_opsHash.put ( "reportNotification", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/csapi/am/IpAppAccountManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.am.IpAppAccountManager _this()
	{
		return org.csapi.am.IpAppAccountManagerHelper.narrow(_this_object());
	}
	public org.csapi.am.IpAppAccountManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.am.IpAppAccountManagerHelper.narrow(_this_object(orb));
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
			case 0: // retrieveTransactionHistoryErr
			{
				int _arg0=_input.read_long();
				org.csapi.am.TpTransactionHistoryStatus _arg1=org.csapi.am.TpTransactionHistoryStatusHelper.read(_input);
				_out = handler.createReply();
				retrieveTransactionHistoryErr(_arg0,_arg1);
				break;
			}
			case 1: // queryBalanceRes
			{
				int _arg0=_input.read_long();
				org.csapi.am.TpBalance[] _arg1=org.csapi.am.TpBalanceSetHelper.read(_input);
				_out = handler.createReply();
				queryBalanceRes(_arg0,_arg1);
				break;
			}
			case 2: // retrieveTransactionHistoryRes
			{
				int _arg0=_input.read_long();
				org.csapi.am.TpTransactionHistory[] _arg1=org.csapi.am.TpTransactionHistorySetHelper.read(_input);
				_out = handler.createReply();
				retrieveTransactionHistoryRes(_arg0,_arg1);
				break;
			}
			case 3: // queryBalanceErr
			{
				int _arg0=_input.read_long();
				org.csapi.am.TpBalanceQueryError _arg1=org.csapi.am.TpBalanceQueryErrorHelper.read(_input);
				_out = handler.createReply();
				queryBalanceErr(_arg0,_arg1);
				break;
			}
			case 4: // reportNotification
			{
				org.csapi.am.TpChargingEventInfo _arg0=org.csapi.am.TpChargingEventInfoHelper.read(_input);
				int _arg1=_input.read_long();
				_out = handler.createReply();
				reportNotification(_arg0,_arg1);
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
