package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpAppDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppDataSessionPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.dsc.IpAppDataSessionOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "dataSessionFaultDetected", new java.lang.Integer(0));
		m_opsHash.put ( "superviseDataSessionErr", new java.lang.Integer(1));
		m_opsHash.put ( "superviseDataSessionRes", new java.lang.Integer(2));
		m_opsHash.put ( "connectRes", new java.lang.Integer(3));
		m_opsHash.put ( "connectErr", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:org/csapi/dsc/IpAppDataSession:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.dsc.IpAppDataSession _this()
	{
		return org.csapi.dsc.IpAppDataSessionHelper.narrow(_this_object());
	}
	public org.csapi.dsc.IpAppDataSession _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.dsc.IpAppDataSessionHelper.narrow(_this_object(orb));
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
			case 0: // dataSessionFaultDetected
			{
				int _arg0=_input.read_long();
				org.csapi.dsc.TpDataSessionFault _arg1=org.csapi.dsc.TpDataSessionFaultHelper.read(_input);
				_out = handler.createReply();
				dataSessionFaultDetected(_arg0,_arg1);
				break;
			}
			case 1: // superviseDataSessionErr
			{
				int _arg0=_input.read_long();
				org.csapi.dsc.TpDataSessionError _arg1=org.csapi.dsc.TpDataSessionErrorHelper.read(_input);
				_out = handler.createReply();
				superviseDataSessionErr(_arg0,_arg1);
				break;
			}
			case 2: // superviseDataSessionRes
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.dsc.TpDataSessionSuperviseVolume _arg2=org.csapi.dsc.TpDataSessionSuperviseVolumeHelper.read(_input);
				org.csapi.TpDataSessionQosClass _arg3=org.csapi.TpDataSessionQosClassHelper.read(_input);
				_out = handler.createReply();
				superviseDataSessionRes(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 3: // connectRes
			{
				int _arg0=_input.read_long();
				org.csapi.dsc.TpDataSessionReport _arg1=org.csapi.dsc.TpDataSessionReportHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				connectRes(_arg0,_arg1,_arg2);
				break;
			}
			case 4: // connectErr
			{
				int _arg0=_input.read_long();
				org.csapi.dsc.TpDataSessionError _arg1=org.csapi.dsc.TpDataSessionErrorHelper.read(_input);
				int _arg2=_input.read_long();
				_out = handler.createReply();
				connectErr(_arg0,_arg1,_arg2);
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
