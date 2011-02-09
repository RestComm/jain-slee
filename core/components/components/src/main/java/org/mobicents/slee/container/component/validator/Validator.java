/**
 * Start time:17:12:16 2009-01-30<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import org.mobicents.slee.container.component.ComponentRepository;

/**
 * 
 * Start time:17:12:16 2009-01-30<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * This is super interface for all validators. Validator asums that everything
 * has been loaded into component - class objects are set accordingly,
 * classloader has every needed class loaded. That is component class loader is
 * capable of providing class definition for matched component. Validator task
 * is only to validate SLEE constraints on classes, for istance to see if sbb
 * abstract class has proper methods for event handlers, has initial event
 * selector methods, definition of CMP fields, etc. Constraints are defined in
 * xml descriptor. Validator does not validate references - ie. it does not
 * check if referenced libraries are present, this is task of different class.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface Validator {

	/**
	 * Should return the same value for each call if components state has not
	 * been changed. It checks component against xml.
	 * 
	 * @return <ul>
	 *         <li><b>true</b> - if component does not violate constraints
	 *         <li><b>false</b> - if component does violate constraints
	 *         </ul>
	 */
	public boolean validate();

	/**
	 * Sets component repository - plase where all components are stored and can be referenced by ComopnentKey or XID
	 * @param repository
	 */
	public void setComponentRepository(ComponentRepository repository);
	
	
}
