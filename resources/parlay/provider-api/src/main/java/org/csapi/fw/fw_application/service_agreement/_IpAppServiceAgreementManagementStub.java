package org.csapi.fw.fw_application.service_agreement;


/**
 *	Generated from IDL interface "IpAppServiceAgreementManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppServiceAgreementManagementStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagement
{
	private String[] ids = {"IDL:org/csapi/fw/fw_application/service_agreement/IpAppServiceAgreementManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagementOperations.class;
	public byte[] signServiceAgreement(java.lang.String serviceToken, java.lang.String agreementText, java.lang.String signingAlgorithm) throws org.csapi.fw.P_INVALID_SIGNING_ALGORITHM,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_AGREEMENT_TEXT,org.csapi.fw.P_INVALID_SERVICE_TOKEN
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "signServiceAgreement", true);
				_os.write_string(serviceToken);
				_os.write_string(agreementText);
				_os.write_string(signingAlgorithm);
				_is = _invoke(_os);
				byte[] _result = org.csapi.TpOctetSetHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SIGNING_ALGORITHM:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SIGNING_ALGORITHMHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_AGREEMENT_TEXT:1.0"))
				{
					throw org.csapi.fw.P_INVALID_AGREEMENT_TEXTHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_TOKEN:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.read(_ax.getInputStream());
				}
				else 
					throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "signServiceAgreement", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppServiceAgreementManagementOperations _localServant = (IpAppServiceAgreementManagementOperations)_so.servant;
			byte[] _result;			try
			{
			_result = _localServant.signServiceAgreement(serviceToken,agreementText,signingAlgorithm);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void terminateServiceAgreement(java.lang.String serviceToken, java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE,org.csapi.fw.P_INVALID_SERVICE_TOKEN
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "terminateServiceAgreement", true);
				_os.write_string(serviceToken);
				_os.write_string(terminationText);
				org.csapi.TpOctetSetHelper.write(_os,digitalSignature);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SIGNATURE:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SIGNATUREHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_TOKEN:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_TOKENHelper.read(_ax.getInputStream());
				}
				else 
					throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "terminateServiceAgreement", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppServiceAgreementManagementOperations _localServant = (IpAppServiceAgreementManagementOperations)_so.servant;
			try
			{
			_localServant.terminateServiceAgreement(serviceToken,terminationText,digitalSignature);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

}
