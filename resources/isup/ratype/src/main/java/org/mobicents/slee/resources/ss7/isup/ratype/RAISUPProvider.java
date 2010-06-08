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
	
	/**
	 * Create client transaction activity
	 * @param arg0
	 * @return
	 * @throws TransactionAlredyExistsException
	 * @throws IllegalArgumentException
	 * @throws ActivityAlreadyExistsException
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 * @throws SLEEException
	 * @throws StartActivityException
	 */
	public ISUPClientTransaction createClientTransaction(ISUPMessage arg0) throws TransactionAlredyExistsException,
			IllegalArgumentException, ActivityAlreadyExistsException, NullPointerException, IllegalStateException, SLEEException,
			StartActivityException;
	/**
	 * Create server transaction activity
	 * @param arg0
	 * @return
	 * @throws TransactionAlredyExistsException
	 * @throws IllegalArgumentException
	 * @throws ActivityAlreadyExistsException
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 * @throws SLEEException
	 * @throws StartActivityException
	 */
	public ISUPServerTransaction createServerTransaction(ISUPMessage arg0) throws TransactionAlredyExistsException,
			IllegalArgumentException, ActivityAlreadyExistsException, NullPointerException, IllegalStateException, SLEEException,
			StartActivityException;

	/**
	 * Get message factory.
	 * @return
	 */
	public ISUPMessageFactory getMessageFactory();
	/**
	 * Get parameter factory.
	 * @return
	 */
	public ISUPParameterFactory getParameterFactory();
	/**
	 * Send message statelesly.
	 * @param arg0
	 * @throws ParameterRangeInvalidException
	 * @throws IOException
	 */
	public void sendMessage(ISUPMessage arg0) throws ParameterRangeInvalidException, IOException;
	/**
	 * Determine if transport layer is connected and links are up.
	 * @return
	 */
	public boolean isTransportUp();
}
