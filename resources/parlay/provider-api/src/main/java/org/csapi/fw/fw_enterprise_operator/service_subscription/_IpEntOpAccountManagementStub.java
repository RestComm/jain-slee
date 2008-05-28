package org.csapi.fw.fw_enterprise_operator.service_subscription;


/**
 *	Generated from IDL interface "IpEntOpAccountManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpEntOpAccountManagementStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountManagement
{
	private String[] ids = {"IDL:org/csapi/fw/fw_enterprise_operator/service_subscription/IpEntOpAccountManagement:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_enterprise_operator.service_subscription.IpEntOpAccountManagementOperations.class;
	public void deleteEntOpAccount() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "deleteEntOpAccount", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "deleteEntOpAccount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEntOpAccountManagementOperations _localServant = (IpEntOpAccountManagementOperations)_so.servant;
			try
			{
			_localServant.deleteEntOpAccount();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void modifyEntOpAccount(org.csapi.fw.TpProperty[] enterpriseOperatorProperties) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "modifyEntOpAccount", true);
				org.csapi.fw.TpPropertyListHelper.write(_os,enterpriseOperatorProperties);
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
				else if( _id.equals("IDL:org/csapi/fw/P_ACCESS_DENIED:1.0"))
				{
					throw org.csapi.fw.P_ACCESS_DENIEDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/fw/P_INVALID_PROPERTY:1.0"))
				{
					throw org.csapi.fw.P_INVALID_PROPERTYHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "modifyEntOpAccount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEntOpAccountManagementOperations _localServant = (IpEntOpAccountManagementOperations)_so.servant;
			try
			{
			_localServant.modifyEntOpAccount(enterpriseOperatorProperties);
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
