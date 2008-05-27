package org.csapi.mm.ulc;


/**
 *	Generated from IDL interface "IpAppUserLocationCamel"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppUserLocationCamelStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.mm.ulc.IpAppUserLocationCamel
{
	private String[] ids = {"IDL:org/csapi/mm/ulc/IpAppUserLocationCamel:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.mm.ulc.IpAppUserLocationCamelOperations.class;
	public void periodicLocationReport(int assignmentId, org.csapi.mm.TpUserLocationCamel[] locations)
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
				org.csapi.mm.TpUserLocationCamelSetHelper.write(_os,locations);
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
			IpAppUserLocationCamelOperations _localServant = (IpAppUserLocationCamelOperations)_so.servant;
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

	public void locationReportRes(int assignmentId, org.csapi.mm.TpUserLocationCamel[] locations)
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
				org.csapi.mm.TpUserLocationCamelSetHelper.write(_os,locations);
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
			IpAppUserLocationCamelOperations _localServant = (IpAppUserLocationCamelOperations)_so.servant;
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
			IpAppUserLocationCamelOperations _localServant = (IpAppUserLocationCamelOperations)_so.servant;
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

	public void triggeredLocationReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "triggeredLocationReportErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "triggeredLocationReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationCamelOperations _localServant = (IpAppUserLocationCamelOperations)_so.servant;
			try
			{
			_localServant.triggeredLocationReportErr(assignmentId,cause,diagnostic);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void triggeredLocationReport(int assignmentId, org.csapi.mm.TpUserLocationCamel location, org.csapi.mm.TpLocationTriggerCamel criterion)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "triggeredLocationReport", true);
				_os.write_long(assignmentId);
				org.csapi.mm.TpUserLocationCamelHelper.write(_os,location);
				org.csapi.mm.TpLocationTriggerCamelHelper.write(_os,criterion);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "triggeredLocationReport", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserLocationCamelOperations _localServant = (IpAppUserLocationCamelOperations)_so.servant;
			try
			{
			_localServant.triggeredLocationReport(assignmentId,location,criterion);
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
			IpAppUserLocationCamelOperations _localServant = (IpAppUserLocationCamelOperations)_so.servant;
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
