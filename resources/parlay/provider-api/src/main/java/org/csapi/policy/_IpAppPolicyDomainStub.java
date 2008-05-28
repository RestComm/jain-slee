package org.csapi.policy;


/**
 *	Generated from IDL interface "IpAppPolicyDomain"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class _IpAppPolicyDomainStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.csapi.policy.IpAppPolicyDomain
{
	private String[] ids = {"IDL:org/csapi/policy/IpAppPolicyDomain:1.0","IDL:org/csapi/IpInterface:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.csapi.policy.IpAppPolicyDomainOperations.class;
	public void reportNotification(int assignmentID, org.csapi.policy.TpPolicyEvent event)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reportNotification", true);
				_os.write_long(assignmentID);
				org.csapi.policy.TpPolicyEventHelper.write(_os,event);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reportNotification", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			IpAppPolicyDomainOperations _localServant = (IpAppPolicyDomainOperations)_so.servant;
			try
			{
			_localServant.reportNotification(assignmentID,event);
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
