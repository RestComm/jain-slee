package org.csapi.termcap;

/**
 *	Generated from IDL interface "IpAppExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppExtendedTerminalCapabilitiesPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.termcap.IpAppExtendedTerminalCapabilitiesOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "triggeredTerminalCapabilityReport", new java.lang.Integer(0));
		m_opsHash.put ( "triggeredTerminalCapabilityReportErr", new java.lang.Integer(1));
	}
	private String[] ids = {"IDL:org/csapi/termcap/IpAppExtendedTerminalCapabilities:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.termcap.IpAppExtendedTerminalCapabilities _this()
	{
		return org.csapi.termcap.IpAppExtendedTerminalCapabilitiesHelper.narrow(_this_object());
	}
	public org.csapi.termcap.IpAppExtendedTerminalCapabilities _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.termcap.IpAppExtendedTerminalCapabilitiesHelper.narrow(_this_object(orb));
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
			case 0: // triggeredTerminalCapabilityReport
			{
				int _arg0=_input.read_long();
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				int _arg2=_input.read_long();
				org.csapi.termcap.TpTerminalCapabilities _arg3=org.csapi.termcap.TpTerminalCapabilitiesHelper.read(_input);
				_out = handler.createReply();
				triggeredTerminalCapabilityReport(_arg0,_arg1,_arg2,_arg3);
				break;
			}
			case 1: // triggeredTerminalCapabilityReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.TpAddress[] _arg1=org.csapi.TpAddressSetHelper.read(_input);
				org.csapi.termcap.TpTerminalCapabilitiesError _arg2=org.csapi.termcap.TpTerminalCapabilitiesErrorHelper.read(_input);
				_out = handler.createReply();
				triggeredTerminalCapabilityReportErr(_arg0,_arg1,_arg2);
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
