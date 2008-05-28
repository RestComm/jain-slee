package org.csapi.fw.fw_access.trust_and_security;


/**
 *	Generated from IDL interface "IpClientAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpClientAccessStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_access.trust_and_security.IpClientAccess
{
	private String[] ids = {"IDL:org/csapi/fw/fw_access/trust_and_security/IpClientAccess:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_access.trust_and_security.IpClientAccessOperations.class;
	public void terminateAccess(java.lang.String terminationText, java.lang.String signingAlgorithm, byte[] digitalSignature) throws org.csapi.fw.P_INVALID_SIGNING_ALGORITHM,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "terminateAccess", true);
				_os.write_string(terminationText);
				_os.write_string(signingAlgorithm);
				org.csapi.TpOctetSetHelper.write(_os,digitalSignature);
				_is = _invoke(_os);
				return;
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SIGNATURE:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SIGNATUREHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "terminateAccess", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpClientAccessOperations _localServant = (IpClientAccessOperations)_so.servant;
			try
			{
			_localServant.terminateAccess(terminationText,signingAlgorithm,digitalSignature);
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
