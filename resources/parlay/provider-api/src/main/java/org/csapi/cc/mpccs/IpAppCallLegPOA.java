package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpAppCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppCallLegPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.mpccs.IpAppCallLegOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "attachMediaRes", new java.lang.Integer(0));
		m_opsHash.put ( "routeErr", new java.lang.Integer(1));
		m_opsHash.put ( "superviseErr", new java.lang.Integer(2));
		m_opsHash.put ( "superviseRes", new java.lang.Integer(3));
		m_opsHash.put ( "eventReportRes", new java.lang.Integer(4));
		m_opsHash.put ( "attachMediaErr", new java.lang.Integer(5));
		m_opsHash.put ( "eventReportErr", new java.lang.Integer(6));
		m_opsHash.put ( "detachMediaRes", new java.lang.Integer(7));
		m_opsHash.put ( "detachMediaErr", new java.lang.Integer(8));
		m_opsHash.put ( "callLegEnded", new java.lang.Integer(9));
		m_opsHash.put ( "getInfoErr", new java.lang.Integer(10));
		m_opsHash.put ( "getInfoRes", new java.lang.Integer(11));
	}
	private String[] ids = {"IDL:org/csapi/cc/mpccs/IpAppCallLeg:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.cc.mpccs.IpAppCallLeg _this()
	{
		return org.csapi.cc.mpccs.IpAppCallLegHelper.narrow(_this_object());
	}
	public org.csapi.cc.mpccs.IpAppCallLeg _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mpccs.IpAppCallLegHelper.narrow(_this_object(orb));
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
			case 0: // attachMediaRes
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				attachMediaRes(_arg0);
				break;
			}
			case 1: // routeErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				routeErr(_arg0,_arg1);
				break;
			}
			case 2: // superviseErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				superviseErr(_arg0,_arg1);
				break;
			}
			case 3: // superviseRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				superviseRes(_arg0,_arg1,_arg2);
				break;
			}
			case 4: // eventReportRes
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallEventInfo _arg1=org.csapi.cc.TpCallEventInfoHelper.read(_input);
				_out = handler.createReply();
				eventReportRes(_arg0,_arg1);
				break;
			}
			case 5: // attachMediaErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				attachMediaErr(_arg0,_arg1);
				break;
			}
			case 6: // eventReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				eventReportErr(_arg0,_arg1);
				break;
			}
			case 7: // detachMediaRes
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				detachMediaRes(_arg0);
				break;
			}
			case 8: // detachMediaErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				detachMediaErr(_arg0,_arg1);
				break;
			}
			case 9: // callLegEnded
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpReleaseCause _arg1=org.csapi.cc.TpReleaseCauseHelper.read(_input);
				_out = handler.createReply();
				callLegEnded(_arg0,_arg1);
				break;
			}
			case 10: // getInfoErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				getInfoErr(_arg0,_arg1);
				break;
			}
			case 11: // getInfoRes
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallLegInfoReport _arg1=org.csapi.cc.TpCallLegInfoReportHelper.read(_input);
				_out = handler.createReply();
				getInfoRes(_arg0,_arg1);
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
