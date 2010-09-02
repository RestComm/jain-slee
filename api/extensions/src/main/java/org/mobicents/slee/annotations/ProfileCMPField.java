/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation for a CMP Field belonging to JAIN SLEE 1.1 Profile Spec CMP Interface.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileCMPField {
	
	boolean unique() default false;
	
	String uniqueCollatorRef() default "";

	// TODO add index hints
}
