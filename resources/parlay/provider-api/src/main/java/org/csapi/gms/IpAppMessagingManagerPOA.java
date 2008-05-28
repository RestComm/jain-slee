package org.csapi.gms;

/**
 *	Generated from IDL interface "IpAppMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppMessagingManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.gms.IpAppMessagingManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "mailboxTerminated", new java.lang.Integer(0));
		m_opsHash.put ( "messagingEventNotify", new java.lang.Integer(1));
		m_opsHash.put ( "messagingNotificationTerminated", new java.lang.Integer(2));
		m_opsHash.put ( "mailboxFaultDetected", new java.lang.Integer(3));
	}
	private String[] ids = {"IDL:org/csapi/gms/IpAppMessagingManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.gms.IpAppMessagingManager _this()
	{
		return org.csapi.gms.IpAppMessagingManagerHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpAppMessagingManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpAppMessagingManagerHelper.narrow(_this_object(orb));
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
			case 0: // mailboxTerminated
			{
				org.csapi.gms.IpMailbox _arg0=org.csapi.gms.IpMailboxHelper.read(_input);
				int _arg1=_input.read_long();
				_out = handler.createReply();
				mailboxTerminated(_arg0,_arg1);
				break;
			}
			case 1: // messagingEventNotify
			{
				org.csapi.gms.IpMessagingManager _arg0=org.csapi.gms.IpMessagingManagerHelper.read(_input);
				org.csapi.gms.TpMessagingEventInfo _arg1=org.csapi.gms.TpMessagingEventInfoHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				messagingEventNotify(_arg0,_arg1,_arg2);
				break;
			}
			case 2: // messagingNotificationTerminated
			{
				_out = handler.createReply();
				messagingNotificationTerminated();
				break;
			}
			case 3: // mailboxFaultDetected
			{
				org.csapi.gms.IpMailbox _arg0=org.csapi.gms.IpMailboxHelper.read(_input);
				int _arg1=_input.read_long();
				org.csapi.gms.TpMessagingFault _arg2=org.csapi.gms.TpMessagingFaultHelper.read(_input);
				_out = handler.createReply();
				mailboxFaultDetected(_arg0,_arg1,_arg2);
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
