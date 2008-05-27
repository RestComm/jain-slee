package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpAppDataSessionControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppDataSessionControlManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.dsc.IpAppDataSessionControlManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "dataSessionNotificationInterrupted", new java.lang.Integer(0));
		m_opsHash.put ( "reportNotification", new java.lang.Integer(1));
		m_opsHash.put ( "dataSessionNotificationContinued", new java.lang.Integer(2));
		m_opsHash.put ( "dataSessionAborted", new java.lang.Integer(3));
	}
	private String[] ids = {"IDL:org/csapi/dsc/IpAppDataSessionControlManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.dsc.IpAppDataSessionControlManager _this()
	{
		return org.csapi.dsc.IpAppDataSessionControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.dsc.IpAppDataSessionControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.dsc.IpAppDataSessionControlManagerHelper.narrow(_this_object(orb));
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
			case 0: // dataSessionNotificationInterrupted
			{
				_out = handler.createReply();
				dataSessionNotificationInterrupted();
				break;
			}
			case 1: // reportNotification
			{
				org.csapi.dsc.TpDataSessionIdentifier _arg0=org.csapi.dsc.TpDataSessionIdentifierHelper.read(_input);
				org.csapi.dsc.TpDataSessionEventInfo _arg1=org.csapi.dsc.TpDataSessionEventInfoHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				org.csapi.dsc.IpAppDataSessionHelper.write(_out,reportNotification(_arg0,_arg1,_arg2));
				break;
			}
			case 2: // dataSessionNotificationContinued
			{
				_out = handler.createReply();
				dataSessionNotificationContinued();
				break;
			}
			case 3: // dataSessionAborted
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				dataSessionAborted(_arg0);
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
