package org.csapi.gms;

/**
 *	Generated from IDL definition of struct "TpGMSNewMessageArrivedCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpGMSNewMessageArrivedCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpGMSNewMessageArrivedCriteria(){}
	public org.csapi.TpAddress MailboxID;
	public java.lang.String AuthenticationInfo;
	public TpGMSNewMessageArrivedCriteria(org.csapi.TpAddress MailboxID, java.lang.String AuthenticationInfo)
	{
		this.MailboxID = MailboxID;
		this.AuthenticationInfo = AuthenticationInfo;
	}
}
