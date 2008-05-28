package org.csapi.ui;

/**
 *	Generated from IDL interface "IpAppUIManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppUIManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.ui.IpAppUIManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "userInteractionNotificationContinued", new java.lang.Integer(0));
		m_opsHash.put ( "reportNotification", new java.lang.Integer(1));
		m_opsHash.put ( "userInteractionNotificationInterrupted", new java.lang.Integer(2));
		m_opsHash.put ( "reportEventNotification", new java.lang.Integer(3));
		m_opsHash.put ( "userInteractionAborted", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/csapi/ui/IpAppUIManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.ui.IpAppUIManager _this()
	{
		return org.csapi.ui.IpAppUIManagerHelper.narrow(_this_object());
	}
	public org.csapi.ui.IpAppUIManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.ui.IpAppUIManagerHelper.narrow(_this_object(orb));
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
			case 0: // userInteractionNotificationContinued
			{
				_out = handler.createReply();
				userInteractionNotificationContinued();
				break;
			}
			case 1: // reportNotification
			{
				org.csapi.ui.TpUIIdentifier _arg0=org.csapi.ui.TpUIIdentifierHelper.read(_input);
				org.csapi.ui.TpUIEventInfo _arg1=org.csapi.ui.TpUIEventInfoHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				org.csapi.ui.IpAppUIHelper.write(_out,reportNotification(_arg0,_arg1,_arg2));
				break;
			}
			case 2: // userInteractionNotificationInterrupted
			{
				_out = handler.createReply();
				userInteractionNotificationInterrupted();
				break;
			}
			case 3: // reportEventNotification
			{
				org.csapi.ui.TpUIIdentifier _arg0=org.csapi.ui.TpUIIdentifierHelper.read(_input);
				org.csapi.ui.TpUIEventNotificationInfo _arg1=org.csapi.ui.TpUIEventNotificationInfoHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				org.csapi.ui.IpAppUIHelper.write(_out,reportEventNotification(_arg0,_arg1,_arg2));
				break;
			}
			case 4: // userInteractionAborted
			{
				org.csapi.ui.TpUIIdentifier _arg0=org.csapi.ui.TpUIIdentifierHelper.read(_input);
				_out = handler.createReply();
				userInteractionAborted(_arg0);
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
