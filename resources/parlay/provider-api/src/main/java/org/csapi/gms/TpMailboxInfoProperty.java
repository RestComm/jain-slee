package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpMailboxInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxInfoProperty
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.gms.TpMailboxInfoPropertyName discriminator;
	private org.csapi.TpAddress MessagingMailboxID;
	private java.lang.String MessagingMailboxOwner;
	private java.lang.String MessagingMailboxFolder;
	private java.lang.String MessagingMailboxDateCreated;
	private java.lang.String MessagingMailboxDateChanged;
	private short Dummy;

	public TpMailboxInfoProperty ()
	{
	}

	public org.csapi.gms.TpMailboxInfoPropertyName discriminator ()
	{
		return discriminator;
	}

	public org.csapi.TpAddress MessagingMailboxID ()
	{
		if (discriminator != org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_ID)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMailboxID;
	}

	public void MessagingMailboxID (org.csapi.TpAddress _x)
	{
		discriminator = org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_ID;
		MessagingMailboxID = _x;
	}

	public java.lang.String MessagingMailboxOwner ()
	{
		if (discriminator != org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_OWNER)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMailboxOwner;
	}

	public void MessagingMailboxOwner (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_OWNER;
		MessagingMailboxOwner = _x;
	}

	public java.lang.String MessagingMailboxFolder ()
	{
		if (discriminator != org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_FOLDER)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMailboxFolder;
	}

	public void MessagingMailboxFolder (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_FOLDER;
		MessagingMailboxFolder = _x;
	}

	public java.lang.String MessagingMailboxDateCreated ()
	{
		if (discriminator != org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_DATE_CREATED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMailboxDateCreated;
	}

	public void MessagingMailboxDateCreated (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_DATE_CREATED;
		MessagingMailboxDateCreated = _x;
	}

	public java.lang.String MessagingMailboxDateChanged ()
	{
		if (discriminator != org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_DATE_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingMailboxDateChanged;
	}

	public void MessagingMailboxDateChanged (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_DATE_CHANGED;
		MessagingMailboxDateChanged = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.gms.TpMailboxInfoPropertyName _discriminator, short _x)
	{
		if (_discriminator != org.csapi.gms.TpMailboxInfoPropertyName.P_MESSAGING_MAILBOX_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
