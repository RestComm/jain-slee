package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppLoadManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_application.integrity.IpAppLoadManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "loadLevelNotification", new java.lang.Integer(0));
		m_opsHash.put ( "queryLoadStatsRes", new java.lang.Integer(1));
		m_opsHash.put ( "queryAppLoadStatsReq", new java.lang.Integer(2));
		m_opsHash.put ( "createLoadLevelNotification", new java.lang.Integer(3));
		m_opsHash.put ( "queryLoadErr", new java.lang.Integer(4));
		m_opsHash.put ( "queryLoadStatsErr", new java.lang.Integer(5));
		m_opsHash.put ( "resumeNotification", new java.lang.Integer(6));
		m_opsHash.put ( "suspendNotification", new java.lang.Integer(7));
		m_opsHash.put ( "queryAppLoadReq", new java.lang.Integer(8));
		m_opsHash.put ( "queryLoadRes", new java.lang.Integer(9));
		m_opsHash.put ( "destroyLoadLevelNotification", new java.lang.Integer(10));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpAppLoadManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_application.integrity.IpAppLoadManager _this()
	{
		return org.csapi.fw.fw_application.integrity.IpAppLoadManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpAppLoadManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpAppLoadManagerHelper.narrow(_this_object(orb));
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
			case 0: // loadLevelNotification
			{
				org.csapi.fw.TpLoadStatistic[] _arg0=org.csapi.fw.TpLoadStatisticListHelper.read(_input);
				_out = handler.createReply();
				loadLevelNotification(_arg0);
				break;
			}
			case 1: // queryLoadStatsRes
			{
				int _arg0=_input.read_long();
				org.csapi.fw.TpLoadStatistic[] _arg1=org.csapi.fw.TpLoadStatisticListHelper.read(_input);
				_out = handler.createReply();
				queryLoadStatsRes(_arg0,_arg1);
				break;
			}
			case 2: // queryAppLoadStatsReq
			{
				int _arg0=_input.read_long();
				org.csapi.TpTimeInterval _arg1=org.csapi.TpTimeIntervalHelper.read(_input);
				_out = handler.createReply();
				queryAppLoadStatsReq(_arg0,_arg1);
				break;
			}
			case 3: // createLoadLevelNotification
			{
				_out = handler.createReply();
				createLoadLevelNotification();
				break;
			}
			case 4: // queryLoadErr
			{
				org.csapi.fw.TpLoadStatisticError _arg0=org.csapi.fw.TpLoadStatisticErrorHelper.read(_input);
				_out = handler.createReply();
				queryLoadErr(_arg0);
				break;
			}
			case 5: // queryLoadStatsErr
			{
				int _arg0=_input.read_long();
				org.csapi.fw.TpLoadStatisticError _arg1=org.csapi.fw.TpLoadStatisticErrorHelper.read(_input);
				_out = handler.createReply();
				queryLoadStatsErr(_arg0,_arg1);
				break;
			}
			case 6: // resumeNotification
			{
				_out = handler.createReply();
				resumeNotification();
				break;
			}
			case 7: // suspendNotification
			{
				_out = handler.createReply();
				suspendNotification();
				break;
			}
			case 8: // queryAppLoadReq
			{
				org.csapi.TpTimeInterval _arg0=org.csapi.TpTimeIntervalHelper.read(_input);
				_out = handler.createReply();
				queryAppLoadReq(_arg0);
				break;
			}
			case 9: // queryLoadRes
			{
				org.csapi.fw.TpLoadStatistic[] _arg0=org.csapi.fw.TpLoadStatisticListHelper.read(_input);
				_out = handler.createReply();
				queryLoadRes(_arg0);
				break;
			}
			case 10: // destroyLoadLevelNotification
			{
				_out = handler.createReply();
				destroyLoadLevelNotification();
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
