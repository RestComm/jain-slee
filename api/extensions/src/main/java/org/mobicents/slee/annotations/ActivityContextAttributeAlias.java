/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * The activity-context-attribute-alias element allows an SBB to alias activity
 * context attributes. Aliasing an attribute causes it to become available in a
 * global namespace public to all SBBs. The element contains the attribute alias
 * name, and the names of zero or more attributes in the SBB's activity context
 * interface that should be aliased to the given name.
 * 
 * @author Eduardo Martins
 * 
 */
@Documented
@Target(value = { ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityContextAttributeAlias {

	/**
	 * the attribute alias, that is, it's name in the global space shared by all Sbbs.
	 * @return
	 */
	String attributeAlias();

	/**
	 * the name of all attributes pointing to the alias. 
	 * @return
	 */
	String[] attributeNames();

}
