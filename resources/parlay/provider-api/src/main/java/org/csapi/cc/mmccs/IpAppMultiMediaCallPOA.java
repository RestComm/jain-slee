package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpAppMultiMediaCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppMultiMediaCallPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cc.mmccs.IpAppMultiMediaCallOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getInfoErr", new java.lang.Integer(0));
		m_opsHash.put ( "callEnded", new java.lang.Integer(1));
		m_opsHash.put ( "superviseRes", new java.lang.Integer(2));
		m_opsHash.put ( "createAndRouteCallLegErr", new java.lang.Integer(3));
		m_opsHash.put ( "superviseVolumeRes", new java.lang.Integer(4));
		m_opsHash.put ( "getInfoRes", new java.lang.Integer(5));
		m_opsHash.put ( "superviseErr", new java.lang.Integer(6));
		m_opsHash.put ( "superviseVolumeErr", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/csapi/cc/mmccs/IpAppMultiMediaCall:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/cc/mpccs/IpAppMultiPartyCall:1.0"};
	public org.csapi.cc.mmccs.IpAppMultiMediaCall _this()
	{
		return org.csapi.cc.mmccs.IpAppMultiMediaCallHelper.narrow(_this_object());
	}
	public org.csapi.cc.mmccs.IpAppMultiMediaCall _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cc.mmccs.IpAppMultiMediaCallHelper.narrow(_this_object(orb));
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
			case 0: // getInfoErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				getInfoErr(_arg0,_arg1);
				break;
			}
			case 1: // callEnded
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallEndedReport _arg1=org.csapi.cc.TpCallEndedReportHelper.read(_input);
				_out = handler.createReply();
				callEnded(_arg0,_arg1);
				break;
			}
			case 2: // superviseRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				superviseRes(_arg0,_arg1,_arg2);
				break;
			}
			case 3: // createAndRouteCallLegErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.mpccs.TpCallLegIdentifier _arg1=org.csapi.cc.mpccs.TpCallLegIdentifierHelper.read(_input);
				org.csapi.cc.TpCallError _arg2=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				createAndRouteCallLegErr(_arg0,_arg1,_arg2);
				break;
			}
			case 4: // superviseVolumeRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.cc.mmccs.TpCallSuperviseVolume _arg2=org.csapi.cc.mmccs.TpCallSuperviseVolumeHelper.read(_input);
				_out = handler.createReply();
				superviseVolumeRes(_arg0,_arg1,_arg2);
				break;
			}
			case 5: // getInfoRes
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallInfoReport _arg1=org.csapi.cc.TpCallInfoReportHelper.read(_input);
				_out = handler.createReply();
				getInfoRes(_arg0,_arg1);
				break;
			}
			case 6: // superviseErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				superviseErr(_arg0,_arg1);
				break;
			}
			case 7: // superviseVolumeErr
			{
				int _arg0=_input.read_long();
				org.csapi.cc.TpCallError _arg1=org.csapi.cc.TpCallErrorHelper.read(_input);
				_out = handler.createReply();
				superviseVolumeErr(_arg0,_arg1);
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
