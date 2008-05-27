package org.csapi.mm.us;


/**
 *	Generated from IDL interface "IpAppUserStatus"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppUserStatusStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.mm.us.IpAppUserStatus
{
	private String[] ids = {"IDL:org/csapi/mm/us/IpAppUserStatus:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.mm.us.IpAppUserStatusOperations.class;
	public void statusReportRes(int assignmentId, org.csapi.mm.TpUserStatus[] status)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "statusReportRes", true);
				_os.write_long(assignmentId);
				org.csapi.mm.TpUserStatusSetHelper.write(_os,status);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "statusReportRes", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserStatusOperations _localServant = (IpAppUserStatusOperations)_so.servant;
			try
			{
			_localServant.statusReportRes(assignmentId,status);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void triggeredStatusReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "triggeredStatusReportErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "triggeredStatusReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserStatusOperations _localServant = (IpAppUserStatusOperations)_so.servant;
			try
			{
			_localServant.triggeredStatusReportErr(assignmentId,cause,diagnostic);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void triggeredStatusReport(int assignmentId, org.csapi.mm.TpUserStatus status)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "triggeredStatusReport", true);
				_os.write_long(assignmentId);
				org.csapi.mm.TpUserStatusHelper.write(_os,status);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "triggeredStatusReport", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserStatusOperations _localServant = (IpAppUserStatusOperations)_so.servant;
			try
			{
			_localServant.triggeredStatusReport(assignmentId,status);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public void statusReportErr(int assignmentId, org.csapi.mm.TpMobilityError cause, org.csapi.mm.TpMobilityDiagnostic diagnostic)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "statusReportErr", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "statusReportErr", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppUserStatusOperations _localServant = (IpAppUserStatusOperations)_so.servant;
			try
			{
			_localServant.statusReportErr(assignmentId,cause,diagnostic);
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
