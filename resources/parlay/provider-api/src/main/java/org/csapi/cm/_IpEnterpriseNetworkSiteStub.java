package org.csapi.cm;


/**
 *	Generated from IDL interface "IpEnterpriseNetworkSite"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpEnterpriseNetworkSiteStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cm.IpEnterpriseNetworkSite
{
	private String[] ids = {"IDL:org/csapi/cm/IpEnterpriseNetworkSite:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/cm/IpEnterpriseNetwork:1.0","IDL:org/csapi/IpService:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cm.IpEnterpriseNetworkSiteOperations.class;
	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setCallback", true);
				org.csapi.IpInterfaceHelper.write(_os,appInterface);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setCallback", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			try
			{
			_localServant.setCallback(appInterface);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public org.csapi.cm.TpIPSubnet getSAPIPSubnet(java.lang.String sapID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SAP,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_IPSUBNET
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getSAPIPSubnet", true);
				_os.write_string(sapID);
				_is = _invoke(_os);
				org.csapi.cm.TpIPSubnet _result = org.csapi.cm.TpIPSubnetHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_SAP:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_SAPHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cm/P_ILLEGAL_SITE_ID:1.0"))
				{
					throw org.csapi.cm.P_ILLEGAL_SITE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_IPSUBNET:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getSAPIPSubnet", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			org.csapi.cm.TpIPSubnet _result;			try
			{
			_result = _localServant.getSAPIPSubnet(sapID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String getSiteLocation() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_LOCATION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getSiteLocation", true);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
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
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_SITE_LOCATION:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_SITE_LOCATIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getSiteLocation", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			java.lang.String _result;			try
			{
			_result = _localServant.getSiteLocation();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.IpInterface getVPrN() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRN
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getVPrN", true);
				_is = _invoke(_os);
				org.csapi.IpInterface _result = org.csapi.IpInterfaceHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_VPRN:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_VPRNHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getVPrN", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			org.csapi.IpInterface _result;			try
			{
			_result = _localServant.getVPrN();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.cm.TpIPSubnet getIPSubnet() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_IPSUBNET
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getIPSubnet", true);
				_is = _invoke(_os);
				org.csapi.cm.TpIPSubnet _result = org.csapi.cm.TpIPSubnetHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_IPSUBNET:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getIPSubnet", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			org.csapi.cm.TpIPSubnet _result;			try
			{
			_result = _localServant.getIPSubnet();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] getSiteList() throws org.csapi.cm.P_UNKNOWN_SITES,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getSiteList", true);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.TpStringListHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_SITES:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_SITESHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getSiteList", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.getSiteList();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String getSiteID() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getSiteID", true);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
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
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_SITE_ID:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_SITE_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getSiteID", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			java.lang.String _result;			try
			{
			_result = _localServant.getSiteID();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.IpInterface getSite(java.lang.String siteID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SITE_ID,org.csapi.cm.P_UNKNOWN_SITE_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getSite", true);
				_os.write_string(siteID);
				_is = _invoke(_os);
				org.csapi.IpInterface _result = org.csapi.IpInterfaceHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/cm/P_ILLEGAL_SITE_ID:1.0"))
				{
					throw org.csapi.cm.P_ILLEGAL_SITE_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_SITE_ID:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_SITE_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getSite", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			org.csapi.IpInterface _result;			try
			{
			_result = _localServant.getSite(siteID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String[] getSAPList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SAPS
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getSAPList", true);
				_is = _invoke(_os);
				java.lang.String[] _result = org.csapi.TpStringListHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_SAPS:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_SAPSHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getSAPList", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			java.lang.String[] _result;			try
			{
			_result = _localServant.getSAPList();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String getSiteDescription() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTION
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getSiteDescription", true);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
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
				else if( _id.equals("IDL:org/csapi/cm/P_UNKNOWN_SITE_DESCRIPTION:1.0"))
				{
					throw org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTIONHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getSiteDescription", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			java.lang.String _result;			try
			{
			_result = _localServant.getSiteDescription();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "setCallbackWithSessionID", true);
				org.csapi.IpInterfaceHelper.write(_os,appInterface);
				_os.write_long(sessionID);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_SESSION_ID:1.0"))
				{
					throw org.csapi.P_INVALID_SESSION_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "setCallbackWithSessionID", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpEnterpriseNetworkSiteOperations _localServant = (IpEnterpriseNetworkSiteOperations)_so.servant;
			try
			{
			_localServant.setCallbackWithSessionID(appInterface,sessionID);
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
