package org.csapi.mm.ule;


/**
 *	Generated from IDL interface "IpAppUserLocationEmergency"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppUserLocationEmergencyStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.mm.ule.IpAppUserLocationEmergency
{
	private String[] ids = {"IDL:org/csapi/mm/ule/IpAppUserLocationEmergency:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.mm.ule.IpAppUserLocationEmergencyOperations.class;
	public void emergencyLocationReport(int assignmentId, org.csapi.mm.TpUserLocationEmergency location)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "emergencyLocationReport", true);
				_os.write_long(assignmentId);
				org.csapi.mm.TpUserLocationEmergencyHelper.write(_os,location);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "emergencyLocationReport", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationEmergencyOperations _localServant = (IpAppUserLocationEmergencyOperations)_so.servant;
			try
			{
			_localServant.emergencyLocationReport(assignmentId,location);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void emergencyLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "emergencyLocationReportErr", true);
				_os.write_long(assignmentId);
				org.csapi.mm.TpMobilityErrorHelper.write(_os,cause);
				org.csapi.mm.TpMobilityDiagnosticHelper.write(_os,diagnostic);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "emergencyLocationReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationEmergencyOperations _localServant = (IpAppUserLocationEmergencyOperations)_so.servant;
			try
			{
			_localServant.emergencyLocationReportErr(assignmentId,cause,diagnostic);
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
