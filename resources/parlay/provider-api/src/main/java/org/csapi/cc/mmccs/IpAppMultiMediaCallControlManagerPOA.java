package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpAppMultiMediaCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppMultiMediaCallControlManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.mmccs.IpAppMultiMediaCallControlManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "callAborted", new java.lang.Integer(0));
		m_opsHash.put ( "callOverloadCeased", new java.lang.Integer(1));
		m_opsHash.put ( "managerInterrupted", new java.lang.Integer(2));
		m_opsHash.put ( "managerResumed", new java.lang.Integer(3));
		m_opsHash.put ( "reportMediaNotification", new java.lang.Integer(4));
		m_opsHash.put ( "callOverloadEncountered", new java.lang.Integer(5));
		m_opsHash.put ( "reportNotification", new java.lang.Integer(6));
	}
	private String[] ids = {"IDL:org/csapi/cc/mmccs/IpAppMultiMediaCallControlManager:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/cc/mpccs/IpAppMultiPartyCallControlManager:1.0"};
	public org.csapi.cc.mmccs.IpAppMultiMediaCallControlManager _this()
	{
		return org.csapi.cc.mmccs.IpAppMultiMediaCallControlManagerHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpAppMultiMediaCallControlManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpAppMultiMediaCallControlManagerHelper.narrow(_this_object(orb));
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
			case 0: // callAborted
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				callAborted(_arg0);
				break;
			}
			case 1: // callOverloadCeased
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				callOverloadCeased(_arg0);
				break;
			}
			case 2: // managerInterrupted
			{
				_out = handler.createReply();
				managerInterrupted();
				break;
			}
			case 3: // managerResumed
			{
				_out = handler.createReply();
				managerResumed();
				break;
			}
			case 4: // reportMediaNotification
			{
				org.csapi.cc.mmccs.TpMultiMediaCallIdentifier _arg0=org.csapi.cc.mmccs.TpMultiMediaCallIdentifierHelper.read(_input);
				org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifier[] _arg1=org.csapi.cc.mmccs.TpMultiMediaCallLegIdentifierSetHelper.read(_input);
				org.csapi.cc.mmccs.TpMediaStream[] _arg2=org.csapi.cc.mmccs.TpMediaStreamSetHelper.read(_input);
				org.csapi.cc.mmccs.TpMediaStreamEventType _arg3=org.csapi.cc.mmccs.TpMediaStreamEventTypeHelper.read(_input);
				int _arg4=_input.read_long();
				_out = handler.createReply();
				org.csapi.cc.mmccs.TpAppMultiMediaCallBackHelper.write(_out,reportMediaNotification(_arg0,_arg1,_arg2,_arg3,_arg4));
				break;
			}
			case 5: // callOverloadEncountered
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				callOverloadEncountered(_arg0);
				break;
			}
			case 6: // reportNotification
			{
				org.csapi.cc.mpccs.TpMultiPartyCallIdentifier _arg0=org.csapi.cc.mpccs.TpMultiPartyCallIdentifierHelper.read(_input);
				org.csapi.cc.mpccs.TpCallLegIdentifier[] _arg1=org.csapi.cc.mpccs.TpCallLegIdentifierSetHelper.read(_input);
				org.csapi.cc.TpCallNotificationInfo _arg2=org.csapi.cc.TpCallNotificationInfoHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				org.csapi.cc.mpccs.TpAppMultiPartyCallBackHelper.write(_out,reportNotification(_arg0,_arg1,_arg2,_arg3));
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
