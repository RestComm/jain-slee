/**
 * 
 */
package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.slee.SLEEException;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedAttributeException;

import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.facilities.NotificationSourceWrapper;

/**
 * @author martins
 *
 */
public interface ProfileTable extends javax.slee.profile.ProfileTable {

	public Collection<ProfileID> getProfilesByAttribute(
			String attributeName, Object attributeValue, boolean isSlee11)
			throws UnrecognizedAttributeException,
			AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException;
			
	public NotificationSourceWrapper getProfileTableNotification();

	/**
	 * @param profileName
	 * @return
	 */
	public boolean profileExists(String profileName);

	/**
	 * @param profileName
	 * @return
	 */
	public ProfileObject getProfile(String profileName);
	
	public ProfileSpecificationComponent getProfileSpecificationComponent();

}
