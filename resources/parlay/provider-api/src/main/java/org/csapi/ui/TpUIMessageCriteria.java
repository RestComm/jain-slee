package org.csapi.ui;

/**
 *	Generated from IDL definition of struct "TpUIMessageCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpUIMessageCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpUIMessageCriteria(){}
	public java.lang.String EndSequence;
	public int MaxMessageTime;
	public int MaxMessageSize;
	public TpUIMessageCriteria(java.lang.String EndSequence, int MaxMessageTime, int MaxMessageSize)
	{
		this.EndSequence = EndSequence;
		this.MaxMessageTime = MaxMessageTime;
		this.MaxMessageSize = MaxMessageSize;
	}
}
