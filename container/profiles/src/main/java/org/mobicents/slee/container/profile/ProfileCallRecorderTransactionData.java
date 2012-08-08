/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.profile;

import java.util.LinkedList;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.facilities.NotificationSourceWrapper;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;

/**
 * 
 * Start time:17:47:16 2009-03-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * This is class that carefully records calls to profile classes. On calls
 * within transaction this records profile name that has been called. In case of
 * reentrant profile we look for:
 * <ul>
 * <li>loop call - if currently called profile is reentrant && call names
 * contain this profile && last call name is not this profile</li>
 * <li>calls from different thread</li> --- how should we do that ? lol
 * </ul>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * 
 */
public class ProfileCallRecorderTransactionData {

	// FIXME: Shoudl we also look up threads ?
	private final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	private static Logger logger = Logger.getLogger(ProfileCallRecorderTransactionData.class);
	private static final String TRANSACTION_CONTEXT_KEY = "pctd";

	/**
	 * a linked list with the which contains string values representing profile
	 * table - profile pairs that has been called within transaction.
	 */
	private final LinkedList<String> invokedProfiles = new LinkedList<String>();
	
	/**
	 * Stores profile table name. This is required for alarm factility, as
	 * source changes with call to different profile table+profile pair.
	 */
	private final LinkedList<String> invokedProfileTablesNames = new LinkedList<String>();

	/**
	 * Adds call to this profile.
	 * 
	 * @param po
	 * @throws SLEEException
	 */
	@SuppressWarnings("unchecked")
	public static void addProfileCall(ProfileObjectImpl po) throws SLEEException
	{
    SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
    
    try {
      if(sleeTransactionManager.getTransaction() == null) {
        return;
      }
    }
    catch ( SystemException se ) {
      throw new SLEEException("Unable to verify SLEE Transaction.", se);
    }
    
	  String key = makeKey(po);
	  
		if (logger.isTraceEnabled()) {
			logger.trace("Recording call to profile. Key[" + key + "]");
		}
		
		final TransactionContext txContext = sleeTransactionManager.getTransactionContext();
		ProfileCallRecorderTransactionData data = (ProfileCallRecorderTransactionData) txContext.getData().get(TRANSACTION_CONTEXT_KEY);

		// If data does not exist, create it
		if (data == null) {
			data = new ProfileCallRecorderTransactionData();
			txContext.getData().put(TRANSACTION_CONTEXT_KEY, data);
		}

		if (!po.isProfileReentrant())
		{
			// we need to check
			if (data.invokedProfiles.contains(key) && data.invokedProfiles.getLast().compareTo(key) != 0) {
				throw new SLEEException("Detected loopback call. Call sequence: " + data.invokedProfiles);
			}
			data.invokedProfiles.add(key);
			data.invokedProfileTablesNames.add(po.getProfileTable().getProfileTableName());
		}

	}

	public static void removeProfileCall(ProfileObjectImpl po) throws SLEEException
	{
		SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();

		try {
			if(sleeTransactionManager.getTransaction() == null) {
				return;
			}
		}
		catch ( SystemException se ) {
			throw new SLEEException("Unable to verify SLEE Transaction.", se);
		}

		String key = makeKey(po);

		if (logger.isTraceEnabled()) {
			logger.trace("Removing call to profile. Key[" + key + "]");
		}

		final TransactionContext txContext = sleeTransactionManager.getTransactionContext();
		ProfileCallRecorderTransactionData data = (ProfileCallRecorderTransactionData) txContext.getData().get(TRANSACTION_CONTEXT_KEY);

		if (data == null) {
			throw new SLEEException("No Profile call recorder in memory, this is a bug.");
		}

		if (!po.isProfileReentrant())
		{
			// we need to check
			String lastKey = data.invokedProfiles.getLast();
			if (lastKey.compareTo(key) != 0)
			{
				// logger.error("Last called profile does not match current: " + key + ", last call: " + lastKey + ". Please report this, it is a bug.");
				throw new SLEEException("Last called profile does not match current: " + key + ", last call: " + lastKey);
			}
			else
			{
				data.invokedProfiles.removeLast();
				data.invokedProfileTablesNames.removeLast();
				if (data.invokedProfiles.isEmpty())
				{
					txContext.getData().remove(TRANSACTION_CONTEXT_KEY);
				}
			}
		}

	}

	public static NotificationSourceWrapper getCurrentNotificationSource() throws TransactionRequiredLocalException, SLEEException
	{
		if(logger.isTraceEnabled()) {
			logger.trace("Trying to get Notification source for profile table.");
		}

		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		ProfileCallRecorderTransactionData data = (ProfileCallRecorderTransactionData) txContext.getData().get(TRANSACTION_CONTEXT_KEY);
		if (data == null) {
			throw new SLEEException("No Profile call recorder in memory, this is a bug.");
		}

		//IF data is present, there is something in it.
		String tableName = data.invokedProfileTablesNames.getLast();
		try {	
			return sleeContainer.getSleeProfileTableManager().getProfileTable(tableName).getProfileTableNotification();			
		}
		catch (UnrecognizedProfileTableNameException e) {
			throw new SLEEException("Failed to fetch notification source due to some system level error.", e);
		}
	}

	private static String makeKey(ProfileObjectImpl pc)
	{
		return pc.getProfileTable().getProfileTableName() + "-" + (pc.getProfileEntity() == null ? "NO_PROFILE_ENTITY" : pc.getProfileEntity().getProfileName());
	}
}
