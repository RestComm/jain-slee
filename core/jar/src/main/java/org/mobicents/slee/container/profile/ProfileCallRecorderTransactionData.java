package org.mobicents.slee.container.profile;

import java.util.LinkedList;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.facilities.MNotificationSource;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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
	public static void addProfileCall(ProfileObject po) throws TransactionRequiredLocalException, SLEEException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("Recording call to profile, stored key: " + makeKey(po));
		}
		
		SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		
		try
		{
			sleeTransactionManager.mandateTransaction();

			ProfileCallRecorderTransactionData data = (ProfileCallRecorderTransactionData) sleeTransactionManager.getTransactionContext().getData().get(TRANSACTION_CONTEXT_KEY);
			
			// If data does not exist, create it
			if (data == null) {
				data = new ProfileCallRecorderTransactionData();
				sleeTransactionManager.getTransactionContext().getData().put(TRANSACTION_CONTEXT_KEY, data);
			}
			
			if (!po.isProfileReentrant())
			{
				String key = makeKey(po);
				// we need to check
				if (data.invokedProfiles.contains(key) && data.invokedProfiles.getLast().compareTo(key) != 0) {
					throw new SLEEException("Detected loopback call. Call sequence: " + data.invokedProfiles);
				}
				data.invokedProfiles.add(key);
				data.invokedProfileTablesNames.add(po.getProfileTableConcrete().getProfileTableName());
			}
		}
		catch (SystemException e) {
			throw new SLEEException("Failed to verify reentrancy due to some system level errror.", e);
		}
	}

	public static void removeProfileCall(ProfileObject po) throws TransactionRequiredLocalException, SLEEException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("Removing call to profile, stored key: " + makeKey(po));
		}
		
		SleeTransactionManager sleeTransactionManaget = sleeContainer.getTransactionManager();
		
		try
		{
			sleeTransactionManaget.mandateTransaction();

			ProfileCallRecorderTransactionData data = (ProfileCallRecorderTransactionData) sleeTransactionManaget.getTransactionContext().getData().get(TRANSACTION_CONTEXT_KEY);
			
			if (data == null) {
				throw new SLEEException("No Profile call recorder in memory, this is a bug.");
			}
			
			if (!po.isProfileReentrant())
			{
				String key = makeKey(po);
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
						sleeTransactionManaget.getTransactionContext().getData().remove(TRANSACTION_CONTEXT_KEY);
					}
				}

			}
		}
		catch (SystemException e) {
			throw new SLEEException("Failed to verify reentrancy due to some system level errror.", e);
		}
	}

	public static MNotificationSource getCurrentNotificationSource() throws TransactionRequiredLocalException, SLEEException
	{
		if(logger.isDebugEnabled()) {
			logger.debug("Trying to get Notification source for profile table.");
		}
		
		SleeTransactionManager sleeTransactionManaget = sleeContainer.getTransactionManager();
		ProfileCallRecorderTransactionData data;
		
		try
		{
			data = (ProfileCallRecorderTransactionData) sleeTransactionManaget.getTransactionContext().getData().get(TRANSACTION_CONTEXT_KEY);
			if (data == null) {
				throw new SLEEException("No Profile call recorder in memory, this is a bug.");
			}

			//IF data is present, there is something in it.
			String tableName = data.invokedProfileTablesNames.getLast();
			//FIXME: should we create new object? or lookup table? Lets do lookup
			ProfileTableConcrete ptc = sleeContainer.getSleeProfileTableManager().getProfileTable(tableName);
			
			return ptc.getProfileTableNotification();
		}
		catch (SystemException e) {
			throw new SLEEException("Failed to fetch notification source due to some system level error.", e);
		}
		catch (UnrecognizedProfileTableNameException e) {
			throw new SLEEException("Failed to fetch notification source due to some system level error.", e);
		}
	}

	private static String makeKey(ProfileObject pc) {
		// FIXME: Alexandre: Removed toString() as it may cause it to identify as differente profile
		return pc.getProfileTableConcrete().getProfileTableName() + "-" + (pc.getProfileEntity() == null ? "" : pc.getProfileEntity().getProfileName());// + "-" + pc.toString();
	}
}
