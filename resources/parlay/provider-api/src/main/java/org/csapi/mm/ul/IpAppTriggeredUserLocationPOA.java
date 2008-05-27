package org.csapi.mm.ul;

/**
 *	Generated from IDL interface "IpAppTriggeredUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppTriggeredUserLocationPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.mm.ul.IpAppTriggeredUserLocationOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "locationReportRes", new java.lang.Integer(0));
		m_opsHash.put ( "extendedLocationReportErr", new java.lang.Integer(1));
		m_opsHash.put ( "triggeredLocationReport", new java.lang.Integer(2));
		m_opsHash.put ( "periodicLocationReport", new java.lang.Integer(3));
		m_opsHash.put ( "extendedLocationReportRes", new java.lang.Integer(4));
		m_opsHash.put ( "locationReportErr", new java.lang.Integer(5));
		m_opsHash.put ( "triggeredLocationReportErr", new java.lang.Integer(6));
		m_opsHash.put ( "periodicLocationReportErr", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/csapi/mm/ul/IpAppTriggeredUserLocation:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/mm/ul/IpAppUserLocation:1.0"};
	public org.csapi.mm.ul.IpAppTriggeredUserLocation _this()
	{
		return org.csapi.mm.ul.IpAppTriggeredUserLocationHelper.narrow(_this_object());
	}
	public org.csapi.mm.ul.IpAppTriggeredUserLocation _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.mm.ul.IpAppTriggeredUserLocationHelper.narrow(_this_object(orb));
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
			case 0: // locationReportRes
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserLocation[] _arg1=org.csapi.mm.TpUserLocationSetHelper.read(_input);
				_out = handler.createReply();
				locationReportRes(_arg0,_arg1);
				break;
			}
			case 1: // extendedLocationReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				extendedLocationReportErr(_arg0,_arg1,_arg2);
				break;
			}
			case 2: // triggeredLocationReport
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserLocationExtended _arg1=org.csapi.mm.TpUserLocationExtendedHelper.read(_input);
				org.csapi.mm.TpLocationTriggerCriteria _arg2=org.csapi.mm.TpLocationTriggerCriteriaHelper.read(_input);
				_out = handler.createReply();
				triggeredLocationReport(_arg0,_arg1,_arg2);
				break;
			}
			case 3: // periodicLocationReport
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserLocationExtended[] _arg1=org.csapi.mm.TpUserLocationExtendedSetHelper.read(_input);
				_out = handler.createReply();
				periodicLocationReport(_arg0,_arg1);
				break;
			}
			case 4: // extendedLocationReportRes
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpUserLocationExtended[] _arg1=org.csapi.mm.TpUserLocationExtendedSetHelper.read(_input);
				_out = handler.createReply();
				extendedLocationReportRes(_arg0,_arg1);
				break;
			}
			case 5: // locationReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				locationReportErr(_arg0,_arg1,_arg2);
				break;
			}
			case 6: // triggeredLocationReportErr
			{
				int _arg0=_input.read_long();
				org.csapi.mm.TpMobilityError _arg1=org.csapi.mm.TpMobilityErrorHelper.read(_input);
				org.csapi.mm.TpMobilityDiagnostic _arg2=org.csapi.mm.TpMobilityDiagnosticHelper.read(_input);
				_out = handler.createReply();
				triggeredLocationReportErr(_arg0,_arg1,_arg2);
				break;
			}
			case 7: // periodicLocationReportErr
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
