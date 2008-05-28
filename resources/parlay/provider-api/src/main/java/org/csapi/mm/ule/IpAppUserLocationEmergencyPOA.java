package org.csapi.mm.ule;

/**
 *	Generated from IDL interface "IpAppUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppUserLocationEmergencyPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.mm.ule.IpAppUserLocationEmergencyOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "emergencyLocationReport", new java.lang.Integer(0));
		m_opsHash.put ( "emergencyLocationReportErr", new java.lang.Integer(1));
	}
	private String[] ids = {"IDL:org/csapi/mm/ule/IpAppUserLocationEmergency:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.mm.ule.IpAppUserLocationEmergency _this()
	{
		return org.csapi.mm.ule.IpAppUserLocationEmergencyHelper.narrow(_this_object());
	}
	public org.csapi.mm.ule.IpAppUserLocationEmergency _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ule.IpAppUserLocationEmergencyHelper.narrow(_this_object(orb));
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
			case 0: // emergencyLocationReport
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserLocationEmergency _arg1=org.csapi.mm.TpUserLocationEmergencyHelper.read(_input);
				_out = handler.createReply();
				emergencyLocationReport(_arg0,_arg1);
				break;
			}
			case 1: // emergencyLocationReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				emergencyLocationReportErr(_arg0,_arg1,_arg2);
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
