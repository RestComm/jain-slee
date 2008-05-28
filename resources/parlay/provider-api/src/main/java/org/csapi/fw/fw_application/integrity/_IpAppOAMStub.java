package org.csapi.fw.fw_application.integrity;


/**
 *	Generated from IDL interface "IpAppOAM"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppOAMStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.fw.fw_application.integrity.IpAppOAM
{
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpAppOAM:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.fw.fw_application.integrity.IpAppOAMOperations.class;
	public java.lang.String systemDateTimeQuery(java.lang.String systemDateAndTime)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "systemDateTimeQuery", true);
				_os.write_string(systemDateAndTime);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
				return _result;
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "systemDateTimeQuery", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppOAMOperations _localServant = (IpAppOAMOperations)_so.servant;
			java.lang.String _result;			try
			{
			_result = _localServant.systemDateTimeQuery(systemDateAndTime);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

}
