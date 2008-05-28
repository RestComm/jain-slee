package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpMessageInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpMessageInfoProperty
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.gms.TpMessageInfoPropertyName discriminator;
	private java.lang.String MessagingMessageID;
	private java.lang.String MessagingMessageSubject;
	private java.lang.String MessagingMessageDateSent;
	private java.lang.String MessagingMessageDateReceived;
	private java.lang.String MessagingMessageDateChanged;
	private org.csapi.TpAddress MessagingMessageSentFrom;
	private org.csapi.TpAddress MessagingMessageSentTo;
	private org.csapi.TpAddress MessagingMessageCCTo;
	private org.csapi.TpAddress MessagingMessageBCCTo;
	private int MessagingMessageSize;
	private org.csapi.gms.TpMessagePriority MessagingMessagePriority;
	private org.csapi.gms.TpMessageFormat MessagingMessageFormat;
	private java.lang.String MessagingMessageFolder;
	private org.csapi.gms.TpMessageStatus MessagingMessageStatus;
	private short Dummy;

	public TpMessageInfoProperty ()
	{
	}

	public org.csapi.gms.TpMessageInfoPropertyName discriminator ()
	{
		return discriminator;
	}

	public java.lang.String MessagingMessageID ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_ID)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageID;
	}

	public void MessagingMessageID (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_ID;
		MessagingMessageID = _x;
	}

	public java.lang.String MessagingMessageSubject ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SUBJECT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageSubject;
	}

	public void MessagingMessageSubject (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SUBJECT;
		MessagingMessageSubject = _x;
	}

	public java.lang.String MessagingMessageDateSent ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_SENT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageDateSent;
	}

	public void MessagingMessageDateSent (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_SENT;
		MessagingMessageDateSent = _x;
	}

	public java.lang.String MessagingMessageDateReceived ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_RECEIVED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageDateReceived;
	}

	public void MessagingMessageDateReceived (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_RECEIVED;
		MessagingMessageDateReceived = _x;
	}

	public java.lang.String MessagingMessageDateChanged ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageDateChanged;
	}

	public void MessagingMessageDateChanged (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_DATE_CHANGED;
		MessagingMessageDateChanged = _x;
	}

	public org.csapi.TpAddress MessagingMessageSentFrom ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SENT_FROM)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageSentFrom;
	}

	public void MessagingMessageSentFrom (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SENT_FROM;
		MessagingMessageSentFrom = _x;
	}

	public org.csapi.TpAddress MessagingMessageSentTo ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SENT_TO)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageSentTo;
	}

	public void MessagingMessageSentTo (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SENT_TO;
		MessagingMessageSentTo = _x;
	}

	public org.csapi.TpAddress MessagingMessageCCTo ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_CC_TO)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageCCTo;
	}

	public void MessagingMessageCCTo (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_CC_TO;
		MessagingMessageCCTo = _x;
	}

	public org.csapi.TpAddress MessagingMessageBCCTo ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_BCC_TO)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageBCCTo;
	}

	public void MessagingMessageBCCTo (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_BCC_TO;
		MessagingMessageBCCTo = _x;
	}

	public int MessagingMessageSize ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SIZE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageSize;
	}

	public void MessagingMessageSize (int _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_SIZE;
		MessagingMessageSize = _x;
	}

	public org.csapi.gms.TpMessagePriority MessagingMessagePriority ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_PRIORITY)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessagePriority;
	}

	public void MessagingMessagePriority (org.csapi.gms.TpMessagePriority _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_PRIORITY;
		MessagingMessagePriority = _x;
	}

	public org.csapi.gms.TpMessageFormat MessagingMessageFormat ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_FORMAT)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageFormat;
	}

	public void MessagingMessageFormat (org.csapi.gms.TpMessageFormat _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_FORMAT;
		MessagingMessageFormat = _x;
	}

	public java.lang.String MessagingMessageFolder ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_FOLDER)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageFolder;
	}

	public void MessagingMessageFolder (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_FOLDER;
		MessagingMessageFolder = _x;
	}

	public org.csapi.gms.TpMessageStatus MessagingMessageStatus ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_STATUS)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMessageStatus;
	}

	public void MessagingMessageStatus (org.csapi.gms.TpMessageStatus _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_STATUS;
		MessagingMessageStatus = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.gms.TpMessageInfoPropertyName _discriminator, short _x)
	{
		if (_discriminator != org.csapi.gms.TpMessageInfoPropertyName.P_MESSAGING_MESSAGE_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
