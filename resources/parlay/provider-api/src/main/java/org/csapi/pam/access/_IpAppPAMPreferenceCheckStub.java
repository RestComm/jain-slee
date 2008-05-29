package org.csapi.pam.access;


/**
 *	Generated from IDL interface "IpAppPAMPreferenceCheck"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppPAMPreferenceCheckStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.pam.access.IpAppPAMPreferenceCheck
{
	private String[] ids = {"IDL:org/csapi/pam/access/IpAppPAMPreferenceCheck:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.pam.access.IpAppPAMPreferenceCheckOperations.class;
	public org.csapi.pam.TpPAMAvailabilityProfile[] computeAvailability(java.lang.String identity, org.csapi.pam.TpPAMContext pamContext, java.lang.String[] attributeNames, byte[] authToken)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "computeAvailability", true);
				_os.write_string(identity);
				org.csapi.pam.TpPAMContextHelper.write(_os,pamContext);
				org.csapi.TpStringListHelper.write(_os,attributeNames);
				org.csapi.TpOctetSetHelper.write(_os,authToken);
				_is = _invoke(_os);
				org.csapi.pam.TpPAMAvailabilityProfile[] _result = org.csapi.pam.TpPAMAvailabilityProfileListHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "computeAvailability", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppPAMPreferenceCheckOperations _localServant = (IpAppPAMPreferenceCheckOperations)_so.servant;
			org.csapi.pam.TpPAMAvailabilityProfile[] _result;			try
			{
			_result = _localServant.computeAvailability(identity,pamContext,attributeNames,authToken);
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
