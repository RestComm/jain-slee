package org.csapi.termcap;


/**
 *	Generated from IDL interface "IpAppExtendedTerminalCapabilities"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppExtendedTerminalCapabilitiesStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.termcap.IpAppExtendedTerminalCapabilities
{
	private String[] ids = {"IDL:org/csapi/termcap/IpAppExtendedTerminalCapabilities:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.termcap.IpAppExtendedTerminalCapabilitiesOperations.class;
	public void triggeredTerminalCapabilityReport(int assignmentID, org.csapi.TpAddress[] terminals, int criteria, org.csapi.termcap.TpTerminalCapabilities capabilities)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "triggeredTerminalCapabilityReport", true);
				_os.write_long(assignmentID);
				org.csapi.TpAddressSetHelper.write(_os,terminals);
				_os.write_long(criteria);
				org.csapi.termcap.TpTerminalCapabilitiesHelper.write(_os,capabilities);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "triggeredTerminalCapabilityReport", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppExtendedTerminalCapabilitiesOperations _localServant = (IpAppExtendedTerminalCapabilitiesOperations)_so.servant;
			try
			{
			_localServant.triggeredTerminalCapabilityReport(assignmentID,terminals,criteria,capabilities);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void triggeredTerminalCapabilityReportErr(int assignmentId, org.csapi.TpAddress[] terminals, org.csapi.termcap.TpTerminalCapabilitiesError cause)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "triggeredTerminalCapabilityReportErr", true);
				_os.write_long(assignmentId);
				org.csapi.TpAddressSetHelper.write(_os,terminals);
				org.csapi.termcap.TpTerminalCapabilitiesErrorHelper.write(_os,cause);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "triggeredTerminalCapabilityReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppExtendedTerminalCapabilitiesOperations _localServant = (IpAppExtendedTerminalCapabilitiesOperations)_so.servant;
			try
			{
			_localServant.triggeredTerminalCapabilityReportErr(assignmentId,terminals,cause);
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
