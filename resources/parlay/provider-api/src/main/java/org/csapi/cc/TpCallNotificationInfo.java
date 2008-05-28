package org.csapi.cc;

/**
 *	Generated from IDL definition of struct "TpCallNotificationInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpCallNotificationInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallNotificationInfo(){}
	public org.csapi.cc.TpCallNotificationReportScope CallNotificationReportScope;
	public org.csapi.cc.TpCallAppInfo[] CallAppInfo;
	public org.csapi.cc.TpCallEventInfo CallEventInfo;
	public TpCallNotificationInfo(org.csapi.cc.TpCallNotificationReportScope CallNotificationReportScope, org.csapi.cc.TpCallAppInfo[] CallAppInfo, org.csapi.cc.TpCallEventInfo CallEventInfo)
	{
		this.CallNotificationReportScope = CallNotificationReportScope;
		this.CallAppInfo = CallAppInfo;
		this.CallEventInfo = CallEventInfo;
	}
}
