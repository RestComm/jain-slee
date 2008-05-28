package org.csapi.fw.fw_enterprise_operator.service_subscription;


/**
 *	Generated from IDL interface "IpServiceProfileInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpServiceProfileInfoQueryStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQuery
{
	private String[] ids = {"IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpServiceProfileInfoQuery:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceProfileInfoQueryOperations.class;
	public java.lang.String[] listServiceProfiles() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listServiceProfiles", true);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.fw.TpServiceProfileIDListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listServiceProfiles", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceProfileInfoQueryOperations _localServant = (IpServiceProfileInfoQueryOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listServiceProfiles();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.fw.TpServiceProfileDescription describeServiceProfile(java.lang.String serviceProfileID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "describeServiceProfile", true);
				_os.write_string(serviceProfileID);
				_is = _invoke(_os);
				org.csapi.fw.TpServiceProfileDescription _result = org.csapi.fw.TpServiceProfileDescriptionHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_PROFILE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_PROFILE_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "describeServiceProfile", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceProfileInfoQueryOperations _localServant = (IpServiceProfileInfoQueryOperations)_so.servant;
			org.csapi.fw.TpServiceProfileDescription _result;			try
			{
			_result = _localServant.describeServiceProfile(serviceProfileID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] listAssignedMembers(java.lang.String serviceProfileID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "listAssignedMembers", true);
				_os.write_string(serviceProfileID);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.fw.TpSagIDListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_PROFILE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_PROFILE_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "listAssignedMembers", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceProfileInfoQueryOperations _localServant = (IpServiceProfileInfoQueryOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.listAssignedMembers(serviceProfileID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

}
