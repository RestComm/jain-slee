/**
 * 
 */
package org.mobicents.slee.container.facilities;

import javax.slee.ComponentID;
import javax.slee.facilities.Level;
import javax.transaction.SystemException;

/**
 * @author martins
 * 
 */
@SuppressWarnings("deprecation")
public interface TraceFacility extends javax.slee.facilities.TraceFacility {

	public void setTraceLevelOnTransaction(final ComponentID componentId,
			Level newLevel) throws SystemException;

	public void unSetTraceLevel(final ComponentID componentId)
			throws SystemException;

}
