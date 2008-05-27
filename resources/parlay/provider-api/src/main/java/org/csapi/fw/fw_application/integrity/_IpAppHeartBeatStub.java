package org.csapi.fw.fw_application.integrity;


/**
 *	Generated from IDL interface "IpAppHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppHeartBeatStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_application.integrity.IpAppHeartBeat
{
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpAppHeartBeat:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_application.integrity.IpAppHeartBeatOperations.class;
	public void pulse()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "pulse", true);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "pulse", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppHeartBeatOperations _localServant = (IpAppHeartBeatOperations)_so.servant;
			try
			{
			_localServant.pulse();
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
