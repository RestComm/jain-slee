package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpAppCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppCallControlManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.gccs.IpAppCallControlManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "callNotificationInterrupted", new java.lang.Integer(0));
		m_opsHash.put ( "callAborted", new java.lang.Integer(1));
		m_opsHash.put ( "callOverloadCeased", new java.lang.Integer(2));
		m_opsHash.put ( "callNotificationContinued", new java.lang.Integer(3));
		m_opsHash.put ( "callOverloadEncountered", new java.lang.Integer(4));
		m_opsHash.put ( "callEventNotify", new java.lang.Integer(5));
	}
	private String[] ids = {"IDL:org/csapi/cc/gccs/IpAppCallControlManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.cc.gccs.IpAppCallControlManager _this()
	{
		return org.csapi.cc.gccs.IpAppCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.gccs.IpAppCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.gccs.IpAppCallControlManagerHelper.narrow(_this_object(orb));
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
			case 0: // callNotificationInterrupted
			{
				_out = handler.createReply();
				callNotificationInterrupted();
				break;
			}
			case 1: // callAborted
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				callAborted(_arg0);
				break;
			}
			case 2: // callOverloadCeased
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				callOverloadCeased(_arg0);
				break;
			}
			case 3: // callNotificationContinued
			{
				_out = handler.createReply();
				callNotificationContinued();
				break;
			}
			case 4: // callOverloadEncountered
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				callOverloadEncountered(_arg0);
				break;
			}
			case 5: // callEventNotify
			{
				org.csapi.cc.gccs.TpCallIdentifier _arg0=org.csapi.cc.gccs.TpCallIdentifierHelper.read(_input);
				org.csapi.cc.gccs.TpCallEventInfo _arg1=org.csapi.cc.gccs.TpCallEventInfoHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				org.csapi.cc.gccs.IpAppCallHelper.write(_out,callEventNotify(_arg0,_arg1,_arg2));
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
