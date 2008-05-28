package org.csapi.pam.access;


/**
 *	Generated from IDL interface "IpPAMIdentityPresence"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpPAMIdentityPresenceStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.pam.access.IpPAMIdentityPresence
{
	private String[] ids = {"IDL:org/csapi/pam/access/IpPAMIdentityPresence:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.pam.access.IpPAMIdentityPresenceOperations.class;
	public void setIdentityPresence(java.lang.String identity, java.lang.String identityType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setIdentityPresence", true);
				_os.write_string(identity);
				_os.write_string(identityType);
				org.csapi.pam.TpPAMAttributeListHelper.write(_os,attributes);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setIdentityPresence", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMIdentityPresenceOperations _localServant = (IpPAMIdentityPresenceOperations)_so.servant;
			try
			{
			_localServant.setIdentityPresence(identity,identityType,attributes,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.pam.TpPAMAttribute[] getIdentityPresence(java.lang.String identity, java.lang.String identityType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getIdentityPresence", true);
				_os.write_string(identity);
				_os.write_string(identityType);
				org.csapi.TpStringListHelper.write(_os,attributeNames);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				org.csapi.pam.TpPAMAttribute[] _result = org.csapi.pam.TpPAMAttributeListHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getIdentityPresence", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMIdentityPresenceOperations _localServant = (IpPAMIdentityPresenceOperations)_so.servant;
			org.csapi.pam.TpPAMAttribute[] _result;			try
			{
			_result = _localServant.getIdentityPresence(identity,identityType,attributeNames,authToken);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setIdentityPresenceExpiration(java.lang.String identity, java.lang.String identityType, java.lang.String[] attributeNames, long expiresIn, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setIdentityPresenceExpiration", true);
				_os.write_string(identity);
				_os.write_string(identityType);
				org.csapi.TpStringListHelper.write(_os,attributeNames);
				_os.write_longlong(expiresIn);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
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
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_TYPE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_IDENTITY:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_INVALID_CREDENTIAL:1.0"))
				{
					throw org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/pam/P_PAM_UNKNOWN_ATTRIBUTE:1.0"))
				{
					throw org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setIdentityPresenceExpiration", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpPAMIdentityPresenceOperations _localServant = (IpPAMIdentityPresenceOperations)_so.servant;
			try
			{
			_localServant.setIdentityPresenceExpiration(identity,identityType,attributeNames,expiresIn,authToken);
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
