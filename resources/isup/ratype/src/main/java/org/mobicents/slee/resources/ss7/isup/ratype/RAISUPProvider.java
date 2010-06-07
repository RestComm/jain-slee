package org.mobicents.slee.resources.ss7.isup.ratype;

import java.io.IOException;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;

import javax.slee.resource.StartActivityException;

import org.mobicents.protocols.ss7.isup.ISUPClientTransaction;
import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPServerTransaction;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.TransactionAlredyExistsException;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;

public interface RAISUPProvider {

	public ISUPClientTransaction createClientTransaction(ISUPMessage arg0) throws TransactionAlredyExistsException,
			IllegalArgumentException, ActivityAlreadyExistsException, NullPointerException, IllegalStateException, SLEEException,
			StartActivityException;

	public ISUPServerTransaction createServerTransaction(ISUPMessage arg0) throws TransactionAlredyExistsException,
			IllegalArgumentException, ActivityAlreadyExistsException, NullPointerException, IllegalStateException, SLEEException,
			StartActivityException;

	public ISUPMessageFactory getMessageFactory();

	public ISUPParameterFactory getParameterFactory();

	public void sendMessage(ISUPMessage arg0) throws ParameterRangeInvalidException, IOException;
	
	public boolean isTransportUp();
}
