package org.csapi.gms;

/**
 *	Generated from IDL definition of union "TpFolderInfoProperty"
 *	@author JacORB IDL compiler 
 */

public final class TpFolderInfoProperty
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.gms.TpFolderInfoPropertyName discriminator;
	private java.lang.String MessagingFolderID;
	private java.lang.String MessagingFolderMessage;
	private java.lang.String MessagingFolderSubfolder;
	private java.lang.String MessagingFolderDateCreated;
	private java.lang.String MessagingFolderDateChanged;
	private short Dummy;

	public TpFolderInfoProperty ()
	{
	}

	public org.csapi.gms.TpFolderInfoPropertyName discriminator ()
	{
		return discriminator;
	}

	public java.lang.String MessagingFolderID ()
	{
		if (discriminator != org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_ID)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingFolderID;
	}

	public void MessagingFolderID (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_ID;
		MessagingFolderID = _x;
	}

	public java.lang.String MessagingFolderMessage ()
	{
		if (discriminator != org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_MESSAGE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingFolderMessage;
	}

	public void MessagingFolderMessage (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_MESSAGE;
		MessagingFolderMessage = _x;
	}

	public java.lang.String MessagingFolderSubfolder ()
	{
		if (discriminator != org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_SUBFOLDER)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingFolderSubfolder;
	}

	public void MessagingFolderSubfolder (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_SUBFOLDER;
		MessagingFolderSubfolder = _x;
	}

	public java.lang.String MessagingFolderDateCreated ()
	{
		if (discriminator != org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_DATE_CREATED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingFolderDateCreated;
	}

	public void MessagingFolderDateCreated (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_DATE_CREATED;
		MessagingFolderDateCreated = _x;
	}

	public java.lang.String MessagingFolderDateChanged ()
	{
		if (discriminator != org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_DATE_CHANGED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return MessagingFolderDateChanged;
	}

	public void MessagingFolderDateChanged (java.lang.String _x)
	{
		discriminator = org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_DATE_CHANGED;
		MessagingFolderDateChanged = _x;
	}

	public short Dummy ()
	{
		if (discriminator != org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		return Dummy;
	}

	public void Dummy (short _x)
	{
		discriminator = org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_UNDEFINED;
		Dummy = _x;
	}

	public void Dummy (org.csapi.gms.TpFolderInfoPropertyName _discriminator, short _x)
	{
		if (_discriminator != org.csapi.gms.TpFolderInfoPropertyName.P_MESSAGING_FOLDER_UNDEFINED)
			throw new org.omg.CORBA.BAD_OPERATION();
		discriminator = _discriminator;
		Dummy = _x;
	}

}
