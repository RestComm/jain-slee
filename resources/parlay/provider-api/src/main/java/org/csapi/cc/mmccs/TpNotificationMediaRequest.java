package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpNotificationMediaRequest"
 *	@author JacORB IDL compiler 
 */

public final class TpNotificationMediaRequest
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpNotificationMediaRequest(){}
	public org.csapi.cc.TpCallNotificationScope MediaNotificationScope;
	public org.csapi.cc.mmccs.TpMediaStreamRequest[] MediaStreamsRequested;
	public TpNotificationMediaRequest(org.csapi.cc.TpCallNotificationScope MediaNotificationScope, org.csapi.cc.mmccs.TpMediaStreamRequest[] MediaStreamsRequested)
	{
		this.MediaNotificationScope = MediaNotificationScope;
		this.MediaStreamsRequested = MediaStreamsRequested;
	}
}
