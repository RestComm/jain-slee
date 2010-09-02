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
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LibraryRef {
	
	/**
	 * the library name
	 * @return
	 */
	String name();
	
	/**
	 * the library vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the library version
	 * @return
	 */
	String version();
	
}
