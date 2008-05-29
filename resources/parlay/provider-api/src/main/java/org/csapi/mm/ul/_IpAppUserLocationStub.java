package org.csapi.mm.ul;


/**
 *	Generated from IDL interface "IpAppUserLocation"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppUserLocationStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.mm.ul.IpAppUserLocation
{
	private String[] ids = {"IDL:org/csapi/mm/ul/IpAppUserLocation:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.mm.ul.IpAppUserLocationOperations.class;
	public void locationReportRes(int assignmentId, org.csapi.mm.TpUserLocation[] locations)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "locationReportRes", true);
				_os.write_long(assignmentId);
				org.csapi.mm.TpUserLocationSetHelper.write(_os,locations);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "locationReportRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationOperations _localServant = (IpAppUserLocationOperations)_so.servant;
			try
			{
			_localServant.locationReportRes(assignmentId,locations);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void extendedLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "extendedLocationReportErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "extendedLocationReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationOperations _localServant = (IpAppUserLocationOperations)_so.servant;
			try
			{
			_localServant.extendedLocationReportErr(assignmentId,cause,diagnostic);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void periodicLocationReport(int assignmentId, org.csapi.mm.TpUserLocationExtended[] locations)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "periodicLocationReport", true);
				_os.write_long(assignmentId);
				org.csapi.mm.TpUserLocationExtendedSetHelper.write(_os,locations);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "periodicLocationReport", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationOperations _localServant = (IpAppUserLocationOperations)_so.servant;
			try
			{
			_localServant.periodicLocationReport(assignmentId,locations);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void extendedLocationReportRes(int assignmentId, org.csapi.mm.TpUserLocationExtended[] locations)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "extendedLocationReportRes", true);
				_os.write_long(assignmentId);
				org.csapi.mm.TpUserLocationExtendedSetHelper.write(_os,locations);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "extendedLocationReportRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationOperations _localServant = (IpAppUserLocationOperations)_so.servant;
			try
			{
			_localServant.extendedLocationReportRes(assignmentId,locations);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void locationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "locationReportErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "locationReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationOperations _localServant = (IpAppUserLocationOperations)_so.servant;
			try
			{
			_localServant.locationReportErr(assignmentId,cause,diagnostic);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void periodicLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "periodicLocationReportErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "periodicLocationReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationOperations _localServant = (IpAppUserLocationOperations)_so.servant;
			try
			{
			_localServant.periodicLocationReportErr(assignmentId,cause,diagnostic);
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
