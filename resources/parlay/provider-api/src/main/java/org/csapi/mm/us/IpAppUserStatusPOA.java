package org.csapi.mm.us;

/**
 *	Generated from IDL interface "IpAppUserStatus"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppUserStatusPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.mm.us.IpAppUserStatusOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "statusReportRes", new java.lang.Integer(0));
		m_opsHash.put ( "triggeredStatusReportErr", new java.lang.Integer(1));
		m_opsHash.put ( "triggeredStatusReport", new java.lang.Integer(2));
		m_opsHash.put ( "statusReportErr", new java.lang.Integer(3));
	}
	private String[] ids = {"IDL:org/csapi/mm/us/IpAppUserStatus:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.mm.us.IpAppUserStatus _this()
	{
		return org.csapi.mm.us.IpAppUserStatusHelper.narrow(_this_object());
	}
	public org.csapi.mm.us.IpAppUserStatus _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.us.IpAppUserStatusHelper.narrow(_this_object(orb));
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
			case 0: // statusReportRes
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserStatus[] _arg1=org.csapi.mm.TpUserStatusSetHelper.read(_input);
				_out = handler.createReply();
				statusReportRes(_arg0,_arg1);
				break;
			}
			case 1: // triggeredStatusReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				triggeredStatusReportErr(_arg0,_arg1,_arg2);
				break;
			}
			case 2: // triggeredStatusReport
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserStatus _arg1=org.csapi.mm.TpUserStatusHelper.read(_input);
				_out = handler.createReply();
				triggeredStatusReport(_arg0,_arg1);
				break;
			}
			case 3: // statusReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				statusReportErr(_arg0,_arg1,_arg2);
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
