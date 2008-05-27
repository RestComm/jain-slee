package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpAppCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppCallPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.gccs.IpAppCallOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "callFaultDetected", new java.lang.Integer(0));
		m_opsHash.put ( "superviseCallErr", new java.lang.Integer(1));
		m_opsHash.put ( "routeRes", new java.lang.Integer(2));
		m_opsHash.put ( "getMoreDialledDigitsErr", new java.lang.Integer(3));
		m_opsHash.put ( "routeErr", new java.lang.Integer(4));
		m_opsHash.put ( "callEnded", new java.lang.Integer(5));
		m_opsHash.put ( "getCallInfoErr", new java.lang.Integer(6));
		m_opsHash.put ( "getCallInfoRes", new java.lang.Integer(7));
		m_opsHash.put ( "getMoreDialledDigitsRes", new java.lang.Integer(8));
		m_opsHash.put ( "superviseCallRes", new java.lang.Integer(9));
	}
	private String[] ids = {"IDL:org/csapi/cc/gccs/IpAppCall:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.cc.gccs.IpAppCall _this()
	{
		return org.csapi.cc.gccs.IpAppCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.gccs.IpAppCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.gccs.IpAppCallHelper.narrow(_this_object(orb));
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
			case 0: // callFaultDetected
			{
				int _arg0=_input.read_long();
				org.csapi.cc.gccs.TpCallFault _arg1=org.csapi.cc.gccs.TpCallFaultHelper.read(_input);
				_out = handler.createReply();
				callFaultDetected(_arg0,_arg1);
				break;
			}
			case 1: // superviseCallErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				superviseCallErr(_arg0,_arg1);
				break;
			}
			case 2: // routeRes
			{
				int _arg0=_input.read_long();
				org.csapi.cc.gccs.TpCallReport _arg1=org.csapi.cc.gccs.TpCallReportHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				routeRes(_arg0,_arg1,_arg2);
				break;
			}
			case 3: // getMoreDialledDigitsErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				getMoreDialledDigitsErr(_arg0,_arg1);
				break;
			}
			case 4: // routeErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				routeErr(_arg0,_arg1,_arg2);
				break;
			}
			case 5: // callEnded
			{
				int _arg0=_input.read_long();
				org.csapi.cc.gccs.TpCallEndedReport _arg1=org.csapi.cc.gccs.TpCallEndedReportHelper.read(_input);
				_out = handler.createReply();
				callEnded(_arg0,_arg1);
				break;
			}
			case 6: // getCallInfoErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				getCallInfoErr(_arg0,_arg1);
				break;
			}
			case 7: // getCallInfoRes
			{
				int _arg0=_input.read_long();
				org.csapi.cc.gccs.TpCallInfoReport _arg1=org.csapi.cc.gccs.TpCallInfoReportHelper.read(_input);
				_out = handler.createReply();
				getCallInfoRes(_arg0,_arg1);
				break;
			}
			case 8: // getMoreDialledDigitsRes
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				getMoreDialledDigitsRes(_arg0,_arg1);
				break;
			}
			case 9: // superviseCallRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				superviseCallRes(_arg0,_arg1,_arg2);
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
