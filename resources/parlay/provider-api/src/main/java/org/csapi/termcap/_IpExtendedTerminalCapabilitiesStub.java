package org.csapi.termcap;


/**
 *	Generated from IDL interface "IpExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpExtendedTerminalCapabilitiesStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.termcap.IpExtendedTerminalCapabilities
{
	private String[] ids = {"IDL:org/csapi/termcap/IpExtendedTerminalCapabilities:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/termcap/IpTerminalCapabilities:1.0","IDL:org/csapi/IpService:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.termcap.IpExtendedTerminalCapabilitiesOperations.class;
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
			IpExtendedTerminalCapabilitiesOperations _localServant = (IpExtendedTerminalCapabilitiesOperations)_so.servant;
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

	public int triggeredTerminalCapabilityStartReq(org.csapi.termcap.IpAppExtendedTerminalCapabilities appTerminalCapabilities, org.csapi.TpAddress[] terminals, org.csapi.termcap.TpTerminalCapabilityScope capabilityScope, int criteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INFORMATION_NOT_AVAILABLE,org.csapi.TpCommonExceptions,org.csapi.termcap.P_INVALID_TERMINAL_ID,org.csapi.P_INVALID_CRITERIA
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "triggeredTerminalCapabilityStartReq", true);
				org.csapi.termcap.IpAppExtendedTerminalCapabilitiesHelper.write(_os,appTerminalCapabilities);
				org.csapi.TpAddressSetHelper.write(_os,terminals);
				org.csapi.termcap.TpTerminalCapabilityScopeHelper.write(_os,capabilityScope);
				_os.write_long(criteria);
				_is = _invoke(_os);
				int _result = _is.read_long();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_INTERFACE_TYPE:1.0"))
				{
					throw org.csapi.P_INVALID_INTERFACE_TYPEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INFORMATION_NOT_AVAILABLE:1.0"))
				{
					throw org.csapi.P_INFORMATION_NOT_AVAILABLEHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/TpCommonExceptions:1.0"))
				{
					throw org.csapi.TpCommonExceptionsHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/termcap/P_INVALID_TERMINAL_ID:1.0"))
				{
					throw org.csapi.termcap.P_INVALID_TERMINAL_IDHelper.read(_ax.getInputStream());
				}
				else if( _id.equals("IDL:org/csapi/P_INVALID_CRITERIA:1.0"))
				{
					throw org.csapi.P_INVALID_CRITERIAHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "triggeredTerminalCapabilityStartReq", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpExtendedTerminalCapabilitiesOperations _localServant = (IpExtendedTerminalCapabilitiesOperations)_so.servant;
			int _result;			try
			{
			_result = _localServant.triggeredTerminalCapabilityStartReq(appTerminalCapabilities,terminals,capabilityScope,criteria);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.csapi.termcap.TpTerminalCapabilities getTerminalCapabilities(java.lang.String terminalIdentity) throws org.csapi.TpCommonExceptions,org.csapi.termcap.P_INVALID_TERMINAL_ID
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getTerminalCapabilities", true);
				_os.write_string(terminalIdentity);
				_is = _invoke(_os);
				org.csapi.termcap.TpTerminalCapabilities _result = org.csapi.termcap.TpTerminalCapabilitiesHelper.read(_is);
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
				else if( _id.equals("IDL:org/csapi/termcap/P_INVALID_TERMINAL_ID:1.0"))
				{
					throw org.csapi.termcap.P_INVALID_TERMINAL_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getTerminalCapabilities", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpExtendedTerminalCapabilitiesOperations _localServant = (IpExtendedTerminalCapabilitiesOperations)_so.servant;
			org.csapi.termcap.TpTerminalCapabilities _result;			try
			{
			_result = _localServant.getTerminalCapabilities(terminalIdentity);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void triggeredTerminalCapabilityStop(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "triggeredTerminalCapabilityStop", true);
				_os.write_long(assignmentID);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/csapi/P_INVALID_ASSIGNMENT_ID:1.0"))
				{
					throw org.csapi.P_INVALID_ASSIGNMENT_IDHelper.read(_ax.getInputStream());
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "triggeredTerminalCapabilityStop", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpExtendedTerminalCapabilitiesOperations _localServant = (IpExtendedTerminalCapabilitiesOperations)_so.servant;
			try
			{
			_localServant.triggeredTerminalCapabilityStop(assignmentID);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

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
			IpExtendedTerminalCapabilitiesOperations _localServant = (IpExtendedTerminalCapabilitiesOperations)_so.servant;
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

}
