package org.csapi.cs;


/**
 *	Generated from IDL interface "IpAppChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppChargingManagerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.cs.IpAppChargingManager
{
	private String[] ids = {"IDL:org/csapi/cs/IpAppChargingManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.cs.IpAppChargingManagerOperations.class;
	public void sessionAborted(int sessionID)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "sessionAborted", true);
				_os.write_long(sessionID);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "sessionAborted", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppChargingManagerOperations _localServant = (IpAppChargingManagerOperations)_so.servant;
			try
			{
			_localServant.sessionAborted(sessionID);
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
