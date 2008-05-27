package org.csapi.mm.ulc;

/**
 *	Generated from IDL interface "IpAppUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppUserLocationCamelPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.mm.ulc.IpAppUserLocationCamelOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "periodicLocationReport", new java.lang.Integer(0));
		m_opsHash.put ( "locationReportRes", new java.lang.Integer(1));
		m_opsHash.put ( "locationReportErr", new java.lang.Integer(2));
		m_opsHash.put ( "triggeredLocationReportErr", new java.lang.Integer(3));
		m_opsHash.put ( "triggeredLocationReport", new java.lang.Integer(4));
		m_opsHash.put ( "periodicLocationReportErr", new java.lang.Integer(5));
	}
	private String[] ids = {"IDL:org/csapi/mm/ulc/IpAppUserLocationCamel:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.mm.ulc.IpAppUserLocationCamel _this()
	{
		return org.csapi.mm.ulc.IpAppUserLocationCamelHelper.narrow(_this_object());
	}
	public org.csapi.mm.ulc.IpAppUserLocationCamel _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ulc.IpAppUserLocationCamelHelper.narrow(_this_object(orb));
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
			case 0: // periodicLocationReport
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserLocationCamel[] _arg1=org.csapi.mm.TpUserLocationCamelSetHelper.read(_input);
				_out = handler.createReply();
				periodicLocationReport(_arg0,_arg1);
				break;
			}
			case 1: // locationReportRes
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserLocationCamel[] _arg1=org.csapi.mm.TpUserLocationCamelSetHelper.read(_input);
				_out = handler.createReply();
				locationReportRes(_arg0,_arg1);
				break;
			}
			case 2: // locationReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				locationReportErr(_arg0,_arg1,_arg2);
				break;
			}
			case 3: // triggeredLocationReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				triggeredLocationReportErr(_arg0,_arg1,_arg2);
				break;
			}
			case 4: // triggeredLocationReport
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserLocationCamel _arg1=org.csapi.mm.TpUserLocationCamelHelper.read(_input);
				org.csapi.mm.TpLocationTriggerCamel _arg2=org.csapi.mm.TpLocationTriggerCamelHelper.read(_input);
				_out = handler.createReply();
				triggeredLocationReport(_arg0,_arg1,_arg2);
				break;
			}
			case 5: // periodicLocationReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				periodicLocationReportErr(_arg0,_arg1,_arg2);
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
