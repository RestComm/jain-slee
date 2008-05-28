package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpJoinEventInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpJoinEventInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpJoinEventInfo(){}
	public org.csapi.TpAddress DestinationAddress;
	public org.csapi.TpAddress OriginatingAddress;
	public org.csapi.cc.TpCallAppInfo[] CallAppInfo;
	public TpJoinEventInfo(org.csapi.TpAddress DestinationAddress, org.csapi.TpAddress OriginatingAddress, org.csapi.cc.TpCallAppInfo[] CallAppInfo)
	{
		this.DestinationAddress = DestinationAddress;
		this.OriginatingAddress = OriginatingAddress;
		this.CallAppInfo = CallAppInfo;
	}
}
