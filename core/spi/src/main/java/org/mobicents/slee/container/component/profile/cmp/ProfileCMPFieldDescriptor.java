/**
 * 
 */
package org.mobicents.slee.container.component.profile.cmp;

import java.util.List;

/**
 * @author martins
 *
 */
public interface ProfileCMPFieldDescriptor {

	/**
	 * 
	 * @return
	 */
	public String getCmpFieldName();

	/**
	 * 
	 * @return
	 */
	public List<? extends IndexHintDescriptor> getIndexHints();

	/**
	 * 
	 * @return
	 */
	public boolean isUnique();

	/**
	 * 
	 * @return
	 */
	public String getUniqueCollatorRef();
}
