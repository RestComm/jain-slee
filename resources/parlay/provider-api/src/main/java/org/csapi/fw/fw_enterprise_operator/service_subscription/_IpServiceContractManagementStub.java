package org.csapi.fw.fw_enterprise_operator.service_subscription;


/**
 *	Generated from IDL interface "IpServiceContractManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpServiceContractManagementStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractManagement
{
	private String[] ids = {"IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpServiceContractManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_enterprise_operator.service_subscription.IpServiceContractManagementOperations.class;
	public java.lang.String createServiceContract(org.csapi.fw.TpServiceContractDescription serviceContractDescription) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createServiceContract", true);
				org.csapi.fw.TpServiceContractDescriptionHelper.write(_os,serviceContractDescription);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createServiceContract", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceContractManagementOperations _localServant = (IpServiceContractManagementOperations)_so.servant;
			java.lang.String _result;			try
			{
			_result = _localServant.createServiceContract(serviceContractDescription);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void modifyServiceContract(org.csapi.fw.TpServiceContract serviceContract) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "modifyServiceContract", true);
				org.csapi.fw.TpServiceContractHelper.write(_os,serviceContract);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_CONTRACT_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_CONTRACT_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "modifyServiceContract", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceContractManagementOperations _localServant = (IpServiceContractManagementOperations)_so.servant;
			try
			{
			_localServant.modifyServiceContract(serviceContract);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void deleteServiceContract(java.lang.String serviceContractID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "deleteServiceContract", true);
				_os.write_string(serviceContractID);
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
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_SERVICE_CONTRACT_ID:1.0"))
				{
					throw org.csapi.fw.P_INVALID_SERVICE_CONTRACT_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "deleteServiceContract", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpServiceContractManagementOperations _localServant = (IpServiceContractManagementOperations)_so.servant;
			try
			{
			_localServant.deleteServiceContract(serviceContractID);
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
