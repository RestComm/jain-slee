package org.csapi.ui;

/**
 *	Generated from IDL interface "IpAppUICall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppUICallPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.ui.IpAppUICallOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "abortActionErr", new java.lang.Integer(0));
		m_opsHash.put ( "deleteMessageErr", new java.lang.Integer(1));
		m_opsHash.put ( "sendInfoAndCollectRes", new java.lang.Integer(2));
		m_opsHash.put ( "sendInfoErr", new java.lang.Integer(3));
		m_opsHash.put ( "recordMessageErr", new java.lang.Integer(4));
		m_opsHash.put ( "sendInfoRes", new java.lang.Integer(5));
		m_opsHash.put ( "userInteractionFaultDetected", new java.lang.Integer(6));
		m_opsHash.put ( "abortActionRes", new java.lang.Integer(7));
		m_opsHash.put ( "recordMessageRes", new java.lang.Integer(8));
		m_opsHash.put ( "sendInfoAndCollectErr", new java.lang.Integer(9));
		m_opsHash.put ( "deleteMessageRes", new java.lang.Integer(10));
	}
	private String[] ids = {"IDL:org/csapi/ui/IpAppUICall:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/ui/IpAppUI:1.0"};
	public org.csapi.ui.IpAppUICall _this()
	{
		return org.csapi.ui.IpAppUICallHelper.narrow(_this_object());
	}
	public org.csapi.ui.IpAppUICall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.ui.IpAppUICallHelper.narrow(_this_object(orb));
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
			case 0: // abortActionErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.ui.TpUIError _arg2=org.csapi.ui.TpUIErrorHelper.read(_input);
				_out = handler.createReply();
				abortActionErr(_arg0,_arg1,_arg2);
				break;
			}
			case 1: // deleteMessageErr
			{
				int _arg0=_input.read_long();
				org.csapi.ui.TpUIError _arg1=org.csapi.ui.TpUIErrorHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				deleteMessageErr(_arg0,_arg1,_arg2);
				break;
			}
			case 2: // sendInfoAndCollectRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.ui.TpUIReport _arg2=org.csapi.ui.TpUIReportHelper.read(_input);
				java.lang.String _arg3=_input.read_string();
				_out = handler.createReply();
				sendInfoAndCollectRes(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 3: // sendInfoErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.ui.TpUIError _arg2=org.csapi.ui.TpUIErrorHelper.read(_input);
				_out = handler.createReply();
				sendInfoErr(_arg0,_arg1,_arg2);
				break;
			}
			case 4: // recordMessageErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.ui.TpUIError _arg2=org.csapi.ui.TpUIErrorHelper.read(_input);
				_out = handler.createReply();
				recordMessageErr(_arg0,_arg1,_arg2);
				break;
			}
			case 5: // sendInfoRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.ui.TpUIReport _arg2=org.csapi.ui.TpUIReportHelper.read(_input);
				_out = handler.createReply();
				sendInfoRes(_arg0,_arg1,_arg2);
				break;
			}
			case 6: // userInteractionFaultDetected
			{
				int _arg0=_input.read_long();
				org.csapi.ui.TpUIFault _arg1=org.csapi.ui.TpUIFaultHelper.read(_input);
				_out = handler.createReply();
				userInteractionFaultDetected(_arg0,_arg1);
				break;
			}
			case 7: // abortActionRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				_out = handler.createReply();
				abortActionRes(_arg0,_arg1);
				break;
			}
			case 8: // recordMessageRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.ui.TpUIReport _arg2=org.csapi.ui.TpUIReportHelper.read(_input);
				int _arg3=_input.read_long();
				_out = handler.createReply();
				recordMessageRes(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 9: // sendInfoAndCollectErr
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.ui.TpUIError _arg2=org.csapi.ui.TpUIErrorHelper.read(_input);
				_out = handler.createReply();
				sendInfoAndCollectErr(_arg0,_arg1,_arg2);
				break;
			}
			case 10: // deleteMessageRes
			{
				int _arg0=_input.read_long();
				org.csapi.ui.TpUIReport _arg1=org.csapi.ui.TpUIReportHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				deleteMessageRes(_arg0,_arg1,_arg2);
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
