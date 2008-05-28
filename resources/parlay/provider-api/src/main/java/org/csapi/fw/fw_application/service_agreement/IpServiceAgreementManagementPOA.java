package org.csapi.fw.fw_application.service_agreement;

/**
 *	Generated from IDL interface "IpServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpServiceAgreementManagementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "signServiceAgreement", new java.lang.Integer(0));
		m_opsHash.put ( "initiateSignServiceAgreement", new java.lang.Integer(1));
		m_opsHash.put ( "selectService", new java.lang.Integer(2));
		m_opsHash.put ( "terminateServiceAgreement", new java.lang.Integer(3));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_application/service_agreement/IpServiceAgreementManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement _this()
	{
		return org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagementHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagementHelper.narrow(_this_object(orb));
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
			case 0: // signServiceAgreement
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				java.lang.String _arg2=_input.read_string();
				_out = handler.createReply();
				org.csapi.fw.TpSignatureAndServiceMgrHelper.write(_out,signServiceAgreement(_arg0,_arg1,_arg2));
			}
			catch(org.csapi.fw.P_INVALID_SIGNING_ALGORITHM _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SIGNING_ALGORITHMHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_INVALID_AGREEMENT_TEXT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_AGREEMENT_TEXTHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex3);
			}
			catch(org.csapi.fw.P_INVALID_SERVICE_TOKEN _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.write(_out, _ex4);
			}
			catch(org.csapi.fw.P_SERVICE_ACCESS_DENIED _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_SERVICE_ACCESS_DENIEDHelper.write(_out, _ex5);
			}
				break;
			}
			case 1: // initiateSignServiceAgreement
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				initiateSignServiceAgreement(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_SERVICE_TOKEN _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_SERVICE_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_SERVICE_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
				break;
			}
			case 2: // selectService
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				_out.write_string(selectService(_arg0));
			}
			catch(org.csapi.fw.P_INVALID_SERVICE_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_SERVICE_ACCESS_DENIED _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_SERVICE_ACCESS_DENIEDHelper.write(_out, _ex3);
			}
				break;
			}
			case 3: // terminateServiceAgreement
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				terminateServiceAgreement(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.fw.P_INVALID_SIGNATURE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SIGNATUREHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.fw.P_INVALID_SERVICE_TOKEN _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.write(_out, _ex3);
			}
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
