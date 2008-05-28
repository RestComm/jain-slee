package org.csapi.pam.event;

/**
 *	Generated from IDL interface "IpAppPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppPAMEventHandlerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.event.IpAppPAMEventHandlerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "eventNotify", new java.lang.Integer(0));
		m_opsHash.put ( "eventNotifyErr", new java.lang.Integer(1));
	}
	private String[] ids = {"IDL:org/csapi/pam/event/IpAppPAMEventHandler:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.event.IpAppPAMEventHandler _this()
	{
		return org.csapi.pam.event.IpAppPAMEventHandlerHelper.narrow(_this_object());
	}
	public org.csapi.pam.event.IpAppPAMEventHandler _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.event.IpAppPAMEventHandlerHelper.narrow(_this_object(orb));
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
			case 0: // eventNotify
			{
				int _arg0=_input.read_long();
				org.csapi.pam.TpPAMNotificationInfo[] _arg1=org.csapi.pam.TpPAMNotificationInfoListHelper.read(_input);
				_out = handler.createReply();
				eventNotify(_arg0,_arg1);
				break;
			}
			case 1: // eventNotifyErr
			{
				int _arg0=_input.read_long();
				org.csapi.pam.TpPAMErrorInfo _arg1=org.csapi.pam.TpPAMErrorInfoHelper.read(_input);
				_out = handler.createReply();
				eventNotifyErr(_arg0,_arg1);
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
